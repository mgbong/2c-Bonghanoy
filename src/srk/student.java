package srk;

import config.config;
import java.util.Scanner;

public class student {

    Scanner sc = new Scanner(System.in);
    config db = new config();
    int choice;

    public static void viewStudent() {
        String query = "SELECT * FROM tbl_student";
        String[] headers = {"ID", "Name", "Age", "Gender", "Program", "Year Level"};
        String[] columns = {"s_id", "s_name", "s_age", "s_gender", "s_program", "s_year"};

        config db = new config();
        db.viewRecords(query, headers, columns);
    }

    public static void viewUsers() {
        String query = "SELECT * FROM tbl_user";
        String[] headers = {"ID", "Name", "Email", "Type", "Status"};
        String[] columns = {"u_id", "u_name", "u_email", "u_type", "u_status"};

        config db = new config();
        db.viewRecords(query, headers, columns);
    }

    public static void viewDocument() {
        String query = "SELECT * FROM tbl_document";
        String[] headers = {"ID", "Document Name"};
        String[] columns = {"tid", "tname"};

        config db = new config();
        db.viewRecords(query, headers, columns);
    }

    public static void viewPersonalRecord(int id) {
        System.out.println("\n========================================");
        System.out.println("         YOUR PERSONAL RECORDS");
        System.out.println("========================================");

        String query = "SELECT * FROM tbl_student WHERE s_id = ?";
        String[] headers = {"ID", "Name", "Age", "Gender", "Program", "Year Level"};
        String[] columns = {"s_id", "s_name", "s_age", "s_gender", "s_program", "s_year"};

        config db = new config();

        java.util.List<java.util.Map<String, Object>> records = db.fetchRecords(query, id);

        if (records.isEmpty()) {
            System.out.println("No student record found for your account.");
            System.out.println("Please contact the registrar to create your student record.");
        } else {
            db.viewRecordsWithParams(query, headers, columns, id);
        }
    }

    public static void viewRequiredDocuments(int id) {
    System.out.println("\n========================================");
    System.out.println("DOCUMENTS REQUESTED BY REGISTRAR");
    System.out.println("========================================");
    String query = "SELECT sub_id, tbl_submission.tid, tname, date_requested, status "
            + "FROM tbl_submission "
            + "JOIN tbl_document td ON tbl_submission.tid = td.tid "
            + "WHERE s_id = ?";
    String[] headers = {"Submission ID", "Document ID", "Document Name", "Date Requested", "Status"};
    String[] columns = {"sub_id", "tid", "tname", "date_requested", "status"};

    config db = new config();

    java.util.List<java.util.Map<String, Object>> records = db.fetchRecords(query, id);

    if (records.isEmpty()) {
        System.out.println("No document submission requests found.");
    } else {
        db.viewRecordsWithParams(query, headers, columns, id);
    }

    System.out.println("\nNote: These are documents the registrar has requested you to submit.");
}

public static void viewMyRequests(int id) {
    System.out.println("\n========================================");
    System.out.println("MY DOCUMENT REQUESTS");
    System.out.println("========================================");
    String query = "SELECT r.r_id, d.tname, r.status "
            + "FROM tbl_request r "
            + "JOIN tbl_document d ON r.tid = d.tid "
            + "WHERE r.s_id = ?";
    String[] headers = {"Request ID", "Document Name", "Status"};
    String[] columns = {"r_id", "tname", "status"};

    config db = new config();

    java.util.List<java.util.Map<String, Object>> records = db.fetchRecords(query, id);

    if (records.isEmpty()) {
        System.out.println("You have not made any document requests yet.");
    } else {
        db.viewRecordsWithParams(query, headers, columns, id);
    }
}

    public void student(int id) {
        do {
            System.out.println("\n===== STUDENT MENU =====");
            System.out.println("1. View Personal Records");
            System.out.println("2. Request Documents");
            System.out.println("3. View Required Documents to Submit");
            System.out.println("4. View My Document Requests");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Enter a number (1-5): ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    viewPersonalRecord(id);
                    break;

                case 2:
                    System.out.println("\n=== REQUEST DOCUMENTS ===");
                    System.out.println("Available Documents:");
                    viewDocument();

                    int docId;
                    boolean docExists = false;

                    do {
                        System.out.print("\nEnter Document ID to request (or 0 to cancel): ");
                        while (!sc.hasNextInt()) {
                            System.out.print("Invalid input. Enter a valid Document ID: ");
                            sc.next();
                        }
                        docId = sc.nextInt();
                        sc.nextLine();

                        if (docId == 0) {
                            System.out.println("Request cancelled.");
                            docExists = true;
                            break;
                        }

                        String checkDocSQL = "SELECT * FROM tbl_document WHERE tid = ?";
                        java.util.List<java.util.Map<String, Object>> docs = db.fetchRecords(checkDocSQL, docId);

                        if (docs.isEmpty()) {
                            System.out.println("Document ID not found. Please try again.");
                        } else {
                            docExists = true;
                        }

                    } while (!docExists);

                    if (docId == 0) {
                        break;
                    }

                    // FIXED: Changed did to tid
                    String checkExistingSQL = "SELECT * FROM tbl_request WHERE s_id = ? AND tid = ?";
                    java.util.List<java.util.Map<String, Object>> existing = db.fetchRecords(checkExistingSQL, id, docId);

                    if (!existing.isEmpty()) {
                        System.out.println("You have already requested this document!");
                    } else {
                        // FIXED: Changed did to tid
                        String sqlAddRequest = "INSERT INTO tbl_request (s_id, tid, status) VALUES (?, ?, 'Pending')";
                        db.addRecord(sqlAddRequest, id, docId);

                        System.out.println("Document request submitted successfully!");
                    }
                    break;

                case 3:
                    viewRequiredDocuments(id);
                    break;

                case 4:
                    viewMyRequests(id);
                    break;

                case 5:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

        } while (choice != 5);
    }
}
