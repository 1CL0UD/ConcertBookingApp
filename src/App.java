import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DatabaseConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application{
    // public static void main(String[] args) throws ClassNotFoundException, SQLException {
    
    //     List<Concert> concerts = Concert.getAllConcerts();

    //     for (Concert concert : concerts) {
    //         System.out.println(concert.displayConcertDetails());
    //     }
    // }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/Login.fxml"));
        primaryStage.setTitle("Concert App");
        primaryStage.setScene(new Scene(root, 360, 640));
        primaryStage.show();
            if (DatabaseConnector.connect() != null){
            System.out.println("DB Connected");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
