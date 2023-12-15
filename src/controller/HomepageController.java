package controller;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Concert;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomepageController {

    @FXML
    private GridPane concertGrid;

    @FXML
    public void initialize(int customerId) {
        // Populate the Gridpane with concert names
        List<Concert> concerts = Concert.getAllConcerts();

        System.out.println("Customer ID " + customerId);

        int columnIndex = 0;
        int rowIndex = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");


        for (Concert concert : concerts) {
            // Create a VBox for each concert card
            VBox concertCard = new VBox();
            concertCard.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-border-radius: 5px; -fx-background-color: #f0f0f0;");

            concertCard.setSpacing(5); // Adjust spacing as needed

            // Create an ImageView to display the concert image
            ImageView imageView = new ImageView();
            imageView.setFitWidth(100); // Set the image width
            imageView.setFitHeight(100); // Set the image height

            try {
                // Try loading the image
                Image image = new Image(concert.getImageUrl());
                imageView.setImage(image);
            } catch (IllegalArgumentException e) {
                // Handle the case where the image URL is invalid
                System.out.println("Invalid image URL: " + concert.getImageUrl());
                e.printStackTrace();
                // You might want to set a default image here or handle the error in some other way
            }

            // Create a Label to display the concert name
            Label nameLabel = new Label(concert.getName());

            LocalDateTime concertDate = concert.getDateTime();
            String formattedDate = concertDate.format(formatter);
            Label dateLabel = new Label("Date: " + formattedDate);

            Button buyButton = new Button("Buy");
            buyButton.setOnAction((ActionEvent event) -> {
                // Logic to handle buying tickets for the selected concert
                buyTickets(concert, customerId); // Pass the selected concert to the method
            });

            // Add image and name to the VBox
            concertCard.getChildren().addAll(imageView, nameLabel, dateLabel, buyButton);

            // Add the concert card to the grid at the appropriate position
            concertGrid.add(concertCard, columnIndex, rowIndex);

            // Increment the row and column index for the next concert
            columnIndex++;
            if (columnIndex == 2) {
                columnIndex = 0;
                rowIndex++;
            }
        }
    }
    @FXML
    private void buyTickets(Concert concert, int customerId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TicketsPage.fxml"));
            Parent root = loader.load();

            TicketsPageController ticketsPageController = loader.getController();
            ticketsPageController.initData(concert, customerId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Tickets for " + concert.getName());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
