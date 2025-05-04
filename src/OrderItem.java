/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kwize
 */
class OrderItem {
    private Meal meal;
    private String drinkOption;
    private int quantity;
    private double price;
    
    public OrderItem(Meal meal, String drinkOption, int quantity) {
        this.meal = meal;
        this.drinkOption = drinkOption;
        this.quantity = quantity;
        
        // Set price based on drink option
        switch (drinkOption) {
            case "None":
                this.price = meal.getPriceOnly();
                break;
            case "Apple Juice":
                this.price = meal.getPriceWithJuice();
                break;
            case "Water":
                this.price = meal.getPriceWithWater();
                break;
            default:
                this.price = meal.getPriceOnly();
        }
    }
    
    public Meal getMeal() { return meal; }
    public String getDrinkOption() { return drinkOption; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getSubtotal() { return price * quantity; }
    
    public String toTableRow() {
        return meal.getName() + " (" + drinkOption + ")";
    }
}