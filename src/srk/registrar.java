package srk;

import config.config; 
import java.util.Scanner; 

public class registrar {

    Scanner sc = new Scanner(System.in); 
    config db = new config();
    int choice;

    public static void viewStudent() {
        String votersQuery = "SELECT * FROM tbl_student";
        String[] votersHeaders = {"ID", "Name", "Age", "Gender", "Program", "Year Level"}; //These are the titles that appear above each column when you show student data on screen.
        String[] votersColumns = {"s_id", "s_name", "s_age", "s_gender", "s_program", "s_year"}; //These are the real field names stored in your database (tbl_student) used for fetching data.

        config db = new config();
        db.viewRecords(votersQuery, votersHeaders, votersColumns);
    }

    public static void viewUsers() {
        String votersQuery = "SELECT * FROM tbl_user";
        String[] votersHeaders = {"ID", "Name", "Email", "Type", "Status"};
        String[] votersColumns = {"u_id", "u_name", "u_email", "u_type", "u_status"};

        config db = new config();
        db.viewRecords(votersQuery, votersHeaders, votersColumns);
    }

    public static void viewDocument() {
        String votersQuery = "SELECT * FROM tbl_document";
        String[] votersHeaders = {"ID", "Document Name"};
        String[] votersColumns = {"tid", "tname"};

        config db = new config();
        db.viewRecords(votersQuery, votersHeaders, votersColumns);
    }

    public static void viewRequests() {
        String reqQuery = "SELECT r.r_id, s.s_name, d.tname, r.status " +
                         "FROM tbl_request r " +
                         "JOIN tbl_student s ON r.s_id = s.s_id " +
                         "JOIN tbl_document d ON r.tid = d.tid";
        String[] reqHeaders = {"Request ID", "Student Name", "Document", "Status"};
        String[] reqColumns = {"r_id", "s_name", "tname", "status"};

        config db = new config();
        db.viewRecords(reqQuery, reqHeaders, reqColumns);
    }

