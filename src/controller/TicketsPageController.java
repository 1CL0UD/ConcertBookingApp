package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Concert;
import model.Ticket;

import java.util.List;



public class TicketsPageController {
    @FXML
    private Label concertLabel;

    @FXML
    private ImageView concertImageView;

    @FXML
    private VBox concertDetailsVBox;

    @FXML
    private VBox ticketsVBox;

    public void initData(Concert selectedConcert, int customerId) {
        concertLabel.setText("Tickets for " + selectedConcert.getName());

        List<Ticket> tickets = selectedConcert.getTicketList();

        // Display concert details and available tickets
        displayConcertDetails(selectedConcert);
        displayAvailableTickets(tickets, customerId);

        // Load and display concert image
        displayConcertImage(selectedConcert.getImageUrl());
    }

    private void displayConcertImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                Image image = new Image(imageUrl);
                concertImageView.setImage(image);
                concertImageView.setFitWidth(200); // Adjust the width as needed
                concertImageView.setFitHeight(200); // Adjust the height as needed
            } catch (Exception e) {
                System.out.println("Error loading image: " + e.getMessage());
                // Handle the exception if the image couldn't be loaded
            }
        } else {
            System.out.println("No image URL provided.");
        }
    }

    private void displayConcertDetails(Concert concert) {
        // Create labels to display concert name, description, and venue
        Label nameLabel = new Label("Concert Name: " + concert.getName());

        // Use concert.getDescription() and concert.getVenue() to access the values from the Concert object
        Label descriptionLabel = new Label("Description: " + concert.getDescription());
        Label venueLabel = new Label("Venue: " + concert.getVenue());

        // Add concert details labels to the concertDetailsVBox
        concertDetailsVBox.getChildren().addAll(nameLabel, descriptionLabel, venueLabel);
    }   


    private void displayAvailableTickets(List<Ticket> tickets, int customerId) {
        for (Ticket ticket : tickets) {
            VBox ticketCard = createTicketCard(ticket, customerId);
            ticketsVBox.getChildren().add(ticketCard);
        }
    }

    private VBox createTicketCard(Ticket ticket, int customerId) {
        VBox ticketCard = new VBox();
        ticketCard.setStyle("-fx-padding: 10px; -fx-border-radius: 5px; -fx-background-color: #f0f0f0; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 5);");

        Label typeLabel = new Label("Type: " + ticket.getType());
        Label priceLabel = new Label("Price: Rp " + ticket.getPrice());
        Label stockLabel = new Label("Stock: " + ticket.getStock());

        // Number field for selecting the quantity of tickets
        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter quantity");

        // Button to proceed to payment
        Button buyButton = new Button("Buy");
        buyButton.setStyle("-fx-background-color: #E49B0F; -fx-text-fill: #f0f0f0;");


        HBox buyField = new HBox(10); // Spacing between elements
        buyField.getChildren().addAll(quantityField, buyButton);
        buyButton.setOnAction(event -> {
            String quantityText = quantityField.getText();
            int quantity = 0;
            try {
                quantity = Integer.parseInt(quantityText);
            } catch (NumberFormatException e) {
                quantityField.clear();
                
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Quantity");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid quantity.");
                alert.showAndWait();
                return;
            }

            // Logic to proceed to payment with the selected quantity
            // You can implement the logic to handle payment or navigate to a payment screen here
            System.out.println("Customer ID " + customerId);
            System.out.println("Quantity selected: " + quantity);
            System.out.println("Ticket Type: " + ticket.getType());
            System.out.println("Total Price: " + (ticket.getPrice() * quantity));
            // Proceed to payment or handle payment-related logic
            if (isPaymentPossible(quantity, ticket)) {
                navigateToDataScreen(customerId, quantity, ticket);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Out of stock! ");
                alert.setHeaderText(null);
                alert.setContentText("Sorry, this ticket is out of stock");
                alert.showAndWait();
            }
        });
        ticketCard.getChildren().addAll(typeLabel, priceLabel, stockLabel, buyField);
        return ticketCard;
    }

    private boolean isPaymentPossible(int quantity, Ticket ticket) {
        return ticket.getStock() >= quantity;
    }

    private void navigateToDataScreen(int customerId, int quantity, Ticket ticket) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomerData.fxml"));
            Parent dataPage = loader.load();

            // Access the controller of the CustomerData.fxml
            CustomerDataController dataController = loader.getController();
            
            dataController.initData(customerId, ticket.getTicketID(), quantity, ticket.getType(), (ticket.getPrice() * quantity));

            Stage currentStage = (Stage) concertLabel.getScene().getWindow();
            
            Scene dataScene = new Scene(dataPage);
            currentStage.setScene(dataScene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception/error if loading the CustomerData.fxml fails
        }
    }

}
