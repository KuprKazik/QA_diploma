package data;

import java.sql.*;

public class DataSQL {

    public static class MySQL {

        public static String getStatusFromPaymentTable(String status) throws SQLException {

            String url = "jdbc:mysql://localhost:3306/app";
            String user = "app";
            String password = "pass";

            String selectQuery = "SELECT status FROM payment_entity WHERE status = ?";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(selectQuery)) {

                stmt.setString(1, status);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("status");
                    } else {
                        return null;
                    }
                }
            }
        }

        public static String getStatusFromCreditTable(String status) throws SQLException {

            String url = "jdbc:mysql://localhost:3306/app";
            String user = "app";
            String password = "pass";

            String selectQuery = "SELECT status FROM credit_request_entity WHERE status = ?";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(selectQuery)) {

                stmt.setString(1, status);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("status");
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    public static class PostgreSQL {

        public static String getStatusFromPaymentTable(String status) throws SQLException {

            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "password";

            String selectQuery = "SELECT status FROM payment_entity WHERE status = ?";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(selectQuery)) {

                stmt.setString(1, status);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("status");
                    } else {
                        return null;
                    }
                }
            }
        }

        public static String getStatusFromCreditTable(String status) throws SQLException {

            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "password";

            String selectQuery = "SELECT status FROM credit_request_entity WHERE status = ?";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(selectQuery)) {

                stmt.setString(1, status);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("status");
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    public static class DatabaseCleaner {

        private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/app";
        private static final String MYSQL_USER = "app";
        private static final String MYSQL_PASSWORD = "pass";

        private static final String POSTGRES_URL = "jdbc:postgresql://localhost:5432/postgres";
        private static final String POSTGRES_USER = "postgres";
        private static final String POSTGRES_PASSWORD = "password";


        private static void deleteDataFromTable(Connection conn, String tableName) throws SQLException {
            String deleteQuery = "DELETE FROM " + tableName;
        }

        public static void deleteAllData(Connection conn) throws SQLException {
            deleteDataFromTable(conn, "payment_entity");
            deleteDataFromTable(conn, "order_entity");
            deleteDataFromTable(conn, "credit_request_entity");
        }

        public static void deleteAllDataFromMySQL() throws SQLException {
            try (Connection conn = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD)) {
                deleteAllData(conn);
            }
        }

        public static void deleteAllDataFromPostgres() throws SQLException {
            try (Connection conn = DriverManager.getConnection(POSTGRES_URL, POSTGRES_USER, POSTGRES_PASSWORD)) {
                deleteAllData(conn);
            }
        }

    }
}






