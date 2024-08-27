
package com.example.projectdreamline;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Menu extends Application {

    private Button option(String s, int offset) {
        Button button = new Button(s);
        button.setPrefWidth(150);
        button.setPrefHeight(50);
        button.setTranslateX(325);
        button.setTranslateY(offset);
        button.setStyle("-fx-base: darkcyan");

        return button;
    }
    private Button exit() {
        Button button = new Button("Exit");
        button.setPrefWidth(150);
        button.setPrefHeight(50);
        button.setTranslateX(325);
        button.setTranslateY(450);
        button.setStyle("-fx-base: darkcyan");

        button.setOnAction(actionEvent -> System.exit(0));

        return button;
    }

    public void dreamScoreScreen(Stage stage) {
        List<FlightTrackingExample.FlightData> flightDataList = FlightTrackingExample.getFlightDataList("");

        Label label = new Label("Top Flights - Dreamtime");
        label.setTranslateX(175);
        label.setTranslateY(50);
        label.setFont(new Font(25));

        // Sort by scores
        /*ObservableList<String> scores = flightScores();

        FXCollections.sort(scores);
        FXCollections.reverse(scores);*/


        ListView<String> listView = new ListView<>();
        listView.setPrefHeight(400);
        listView.setPrefWidth(550);
        listView.setTranslateX(25);
        listView.setTranslateY(100);
        listView.setStyle("-fx-font-size: 1.5em ; -fx-control-inner-background: azure;");

        //Back button
        Button back_button = new Button("BACK");
        back_button.setAlignment(Pos.CENTER_LEFT);
        back_button.setStyle("-fx-base: cornflowerblue");

        back_button.setOnAction(actionEvent -> {start(stage);});


        for (FlightTrackingExample.FlightData flightData : flightDataList) {
            listView.getItems().add("Flight " + flightData.getAirlineCode() + " " + flightData.getFlightNumber()
                    + " " + flightData.getSourceLocationCode() + " to " + flightData.getDestinationLocationCode()
                    + " departs at " + flightData.getDepartureDate() + flightData.getDepartureTime());
        }

        Group root = new Group(label, listView, back_button);

        Scene scene = new Scene(root, 600, 600, Color.MINTCREAM);
        stage.setScene(scene);
        stage.show();
    }

    public Button newbutton(String title){
        Button b1 = new Button(title);
        b1.setPrefWidth(200);
        b1.setPrefHeight(40);
        b1.setStyle("-fx-base: cornflowerblue");
        return b1;
    }

    public void reciept(Stage stage, Scene main, Scene back, String info){
        stage.setTitle("Reciept");

        Label title = new Label("RECIEPT");
        title.setAlignment(Pos.CENTER);
        title.setFont(Font.font(20));

        Label details = new Label(info);
        details.setFont(Font.font(15));
        details.setAlignment(Pos.CENTER_LEFT);

        Button home = newbutton("HOME");

        VBox context = new VBox(30, title, details, home);
        context.setAlignment(Pos.CENTER);

        BorderPane layout = new BorderPane();
        layout.setCenter(context);

        Scene reciept_scene = new Scene(layout,900, 700, Color.MINTCREAM);

        home.setOnAction(e-> {
            stage.setScene(main);
            stage.show();
        });

        stage.setScene(reciept_scene);
        stage.show();
    }

    public void payment_screen(Stage stage, Scene main, Scene back, String info) {

        stage.setTitle("Payment");

        Label title = new Label("PAYMENT");
        title.setAlignment(Pos.CENTER);
        title.setFont(Font.font(20));

        Label name = new Label("NAME ON CARD");
        Label cdn = new Label("CARD NUMBER");
        Label exd = new Label("VALID THROUGH");
        Label secn = new Label("SECURITY NUMBER");

        TextField namet = new TextField();
        TextField cdnt = new TextField();
        TextField exdt = new TextField();
        exdt.setPromptText("MM/YY");
        TextField secnt = new TextField();

        HBox namel = new HBox(10, name, namet);
        HBox cdnl = new HBox(10, cdn, cdnt);
        HBox exdl = new HBox(10, exd, exdt);
        HBox secnl = new HBox(10, secn, secnt);

        Button continub = newbutton("PAY");

        Label details = new Label(info);
        details.setFont(Font.font(15));
        details.setAlignment(Pos.CENTER_LEFT);

        VBox context = new VBox(30, title, details, namel, cdnl, exdl, secnl, continub);
        context.setAlignment(Pos.CENTER);

        Button home = new Button("HOME");
        Button backb = new Button("BACK");

        HBox backhomeb = new HBox(10, home, backb);

        BorderPane layout = new BorderPane();
        layout.setLeft(backhomeb);
        layout.setCenter(context);

        Scene payment_scene = new Scene(layout,900, 700, Color.MINTCREAM);

        home.setOnAction(e-> {
            stage.setTitle("Home");
            stage.setScene(main);
            stage.show();
        });

        backb.setOnAction(e-> {
            stage.setTitle("Sign-up/Log-in");
            stage.setScene(back);
            stage.show();
        });

        continub.setOnAction(e->{
            reciept(stage, main, payment_scene, (info + "\n\nCARD HOLDER NAME: " + namet.getText() + "\nCARD NUMBER: " + cdnt.getText() + "\nVALID THROUGH: " + exdt.getText() + "\nSECURITY CODE" + secnt.getText()));
        });

        stage.setScene(payment_scene);
        stage.show();

    }
    public void signin_screen(Stage stage, Scene main, Scene back, String info){

        stage.setTitle("Sign-up/Log-in");

        Label title = new Label("SIGN-UP / LOG-IN");
        title.setAlignment(Pos.CENTER);
        title.setFont(Font.font(20));

        Button signupb = newbutton("SIGN-UP");
        Button loginb = newbutton("LOG-IN");
        Button skipb = newbutton("SKIP");


        Button home = new Button("HOME");
        Button backb = new Button("BACK");
        Button continueb = newbutton("CONTINUE");
        continueb.setAlignment(Pos.CENTER);

        HBox backhomeb = new HBox(10, home, backb);


        HBox signlog = new HBox(20, signupb, loginb);
        signlog.setAlignment(Pos.CENTER);

        VBox options = new VBox(10, signlog, skipb);
        options.setAlignment(Pos.CENTER);

        VBox content = new VBox(30, title, options);
        content.setAlignment(Pos.CENTER);

        BorderPane layout = new BorderPane();
        layout.setCenter(content);
        layout.setLeft(backhomeb);

        Scene sign_scene = new Scene(layout,900, 700, Color.MINTCREAM);

        Label username = new Label("USERNAME");
        Label password = new Label("PASSWORD");
        Label cpassword = new Label("CONFIRM PASSWORD");
        TextField ust = new TextField();
        PasswordField pst = new PasswordField();
        PasswordField pst2 = new PasswordField();
        PasswordField cpst = new PasswordField();

        HBox usi = new HBox(10, username, ust);
        HBox psi = new HBox(10, password, pst);
        HBox psi2 = new HBox(10, (new Label("PASSWORD")), pst2);
        HBox cpsi = new HBox(10, cpassword, cpst);

        signupb.setOnAction(e -> {
            content.getChildren().clear();
            content.getChildren().add(title);
            content.getChildren().add(signlog);
            content.getChildren().add(usi);
            content.getChildren().add(psi);
            content.getChildren().add(cpsi);
            content.getChildren().add(continueb);
            content.getChildren().add(skipb);
        });

        continueb.setOnAction(e-> {
            if(!pst.getText().equals(cpst.getText())){
                content.getChildren().add(new Label("THE PASSWORDS DON'T MATCH"));
            }
            else {
                payment_screen(stage, main, sign_scene, info);
            }
        });

        loginb.setOnAction(e-> {
            content.getChildren().clear();
            content.getChildren().add(title);
            content.getChildren().add(signlog);
            content.getChildren().add(usi);
            content.getChildren().add(psi2);
            content.getChildren().add(continueb);
            content.getChildren().add(skipb);
        });

        skipb.setOnAction(e-> {
            payment_screen(stage, main, sign_scene, info);
        });

        home.setOnAction(e-> {
            stage.setTitle("Home");
            stage.setScene(main);
            stage.show();
        });

        backb.setOnAction(e-> {
            stage.setTitle("Search Flights");
            stage.setScene(back);
            stage.show();
        });

        stage.setScene(sign_scene);
        stage.show();

    }


    public void searchscreen(Stage stage, Scene main) {

        stage.setTitle("Search Flights");

        //title of page
        Text title = new Text("SEARCH FLIGHTS");
        title.setFont(Font.font(20));

        //instructions
        Label from = new Label("Home Location:");
        Label to = new Label("Destination:");
        Label date = new Label("Date(YYYY-MM-DD):");

        TextField fromt = new TextField();
        TextField tot = new TextField();
        TextField datet = new TextField();

        //Search button
        Button search_button = new Button("SEARCH");
        search_button.setStyle("-fx-base: cornflowerblue");

        //Back button
        Button back_button = new Button("BACK");
        back_button.setAlignment(Pos.CENTER_LEFT);
        back_button.setStyle("-fx-base: cornflowerblue");

        back_button.setOnAction(actionEvent -> {stage.setScene(main);});

        ListView<Button> results = new ListView<Button>();
        results.setMaxHeight(400);
        results.setMaxWidth(800);
        results.setStyle("-fx-font-size: 1.5em ; -fx-control-inner-background: azure;");

        Scene search_scene = new Scene(results, 900, 700, Color.MINTCREAM);


        search_button.setOnAction(e->
        {
            results.getItems().clear();
            Button titleb = new Button("Flight#\t\t" + "Airline Code\t" + "From\t\t" + "To\t\t\t\t" + "Date\t\t\t\t\t" + "Time\t");
            titleb.setMaxWidth(790);
            results.getItems().add(titleb);

            List<FlightTrackingExample.FlightData> flightDataList = FlightTrackingExample.getFlightDataList(datet.getText());
            for (FlightTrackingExample.FlightData flightData : flightDataList) {
                if(flightData.getSourceLocationCode().equals(fromt.getText().toUpperCase()) && flightData.getDestinationLocationCode().equals(tot.getText().toUpperCase()) && flightData.getDepartureDate().equals(datet.getText())){
                    String info = "Flight Number: " + flightData.getFlightNumber() + "\nAIRLINE CODE: " + flightData.getAirlineCode() + "\nHOME LOCATION: " + flightData.getSourceLocationCode() + "\nDESTINATION LOCATION: " + flightData.getDestinationLocationCode() + "\nDATE: " + flightData.getDepartureDate() + "\nARRIVAL TIME: " + flightData.getArrivalTime() + "\nDEPARTURE TIME: " + flightData.getDepartureTime();
                    Button b1 = new Button(flightData.getFlightNumber() + "\t\t\t" + flightData.getAirlineCode() + "\t\t\t" + flightData.getSourceLocationCode() + "\t\t\t" + flightData.getDestinationLocationCode() + "\t\t\t" + flightData.getDepartureDate() + "\t\t\t" + flightData.getDepartureTime());
                    b1.setMaxWidth(790);
                    results.getItems().add(b1);
                    b1.setOnAction(a -> {signin_screen(stage, main, search_scene, info);});
                }
            }
            if (results.getItems().size() == 1) {
                results.getItems().add(new Button ("No flights available"));
            }
        });

        VBox froml = new VBox(from, fromt);
        VBox tol = new VBox(to, tot);
        VBox datel = new VBox(date, datet);

        //Layout implementation

        HBox search_options = new HBox(10, froml, tol, datel);
        search_options.setAlignment(Pos.CENTER);

        VBox Mid_section = new VBox(50, title, search_options, search_button, results);
        Mid_section.setAlignment(Pos.TOP_CENTER);

        BorderPane layout = new BorderPane();
        layout.setCenter(Mid_section);
        layout.setTop(back_button);
        layout.setStyle("-fx-background-color: MINTCREAM;");

        search_scene.setRoot(layout);
        stage.setScene(search_scene);
        stage.show();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Dreamline - Fly in comfort!");

        Label label = new Label("Fly comfortable. Fly like a dream!");
        label.setTranslateX(220);
        label.setTranslateY(50);
        label.setStyle("-fx-text-fill: white; -fx-font: 25 georgia");

        Button searchFlights = option("Search Available Flights", 150);
        searchFlights.setOnAction(actionEvent -> {});
        Button inputFlight = option("Track A Flight", 250);
        inputFlight.setOnAction(actionEvent -> {
            try {
                FlightInfoMain flightInfoMain = new FlightInfoMain();
                flightInfoMain.start(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button viewDreamscores = option("View dreamscores", 350);
        viewDreamscores.setOnAction(actionEvent -> {
            DreamScore ds = new DreamScore();
            ds.dreamScoreScreen(stage);
        });
        Button exit = exit();


        Image backgroundImage = new Image(getClass().getResourceAsStream("menu.jpg"));
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        BackgroundImage backgroundImageObj = new BackgroundImage(backgroundImage, BackgroundRepeat.ROUND, BackgroundRepeat.ROUND, null, backgroundSize);
        Background background = new Background(backgroundImageObj);

        Group root = new Group(label, searchFlights, inputFlight, viewDreamscores, exit);

        StackPane stackPane = new StackPane(root);
        stackPane.setBackground(background);

        Scene scene = new Scene(stackPane, 600, 600, Color.MINTCREAM);

        searchFlights.setOnAction(actionEvent -> {searchscreen(stage, scene);});

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}