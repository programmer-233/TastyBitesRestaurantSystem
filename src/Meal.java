/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kwize
 */
class Meal {
    private int id;
    private String name;
    private double priceOnly;
    private double priceWithJuice;
    private double priceWithWater;
    private boolean available;
    
    public Meal(int id, String name, double priceOnly, double priceWithJuice, double priceWithWater, boolean available) {
        this.id = id;
        this.name = name;
        this.priceOnly = priceOnly;
        this.priceWithJuice = priceWithJuice;
        this.priceWithWater = priceWithWater;
        this.available = available;
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPriceOnly() { return priceOnly; }
    public double getPriceWithJuice() { return priceWithJuice; }
    public double getPriceWithWater() { return priceWithWater; }
    public boolean isAvailable() { return available; }
    
    @Override
    public String toString() {
        return name;
    }
}