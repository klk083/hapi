package Hapi.SQLMethods;

import java.sql.*

class SQLConnection {

    public static Connection openConnection() {
        String databaseDriver = "com.mysql.jdbc.Driver";
        String username = "kehildre", password = "3kMBJrQ2";

        Connection con = null;
        try {
            Class.forName(databaseDriver);
            String databaseName = "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/" + username + "?user=" + username + "&password=" + password;
            con = DriverManager.getConnection(databaseName);
        } catch (Exception e) {
            String errorMessage = "Exception during connection to database, Code: 8000000";
            writeMessage(e, errorMessage);
        }
        return con;
    }

    public static void closeResSet(ResultSet res) {
        try {
            if (res != null) {
                res.close();
            }
        } catch (SQLException e) {
            writeMessage(e, "closeResSet()");
        }
    }

    public static void closeStatement(Statement stm) {
        try {
            if (stm != null) {
                stm.close();
            }
        } catch (SQLException e) {
            writeMessage(e, "closeStatement()");
        }
    }

    public static void closePreparedStatement(PreparedStatement stm) {
        try {
            if (stm != null) {
                stm.close();
            }
        } catch (SQLException e) {
            writeMessage(e, "closePreparedStatement()");
        }
    }

    public static void closeConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            writeMessage(e, "closeConnection()");
        }
    }

    public static void rollback(Connection con) {
        try {
            if (con != null && !con.getAutoCommit()) {
                con.rollback();
            }
        } catch (SQLException e) {
            writeMessage(e, "rollback()");
        }
    }

    public static void setAutoCommitOn(Connection con) {
        try {
            if (con != null && !con.getAutoCommit()) {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            writeMessage(e, "setAutoCommit()");
        }
    }

    public static void setAutoCommitOff(Connection con) {
        try {
            if (con != null && con.getAutoCommit()) {
                con.setAutoCommit(false);
            }
        } catch (SQLException e) {
            writeMessage(e, "setAutoCommit()");
        }
    }

    public static void writeMessage(Exception e, String message) {
        System.err.println("*** Error occured: " + message + ". ***");
        e.printStackTrace(System.err);
    }
}