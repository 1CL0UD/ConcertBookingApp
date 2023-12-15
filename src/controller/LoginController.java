package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    public void login() {
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            // Call Authentication to get the customer by email
            Customer customer = Authentication.getCustomerByEmail(email);
            int custId = customer.getId();
            String emailNew = customer.getEmail();

            if (customer != null && password.equals(customer.getPassword())) {
                // Check the retrieved Customer's role
                if ("customer".equals(customer.getRole())) {
                    // Redirect to the customer dashboard or appropriate page
                    redirectToHomepage(custId);
                    System.out.println("Logged in " + emailNew);
                } else if ("admin".equals(customer.getRole())) {
                    redirectToAdminPage();
                    System.out.println("Logged in as admin: " + emailNew);
                } else {
                    System.out.println("Not Logged in");
                }
            } else {
                // Display error or stay on the login page
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the SQL exception
        }
    }

    private void redirectToHomepage(int custId) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Homepage.fxml"));
            Parent root = loader.load();
            HomepageController homeController = loader.getController();
            homeController.initialize(custId);
            System.out.println("CustId: " + custId);

            // Set up the stage
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle or log the IO exception
        }
    }

    private void redirectToAdminPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminPage.fxml"));
            Parent root = loader.load();

            // Set up the stage
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle or log the IO exception
        }
    }
}
