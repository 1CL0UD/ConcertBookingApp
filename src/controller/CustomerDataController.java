package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Concert;
import model.Customer;
import model.Ticket;

public class CustomerDataController {
    
    @FXML
    private Label quantityLabel;

    @FXML
    private Label ticketTypeLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private GridPane customerDataGrid;

    @FXML
    private TextField nameField1;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker dobPicker;

    @FXML
    private TextField genderField;

    private List<String> extraCustomerNames = new ArrayList<>();

    private int quantity;
    private String ticketType;
    private double totalPrice;
    private int customerId;
    private int ticketId;

    public void initData(int customerId, int ticketId, int quantity, String ticketType, double totalPrice) {
        this.ticketId = ticketId;
        this.customerId = customerId;
        this.quantity = quantity;
        this.ticketType = ticketType;
        this.totalPrice = totalPrice;
        quantityLabel.setText("Quantity: " + quantity);
        ticketTypeLabel.setText("Ticket Type: " + ticketType);
        totalPriceLabel.setText("Total Price: " + totalPrice);

        int rowIndex = 5;
        if (quantity > 1) {
            for (int i = 2; i <= quantity; i++) {
                Label nameLabel = new Label("Customer #" + i);
                TextField nameField = new TextField();
                nameField.setPromptText("Name");
                nameField.setId("nameField" + i);

                customerDataGrid.addRow(rowIndex, nameLabel, nameField);
                rowIndex++;

                nameField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        // Update extraCustomerNames list when the focus leaves the text field
                        extraCustomerNames.add(nameField.getText());
                    }
                });
            }
        }
        
    }

    @FXML
    private void goToPaymentPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PaymentPage.fxml"));
            Parent root = loader.load();

            // Get the controller from the loader
            PaymentPageController paymentController = loader.getController();
            paymentController.initData(this.customerId, this.ticketId, nameField1.getText(), emailField.getText(), phoneField.getText(), genderField.getText(), quantity, ticketType, totalPrice, extraCustomerNames);
            System.out.println("Customer ID " + this.customerId);
            System.out.println(nameField1.getText());
            System.out.println(emailField.getText());
            System.out.println(phoneField.getText());
            System.out.println(genderField.getText());
            System.out.println(this.quantity);
            System.out.println(this.ticketType);
            System.out.println(this.totalPrice);
            System.out.println(extraCustomerNames);
            
            // Get the current stage
            Stage stage = (Stage) customerDataGrid.getScene().getWindow();

            // Set the new scene on the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