    public void registrar() {
        char cont = 'Y';
        int mainChoice;

        System.out.println("\n===== REGISTRAR DASHBOARD =====");

        do {
            System.out.println("\n       REGISTRAR MENU        ");
            System.out.println("1. Manage Student Records");
            System.out.println("2. Document Type");
            System.out.println("3. Process Document Requests");
            System.out.println("4. Document Submission Request");
            System.out.println("5. Exit");
            System.out.print("Enter Choice: ");
            
            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Enter a number (1-5): ");
                sc.next();
            }
            mainChoice = sc.nextInt();
            sc.nextLine();

            switch (mainChoice) {

                case 1:
                    char cont1;
                    do {
                        System.out.println("\n=== MANAGE STUDENT RECORDS ===");
                        System.out.println("1. Add Student Records");
                        System.out.println("2. View Student Records");
                        System.out.println("3. Update Student Records");
                        System.out.println("4. Delete Student Records");
                        System.out.println("5. Exit");
                        System.out.print("Enter Choice: ");
                        
                        while (!sc.hasNextInt()) {
                            System.out.print("Invalid input. Enter a number (1-5): ");
                            sc.next();
                        }
                        choice = sc.nextInt();
                        sc.nextLine();

                        switch (choice) {
                            case 1:
                                System.out.print("Name: ");
                                String name = sc.nextLine().trim();
                                
                                while (name.isEmpty()) {
                                    System.out.print("Name cannot be empty. Name: ");
                                    name = sc.nextLine().trim();
                                }
                                
                                System.out.print("Age: ");
                                while (!sc.hasNextInt()) {
                                    System.out.print("Invalid input. Age: ");
                                    sc.next();
                                }
                                int age = sc.nextInt();
                                sc.nextLine();
                                
                                System.out.print("Gender: ");
                                String gender = sc.nextLine().trim();
                                
                                while (gender.isEmpty()) {
                                    System.out.print("Gender cannot be empty. Gender: ");
                                    gender = sc.nextLine().trim();
                                }
                                
                                System.out.print("Program: ");
                                String program = sc.nextLine().trim();
                                
                                while (program.isEmpty()) {
                                    System.out.print("Program cannot be empty. Program: ");
                                    program = sc.nextLine().trim();
                                }
                                
                                System.out.print("Year Level: ");
                                while (!sc.hasNextInt()) {
                                    System.out.print("Invalid input. Year Level: ");
                                    sc.next();
                                }
                                int year = sc.nextInt();
                                sc.nextLine();

                                String sqlAdd = "INSERT INTO tbl_student (s_name, s_age, s_gender, s_program, s_year) VALUES (?, ?, ?, ?, ?)";
                                db.addRecord(sqlAdd, name, age, gender, program, year);
                                break;

                            case 2:
                                viewStudent();
                                break;

                            case 3:
                                viewStudent();
                                
                                int uid;
                                boolean idFound = false;
                                
                                do {
                                    System.out.print("Enter Student ID to Update: ");
                                    while (!sc.hasNextInt()) {
                                        System.out.print("Invalid input. Enter a valid Student ID: ");
                                        sc.next();
                                    }
                                    uid = sc.nextInt();
                                    sc.nextLine();

                                    String checkSQL = "SELECT * FROM tbl_student WHERE s_id = ?";
                                    java.util.List<java.util.Map<String, Object>> result = db.fetchRecords(checkSQL, uid);

                                    if (result.isEmpty()) {
                                        System.out.println("ID not found, try again.");
                                    } else {
                                        idFound = true;
                                    }
                                } while (!idFound);

                                System.out.print("Name: ");
                                String nname = sc.nextLine().trim();
                                while (nname.isEmpty()) {
                                    System.out.print("Name cannot be empty. Name: ");
                                    nname = sc.nextLine().trim();
                                }
                                
                                System.out.print("Age: ");
                                while (!sc.hasNextInt()) {
                                    System.out.print("Invalid input. Age: ");
                                    sc.next();
                                }
                                int nage = sc.nextInt();
                                sc.nextLine();
                                
                                System.out.print("Gender: ");
                                String ngender = sc.nextLine().trim();
                                while (ngender.isEmpty()) {
                                    System.out.print("Gender cannot be empty. Gender: ");
                                    ngender = sc.nextLine().trim();
                                }
                                
                                System.out.print("Program: ");
                                String nprogram = sc.nextLine().trim();
                                while (nprogram.isEmpty()) {
                                    System.out.print("Program cannot be empty. Program: ");
                                    nprogram = sc.nextLine().trim();
                                }
                                
                                System.out.print("Year Level: ");
                                while (!sc.hasNextInt()) {
                                    System.out.print("Invalid input. Year Level: ");
                                    sc.next();
                                }
                                int nyear = sc.nextInt();
                                sc.nextLine();

                                String sqlUpdate = "UPDATE tbl_student SET s_name = ?, s_age = ?, s_gender = ?, s_program = ?, s_year = ? WHERE s_id = ?";
                                db.updateRecord(sqlUpdate, nname, nage, ngender, nprogram, nyear, uid);
                                break;

                            case 4:
                                viewStudent();
                                
                                int did;
                                boolean deleteFound = false;
                                
                                do {
                                    System.out.print("Enter Student ID to Delete: ");
                                    while (!sc.hasNextInt()) {
                                        System.out.print("Invalid input. Enter a valid Student ID: ");
                                        sc.next();
                                    }
                                    did = sc.nextInt();
                                    sc.nextLine();

                                    String checkSQL = "SELECT * FROM tbl_student WHERE s_id = ?";
                                    java.util.List<java.util.Map<String, Object>> result = db.fetchRecords(checkSQL, did);

                                    if (result.isEmpty()) {
                                        System.out.println("ID not found, try again.");
                                    } else {
                                        deleteFound = true;
                                    }
                                } while (!deleteFound);

                                String sqlDelete = "DELETE FROM tbl_student WHERE s_id = ?";
                                db.deleteRecord(sqlDelete, did);
                                break;

                            case 5:
                                System.out.println("Exiting Manage Student Records Menu...");
                                break;

                            default:
                                System.out.println("Invalid choice.");
                        }

                        if (choice == 5) {
                            cont1 = 'N';
                        } else {
                            System.out.print("Do you want to continue? (Y/N): ");
                            cont1 = sc.next().toUpperCase().charAt(0);
                            sc.nextLine();
                        }
                    } while (cont1 == 'Y');
                    break;

                case 2:
                    char cont2;
                    do {
                        System.out.println("\n=== DOCUMENT TYPE ===");
                        System.out.println("1. Add Document");
                        System.out.println("2. View Document");
                        System.out.println("3. Update Document");
                        System.out.println("4. Remove Document");
                        System.out.println("5. Exit");
                        System.out.print("Enter Choice: ");
                        
                        while (!sc.hasNextInt()) {
                            System.out.print("Invalid input. Enter a number (1-5): ");
                            sc.next();
                        }
                        choice = sc.nextInt();
                        sc.nextLine();

                        switch (choice) {
                            case 1:
                                System.out.print("Enter Document Name: ");
                                String name = sc.nextLine().trim();
                                
                                while (name.isEmpty()) {
                                    System.out.print("Document name cannot be empty. Enter Document Name: ");
                                    name = sc.nextLine().trim();
                                }
                                
                                String checkDupSQL = "SELECT * FROM tbl_document WHERE tname = ?";
                                java.util.List<java.util.Map<String, Object>> dupCheck = db.fetchRecords(checkDupSQL, name);
                                
                                if (!dupCheck.isEmpty()) {
                                    System.out.println("Document with this name already exists!");
                                    break;
                                }

                                String sqlAdd = "INSERT INTO tbl_document (tname) VALUES (?)";
                                db.addRecord(sqlAdd, name);
                                break;

                            case 2:
                                viewDocument();
                                break;

                            case 3:
                                viewDocument();

                                int tid;
                                boolean docFound = false;
                                
                                do {
                                    System.out.print("Enter Document ID to Update: ");
                                    while (!sc.hasNextInt()) {
                                        System.out.print("Invalid input. Enter a valid Document ID: ");
                                        sc.next();
                                    }
                                    tid = sc.nextInt();
                                    sc.nextLine();

                                    String checkSQL = "SELECT * FROM tbl_document WHERE tid = ?";
                                    java.util.List<java.util.Map<String, Object>> result = db.fetchRecords(checkSQL, tid);

                                    if (result.isEmpty()) {
                                        System.out.println("ID not found, try again.");
                                    } else {
                                        docFound = true;
                                    }
                                } while (!docFound);

                                System.out.print("Enter Document Name: ");
                                String nname = sc.nextLine().trim();
                                
                                while (nname.isEmpty()) {
                                    System.out.print("Document name cannot be empty. Enter Document Name: ");
                                    nname = sc.nextLine().trim();
                                }

                                String sqlUpdate = "UPDATE tbl_document SET tname = ? WHERE tid = ?";
                                db.updateRecord(sqlUpdate, nname, tid);
                                break;

                            case 4:
                                viewDocument();
                                
                                int delId;
                                boolean docDeleteFound = false;
                                
                                do {
                                    System.out.print("Enter Document ID to Delete: ");
                                    while (!sc.hasNextInt()) {
                                        System.out.print("Invalid input. Enter a valid Document ID: ");
                                        sc.next();
                                    }
                                    delId = sc.nextInt();
                                    sc.nextLine();
                                    
                                    String checkSQL = "SELECT * FROM tbl_document WHERE tid = ?";
                                    java.util.List<java.util.Map<String, Object>> result = db.fetchRecords(checkSQL, delId);

                                    if (result.isEmpty()) {
                                        System.out.println("ID not found, try again.");
                                    } else {
                                        docDeleteFound = true;
                                    }
                                } while (!docDeleteFound);
                                
                                String sqlDelete = "DELETE FROM tbl_document WHERE tid = ?";
                                db.deleteRecord(sqlDelete, delId);
                                break;

                            case 5:
                                System.out.println("Exiting Document Type Menu...");
                                break;

                            default:
                                System.out.println("Invalid choice. Try Again!");
                        }
                        if (choice == 5) {
                            cont2 = 'N';
                        } else {
                            System.out.print("Do you want to continue in Document Type Menu? (Y/N): ");
                            cont2 = sc.next().toUpperCase().charAt(0);
                            sc.nextLine();
                        }
                    } while (cont2 == 'Y');
                    break;

                case 3:
                    System.out.println("\n=== PROCESS DOCUMENT REQUESTS ===");

                    char cont3;
                    do {
                        viewRequests();
                        
                        String pendingQuery = "SELECT r_id FROM tbl_request WHERE status = 'Pending'";
                        java.util.List<java.util.Map<String, Object>> pendingReqs = db.fetchRecords(pendingQuery);
                        
                        if (pendingReqs.isEmpty()) {
                            System.out.println("\nNo pending requests to process.");
                            break;
                        }

                        int reqId;
                        boolean reqFound = false;

                        do {
                            System.out.print("\nEnter Request ID to process: ");
                            while (!sc.hasNextInt()) {
                                System.out.print("Invalid input. Enter a valid Request ID: ");
                                sc.next();
                            }
                            reqId = sc.nextInt();
                            sc.nextLine();

                            String checkSQL = "SELECT * FROM tbl_request WHERE r_id = ?";
                            java.util.List<java.util.Map<String, Object>> records = db.fetchRecords(checkSQL, reqId);

                            if (records.isEmpty()) {
                                System.out.println("ID not found. Please try again.");
                            } else {
                                reqFound = true;
                            }
                        } while (!reqFound);

                        System.out.println("\n1. Approve Request");
                        System.out.println("2. Reject Request");
                        System.out.print("Enter choice: ");
                        
                        while (!sc.hasNextInt()) {
                            System.out.print("Invalid input. Enter 1 or 2: ");
                            sc.next();
                        }
                        choice = sc.nextInt();
                        sc.nextLine();

                        String newStatus = "";
                        if (choice == 1) {
                            newStatus = "Approved";
                        } else if (choice == 2) {
                            newStatus = "Rejected";
                        } else {
                            System.out.println("Invalid choice.");
                            cont3 = 'N';
                            break;
                        }

                        String updateSQL = "UPDATE tbl_request SET status = ? WHERE r_id = ?";
                        db.updateRecord(updateSQL, newStatus, reqId);

                        System.out.println("Request status updated to " + newStatus + "!");

                        System.out.print("\nDo you want to process another request? (Y/N): ");
                        cont3 = sc.next().toUpperCase().charAt(0);
                        sc.nextLine();

                    } while (cont3 == 'Y');
                    break;

                case 4:
                    System.out.println("\n=== DOCUMENT SUBMISSION REQUEST ===");

                    char cont4;
                    do {
                        viewStudent();
                        
                        int studentId;
                        boolean studentExists = false;

                        do {
                            System.out.print("\nEnter Student ID: ");
                            while (!sc.hasNextInt()) {
                                System.out.print("Invalid input. Enter a valid Student ID: ");
                                sc.next();
                            }
                            studentId = sc.nextInt();
                            sc.nextLine();

                            String checkStudentSQL = "SELECT * FROM tbl_student WHERE s_id = ?";
                            java.util.List<java.util.Map<String, Object>> students = db.fetchRecords(checkStudentSQL, studentId);

                            if (students.isEmpty()) {
                                System.out.println("Student ID not found. Please try again.");
                            } else {
                                studentExists = true;
                            }
                        } while (!studentExists);

                        viewDocument();
                        
                        int docId;
                        boolean docExists = false;

                        do {
                            System.out.print("\nEnter Document ID: ");
                            while (!sc.hasNextInt()) {
                                System.out.print("Invalid input. Enter a valid Document ID: ");
                                sc.next();
                            }
                            docId = sc.nextInt();
                            sc.nextLine();

                            String checkDocSQL = "SELECT * FROM tbl_document WHERE tid = ?";
                            java.util.List<java.util.Map<String, Object>> docs = db.fetchRecords(checkDocSQL, docId);

                            if (docs.isEmpty()) {
                                System.out.println("Document ID not found. Please try again.");
                            } else {
                                docExists = true;
                            }
                        } while (!docExists);

                        String checkExistingSQL = "SELECT * FROM tbl_submission WHERE s_id = ? AND tid = ?";
                        java.util.List<java.util.Map<String, Object>> existing = db.fetchRecords(checkExistingSQL, studentId, docId);

                        if (!existing.isEmpty()) {
                            System.out.println("Submission request already exists for this student and document!");
                        } else {
                            java.sql.Date dateRequested = new java.sql.Date(System.currentTimeMillis());
                            String status = "Pending";

                            String sqlAddRequest = "INSERT INTO tbl_submission (s_id, tid, date_requested, status) VALUES (?, ?, ?, ?)";
                            db.addRecord(sqlAddRequest, studentId, docId, dateRequested, status);
                        }

                        System.out.print("\nDo you want to add another request? (Y/N): ");
                        cont4 = sc.next().toUpperCase().charAt(0);
                        sc.nextLine();

                    } while (cont4 == 'Y');

                    break;

                case 5:
                    System.out.println("Exiting Registrar Dashboard...");
                    cont = 'N';
                    break;

                default:
                    System.out.println("Invalid choice. Try again!");
            }

        } while (cont == 'Y');
    }
}