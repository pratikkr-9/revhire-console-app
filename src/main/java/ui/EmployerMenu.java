package ui;

import model.Job;
import model.User;
import model.Application;

import service.ApplicationService;
import service.JobService;

import exception.DatabaseException;
import exception.ValidationException;
import exception.ResourceNotFoundException;

import java.util.Scanner;
import java.util.List;

public class EmployerMenu {

    static Scanner sc = new Scanner(System.in);
    static JobService jobService = new JobService();
    static ApplicationService appService = new ApplicationService();

    public static void show(User user) {
        while (true) {
            try {
                System.out.println("\n-------- EMPLOYER MENU --------");
                System.out.println("1. Post Job");
                System.out.println("2. View My Jobs");
                System.out.println("3. View Applicants for Job");
                System.out.println("4. Update Application Status");
                System.out.println("0. Logout");

                int ch = sc.nextInt();
                sc.nextLine();

                switch (ch) {
                    case 1 -> postJob(user);
                    case 2 -> viewJobs(user);   // 🔴 pass logged in user
                    case 3 -> viewApplicants();
                    case 4 -> updateStatus();
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

    static void postJob(User user) {
        try {
            Job j = new Job();
            j.setEmployerId(user.getUserId());

            System.out.print("Title: ");
            j.setTitle(sc.nextLine());

            System.out.print("Description: ");
            j.setDescription(sc.nextLine());

            System.out.print("Location: ");
            j.setLocation(sc.nextLine());

            System.out.print("Salary: ");
            j.setSalary(sc.nextDouble());
            sc.nextLine();

            System.out.print("Job Type: ");
            j.setJobType(sc.nextLine());

            jobService.postJob(j);
            System.out.println("Job posted successfully!");

        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("System error. Unable to post job.");
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
            sc.nextLine();
        }
    }

    static void viewJobs(User user) {
        try {
            List<Job> jobs = jobService.getMyJobs(user.getUserId());

            if (jobs.isEmpty()) {
                System.out.println("You have not posted any jobs yet.");
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

        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Unable to fetch your jobs at this time.");
        }
    }

    static void viewApplicants() {
        try {
            System.out.print("Enter Job ID: ");
            int jobId = sc.nextInt();
            sc.nextLine();

            List<Application> apps =
                    appService.getJobApplications(jobId);

            apps.forEach(a ->
                System.out.println(
                    "AppID: " + a.getAppId() +
                    " | SeekerID: " + a.getSeekerId() +
                    " | Status: " + a.getStatus()
                )
            );

        } catch (ResourceNotFoundException e) {
            System.out.println("No applicants found for this job.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Unable to fetch applicants.");
        } catch (Exception e) {
            System.out.println("Invalid input.");
            sc.nextLine();
        }
    }

    static void updateStatus() {
        try {
            System.out.print("Enter Application ID: ");
            int appId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter New Status (Shortlisted/Rejected): ");
            String status = sc.nextLine();

            appService.updateStatus(appId, status);
            System.out.println("Application status updated successfully!");

        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Unable to update application status.");
        } catch (Exception e) {
            System.out.println("Invalid input.");
            sc.nextLine();
        }
    }
}
