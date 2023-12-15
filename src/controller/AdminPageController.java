package controller;

import db.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminPageController {
    @FXML
    private VBox bookingDetailsContainer;

    public void initialize() {
        // Fetch data from the database and populate the booking details
        fetchBookingDetails();
    }

    private void fetchBookingDetails() {
        try (Connection connection = DatabaseConnector.connect()) {
            String query = "SELECT * FROM booking_details";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int bookingId = resultSet.getInt("booking_id");
                int customerId = resultSet.getInt("customer_id");
                int ticketId = resultSet.getInt("ticketID");
                String verificationStatus = resultSet.getString("verification_status");
                String paymentMethod = resultSet.getString("paymentMethod");
                String extras = resultSet.getString("extras");

                // Create a card to display booking details
                Label bookingDetailsLabel = new Label(
                        "Booking ID: " + bookingId + "\n" +
                                "Customer ID: " + customerId + "\n" +
                                "Ticket ID: " + ticketId + "\n" +
                                "Verification Status: " + verificationStatus + "\n" +
                                "Payment Method: " + paymentMethod + "\n" +
                                "Extras: " + extras + "\n"
                );

                Button verifyButton = new Button("Verify");
                verifyButton.setOnAction(event -> {
                    if (!verificationStatus.equals("Verified")) {
                        updateVerificationStatus(bookingId, "Verified");
                        verifyButton.setDisable(true);
                    }
                });

                // Disable the button if the status is already "Verified"
                if (verificationStatus.equals("Verified")) {
                    verifyButton.setDisable(true);
                }

                // Set styles or formatting if needed
                bookingDetailsLabel.setStyle("-fx-border-color: black; -fx-padding: 5px;");

                // Add the label and button to the container
                bookingDetailsContainer.getChildren().addAll(bookingDetailsLabel, verifyButton);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions
        }
    }

    private void updateVerificationStatus(int bookingId, String newStatus) {
        try (Connection connection = DatabaseConnector.connect()) {
            String updateQuery = "UPDATE booking_details SET verification_status = ? WHERE booking_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, bookingId);
            preparedStatement.executeUpdate();
            
            // After update, reinitialize to refresh the window
            bookingDetailsContainer.getChildren().clear(); // Clear existing content
            fetchBookingDetails(); // Fetch updated data
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions
    }
}

}
