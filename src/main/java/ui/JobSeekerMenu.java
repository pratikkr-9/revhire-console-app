package ui;

import model.Job;
import model.Resume;
import model.User;
import model.Application;

import service.ApplicationService;
import service.JobService;
import service.NotificationService;
import service.ResumeService;

import exception.DatabaseException;
import exception.ValidationException;

import java.util.List;
import java.util.Scanner;

public class JobSeekerMenu {

    static Scanner sc = new Scanner(System.in);

    static JobService jobService = new JobService();
    static ApplicationService appService = new ApplicationService();
    static ResumeService resumeService = new ResumeService();
    static NotificationService notifService = new NotificationService();

    public static void show(User user) {
        while (true) {
            try {
                System.out.println("\n-------- JOB SEEKER MENU ---------");
                System.out.println("1. View Jobs");
                System.out.println("2. Apply Job");
                System.out.println("3. View My Applications");
                System.out.println("4. Withdraw Application");
                System.out.println("5. Create / Update Resume");
                System.out.println("6. View Resume");
                System.out.println("7. View Notifications");
                System.out.println("0. Logout");

                int ch = sc.nextInt();
                sc.nextLine(); // clear buffer

                switch (ch) {
                    case 1 -> viewJobs();
                    case 2 -> applyJob(user);
                    case 3 -> viewMyApplications(user);
                    case 4 -> withdrawApplication();
                    case 5 -> manageResume(user);
                    case 6 -> viewResume(user);
                    case 7 -> viewNotifications(user);
                    case 0 -> {
                        System.out.println("Logged out successfully.");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }

            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
            }
        }
    }

    static void viewJobs() {
        try {
            List<Job> jobs = jobService.getAllJobs();

            if (jobs.isEmpty()) {
                System.out.println("No jobs available.");
                return;
            }

            jobs.forEach(j ->
                System.out.println(
                    j.getJobId() + " | " +
                    j.getTitle() + " | " +
                    j.getLocation() + " | " +
                    j.getSalary()
                )
            );

        } catch (DatabaseException e) {
            System.out.println("Unable to fetch jobs at this time.");
        }
    }

    static void applyJob(User user) {
        try {
            System.out.print("Enter Job ID: ");
            int jobId = sc.nextInt();
            sc.nextLine();

            appService.applyJob(jobId, user.getUserId());
            System.out.println("Applied successfully!");

        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Unable to apply for job.");
        } catch (Exception e) {
            System.out.println("Invalid input.");
            sc.nextLine();
        }
    }

    static void viewMyApplications(User user) {
        try {
            List<Application> apps =
                    appService.getMyApplications(user.getUserId());

            if (apps.isEmpty()) {
                System.out.println("No applications found.");
                return;
            }

            apps.forEach(a ->
                System.out.println(
                    "AppID: " + a.getAppId() +
                    " | JobID: " + a.getJobId() +
                    " | Status: " + a.getStatus() +
                    " | Date: " + a.getApplyDate()
                )
            );

        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Unable to fetch applications.");
        }
    }

    static void withdrawApplication() {
        try {
            System.out.print("Enter Application ID to withdraw: ");
            int appId = sc.nextInt();
            sc.nextLine();

            appService.withdraw(appId);
            System.out.println("Application withdrawn successfully.");

        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Unable to withdraw application.");
        } catch (Exception e) {
            System.out.println("Invalid input.");
            sc.nextLine();
        }
    }

    static void manageResume(User user) {
        try {
            Resume r = new Resume();
            r.setSeekerId(user.getUserId());

            System.out.print("Education: ");
            r.setEducation(sc.nextLine());

            System.out.print("Experience: ");
            r.setExperience(sc.nextLine());

            System.out.print("Skills: ");
            r.setSkills(sc.nextLine());

            System.out.print("Certifications: ");
            r.setCertifications(sc.nextLine());

            resumeService.saveOrUpdate(r);
            System.out.println("Resume saved successfully!");

        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Unable to save resume.");
        }
    }

    static void viewResume(User user) {
        try {
            Resume r = resumeService.getResume(user.getUserId());

            if (r == null) {
                System.out.println("No resume found. Please create one.");
                return;
            }

            System.out.println("\n--- YOUR RESUME ---");
            System.out.println("Education: " + r.getEducation());
            System.out.println("Experience: " + r.getExperience());
            System.out.println("Skills: " + r.getSkills());
            System.out.println("Certifications: " + r.getCertifications());

        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Unable to fetch resume.");
        }
    }

    static void viewNotifications(User user) {
        try {
            notifService.getMyNotifications(user.getUserId()).forEach(n ->
                System.out.println(
                    n.getCreatedAt() + " | " + n.getMessage()
                )
            );
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Unable to fetch notifications.");
        }
    }
}
