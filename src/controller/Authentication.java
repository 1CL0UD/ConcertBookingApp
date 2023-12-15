package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import db.DatabaseConnector;
import model.Customer;

public class Authentication {
    public static Customer getCustomerByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM customer WHERE email = ?";
        Customer customer = null;

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Fetch customer details including password from the result set
                    int custId = resultSet.getInt("customer_id");
                    String name = resultSet.getString("name");
                    String phone = resultSet.getString("phone");
                    Date dateOfBirth = resultSet.getDate("date_of_birth");
                    String gender = resultSet.getString("gender");
                    String role = resultSet.getString("role");
                    String password = resultSet.getString("password");

                    // Create a Customer object including the password
                    customer = new Customer(custId, name, phone, email, dateOfBirth, gender, role, password);
                }
            }
        }

        return customer;
    }
}
