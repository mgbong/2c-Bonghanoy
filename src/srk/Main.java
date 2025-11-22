package srk;

import config.config;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        config db = new config();
        db.connectDB();
        int choice;

        System.out.println("WELCOME TO STUDENT RECORD KEEPING");

        do {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. LOGIN");
            System.out.println("2. REGISTER");
            System.out.println("3. ADMIN");
            System.out.println("4. EXIT");
            System.out.print("Enter Choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Enter a number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter Email: ");
                    String em = sc.nextLine().trim();

                    if (em.isEmpty()) {
                        System.out.println("Email cannot be empty!");
                        break;
                    }

                    String checkUserQuery = "SELECT * FROM tbl_user WHERE u_email = ?";
                    java.util.List<java.util.Map<String, Object>> userCheck = db.fetchRecords(checkUserQuery, em);

                    if (userCheck.isEmpty()) {
                        System.out.println("Email not found in the system!");
                        break;
                    }

                    java.util.Map<String, Object> userInfo = userCheck.get(0);
                    String userType = userInfo.get("u_type").toString();
                    int userId = Integer.parseInt(userInfo.get("u_id").toString());

                    Integer studentId = null;
                    if (userType.equals("Student")) {
                        // Check if this user already has a linked student ID
                        String checkExistingLinkSQL = "SELECT s_id FROM tbl_studentuser WHERE u_id = ?";
                        java.util.List<java.util.Map<String, Object>> existingUserLink = db.fetchRecords(checkExistingLinkSQL, userId);
                        
                        if (!existingUserLink.isEmpty()) {
                            // User already has a linked student ID - use that one only
                            studentId = Integer.parseInt(existingUserLink.get(0).get("s_id").toString());
                            System.out.println("Your account is linked to Student ID: " + studentId);
                            System.out.print("Enter your Student ID to continue: ");
                            
                            while (!sc.hasNextInt()) {
                                System.out.print("Invalid input. Enter a valid Student ID: ");
                                sc.next();
                            }
                            int inputStudentId = sc.nextInt();
                            sc.nextLine();
                            
                            if (inputStudentId != studentId) {
                                System.out.println("ERROR: Incorrect Student ID! Your account is linked to Student ID: " + studentId);
                                System.out.println("You cannot use a different Student ID.");
                                break;
                            }
                        } else {
                            // First time login - need to link a student ID
                            boolean validStudentId = false;
                            
                            while (!validStudentId) {
                                System.out.print("Enter Student ID: ");
                                while (!sc.hasNextInt()) {
                                    System.out.print("Invalid input. Enter a valid Student ID: ");
                                    sc.next();
                                }
                                int inputStudentId = sc.nextInt();
                                sc.nextLine();

                                // First check if student exists in tbl_student
                                String checkStudentSQL = "SELECT * FROM tbl_student WHERE s_id = ?";
                                java.util.List<java.util.Map<String, Object>> studentCheck = db.fetchRecords(checkStudentSQL, inputStudentId);

                                if (studentCheck.isEmpty()) {
                                    System.out.println("Student ID not found in the system. Please try again.");
                                    continue;
                                }
                                
                                // Check if this student ID is already linked to ANY user
                                String checkLinkSQL = "SELECT u_id FROM tbl_studentuser WHERE s_id = ?";
                                java.util.List<java.util.Map<String, Object>> existingLink = db.fetchRecords(checkLinkSQL, inputStudentId);
                                
                                if (!existingLink.isEmpty()) {
                                    System.out.println("ERROR: This Student ID is already connected to another user account!");
                                    System.out.println("Please contact the admin if this is your Student ID.");
                                } else {
                                    // Not linked to anyone - good to use
                                    studentId = inputStudentId;
                                    validStudentId = true;
                                    System.out.println("Student ID " + studentId + " will be linked to your account.");
                                }
                            }
                        }
                    }
                    
                    System.out.print("Enter Password: ");
                    String pas = sc.nextLine();

                    if (pas.isEmpty()) {
                        System.out.println("Password cannot be empty!");
                        break;
                    }

                    String hashedInputPassword = db.hashPassword(pas);
                    String qry = "SELECT * FROM tbl_user WHERE u_email = ? AND u_pass = ?";
                    java.util.List<java.util.Map<String, Object>> result = db.fetchRecords(qry, em, hashedInputPassword);

                    if (result.isEmpty()) {
                        System.out.println("INVALID CREDENTIALS");
                    } else {
                        java.util.Map<String, Object> user = result.get(0);
                        String stat = user.get("u_status").toString();
                        String type = user.get("u_type").toString();

                        if (!stat.equals("Approved")) {
                            System.out.println("Account Status: " + stat + ". Please contact the admin!");
                        } else {
                            System.out.println("LOGIN SUCCESS!");

                            if (type.equals("Student")) {
                                // Insert or update the student-user link
                                String insertLinkSQL = "INSERT OR IGNORE INTO tbl_studentuser (u_id, s_id) VALUES (?, ?)";
                                db.addRecord(insertLinkSQL, userId, studentId);
                                
                                student Student = new student();
                                Student.student(studentId);

                            } else if (type.equals("Registrar")) {
                                registrar Registrar = new registrar();
                                Registrar.registrar();

                            } else if (type.equals("Admin")) {
                                admin Admin = new admin();
                                Admin.admin();
                            }

                            System.out.println("\nReturning to Main Menu...");
                        }
                    }
                    break;

                case 2:
                    System.out.print("Enter Username: ");
                    String un = sc.nextLine();

                    System.out.print("Enter User Email: ");
                    String eml = sc.nextLine().trim();

                    while (eml.isEmpty()) {
                        System.out.print("Email cannot be empty. Enter Email: ");
                        eml = sc.nextLine().trim();
                    }

                    while (true) {
                        qry = "SELECT * FROM tbl_user WHERE u_email = ?";
                        result = db.fetchRecords(qry, eml);

                        if (result.isEmpty()) {
                            break;
                        } else {
                            System.out.print("Email already exists. Enter a different email: ");
                            eml = sc.nextLine().trim();
                        }
                    }

                    System.out.print("Enter User Type (1. Student / 2. Registrar / 3. Admin): ");
                    while (!sc.hasNextInt()) {
                        System.out.print("Invalid input. Enter a number (1-3): ");
                        sc.next();
                    }
                    int typeChoice = sc.nextInt();
                    sc.nextLine();

                    while (typeChoice < 1 || typeChoice > 3) {
                        System.out.print("Invalid. Choose between 1-3 only: ");
                        while (!sc.hasNextInt()) {
                            System.out.print("Invalid input. Enter a number (1-3): ");
                            sc.next();
                        }
                        typeChoice = sc.nextInt();
                        sc.nextLine();
                    }

                    String tp = "";
                    if (typeChoice == 1) {
                        tp = "Student";
                    } else if (typeChoice == 2) {
                        tp = "Registrar";
                    } else {
                        tp = "Admin";
                    }

                    System.out.print("Enter password: ");
                    String pass = sc.nextLine();

                    String hashedPassword = db.hashPassword(pass);

                    String sql = "INSERT INTO tbl_user (u_name, u_email, u_type, u_status, u_pass) VALUES (?,?,?,?,?)";
                    db.addRecord(sql, un, eml, tp, "Pending", hashedPassword);

                    System.out.println("\nRegistration successful! Please wait for admin approval.");
                    
                    break;

                case 3:
                    boolean adminAuthenticated = false;
                    int adminLoginAttempts = 0;
                    
                    while (!adminAuthenticated && adminLoginAttempts < 3) {
                        System.out.println("\n=== ADMIN AUTHENTICATION REQUIRED ===");
                        System.out.print("Enter Admin Email: ");
                        String adminEmail = sc.nextLine().trim();
                        
                        if (adminEmail.isEmpty()) {
                            System.out.println("Email cannot be empty!");
                            adminLoginAttempts++;
                            continue;
                        }
                        
                        System.out.print("Enter Admin Password: ");
                        String adminPassword = sc.nextLine();
                        
                        if (adminPassword.isEmpty()) {
                            System.out.println("Password cannot be empty!");
                            adminLoginAttempts++;
                            continue;
                        }
                        
                        String hashedAdminPassword = db.hashPassword(adminPassword);
                        String adminCheckQuery = "SELECT * FROM tbl_user WHERE u_email = ? AND u_pass = ? AND u_type = 'Admin' AND u_status = 'Approved'";
                        java.util.List<java.util.Map<String, Object>> adminResult = db.fetchRecords(adminCheckQuery, adminEmail, hashedAdminPassword);
                        
                        if (adminResult.isEmpty()) {
                            adminLoginAttempts++;
                            System.out.println("Invalid Admin credentials. Attempts remaining: " + (3 - adminLoginAttempts));
                        } else {
                            adminAuthenticated = true;
                            System.out.println("Admin authenticated successfully!\n");
                            admin Admin = new admin();
                            Admin.admin();
                        }
                    }
                    
                    if (!adminAuthenticated) {
                        System.out.println("Admin authentication failed. Returning to Main Menu.\n");
                    }
                    break;

                case 4:
                    System.out.println("\nThank you for using the Student Record Keeping System!");
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid Choice. Try Again!");
            }

        } while (choice != 4);
    }
}