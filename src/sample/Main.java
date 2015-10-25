package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    public static final String iconPath = "resources" + File.separator + "images/icon.png";

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("DBReader");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image(iconPath));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
