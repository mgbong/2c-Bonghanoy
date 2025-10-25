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
                            System.out.println("Account Status: " + stat + ". Kindly Reach Out to the Admin!");
                        } else {
                            System.out.println("LOGIN SUCCESS!");
                            int userId = Integer.parseInt(user.get("u_id").toString());

                            if (type.equals("Student")) {
                                Object studentIdObj = user.get("s_id");
                                Integer studentId = null;

                                if (studentIdObj != null && !studentIdObj.toString().isEmpty()) {
                                    studentId = Integer.parseInt(studentIdObj.toString());
                                }

                                if (studentId == null) {
                                    System.out.println("\n=== FIRST TIME SETUP ===");
                                    System.out.println("Please link your account to your student record.");

                                    student.viewStudent();

                                    boolean validStudentId = false;
                                    while (!validStudentId) {
                                        System.out.print("\nEnter your Student ID from the list above: ");
                                        while (!sc.hasNextInt()) {
                                            System.out.print("Invalid input. Enter a valid Student ID: ");
                                            sc.next();
                                        }
                                        int inputStudentId = sc.nextInt();
                                        sc.nextLine();

                                        String checkStudentSQL = "SELECT * FROM tbl_student WHERE s_id = ?";
                                        java.util.List<java.util.Map<String, Object>> studentCheck = db.fetchRecords(checkStudentSQL, inputStudentId);

                                        if (studentCheck.isEmpty()) {
                                            System.out.println("Student ID not found. Please try again.");
                                        } else {
                                            String checkLinkSQL = "SELECT * FROM tbl_user WHERE s_id = ? AND u_id != ?";
                                            java.util.List<java.util.Map<String, Object>> linkCheck = db.fetchRecords(checkLinkSQL, inputStudentId, userId);

                                            if (!linkCheck.isEmpty()) {
                                                System.out.println("This Student ID is already linked to another account. Please choose a different ID.");
                                            } else {
                                                String updateUserSQL = "UPDATE tbl_user SET s_id = ? WHERE u_id = ?";
                                                db.updateRecord(updateUserSQL, inputStudentId, userId);
                                                studentId = inputStudentId;
                                                validStudentId = true;
                                                System.out.println("Student ID linked successfully!");
                                            }
                                        }
                                    }
                                }

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
                            System.out.print("Email already exists. Enter other Email: ");
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
                    admin Admin = new admin();
                    Admin.admin();
                    break;

                case 4:
                    System.out.println("\nThank you for using Student Record Keeping System!");
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid Choice. Try Again!");
            }

        } while (choice != 4);
    }
}
