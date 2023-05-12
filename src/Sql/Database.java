package Sql;
import java.sql.*;

class AccountDatabase {
    private Connection conn;

    // Constructor to initialize the database connection
    public AccountDatabase(String url, String username, String password) throws SQLException {
        conn = DriverManager.getConnection(url, username, password);
        createTable();
    }

    // Method to create the Account table
    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Account ("
                + "account_no INT(11) PRIMARY KEY,"
                + "name VARCHAR(50),"
                + "balance FLOAT,"
                + "pin INT(4),"
                + "info VARCHAR(100)"
                + ")";
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    // Method to add a new record to the Account table
    public void addRecord(int account_no, String name, float balance, int pin, String info) throws SQLException {
        String sql = "INSERT INTO Account (account_no, name, balance, pin, info) "
                + "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, account_no);
        pstmt.setString(2, name);
        pstmt.setFloat(3, balance);
        pstmt.setInt(4, pin);
        pstmt.setString(5, info);
        pstmt.executeUpdate();
    }

    // Method to delete a record from the Account table by account_no
    public void deleteRecord(int account_no) throws SQLException {
        String sql = "DELETE FROM Account WHERE account_no=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, account_no);
        pstmt.executeUpdate();
    }
    //Method to retrieve data from database
    public ResultSet getRecord(int account_no, int pin) throws SQLException {
        String sql = "SELECT * FROM Account WHERE account_no=? AND pin=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, account_no);
        pstmt.setInt(2, pin);
        ResultSet rs = pstmt.executeQuery();
        return rs;
    }
    

    // Method to update a record in the Account table by account_no
    public void updateRecord(int account_no, String name, float balance, int pin, String info) throws SQLException {
        String sql = "UPDATE Account SET name=?, balance=?, pin=?, info=? WHERE account_no=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.setFloat(2, balance);
        pstmt.setInt(3, pin);
        pstmt.setString(4, info);
        pstmt.setInt(5, account_no);
        pstmt.executeUpdate();
    }
}


class ATMsimulation{
    public static void main(String[] args) { 
        try { 
            // Create a new AccountDatabase object 
            AccountDatabase db = new AccountDatabase("jdbc:mysql://localhost:3306/mydb", "root", "root"); 
    
            // Add a new record to the Account table 
            db.addRecord(123456789, "John Doe", 1000.0f, 1234, "Some info"); 
            db.addRecord(123456790, "Johny Doey", 1009.0f, 1134, "ome info"); 
    
            // Retrieve the record with account_no=123456789 and pin=1234
            ResultSet rs = db.getRecord(123456789, 1234);
            if (rs.next()) {
                // The record exists, so we can retrieve its values
                int account_no = rs.getInt("account_no");
                String name = rs.getString("name");
                float balance = rs.getFloat("balance");
                int pin = rs.getInt("pin");
                String info = rs.getString("info");
                System.out.println("Account details: ");
                System.out.println("Account no: " + account_no);
                System.out.println("Name: " + name);
                System.out.println("Balance: " + balance);
                System.out.println("PIN: " + pin);
                System.out.println("Info: " + info);
            } else {
                // The record doesn't exist, so we can notify the user
                System.out.println("Invalid account no or PIN");
            }
    
            // Update the record with account_no=123456789 
            db.updateRecord(123456789, "Jane Doe", 2000.0f, 5678, "Updated info"); 
    
            // Delete the record with account_no=123456789 
            db.deleteRecord(123456789); 
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } 
    }
    
    
}