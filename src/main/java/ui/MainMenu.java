package ui;

import model.User;
import service.AuthService;

import exception.AuthenticationException;
import exception.DatabaseException;
import exception.ValidationException;

import java.util.Scanner;

public class MainMenu {

    static Scanner sc = new Scanner(System.in);
    static AuthService service = new AuthService();

    public static void start() {
        while (true) {
            try {
                System.out.println("\n-------- REVHIRE JOB PORTAL --------");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("0. Exit");

                int choice = sc.nextInt();
                sc.nextLine(); // clear buffer

                switch (choice) {
                    case 1 -> register();
                    case 2 -> login();
                    case 0 -> {
                        System.out.println("Thank you for using RevHire!");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }

            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // clear invalid input
            }
        }
    }

    static void register() {
        try {
            User u = new User();

            System.out.print("Role (JOBSEEKER/EMPLOYER): ");
            u.setRole(sc.nextLine());

            System.out.print("Name: ");
            u.setName(sc.nextLine());

            System.out.print("Email: ");
            u.setEmail(sc.nextLine());

            System.out.print("Password: ");
            u.setPassword(sc.nextLine());

            service.register(u);
            System.out.println("Registration successful!");

        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("System error. Please try again later.");
        }
    }

    static void login() {
        try {
            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Password: ");
            String pass = sc.nextLine();

            User user = service.login(email, pass);

            System.out.println("Welcome " + user.getName());

            if (user.getRole().equalsIgnoreCase("EMPLOYER")) {
                EmployerMenu.show(user);
            } else {
                JobSeekerMenu.show(user);
            }

        } catch (ValidationException | AuthenticationException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("System temporarily unavailable. Please try again later.");
        }
    }
}
