package com.example.projectdreamline;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.DatedFlight;
import com.amadeus.resources.Location;

import javafx.scene.control.Alert;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

// creating a controller class to
// handle actions
public class FlightInfoController {

    @FXML
    private TextField airlineField;

    @FXML
    private TextField flightNumberField;

    @FXML
    private DatePicker departureDatePicker;

    @FXML
    private ListView<String> suggestionsListView;

    @FXML
    private Button backButton;

    @FXML
    private Pane contentPane;

    private Map<String, String> airlineNamesAndCodes = AmadeusExample.getAirlineNamesAndCodes();

    private List<FlightTrackingExample.FlightData> flightDataList = FlightTrackingExample.getFlightDataList("", "JFK", "MAD");
    private ObservableList<String> suggestions = FXCollections.observableArrayList();


    @FXML
    private void initialize() {
        // Enable auto-suggestion for airline text field
        airlineField.textProperty().addListener((observable, oldValue, newValue) -> {
            suggestions.clear();
            if (newValue == null || newValue.isEmpty()) {
                suggestionsListView.setVisible(false);
                return;
            }
            for (Map.Entry<String, String> entry : airlineNamesAndCodes.entrySet()) {
                String airlineCode = entry.getKey();
                String airlineName = entry.getValue();
                String airlineEntry = airlineName + " (" + airlineCode + ")";
                if (airlineEntry.toLowerCase().startsWith(newValue.toLowerCase())) {
                    suggestions.add(airlineEntry);
                }
            }
            suggestionsListView.setItems(suggestions);
            suggestionsListView.setVisible(true);
        });

        // Set up selection listener for suggestions list view
        suggestionsListView.setOnMouseClicked(event -> {
            String selected = suggestionsListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                airlineField.setText(selected);
                airlineField.positionCaret(selected.length());
                suggestionsListView.setVisible(false);
                airlineField.requestFocus();
            }
        });
    }

    private Location.GeoCode getGeoCodeForAirport(Amadeus amadeus, String airportCode) throws ResponseException {
        Location[] airports = amadeus.referenceData.locations.get(Params
                .with("keyword", airportCode)
                .and("subType", "AIRPORT")
                .and("page[limit]", 1));
        if (airports.length > 0) {
            return airports[0].getGeoCode();
        }
        return null;
    }

    private void printGeoCode(Location.GeoCode geoCode) {
        System.out.println("Latitude: " + geoCode.getLatitude());
        System.out.println("Longitude: " + geoCode.getLongitude());
    }

    private void loadMapPage(String departureAirportCode, String arrivalAirportCode, String enteredAirlineCode, String enteredFlightNumber,
                             String departureTime, String arrivalTime, String departureGate, String arrivalGate) {
        try {

            Amadeus amadeus = Amadeus.builder(AmadeusExample.getClientID(), AmadeusExample.getClientSecret()).build();

            Location.GeoCode departureGeoCode = getGeoCodeForAirport(amadeus, departureAirportCode);
            if (departureGeoCode != null) {
                System.out.println("Departure Airport:");
                printGeoCode(departureGeoCode);
            } else {
                System.out.println("No information found for departure airport: " + departureAirportCode);
                return; // Exit method if departure information is not available
            }

            // Retrieve latitude and longitude for arrival airport
            Location.GeoCode arrivalGeoCode = getGeoCodeForAirport(amadeus, arrivalAirportCode);
            if (arrivalGeoCode != null) {
                System.out.println("Arrival Airport:");
                printGeoCode(arrivalGeoCode);
            } else {
                System.out.println("No information found for arrival airport: " + arrivalAirportCode);
                return; // Exit method if arrival information is not available
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("MapPage.fxml"));
            Parent root = loader.load();

            LocalDate enteredDepartureDate = departureDatePicker.getValue();
            String formattedEnteredDepartureDate = enteredDepartureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


            // pass necessary information to the map controller
            MapPageController mapController = loader.getController();
            mapController.initialize(departureGeoCode.getLatitude(), departureGeoCode.getLongitude(),
                    arrivalGeoCode.getLatitude(), arrivalGeoCode.getLongitude());
            mapController.flightDetail(departureAirportCode, arrivalAirportCode, enteredAirlineCode, enteredFlightNumber,
                    departureTime, arrivalTime, departureGate, arrivalGate, formattedEnteredDepartureDate);

            // show the map page
            Stage stage = new Stage();
            stage.setScene((new Scene(root)));
            stage.setTitle("Flight Route Map");
            stage.show();

        } catch (IOException | ResponseException e) {
            e.printStackTrace();

        }
    }

    private FlightTrackingExample.FlightData DatedFlightToData(DatedFlight offer) {
        System.out.println(offer.toString());
        String airlineCode = offer.getFlightDesignator().getCarrierCode();
        String flightNumber = String.valueOf(offer.getFlightDesignator().getFlightNumber());
        String departureString = offer.getScheduledDepartureDate();
        String sourceLocationCode = offer.getFlightPoints()[0].getIataCode();
        String destinationLocationCode = offer.getFlightPoints()[1].getIataCode();
//        String departureTime = offer.getFlightPoints()[0].getDeparture().toString();
//        String arrivalTime = offer.getFlightPoints()[1].getArrival().toString();
        String departureDateTime = offer.getFlightPoints()[0].getDeparture().getTimings()[0].getValue();
        String arrivalDateTime = offer.getFlightPoints()[1].getArrival().getTimings()[0].getValue();

//        String departureDateTime = offer.getItineraries()[0].getSegments()[0].getDeparture().getAt();
//        String departureGate = offer.getItineraries()[0].getSegments()[0].getDeparture().getTerminal();
        // System.out.println(departureDateTime);
//        String arrivalDateTime = offer.getItineraries()[0].getSegments()[0].getArrival().getAt();
//        String arrivalGate = offer.getItineraries()[0].getSegments()[0].getArrival().getTerminal();

        String arrivalTime = arrivalDateTime.split("[T\\+\\-]")[3];

        // parsing to get date only and removing time
        LocalDate departureDate = LocalDate.parse(departureDateTime.split("T")[0]);
        // Extracting time separately
        String departureTime = departureDateTime.split("[T\\+\\-]")[3];
        String departureGate = "X";
        String arrivalGate = "X";
//                    System.out.println(offer.toString());
//                    String departureString = departureDate.toString();
        FlightTrackingExample.FlightData flightData = new FlightTrackingExample.FlightData(airlineCode, flightNumber, departureString,
                sourceLocationCode, destinationLocationCode, departureTime,
                arrivalTime, departureGate, arrivalGate);
        return flightData;
    }

    @FXML
    private void handleSubmitButtonAction() {
        flightDataList.clear();
        String enteredAirlineCode = airlineField.getText(); // Assuming the user enters the airline code
        String enteredFlightNumber = flightNumberField.getText();
        LocalDate enteredDepartureDate = departureDatePicker.getValue();

        // Extract airline code from the entered string
        String enteredCode = enteredAirlineCode.substring(enteredAirlineCode.indexOf("(") + 1, enteredAirlineCode.indexOf(")"));
        System.out.println(enteredCode);
        // Format entered departure date to match the format of flight data
        String formattedEnteredDepartureDate = enteredDepartureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Amadeus amadeus = Amadeus.builder(AmadeusExample.getClientID(), AmadeusExample.getClientSecret()).build();
        try {
            DatedFlight[] flightOffers = amadeus.schedule.flights.get(
                    Params.with("carrierCode", enteredCode)
                            .and("flightNumber", enteredFlightNumber)
                            .and("scheduledDepartureDate", formattedEnteredDepartureDate));
            for (DatedFlight df : flightOffers) {
                FlightTrackingExample.FlightData fd = DatedFlightToData(df);
                flightDataList.add(fd);
            }
        } catch (ResponseException e) {
            e.printStackTrace();
        }
        // Check if the entered data matches any entry in the flightDataList
        boolean matchFound = false;
        for (FlightTrackingExample.FlightData flightData : flightDataList) {
            System.out.println("Entered Airline Code: " + enteredCode);
            System.out.println("Entered Flight Number: " + enteredFlightNumber);
            System.out.println("Entered Departure Date: " + formattedEnteredDepartureDate);

            System.out.println("Flight Data Airline Code: " + flightData.getAirlineCode());
            System.out.println("Flight Data Flight Number: " + flightData.getFlightNumber());
            System.out.println("Flight Data Departure Date: " + flightData.getDepartureDate());
            System.out.println("Source and Destination Location: " + flightData.getSourceLocationCode() + " to " + flightData.getDestinationLocationCode());


            // &&
            // flightData.getDepartureDate().equals(formattedEnteredDepartureDate)
            if (flightData.getAirlineCode().equalsIgnoreCase(enteredCode) &&
                    flightData.getFlightNumber().equalsIgnoreCase(enteredFlightNumber)) {
                matchFound = true;
                loadMapPage(flightData.getSourceLocationCode(), flightData.getDestinationLocationCode()
                , enteredAirlineCode, enteredFlightNumber, flightData.getDepartureTime(), flightData.getArrivalTime(),
                        flightData.getDepartureGate(), flightData.getArrivalGate());
                break;
            }
        }
        // Create flight details VBox
        if (!matchFound) {
            Label noMatchLabel = new Label("No Match Found");
            noMatchLabel.setStyle("-fx-text-fill: #ED4040; -fx-font-weight: bold; -fx-background-color: #FFDDDD;");
            // Create a VBox to arrange the labels vertically
            VBox flightDetails = new VBox();
            flightDetails.getChildren().addAll(
                    new Label("Airline: " + enteredAirlineCode),
                    new Label("Flight Number: " + enteredFlightNumber),
                    new Label("Departure Date: " + enteredDepartureDate),
                    noMatchLabel // Include the "No match found" label below the other labels
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(null);
            alert.getDialogPane().setContent(flightDetails);
            alert.setTitle("Flight Details");
            alert.showAndWait();
        }

    }

    @FXML
    private void backButtonAction() {
            // get reference to the current stage
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close(); // close the current stage

            // create a new stage for the dreamline window
            Stage dreamlineStage = new Stage();
        // initialize and show the dreamline window
        Menu dreamline = new Menu();
        dreamline.start(dreamlineStage);
    }
}
