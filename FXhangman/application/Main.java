package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.collections.*;

public class Main extends Application {
    
	public void start(Stage stage) throws Exception {
	
    	
		Parent root = FXMLLoader.load(getClass().getResource("/FXMLhangman.fxml")); //evala / 

		
		Scene scene =  new Scene(root);
		stage.setScene(scene); 
		stage.setTitle("MediaLab Hangman");
		stage.show();
 
    }


	public static void main(String[] args) {
		launch(args);
	}
}


