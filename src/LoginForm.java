
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kwize
 */
class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    
    public LoginForm() {
        // Initialize database
        DBConnection.createTables();
        
        setTitle("Tasty Bites Restaurant - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Logo/title panel
        JPanel titlePanel = new JPanel();
        JLabel lblTitle = new JLabel("Tasty Bites Restaurant");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(lblTitle);
        
        // Login panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2, 10, 10));
        
        JLabel lblUsername = new JLabel("Email/Phone:");
        txtUsername = new JTextField(20);
        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(20);
        
        loginPanel.add(lblUsername);
        loginPanel.add(txtUsername);
        loginPanel.add(lblPassword);
        loginPanel.add(txtPassword);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Button actions
        btnLogin.addActionListener(e -> {
            if (validateLogin()) {
                dispose();
                new MainForm(getUserId());
            }
        });
        
        btnRegister.addActionListener(e -> {
            dispose();
            new RegistrationForm();
        });
        
        setVisible(true);
    }
    
    private boolean validateLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password", 
                    "Login Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT id FROM users WHERE (email = ? OR phone = ?) AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", 
                        "Login Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), 
                    "Login Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private int getUserId() {
        String username = txtUsername.getText().trim();
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT id FROM users WHERE email = ? OR phone = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, username);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return -1; // Invalid ID
    }
}