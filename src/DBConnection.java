
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author kwize
 */
class DBConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tasty_bites";
    private static final String USER = "root";
    private static final String PASS = "o@YmD^fAl&y0ccBspe";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
    
    // SQL for creating tables
    public static void createTables() {
        try (Connection conn = getConnection()) {
            Statement stmt = conn.createStatement();
            
            // Create users table
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "phone VARCHAR(20) NOT NULL UNIQUE," +
                    "email VARCHAR(100) NOT NULL UNIQUE," +
                    "password VARCHAR(100) NOT NULL," +
                    "location VARCHAR(200) NOT NULL," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            stmt.execute(createUsersTable);
            
            // Create meals table
            String createMealsTable = "CREATE TABLE IF NOT EXISTS meals (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "price_only DECIMAL(10,2) NOT NULL," +
                    "price_with_juice DECIMAL(10,2) NOT NULL," +
                    "price_with_water DECIMAL(10,2) NOT NULL," +
                    "available BOOLEAN DEFAULT TRUE" +
                    ")";
            stmt.execute(createMealsTable);
            
            // Insert default meals if table is empty
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM meals");
            rs.next();
            if (rs.getInt(1) == 0) {
                String insertMeals = "INSERT INTO meals (name, price_only, price_with_juice, price_with_water) VALUES " +
                        "('Assorted Fried rice with Grilled Chicken', 100.00, 120.00, 110.00)," +
                        "('Banku with grilled tilapia', 120.00, 140.00, 130.00)," +
                        "('Fufu with goat soup', 80.00, 100.00, 110.00)," +
                        "('Rice balls with Groundnut soup', 85.00, 105.00, 100.00)";
                stmt.execute(insertMeals);
            }
            
            // Create orders table
            String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT," +
                    "name VARCHAR(100) NOT NULL," +
                    "phone VARCHAR(20) NOT NULL," +
                    "location VARCHAR(200) NOT NULL," +
                    "total_amount DECIMAL(10,2) NOT NULL," +
                    "discount_amount DECIMAL(10,2) NOT NULL," +
                    "final_amount DECIMAL(10,2) NOT NULL," +
                    "order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL" +
                    ")";
            stmt.execute(createOrdersTable);
            
            // Create order_items table
            String createOrderItemsTable = "CREATE TABLE IF NOT EXISTS order_items (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "order_id INT NOT NULL," +
                    "meal_id INT NOT NULL," +
                    "meal_name VARCHAR(100) NOT NULL," +
                    "drink_option VARCHAR(50) NOT NULL," +
                    "quantity INT NOT NULL," +
                    "price DECIMAL(10,2) NOT NULL," +
                    "subtotal DECIMAL(10,2) NOT NULL," +
                    "FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE" +
                    ")";
            stmt.execute(createOrderItemsTable);
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error setting up database: " + e.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}