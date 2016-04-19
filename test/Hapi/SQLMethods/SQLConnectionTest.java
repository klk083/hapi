package Hapi.SQLMethods;

import org.junit.Test;

import static org.junit.Assert.*;
import java.sql.*;

/**
 * Created by magnu on 18.04.2016.
 */
public class SQLConnectionTest {
    @Test
    public void openConnection() throws Exception {
        Connection con = SQLConnection.openConnection();

        boolean testRes = (con != null);

        assertEquals(true, testRes);
        con.close();
    }

    @Test
    public void closeResSet() throws Exception {
        Connection con = SQLConnection.openConnection();
        PreparedStatement stm = con.prepareStatement("SELECT * FROM role");
        ResultSet res = stm.executeQuery();
        boolean testRes1 = (res != null);

        assertEquals(true, testRes1);


        SQLConnection.closeResSet(res);
        boolean testRes2 = false;
        // Easiest way to test if a ResultSet is closed, is to catch the exception which occurs when .next() is called
        // on a closed ResultSet
        try {
            res.next();
        } catch (SQLException e) {
            testRes2 = true;
        }

        assertEquals(true, testRes2);
        con.close();
        stm.close();
    }

    @Test
    public void closeStatement() throws Exception {
        Connection con = SQLConnection.openConnection();
        Statement stm = con.createStatement();
        boolean testRes1 = (stm != null);

        assertEquals(true, testRes1);


        SQLConnection.closeStatement(stm);
        boolean testRes2 = false;
        try {
            stm.getConnection();
        } catch (SQLException e) {
            testRes2 = true;
        }


        assertEquals(true, testRes2);
        con.close();
    }

    @Test
    public void closePreparedStatement() throws Exception {
        Connection con = SQLConnection.openConnection();
        PreparedStatement stm = con.prepareStatement("SELECT * FROM role");
        boolean testRes1 = (stm != null);

        assertEquals(true, testRes1);


        SQLConnection.closeStatement(stm);
        boolean testRes2 = false;
        try {
            stm.executeQuery();
        } catch (SQLException e) {
            testRes2 = true;
        }


        assertEquals(true, testRes2);
        con.close();
    }

    @Test
    public void closeConnection() throws Exception {
        Connection con = SQLConnection.openConnection();
        boolean testRes1 = (con != null);

        assertEquals(true, testRes1);


        SQLConnection.closeConnection(con);
        boolean testRes2 = con.isClosed();

        assertEquals(true, testRes2);
    }

    @Test
    public void rollback() throws Exception {
        String testRole = "test role";
        Connection con = SQLConnection.openConnection();
        SQLConnection.setAutoCommitOff(con);

        PreparedStatement stm = con.prepareStatement("INSERT INTO role VALUES(DEFAULT, ?)");
        stm.setString(1, testRole);
        stm.executeUpdate();

        SQLConnection.rollback(con);

        stm = con.prepareStatement("SELECT role FROM role WHERE role = ?");
        stm.setString(1, testRole);
        ResultSet res = stm.executeQuery();

        boolean testRes;
        if (res.next()) {
            testRes = true;
        } else {
            testRes = false;
        }

        assertEquals(false, testRes);
    }

    @Test
    public void setAutoCommitOn() throws Exception {
        Connection con = SQLConnection.openConnection();
        SQLConnection.setAutoCommitOn(con);
        boolean testRes = con.getAutoCommit();

        assertEquals(true, testRes);
    }

    @Test
    public void setAutoCommitOff() throws Exception {
        Connection con = SQLConnection.openConnection();
        SQLConnection.setAutoCommitOff(con);
        boolean testRes = con.getAutoCommit();

        assertEquals(false, testRes);
    }
}