package srk;

import config.config;
import java.util.Scanner;

public class admin {

    Scanner sc = new Scanner(System.in);
    config db = new config();
    int choice;

    public static void viewUsers() {
        String query = "SELECT u_id, u_name, u_email, u_type, u_status FROM tbl_user";
        String[] headers = {"ID", "Name", "Email", "Type", "Status"};
        String[] columns = {"u_id", "u_name", "u_email", "u_type", "u_status"};

        config db = new config();
        db.viewRecords(query, headers, columns);
    }

    public void admin() {
        char cont = 'Y';
        
        System.out.println("\n===== ADMIN DASHBOARD =====");
        
        do {
            System.out.println("\n1. Approve/Reject Account");
            System.out.println("2. View All Users");
            System.out.println("3. Exit");
            System.out.print("Enter Choice: ");
            
            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Enter a number (1-3): ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\n=== PENDING ACCOUNTS ===");
                    String pendingQuery = "SELECT u_id, u_name, u_email, u_type, u_status FROM tbl_user WHERE u_status = 'Pending'";
                    String[] headers = {"ID", "Name", "Email", "Type", "Status"};
                    String[] columns = {"u_id", "u_name", "u_email", "u_type", "u_status"};
                    
                    java.util.List<java.util.Map<String, Object>> pendingUsers = db.fetchRecords(pendingQuery);
                    
                    if (pendingUsers.isEmpty()) {
                        System.out.println("No pending accounts to approve.");
                        break;
                    }
                    
                    db.viewRecords(pendingQuery, headers, columns);
                    
                    int userId;
                    boolean userFound = false;
                    
                    do {
                        System.out.print("\nEnter User ID to process (or 0 to cancel): ");
                        while (!sc.hasNextInt()) {
                            System.out.print("Invalid input. Enter a valid User ID: ");
                            sc.next();
                        }
                        userId = sc.nextInt();
                        sc.nextLine();
                        
                        if (userId == 0) {
                            System.out.println("Operation cancelled.");
                            userFound = true;
                            break;
                        }
                        
                        String checkSQL = "SELECT * FROM tbl_user WHERE u_id = ? AND u_status = 'Pending'";
                        java.util.List<java.util.Map<String, Object>> userCheck = db.fetchRecords(checkSQL, userId);
                        
                        if (userCheck.isEmpty()) {
                            System.out.println("User ID not found or not pending. Please try again.");
                        } else {
                            userFound = true;
                        }
                    } while (!userFound);
                    
                    if (userId == 0) {
                        break;
                    }
                    
                    System.out.println("\n1. Approve Account");
                    System.out.println("2. Reject Account");
                    System.out.print("Enter choice: ");
                    
                    while (!sc.hasNextInt()) {
                        System.out.print("Invalid input. Enter 1 or 2: ");
                        sc.next();
                    }
                    int actionChoice = sc.nextInt();
                    sc.nextLine();
                    
                    String newStatus = "";
                    if (actionChoice == 1) {
                        newStatus = "Approved";
                    } else if (actionChoice == 2) {
                        newStatus = "Rejected";
                    } else {
                        System.out.println("Invalid choice. Operation cancelled.");
                        break;
                    }

                    String sql = "UPDATE tbl_user SET u_status = ? WHERE u_id = ?";
                    db.updateRecord(sql, newStatus, userId);
                    
                    System.out.println("Account status updated to " + newStatus + "!");
                    break;
                    
                case 2:
                    System.out.println("\n=== ALL USERS ===");
                    viewUsers();
                    break;
                    
                case 3:
                    System.out.println("Exiting Admin Dashboard...");
                    cont = 'N';
                    break;
                    
                default:
                    System.out.println("Invalid Choice. Try Again!");
            }
            
            if (choice != 3 && cont == 'Y') {
                System.out.print("\nContinue in Admin Dashboard? (Y/N): ");
                cont = sc.next().toUpperCase().charAt(0);
                sc.nextLine();
            }
            
        } while (cont == 'Y');
    }
}