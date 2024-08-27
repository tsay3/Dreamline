package com.example.projectdreamline;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Search_Screen extends Application {

		private Button search_button;
		private Button back_button;
		
		private TextField search_flight_number;
		
		private Text title;
		private Text instruction;
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Search Flights");
		
		//title of page
		title = new Text("SEARCH FLIGHTS");
		title.setFont(Font.font(20));
		
		//instructions
		instruction = new Text("Flight Number:");
		
		
		//Search button
		search_button = new Button("SEARCH");
		search_button.setStyle("-fx-background-color: #6c6ce4; -fx-text-fill: silver; -fx-font-weight: bold");
		
		//Uncomment the line below and add functionality to the search button
		//search_button.setOnAction(e->);
		
		
		//Back button
		back_button = new Button("BACK");
		back_button.setAlignment(Pos.CENTER_LEFT);
		back_button.setStyle("-fx-background-color: #6c6ce4; -fx-text-fill: silver; -fx-font-weight: bold");
		
		//Uncomment the line below and connect the back button to the menu page
		//back_button.setOnAction(e->);
		
	
		//Search field text box
		search_flight_number = new TextField();
		search_flight_number.setPromptText("Enter Flight Number");
		
		ListView<String> results = new ListView<String>();
		results.setMaxHeight(300);
		results.setMaxWidth(250);
		results.setStyle("-fx-background-color: White;");
		
		//Add the results from the search here
		results.getItems().add("No Results");
		
		
		//Layout implementation
		HBox search_options = new HBox(10, instruction, search_flight_number);
		search_options.setAlignment(Pos.CENTER);
		
		VBox Mid_section = new VBox(50, title, search_options, search_button, results);
		Mid_section.setAlignment(Pos.TOP_CENTER);
		
		BorderPane layout = new BorderPane();
		layout.setCenter(Mid_section);
		layout.setTop(back_button);
		layout.setStyle("-fx-background-color: White;");
		
		Scene search_scene = new Scene(layout, 500, 500);
		primaryStage.setScene(search_scene);
		primaryStage.show();
		
		
	}
		
}
