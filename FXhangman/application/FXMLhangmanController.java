package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.control.ComboBox;
import javafx.collections.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class FXMLhangmanController {
	
	 thefinalhangman fh;
	 
	 static ArrayList<String> secretWords = new ArrayList<String>();
	 static ArrayList<Integer> numEfforts = new ArrayList<Integer>();
	 static ArrayList<String> gamesWon = new ArrayList<String>();
	 
	 static int num =0;
	 
	 @FXML private MenuItem m11;
	 @FXML private MenuItem m12;
	 @FXML private MenuItem m13;
	 @FXML private MenuItem m14;
	 @FXML private MenuItem m21;
	 @FXML private MenuItem m22;
	 @FXML private MenuItem m23;
    
	 @FXML
     public  ComboBox<String> combo1 ;	
     public  ComboBox<String> combo2 ;	

     @FXML private Label textLabel1;   // number of words in lexicon
     @FXML private Label textLabel2;   // number of points
     @FXML private Label textLabel3;   // percent
     @FXML private Label textLabel4;
     @FXML private Label textLabel5;
     
     @FXML private Button button;
     @FXML private AnchorPane anchor;
     
     @FXML private ImageView hangman_image;

    @FXML //position
    public void clicked_combo1(MouseEvent event) {
    	  
    	 ArrayList<Boolean> positions = fh.positions();
    	 String[] arr= new String[positions.size()];
    	 Arrays.fill(arr, "--");

    	 for(int i=0; i<positions.size(); i++) {
    		 if(positions.get(i) == false) {
    			 arr[i] = String.valueOf(i);
    		 }
    	 }
    	 
     	ObservableList<String> List1 = FXCollections.observableArrayList(arr);
     	combo1.setItems(List1);	
     	
    }
     
    @FXML //Letter
    public void clicked_combo2(MouseEvent event) {
    	
    	String alphabet[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    	ObservableList<String> List2 = FXCollections.observableArrayList(alphabet);
    	combo2.setItems(List2);
    	
    }

    @FXML
    public void button_pressed(ActionEvent event) {

    	String	selected1 = combo1.getSelectionModel().getSelectedItem().toString();
    	String selected2 = combo2.getSelectionModel().getSelectedItem().toString();


    	
    	fh.playMove(selected2, selected1);

    	if(! fh.isGameOver() ) {

	    	textLabel1.setText("Total words in lexicon:   "+ fh.printNumWords());
	    	textLabel2.setText("Total Points:   " + fh.printPoints());
	    	textLabel3.setText("Percentage of right guesses:   "+ fh.printLettersfound());
	    	textLabel4.setText(fh.printCorrectLetters());
	    	textLabel5.setText(fh.printSuggestions());
	    	 
	    	
	    	switch(fh.numBadGuesses()) {

	    	case 1:	
	    		File file1 = new File("C:\\Users\\42\\Desktop\\medialab\\1.png");
	            hangman_image.setImage(new Image(file1.toURI().toString()));

	    		break;
	    	
	    	case 2:
	    		File file2 = new File("C:\\Users\\42\\Desktop\\medialab\\2.png");
	            hangman_image.setImage(new Image(file2.toURI().toString()));
				
	    		break;
	    	
	    	case 3:
	    		File file3 = new File("C:\\Users\\42\\Desktop\\medialab\\3.png");
	            hangman_image.setImage(new Image(file3.toURI().toString()));
				
	    		break;

	    	case 4:
	    		File file4 = new File("C:\\Users\\42\\Desktop\\medialab\\4.png");
	            hangman_image.setImage(new Image(file4.toURI().toString()));
				
	    		break;

	    	case 5:
	    		File file5 = new File("C:\\Users\\42\\Desktop\\medialab\\5.png");
	            hangman_image.setImage(new Image(file5.toURI().toString()));
				
	    		break;

	    	case 6:
	    		File file6 = new File("C:\\Users\\42\\Desktop\\medialab\\6.png");
	            hangman_image.setImage(new Image(file6.toURI().toString()));
				
	    		break;	    	
    	
    		case 7:
	    		File file7 = new File("C:\\Users\\42\\Desktop\\medialab\\7.png");
	            hangman_image.setImage(new Image(file7.toURI().toString()));
				
	    		break;	    	
	    	}
	
    	}
    
    	//game is over (win or loss)
    	else {
    		
    		if(fh.numlettersfound() < fh.SecretwordSize()) {
    			
    			//save game
    			secretWords.add(fh.SecretWord());
    			numEfforts.add(fh.numBadGuesses() +fh.numlettersfound());
    			gamesWon.add("LOSS");
    			
    			num++;
    			
    			//YOU LOST
    			Stage stage = new Stage();
    	    	Label t = new Label("			YOU LOST" + "\n" + "			Secret word was :   " + fh.SecretWord());
    	        HBox box = new HBox(5.0D);
    	        box.setPadding(new Insets(25.0D, 5.0D, 5.0D, 50.0D));
    	        box.getChildren().addAll(t);
    	        Scene scene = new Scene(box, 595.0D, 150.0D, Color.BEIGE);
    	        stage.setScene(scene);
    	        stage.show();
    	      
    			
    		}
    		else {
    			//save game
    			secretWords.add(fh.SecretWord());
    			numEfforts.add(fh.numBadGuesses() +fh.numlettersfound());
    			gamesWon.add("WON");
    			
    			//YOU WON
    			Stage stage = new Stage();
    	    	Label t = new Label("			YOU WON" + "\n"  + "			Secret word was :   " + fh.SecretWord());
    	        HBox box = new HBox(5.0D);
    	        box.setPadding(new Insets(25.0D, 5.0D, 5.0D, 50.0D));
    	        box.getChildren().addAll(t);
    	        Scene scene = new Scene(box, 595.0D, 150.0D, Color.BEIGE);
    	        stage.setScene(scene);
    	        stage.show();
    			
    		}
    	}
    	
    }
    
    ////////////////////////////////////////
    //CONTROLLERS FOR:  START, LOAD , CREATE
    ////////////////////////////////////////
    @FXML
    void m11_action(ActionEvent event) {  //START
 
    	if(true) {
		File file0 = new File("C:\\Users\\42\\Desktop\\medialab\\0.png");
        hangman_image.setImage(new Image(file0.toURI().toString()));
    	}

    	textLabel1.setText("Total words in lexicon:   "+ fh.printNumWords());
    	textLabel2.setText("Total Points:   " + fh.printPoints());
    	textLabel3.setText("Percentage of right guesses:   "+ fh.printLettersfound());
    	textLabel4.setText(fh.printCorrectLetters());
    	textLabel5.setText(fh.printSuggestions());
    
    }   
    
    
    @FXML
    void m12_action(ActionEvent event) {  //LOAD
    		Stage stage = new Stage();
	        TextField tf = new TextField();

	    	Label t = new Label();
	        
	        Button button = new Button("Enter");
	        Label id = new Label("Enter DICTIONARY-ID : ");
	        HBox box = new HBox(5.0D);
	        box.setPadding(new Insets(25.0D, 5.0D, 5.0D, 50.0D));
	        box.getChildren().addAll(id, tf, button, t);
	        Scene scene = new Scene(box, 750.0D, 150.0D, Color.BEIGE);
	        stage.setTitle("dictionary selection");
	        stage.setScene(scene);
	        stage.show();
	        
	        button.setOnAction(new EventHandler<ActionEvent>() {
	    	    public void handle(ActionEvent e) {
	            
	    	    String filename = tf.getText();
	    	    
	    	    Path path = Paths.get("C:/Users/42/Desktop/medialab/hangman_"+filename+".txt");
	    	    boolean exists = Files.exists(path);
	    	    
	    	    if (exists) {   //file is valid
	    	    	
	    	    	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	    	    	// load path and then START
	    	    	//Path gamepath = Paths.get("C:/Users/42/Desktop/medialab/hangman_test.txt");
	    	    	fh = new thefinalhangman(path);
	    	  		
	    	    	t.setText("LEXICON LOADED, GO TO MENU AND PRESS START");

	    	    }
	    	    else {
	    	    	t.setText("    DOES NOT EXIST");
	    	    }
	    	    
	    	    
	    	    }
	        });
    }   
    
    @FXML
    void m13_action(ActionEvent event) {  //CREATE
    	Stage stage = new Stage();
	        TextField tf1 = new TextField();
	        TextField tf2 = new TextField();

	        Label t = new Label();
	        
	        Button button = new Button("Enter");
	        Label dic_id = new Label("DICTIONARY-ID : ");
	        Label lib_id = new Label("OPEN LIBRARY-ID : ");
        
	        HBox box = new HBox(5.0D);
	        box.setPadding(new Insets(25, 0, 0, 5));      //top, bottom, right, left
	        box.getChildren().addAll(dic_id, tf1, lib_id , tf2, button , t);
	        
	        Scene scene = new Scene(box, 850.0D, 150.0D, Color.BEIGE);
	        stage.setTitle("create dictionary");
	        stage.setScene(scene);
	        stage.show();
	        
	        button.setOnAction(new EventHandler<ActionEvent>() {
	    	    public void handle(ActionEvent e) {
	            
		    	    String dic_filename = tf1.getText();
		    	    String lib_filename = tf2.getText();
		    	    
		    	    // call the parse class and print exceptions if found
		    	    Parsecode p = new Parsecode(lib_filename);
		    		
		    	    if(p.getWarnings() == "Good choice - let's play!") { 
		    			try {
							if(p.savefile(p.getcleanText(), dic_filename) == true) {t.setText( "GOOD CHOICE - FILE CREATED!");} 
		    			}
		    			catch (IOException e1) {e1.printStackTrace();}
		    				
		    		}
		    		else {t.setText(p.getWarnings());}
	    	    }	
	        });
    }

    @FXML
    void m14_action(ActionEvent event) { System.exit(0); }   

    ////////////////////////////////////////
    //CONTROLLERS FOR:  DICTIONARY, ROUNDS, SOLUTION
    ////////////////////////////////////////
    
    @FXML
    void m21_action(ActionEvent event) { //Dictionary
    	Stage stage = new Stage();

        Label id = new Label("percentage of words with length: "+"\n" + "equal to 6 : 	" +fh.printPercentLex().get(0) +" %"+"\n" + "7 to 9 : 	" +fh.printPercentLex().get(1) +" %"+ "\n" + "at least 10 : 	" + fh.printPercentLex().get(2) +" %");
        HBox box = new HBox(5.0D);
        box.setPadding(new Insets(25.0D, 5.0D, 5.0D, 50.0D));
        box.getChildren().addAll(id);
        Scene scene = new Scene(box, 595.0D, 150.0D, Color.BEIGE);
        stage.setTitle("Dictionary");
        stage.setScene(scene);
        stage.show();
       
    }   

    @FXML
    void m22_action(ActionEvent event) {  //Rounds
    	int start= secretWords.size()-5;
    	
    	try {
    	
    	
    	Stage stage = new Stage();
    	
    	
    	Label id1 = new Label("Last 5 games: ");
    	
    	Label id2 = new Label(secretWords.get(start) + "\n" + String.valueOf(numEfforts.get(start)) + "\n" + gamesWon.get(start));
    	Label id3 = new Label(secretWords.get(start+1) + "\n" + String.valueOf(numEfforts.get(start+1)) + "\n" + gamesWon.get(start+1));
    	Label id4 = new Label(secretWords.get(start+2) + "\n" + String.valueOf(numEfforts.get(start+2)) + "\n" + gamesWon.get(start+2));
    	Label id5 = new Label(secretWords.get(start+3) + "\n" + String.valueOf(numEfforts.get(start+3)) + "\n" + gamesWon.get(start+3));
    	Label id6 = new Label(secretWords.get(start+4) + "\n" + String.valueOf(numEfforts.get(start+4)) + "\n" + gamesWon.get(start+4));
    	
    	HBox box = new HBox(5.0D);
        box.setPadding(new Insets(25.0D, 5.0D, 5.0D, 50.0D));
        box.getChildren().addAll( id1, id2 , id3, id4, id5, id6  );
        Scene scene = new Scene(box, 595.0D, 150.0D, Color.BEIGE);
        stage.setTitle("Rounds");
        stage.setScene(scene);
        stage.show();
    	}
    	
        catch(Exception e){
	        if(start <0) {
	    		Stage stagex = new Stage();
	    	
	    		Label exc = new Label("YOU HAVE TO PLAY AT LEAST 5 GAMES TO SEE SCORES");
	    		HBox box = new HBox(5.0D);
	            box.setPadding(new Insets(25.0D, 5.0D, 5.0D, 50.0D));
	            box.getChildren().addAll( exc);
	            Scene scene = new Scene(box, 595.0D, 150.0D, Color.BEIGE);
	            stagex.setTitle("Rounds");
	            stagex.setScene(scene);
	            stagex.show();
	    	}
        }
    }   

    @FXML
    void m23_action(ActionEvent event) {     //Solution
    	
    	// game saved as lost , hidden word appears
    	
    	secretWords.add(fh.SecretWord());
		numEfforts.add(fh.numBadGuesses() +fh.numlettersfound());
		gamesWon.add("LOSS");
    	
    	Stage stage = new Stage();
    	Label id = new Label("Secret word is :  " + fh.SecretWord());

        HBox box = new HBox(5.0D);
        box.setPadding(new Insets(25.0D, 5.0D, 5.0D, 50.0D));
        box.getChildren().addAll( id);
        Scene scene = new Scene(box, 595.0D, 150.0D, Color.BEIGE);
        stage.setTitle("Solution");
        stage.setScene(scene);
        stage.show();

    }   
  
}

