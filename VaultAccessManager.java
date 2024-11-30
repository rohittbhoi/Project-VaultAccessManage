package Tasks;

import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;
import java.io.*;

public class VaultAccessManager {
    private static HashMap<String, String> users = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String Users_Details = "users.txt";

    public static void main(String[] args) {
    	loadUsersFromFile();
        int choice;
        do {
            System.out.println("\n--- VaultAccess Manager ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                	saveUsersToFile();
                    System.out.println("Exiting... Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);
    }

    private static void registerUser() {
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists. Please try a different username.");
            return;
        }

        // Auto-generate a password using UUID
        String password = UUID.randomUUID().toString().substring(0, 8); // Generate 8-character password
        users.put(username, password);
        saveUsersToFile();

        System.out.println("Registration successful!");
        System.out.println("Your auto-generated password is: " + password);
        System.out.println("Please save this password for future use.");
    }

    private static void loginUser() {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (users.containsKey(username) && users.get(username).equals(password)) {
            System.out.println("Login successful! Welcome " + username);
            accountManagement(username);
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    private static void accountManagement(String username) {
        int choice;
        do {
            System.out.println("\n--- Account Management ---");
            System.out.println("1. Change Password");
            System.out.println("2. Delete Account");
            System.out.println("3. Display Account Info");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    changePassword(username);
                    break;
                case 2:
                    deleteAccount(username);
                    return; // Exit account management after deletion
                case 3:
                    displayAccountInfo(username);
                    break;
                case 4:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }

    private static void changePassword(String username) {
        System.out.print("Enter your current password: ");
        String currentPassword = scanner.nextLine();

        if (users.get(username).equals(currentPassword)) {
            // Auto-generate a new password
            String newPassword = UUID.randomUUID().toString().substring(0, 8); // Generate 8-character password
            users.put(username, newPassword);
            System.out.println("Password changed successfully!");
            System.out.println("Your new auto-generated password is: " + newPassword);
        } else {
            System.out.println("Incorrect current password.");
        }
    }

    private static void deleteAccount(String username) {
        System.out.print("Are you sure you want to delete your account? (yes/no): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            users.remove(username);
            saveUsersToFile();
            System.out.println("Account deleted successfully.");
        } else {
            System.out.println("Account deletion canceled.");	
        }
    }

    private static void displayAccountInfo(String username) {
        System.out.println("Username: " + username);
        System.out.println("Password: " + users.get(username));
    }
    
    //For save users to file
    private static void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Users_Details))) {
            for (String username : users.keySet()) {
                String password = users.get(username);
                writer.write(username + "," + password);
                writer.newLine();
            }
            System.out.println("User data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }
    
    //Load users from file 
    
    private static void loadUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(Users_Details))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
            System.out.println("User data loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("No user data found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
    }
    
    
}
