

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kwize
 */
class MainForm extends JFrame {
    private int userId;
    private JComboBox<Meal> cboMeals;
    private JComboBox<String> cboDrinkOption;
    private JSpinner spinQuantity;
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtLocation;
    private JTable tblOrderItems;
    private DefaultTableModel tableModel;
    private List<OrderItem> orderItems;
    private Map<Integer, Meal> mealsMap;
    
    public MainForm(int userId) {
        this.userId = userId;
        this.orderItems = new ArrayList<>();
        this.mealsMap = new HashMap<>();
        
        setTitle("Tasty Bites Restaurant - Order System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title panel
        JPanel titlePanel = new JPanel();
        JLabel lblTitle = new JLabel("Tasty Bites Restaurant - Order System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(lblTitle);
        
        // Content panel (split into left and right)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(1, 2, 10, 10));
        
        // Left panel (Customer Info & Meal Selection)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout(5, 5));
        
        // Customer info panel
        JPanel customerPanel = new JPanel();
        customerPanel.setLayout(new GridLayout(6, 2, 5, 5));
        customerPanel.setBorder(BorderFactory.createTitledBorder("Customer Information"));
        
        JLabel lblName = new JLabel("Name:");
        txtName = new JTextField(20);
        JLabel lblPhone = new JLabel("Phone:");
        txtPhone = new JTextField(20);
        JLabel lblLocation = new JLabel("Location:");
        txtLocation = new JTextField(20);
        
        customerPanel.add(lblName);
        customerPanel.add(txtName);
        customerPanel.add(lblPhone);
        customerPanel.add(txtPhone);
        customerPanel.add(lblLocation);
        customerPanel.add(txtLocation);
        
        // If user is logged in, fill in the details
        if (userId > 0) {
            fillUserDetails();
        }
        
        // Meal selection panel
        JPanel mealPanel = new JPanel();
        mealPanel.setLayout(new GridLayout(5, 2, 5, 5));
        mealPanel.setBorder(BorderFactory.createTitledBorder("Meal Selection"));
        
        JLabel lblMeal = new JLabel("Select Meal:");
        cboMeals = new JComboBox<>();
        loadMeals();
        
        JLabel lblDrinkOption = new JLabel("Drink Option:");
        cboDrinkOption = new JComboBox<>(new String[]{"None", "Apple Juice", "Water"});
        
        JLabel lblQuantity = new JLabel("Quantity:");
        spinQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        
        JButton btnAddToOrder = new JButton("Add to Order");
        JButton btnClearSelection = new JButton("Clear Selection");
        
        mealPanel.add(lblMeal);
        mealPanel.add(cboMeals);
        mealPanel.add(lblDrinkOption);
        mealPanel.add(cboDrinkOption);
        mealPanel.add(lblQuantity);
        mealPanel.add(spinQuantity);
        mealPanel.add(btnAddToOrder);
        mealPanel.add(btnClearSelection);
        
        leftPanel.add(customerPanel, BorderLayout.NORTH);
        leftPanel.add(mealPanel, BorderLayout.CENTER);
        
        // Right panel (Order Items)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Order Details"));
        
        // Order table
        tableModel = new DefaultTableModel(
                new Object[]{"Meal", "Drink Option", "Quantity", "Price", "Subtotal"}, 0);
        tblOrderItems = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblOrderItems);
        
        // Totals panel
        JPanel totalsPanel = new JPanel();
        totalsPanel.setLayout(new GridLayout(4, 2, 5, 5));
        
        JLabel lblSubtotal = new JLabel("Subtotal:");
        JLabel lblSubtotalValue = new JLabel("GHC 0.00");
        JLabel lblDiscount = new JLabel("Discount (10% over GHC 300):");
        JLabel lblDiscountValue = new JLabel("GHC 0.00");
        JLabel lblTotal = new JLabel("Total:");
        JLabel lblTotalValue = new JLabel("GHC 0.00");
        
        totalsPanel.add(lblSubtotal);
        totalsPanel.add(lblSubtotalValue);
        totalsPanel.add(lblDiscount);
        totalsPanel.add(lblDiscountValue);
        totalsPanel.add(lblTotal);
        totalsPanel.add(lblTotalValue);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton btnRemoveItem = new JButton("Remove Selected Item");
        JButton btnPlaceOrder = new JButton("Place Order");
        JButton btnClearOrder = new JButton("Clear Order");
        
        buttonPanel.add(btnRemoveItem);
        buttonPanel.add(btnClearOrder);
        buttonPanel.add(btnPlaceOrder);
        
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(totalsPanel, BorderLayout.SOUTH);
        
        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Button actions
        btnAddToOrder.addActionListener(e -> {
            addToOrder();
            updateTotals(lblSubtotalValue, lblDiscountValue, lblTotalValue);
        });
        
        btnClearSelection.addActionListener(e -> {
            cboMeals.setSelectedIndex(0);
            cboDrinkOption.setSelectedIndex(0);
            spinQuantity.setValue(1);
        });
        
        btnRemoveItem.addActionListener(e -> {
            removeSelectedItem();
            updateTotals(lblSubtotalValue, lblDiscountValue, lblTotalValue);
        });
        
        btnClearOrder.addActionListener(e -> {
            clearOrder();
            updateTotals(lblSubtotalValue, lblDiscountValue, lblTotalValue);
        });
        
        btnPlaceOrder.addActionListener(e -> {
            if (validateOrder()) {
                placeOrder();
                JOptionPane.showMessageDialog(this, "Order placed successfully!", 
                        "Order Success", JOptionPane.INFORMATION_MESSAGE);
                clearOrder();
                updateTotals(lblSubtotalValue, lblDiscountValue, lblTotalValue);
            }
        });
        
        setVisible(true);
    }
    
    private void fillUserDetails() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT name, phone, location FROM users WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                txtName.setText(rs.getString("name"));
                txtPhone.setText(rs.getString("phone"));
                txtLocation.setText(rs.getString("location"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving user details: " + e.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadMeals() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT id, name, price_only, price_with_juice, price_with_water, available FROM meals";
            PreparedStatement pstmt = conn.prepareStatement(query);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double priceOnly = rs.getDouble("price_only");
                double priceWithJuice = rs.getDouble("price_with_juice");
                double priceWithWater = rs.getDouble("price_with_water");
                boolean available = rs.getBoolean("available");
                
                Meal meal = new Meal(id, name, priceOnly, priceWithJuice, priceWithWater, available);
                cboMeals.addItem(meal);
                mealsMap.put(id, meal);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading meals: " + e.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addToOrder() {
        Meal selectedMeal = (Meal) cboMeals.getSelectedItem();
        String drinkOption = cboDrinkOption.getSelectedItem().toString();
        int quantity = (int) spinQuantity.getValue();
        
        if (selectedMeal == null) {
            JOptionPane.showMessageDialog(this, "Please select a meal", 
                    "Order Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!selectedMeal.isAvailable()) {
            JOptionPane.showMessageDialog(this, "Sorry, " + selectedMeal.getName() + " is not available", 
                    "Order Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        OrderItem item = new OrderItem(selectedMeal, drinkOption, quantity);
        orderItems.add(item);
        
        // Update the table
        Object[] row = {
            selectedMeal.getName(),
            drinkOption,
            quantity,
            String.format("GHC %.2f", item.getPrice()),
            String.format("GHC %.2f", item.getSubtotal())
        };
        tableModel.addRow(row);
    }
    
    private void removeSelectedItem() {
        int selectedRow = tblOrderItems.getSelectedRow();
        if (selectedRow >= 0) {
            orderItems.remove(selectedRow);
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to remove", 
                    "Order Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearOrder() {
        orderItems.clear();
        tableModel.setRowCount(0);
    }
    
    private void updateTotals(JLabel lblSubtotal, JLabel lblDiscount, JLabel lblTotal) {
        double subtotal = 0;
        for (OrderItem item : orderItems) {
            subtotal += item.getSubtotal();
        }
        
        double discount = 0;
        if (subtotal > 300) {
            discount = subtotal * 0.1;
        }
        
        double total = subtotal - discount;
        
        lblSubtotal.setText(String.format("GHC %.2f", subtotal));
        lblDiscount.setText(String.format("GHC %.2f", discount));
        lblTotal.setText(String.format("GHC %.2f", total));
    }
    
    private boolean validateOrder() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String location = txtLocation.getText().trim();
        
        if (name.isEmpty() || phone.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all customer information", 
                    "Order Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (orderItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one item to your order", 
                    "Order Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void placeOrder() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String location = txtLocation.getText().trim();
        
        double subtotal = 0;
        for (OrderItem item : orderItems) {
            subtotal += item.getSubtotal();
        }
        
        double discount = 0;
        if (subtotal > 300) {
            discount = subtotal * 0.1;
        }
        
        double total = subtotal - discount;
        
        try (Connection conn = DBConnection.getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);
            
            try {
                // Insert order
                String orderQuery = "INSERT INTO orders (user_id, name, phone, location, total_amount, discount_amount, final_amount) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement orderStmt = conn.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS);
                orderStmt.setInt(1, userId);
                orderStmt.setString(2, name);
                orderStmt.setString(3, phone);
                orderStmt.setString(4, location);
                orderStmt.setDouble(5, subtotal);
                orderStmt.setDouble(6, discount);
                orderStmt.setDouble(7, total);
                
                orderStmt.executeUpdate();
                
                // Get generated order ID
                ResultSet generatedKeys = orderStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);
                    
                    // Insert order items
                    String itemQuery = "INSERT INTO order_items (order_id, meal_id, meal_name, drink_option, quantity, price, subtotal) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement itemStmt = conn.prepareStatement(itemQuery);
                    
                    for (OrderItem item : orderItems) {
                        itemStmt.setInt(1, orderId);
                        itemStmt.setInt(2, item.getMeal().getId());
                        itemStmt.setString(3, item.getMeal().getName());
                        itemStmt.setString(4, item.getDrinkOption());
                        itemStmt.setInt(5, item.getQuantity());
                        itemStmt.setDouble(6, item.getPrice());
                        itemStmt.setDouble(7, item.getSubtotal());
                        
                        itemStmt.executeUpdate();
                    }
                    
                    // Commit transaction
                    conn.commit();
                    
                    // Show receipt
                    showReceipt(orderId);
                } else {
                    // Rollback transaction
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "Error: Could not generate order ID", 
                            "Order Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                // Rollback transaction
                conn.rollback();
                throw e;
            } finally {
                // Restore auto-commit
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), 
                    "Order Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showReceipt(int orderId) {
        try (Connection conn = DBConnection.getConnection()) {
            // Get order details
            String orderQuery = "SELECT * FROM orders WHERE id = ?";
            PreparedStatement orderStmt = conn.prepareStatement(orderQuery);
            orderStmt.setInt(1, orderId);
            
            ResultSet orderRs = orderStmt.executeQuery();
            if (orderRs.next()) {
                String customerName = orderRs.getString("name");
                String customerPhone = orderRs.getString("phone");
                String customerLocation = orderRs.getString("location");
                double subtotal = orderRs.getDouble("total_amount");
                double discount = orderRs.getDouble("discount_amount");
                double total = orderRs.getDouble("final_amount");
                Timestamp orderDate = orderRs.getTimestamp("order_date");
                
                // Get order items
                String itemQuery = "SELECT * FROM order_items WHERE order_id = ?";
                PreparedStatement itemStmt = conn.prepareStatement(itemQuery);
                itemStmt.setInt(1, orderId);
                
                ResultSet itemRs = itemStmt.executeQuery();
                
                // Create receipt
                JFrame receiptFrame = new JFrame("Tasty Bites Restaurant - Receipt");
                receiptFrame.setSize(400, 600);
                receiptFrame.setLocationRelativeTo(this);
                
                JPanel receiptPanel = new JPanel();
                receiptPanel.setLayout(new BorderLayout(10, 10));
                receiptPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
                
                // Receipt header
                JPanel headerPanel = new JPanel();
                headerPanel.setLayout(new GridLayout(5, 1, 5, 5));
                
                JLabel lblRestaurantName = new JLabel("Tasty Bites Restaurant");
                lblRestaurantName.setFont(new Font("Arial", Font.BOLD, 18));
                lblRestaurantName.setHorizontalAlignment(JLabel.CENTER);
                
                JLabel lblOrderId = new JLabel("Order #" + orderId);
                lblOrderId.setHorizontalAlignment(JLabel.CENTER);
                
                JLabel lblOrderDate = new JLabel("Date: " + orderDate.toString());
                lblOrderDate.setHorizontalAlignment(JLabel.CENTER);
                
                JLabel lblCustomerName = new JLabel("Customer: " + customerName);
                JLabel lblCustomerPhone = new JLabel("Phone: " + customerPhone);
                
                headerPanel.add(lblRestaurantName);
                headerPanel.add(lblOrderId);
                headerPanel.add(lblOrderDate);
                headerPanel.add(lblCustomerName);
                headerPanel.add(lblCustomerPhone);
                
                // Receipt items
                DefaultTableModel receiptModel = new DefaultTableModel(
                        new Object[]{"Item", "Quantity", "Price", "Subtotal"}, 0);
                JTable tblReceipt = new JTable(receiptModel);
                JScrollPane scrollPane = new JScrollPane(tblReceipt);
                
                while (itemRs.next()) {
                    String mealName = itemRs.getString("meal_name");
                    String drinkOption = itemRs.getString("drink_option");
                    int quantity = itemRs.getInt("quantity");
                    double price = itemRs.getDouble("price");
                    double itemSubtotal = itemRs.getDouble("subtotal");
                    
                    String itemDescription = mealName;
                    if (!drinkOption.equals("None")) {
                        itemDescription += " with " + drinkOption;
                    }
                    
                    Object[] row = {
                        itemDescription,
                        quantity,
                        String.format("GHC %.2f", price),
                        String.format("GHC %.2f", itemSubtotal)
                    };
                    receiptModel.addRow(row);
                }
                
                // Receipt footer
                JPanel footerPanel = new JPanel();
                footerPanel.setLayout(new GridLayout(4, 2, 5, 5));
                
                JLabel lblSubtotalLabel = new JLabel("Subtotal:");
                lblSubtotalLabel.setHorizontalAlignment(JLabel.RIGHT);
                JLabel lblSubtotalValue = new JLabel(String.format("GHC %.2f", subtotal));
                
                JLabel lblDiscountLabel = new JLabel("Discount:");
                lblDiscountLabel.setHorizontalAlignment(JLabel.RIGHT);
                JLabel lblDiscountValue = new JLabel(String.format("GHC %.2f", discount));
                
                JLabel lblTotalLabel = new JLabel("Total:");
                lblTotalLabel.setHorizontalAlignment(JLabel.RIGHT);
                lblTotalLabel.setFont(new Font("Arial", Font.BOLD, 14));
                JLabel lblTotalValue = new JLabel(String.format("GHC %.2f", total));
                lblTotalValue.setFont(new Font("Arial", Font.BOLD, 14));
                
                JLabel lblThankYou = new JLabel("Thank you for dining with us!");
                lblThankYou.setHorizontalAlignment(JLabel.CENTER);
                lblThankYou.setFont(new Font("Arial", Font.ITALIC, 14));
                
                footerPanel.add(lblSubtotalLabel);
                footerPanel.add(lblSubtotalValue);
                footerPanel.add(lblDiscountLabel);
                footerPanel.add(lblDiscountValue);
                footerPanel.add(lblTotalLabel);
                footerPanel.add(lblTotalValue);
                footerPanel.add(new JLabel(""));
                footerPanel.add(new JLabel(""));
                
                // Add panels to receipt panel
                receiptPanel.add(headerPanel, BorderLayout.NORTH);
                receiptPanel.add(scrollPane, BorderLayout.CENTER);
                receiptPanel.add(footerPanel, BorderLayout.SOUTH);
                
                // Add receipt panel to frame
                receiptFrame.add(receiptPanel);
                
                // Add print and close buttons
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
                
                JButton btnPrint = new JButton("Print");
                JButton btnClose = new JButton("Close");
                
                buttonPanel.add(btnPrint);
                buttonPanel.add(btnClose);
                
                receiptPanel.add(buttonPanel, BorderLayout.SOUTH);
                
                // Button actions
                btnPrint.addActionListener(e -> {
                    JOptionPane.showMessageDialog(receiptFrame, "Receipt sent to printer", 
                            "Print", JOptionPane.INFORMATION_MESSAGE);
                });
                
                btnClose.addActionListener(e -> {
                    receiptFrame.dispose();
                });
                
                receiptFrame.setVisible(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error generating receipt: " + e.getMessage(), 
                    "Receipt Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}