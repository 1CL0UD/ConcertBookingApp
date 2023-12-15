package model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseConnector;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Concert {
    private int ID;
    private String name;
    private LocalDateTime dateTime;
    private String description;
    private String venue;
    private List<Ticket> ticketList;
    private String imageUrl;

    public Concert(int ID, String name, LocalDateTime dateTime, String description, String venue, List<Ticket> ticketList, String imageUrl) {
        this.ID = ID;
        this.name = name;
        this.dateTime = dateTime;
        this.description = description;
        this.venue = venue;
        this.ticketList = ticketList;
        this.imageUrl = imageUrl;
    }

    public Concert() {
    }

    public static List<Concert> getAllConcerts() {
        List<Concert> concerts = new ArrayList<>();

        String query = "SELECT * FROM concert";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("name");
                LocalDateTime dateTime = resultSet.getTimestamp("dateTime").toLocalDateTime();
                String description = resultSet.getString("description");
                String venue = resultSet.getString("venue");
                String imageUrl = resultSet.getString("imageUrl");

                List<Ticket> ticketList = retrieveTicketListForConcert(id);

                Concert concert = new Concert(id, name, dateTime, description, venue, ticketList, imageUrl);
                concerts.add(concert);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return concerts;
    }

    // Method to retrieve ticketList for a concert
    private static List<Ticket> retrieveTicketListForConcert(int concertID) {
        List<Ticket> ticketList = new ArrayList<>();

        // Logic to retrieve Ticket objects for a given concert ID from the database
        // For example, using JDBC similar to the concert retrieval logic

        String query = "SELECT * FROM tickets WHERE concert_id = ?";
        
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, concertID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int ticketID = resultSet.getInt("ticketID");
                String type = resultSet.getString("type");
                double price = resultSet.getDouble("price");
                int stock = resultSet.getInt("stock");

                Ticket ticket = new Ticket(type, price, stock);
                ticket.setTicketID(ticketID);
                ticketList.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ticketList;
    }

    public String displayConcertDetails() {
        StringBuilder concertDetails = new StringBuilder();
        concertDetails.append("Concert Name: ").append(this.name).append("\n");
        concertDetails.append("Venue: ").append(this.venue).append("\n");

        if (ticketList != null && !ticketList.isEmpty()) {
            concertDetails.append("Tickets Available:\n");
            for (Ticket ticket : ticketList) {
                concertDetails.append("- Type: ").append(ticket.getType())
                        .append(", Price: ").append(ticket.getPrice())
                        .append(", Stock: ").append(ticket.getStock())
                        .append("\n");
            }
        } else {
            concertDetails.append("No tickets available for this concert.\n");
        }

        return concertDetails.toString();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    
    
    
}
