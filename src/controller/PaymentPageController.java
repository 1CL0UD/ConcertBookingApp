package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PaymentPageController{
    @FXML
    private VBox customerDetailsCard;

    @FXML
    private VBox paymentMethodsCard;

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneLabel;    

    private List<String> extraCustomers = new ArrayList<>();

    private int customerId;
    private int ticketId;

    public PaymentPageController() {
        System.out.println("PaymentPageController initialized."); // Check if the controller initializes.
    }
    

    public void initData(int customerId, int ticketId, String name, String email, String phone, String gender, int quantity, String ticketType, double totalPrice, List<String> extraCustomerNames) {
        // Set the labels with the data passed from CustomerDataController
        this.extraCustomers = extraCustomerNames;
        this.customerId = customerId;
        this.ticketId = ticketId;
        nameLabel.setText("Name: " + name);
        emailLabel.setText("Email: " + email);
        phoneLabel.setText("Phone: " + phone);
        // Set other labels with the respective data

        if (extraCustomerNames != null && !extraCustomerNames.isEmpty()) {
            for (String extraName : extraCustomerNames) {
                Label extraCustomerLabel = new Label("Extra Customer Name: " + extraName);
                customerDetailsCard.getChildren().add(extraCustomerLabel);
            }
        }
    }

    @FXML
    private void handleCashPayment(ActionEvent event) {
        String paymentMethod = "Cash";
        insertBooking(paymentMethod, this.customerId, this.ticketId, extraCustomers);
    }

    @FXML
    private void handleCreditPayment(ActionEvent event) {
        String paymentMethod = "Credit";
        insertBooking(paymentMethod, this.customerId, this.ticketId, extraCustomers);
    }

    private void insertBooking(String paymentMethod, int customerId, int ticketId, List<String> extraCustomers){
        // Set the customer and ticket IDs

        // Set other details
        String verificationStatus = "Not Verified";
        
        String extras = String.join(", ", extraCustomers); 

        // Insert data into the database
        try (Connection connection = DatabaseConnector.connect()) {
            String insertQuery = "INSERT INTO booking_details (customer_id, ticketID, verification_status, paymentMethod, extras) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, customerId);
            preparedStatement.setInt(2, ticketId);
            preparedStatement.setString(3, verificationStatus);
            preparedStatement.setString(4, paymentMethod);
            preparedStatement.setString(5, extras);

            // Execute the insert query
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully into booking_details table.");
                redirectToHomepage();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Quantity");
                alert.setHeaderText(null);
                alert.setContentText("Failed to insert data into booking_details table.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }

    private void redirectToHomepage() {
        Stage stage = (Stage) customerDetailsCard.getScene().getWindow();
        // Close the current window
        stage.close();
    }
}




