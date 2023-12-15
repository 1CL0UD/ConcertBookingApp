package model;

public class Ticket {
    private int ticketID;
    private String type;
    private double price;
    private int stock;

    public Ticket(String type, double price, int stock) {
        this.type = type;
        this.price = price;
        this.stock = stock;
    }

    @Override
    public String toString() {
        return getType() + " - Rp. " + getPrice(); // Modify this based on your Ticket properties
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }
    
    

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
}
