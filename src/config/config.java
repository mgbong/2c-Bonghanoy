package config;

import java.sql.*;

public class config {

    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:registrar.db");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }

    public void addRecord(String sql, Object... values) {
        try (Connection conn = this.connectDB();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values[i]);
                } else if (values[i] instanceof Double) {
                    pstmt.setDouble(i + 1, (Double) values[i]);
                } else if (values[i] instanceof Float) {
                    pstmt.setFloat(i + 1, (Float) values[i]);
                } else if (values[i] instanceof Long) {
                    pstmt.setLong(i + 1, (Long) values[i]);
                } else if (values[i] instanceof Boolean) {
                    pstmt.setBoolean(i + 1, (Boolean) values[i]);
                } else if (values[i] instanceof java.util.Date) {
                    pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime()));
                } else if (values[i] instanceof java.sql.Date) {
                    pstmt.setDate(i + 1, (java.sql.Date) values[i]);
                } else if (values[i] instanceof java.sql.Timestamp) {
                    pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]);
                } else {
                    pstmt.setString(i + 1, values[i].toString());
                }
            }

            pstmt.executeUpdate();
            System.out.println("Record added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }

    // FIXED: Dynamic view method with proper column width calculation
    public void viewRecords(String sqlQuery, String[] columnHeaders, String[] columnNames) {
        if (columnHeaders.length != columnNames.length) {
            System.out.println("Error: Mismatch between column headers and column names.");
            return;
        }

        try (Connection conn = this.connectDB();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
                ResultSet rs = pstmt.executeQuery()) {

            // Calculate dynamic column widths
            int[] columnWidths = new int[columnHeaders.length];
            java.util.List<String[]> rowData = new java.util.ArrayList<>();

            // Initialize with header lengths
            for (int i = 0; i < columnHeaders.length; i++) {
                columnWidths[i] = columnHeaders[i].length();
            }

            // Read all data and calculate max widths
            while (rs.next()) {
                String[] row = new String[columnNames.length];
                for (int i = 0; i < columnNames.length; i++) {
                    String value = rs.getString(columnNames[i]);
                    row[i] = value != null ? value : "";
                    columnWidths[i] = Math.max(columnWidths[i], row[i].length());
                }
                rowData.add(row);
            }

            // Add padding to column widths
            for (int i = 0; i < columnWidths.length; i++) {
                columnWidths[i] += 2; // Add 2 spaces padding
            }

            // Calculate total width
            int totalWidth = 1; // Start with 1 for initial |
            for (int width : columnWidths) {
                totalWidth += width + 3; // width + " | "
            }

            // Print top separator
            printLine(totalWidth);

            // Print headers
            System.out.print("| ");
            for (int i = 0; i < columnHeaders.length; i++) {
                System.out.print(padRight(columnHeaders[i], columnWidths[i]));
                System.out.print(" | ");
            }
            System.out.println();

            // Print separator
            printLine(totalWidth);

            // Print data rows
            for (String[] row : rowData) {
                System.out.print("| ");
                for (int i = 0; i < row.length; i++) {
                    System.out.print(padRight(row[i], columnWidths[i]));
                    System.out.print(" | ");
                }
                System.out.println();
            }

            // Print bottom separator
            printLine(totalWidth);

        } catch (SQLException e) {
            System.out.println("Error retrieving records: " + e.getMessage());
        }
    }

    // Helper method to print separator line
    private void printLine(int width) {
        for (int i = 0; i < width; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    // Helper method to pad string to right
    private String padRight(String text, int length) {
        if (text.length() >= length) {
            return text.substring(0, length);
        }
        StringBuilder sb = new StringBuilder(text);
        while (sb.length() < length) {
            sb.append(" ");
        }
        return sb.toString();
    }

    public void updateRecord(String sql, Object... values) {
        try (Connection conn = this.connectDB();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values[i]);
                } else if (values[i] instanceof Double) {
                    pstmt.setDouble(i + 1, (Double) values[i]);
                } else if (values[i] instanceof Float) {
                    pstmt.setFloat(i + 1, (Float) values[i]);
                } else if (values[i] instanceof Long) {
                    pstmt.setLong(i + 1, (Long) values[i]);
                } else if (values[i] instanceof Boolean) {
                    pstmt.setBoolean(i + 1, (Boolean) values[i]);
                } else if (values[i] instanceof java.util.Date) {
                    pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime()));
                } else if (values[i] instanceof java.sql.Date) {
                    pstmt.setDate(i + 1, (java.sql.Date) values[i]);
                } else if (values[i] instanceof java.sql.Timestamp) {
                    pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]);
                } else {
                    pstmt.setString(i + 1, values[i].toString());
                }
            }

            pstmt.executeUpdate();
            System.out.println("Record updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }

    public void deleteRecord(String sql, Object... values) {
        try (Connection conn = this.connectDB();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values[i]);
                } else {
                    pstmt.setString(i + 1, values[i].toString());
                }
            }

            pstmt.executeUpdate();
            System.out.println("Record deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }

    public java.util.List<java.util.Map<String, Object>> fetchRecords(String sqlQuery, Object... values) {
        java.util.List<java.util.Map<String, Object>> records = new java.util.ArrayList<>();

        try (Connection conn = this.connectDB();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                java.util.Map<String, Object> row = new java.util.HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                records.add(row);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching records: " + e.getMessage());
        }

        return records;
    }

    public String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println("Error hashing password: " + e.getMessage());
            return null;
        }
    }

    // FIXED: viewRecordsWithParams with proper formatting
    public void viewRecordsWithParams(String query, String[] headers, String[] columns, Object... params) {
        try (Connection conn = this.connectDB();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Bind parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            ResultSet rs = pstmt.executeQuery();

            // Calculate dynamic column widths
            int[] columnWidths = new int[headers.length];
            java.util.List<String[]> rowData = new java.util.ArrayList<>();

            // Initialize with header lengths
            for (int i = 0; i < headers.length; i++) {
                columnWidths[i] = headers[i].length();
            }

            // Read all data and calculate max widths
            while (rs.next()) {
                String[] row = new String[columns.length];
                for (int i = 0; i < columns.length; i++) {
                    Object value = rs.getObject(columns[i]);
                    row[i] = value != null ? value.toString() : "";
                    columnWidths[i] = Math.max(columnWidths[i], row[i].length());
                }
                rowData.add(row);
            }

            // Add padding to column widths
            for (int i = 0; i < columnWidths.length; i++) {
                columnWidths[i] += 2;
            }

            // Calculate total width
            int totalWidth = 1;
            for (int width : columnWidths) {
                totalWidth += width + 3;
            }

            // Print top separator
            printLine(totalWidth);

            // Print headers
            System.out.print("| ");
            for (int i = 0; i < headers.length; i++) {
                System.out.print(padRight(headers[i], columnWidths[i]));
                System.out.print(" | ");
            }
            System.out.println();

            // Print separator
            printLine(totalWidth);

            // Check if there are any results
            if (rowData.isEmpty()) {
                System.out.println("No records found.");
            } else {
                // Print data rows
                for (String[] row : rowData) {
                    System.out.print("| ");
                    for (int i = 0; i < row.length; i++) {
                        System.out.print(padRight(row[i], columnWidths[i]));
                        System.out.print(" | ");
                    }
                    System.out.println();
                }
            }

            // Print bottom separator
            printLine(totalWidth);

        } catch (SQLException e) {
            System.out.println("Error retrieving records: " + e.getMessage());
        }
    }
}