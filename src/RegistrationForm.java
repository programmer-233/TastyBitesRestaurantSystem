
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
import java.sql.DriverManager;
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
class RegistrationForm extends JFrame {
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtLocation;
    
    public RegistrationForm() {
        setTitle("Tasty Bites Restaurant - Registration");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Logo/title panel
        JPanel titlePanel = new JPanel();
        JLabel lblTitle = new JLabel("Create New Account");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(lblTitle);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2, 10, 10));
        
        JLabel lblName = new JLabel("Name:");
        txtName = new JTextField(20);
        JLabel lblPhone = new JLabel("Phone Number:");
        txtPhone = new JTextField(20);
        JLabel lblEmail = new JLabel("Email:");
        txtEmail = new JTextField(20);
        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(20);
        JLabel lblConfirmPassword = new JLabel("Confirm Password:");
        txtConfirmPassword = new JPasswordField(20);
        JLabel lblLocation = new JLabel("Location:");
        txtLocation = new JTextField(20);
        
        formPanel.add(lblName);
        formPanel.add(txtName);
        formPanel.add(lblPhone);
        formPanel.add(txtPhone);
        formPanel.add(lblEmail);
        formPanel.add(txtEmail);
        formPanel.add(lblPassword);
        formPanel.add(txtPassword);
        formPanel.add(lblConfirmPassword);
        formPanel.add(txtConfirmPassword);
        formPanel.add(lblLocation);
        formPanel.add(txtLocation);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnRegister = new JButton("Register");
        JButton btnCancel = new JButton("Cancel");
        
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Button actions
        btnRegister.addActionListener(e -> {
            if (validateRegistration()) {
                registerUser();
                dispose();
                new LoginForm();
            }
        });
        
        btnCancel.addActionListener(e -> {
            dispose();
            new LoginForm();
        });
        
        setVisible(true);
    }
    
    private boolean validateRegistration() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String location = txtLocation.getText().trim();
        
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || 
                password.isEmpty() || confirmPassword.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", 
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", 
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address", 
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void registerUser() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String location = txtLocation.getText().trim();
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO users (name, phone, email, password, location) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, location);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful! Please login.", 
                        "Registration Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", 
                        "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                if (e.getMessage().contains("email")) {
                    JOptionPane.showMessageDialog(this, "Email already registered!", 
                            "Registration Error", JOptionPane.ERROR_MESSAGE);
                } else if (e.getMessage().contains("phone")) {
                    JOptionPane.showMessageDialog(this, "Phone number already registered!", 
                            "Registration Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), 
                        "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}