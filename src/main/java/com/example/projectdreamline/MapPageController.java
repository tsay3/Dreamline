package com.example.projectdreamline;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;


public class MapPageController {

    @FXML
    private WebView webView;

    @FXML
    private Label sourceLabel;

    @FXML
    private Label destinationLabel;

    @FXML
    private Label airlineLabel;
    @FXML
    private Label flightNumberLabel;

    @FXML
    private HBox airlineFlightNumBox;
    @FXML
    private HBox departureTimeBox;
    @FXML
    private Label departureTimeLabel;

    @FXML
    private Label departingTimeLabel;

    @FXML
    private Label departingAirportLabel;

    @FXML
    private Label arrivalTimeLabel;
    @FXML
    private Label arrivalAirportLabel;


    public void initialize(double departureLatitude, double departureLongitude,
                           double arrivalLatitude, double arrivalLongitude) {
        // get the google_key from api
        String google_key = "";
        try {
            File file = new File(
                    "src/main/resources/com/example/projectdreamline/api_keys.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            google_key = document.getElementsByTagName("google_key").item(0).getTextContent();
        } catch (Exception e) {

        }
        // Set WebView constraints to match the AnchorPane size

        // Load the map content
        WebEngine webEngine = webView.getEngine();
        // Replace "/com/example/projectdreamline/google_map.html" with your actual file path
        //webEngine.load(getClass().getResource("/com/example/projectdreamline/google_map.html").toExternalForm());
        String htmlContent = "<html><head><title>Flight Route</title>"
                + "<script src=\"https://maps.googleapis.com/maps/api/js?key=" + google_key + "\"></script>"
                + "<script>"
                + "function drawFlightRoute() {"
                + "var departureLocation = {lat: " + departureLatitude + ", lng: " + departureLongitude + "};"
                + "var arrivalLocation = {lat: " + arrivalLatitude + ", lng: " + arrivalLongitude + "};"
                + "var centerLat = (departureLocation.lat + arrivalLocation.lat) / 2;"
                + "var bounds = new google.maps.LatLngBounds();"
                + "bounds.extend(new google.maps.LatLng(departureLocation.lat, departureLocation.lng));"
                + "bounds.extend(new google.maps.LatLng(arrivalLocation.lat, arrivalLocation.lng));"
                + "var map = new google.maps.Map(document.getElementById('map'), {"
                + "zoom: 8,"
                + "center: bounds.getCenter()"
                + "});"
                + "map.fitBounds(bounds);"
                + "var flightPath = new google.maps.Polyline({"
                + "path: [departureLocation, arrivalLocation],"
                + "geodesic: true,"
                + "strokeColor: '#FF0000',"
                + "strokeOpacity: 1.0,"
                + "strokeWeight: 2"
                + "});"
                + "flightPath.setMap(map);"
                + "}"
                + "</script></head>"
                + "<body onload=\"drawFlightRoute()\"><div id=\"map\" style=\"width: 90%; height: 75%;\"></div></body></html>";
        webEngine.loadContent(htmlContent);

    }

    public void flightDetail(String departureLocationCode, String arrivalLocationCode, String enteredAirlineCode, String enteredFlightNumber,
                             String departureTime, String arrivalTime, String departureGate, String arrivalGate, String formattedEnteredDepartureDate) {


        sourceLabel.setText(departureLocationCode);
        destinationLabel.setText(arrivalLocationCode);

        airlineLabel.setText(enteredAirlineCode);
        flightNumberLabel.setText(enteredFlightNumber);

        // Parse departure time
        LocalTime time = LocalTime.parse(departureTime, DateTimeFormatter.ofPattern("HH:mm"));
        // Extract hours and minutes
        int hours = time.getHour();
        int minutes = time.getMinute();
        String amOrPm = (hours < 12) ? "AM" : "PM";
        hours = (hours > 12) ? hours - 12 : hours; // Convert to 12-hour format

        Duration remainingTime = calculateTimeRemaining(departureTime);
        long hoursRemaining = remainingTime.toHours();
        long minutesRemaining = remainingTime.toMinutes() % 60;

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String[] departureDates = new String[1];
        departureDates[0] = currentDate.format(formatter);

        System.out.println("Hours remaining: " + hoursRemaining);
        System.out.println("Minutes remaining: " + minutesRemaining);

        if (departureDates[0].equals(formattedEnteredDepartureDate)) {
            departureTimeBox.setStyle("-fx-background-color: rgba(144, 238, 144, 0.5);");
            airlineFlightNumBox.setStyle("-fx-background-color: rgba(144, 238, 144, 0.5);");
            departureTimeLabel.setText(String.format("On Time • Departs in %02dh %02dm ", hoursRemaining, minutesRemaining));
        } else {
            departureTimeBox.setStyle("-fx-background-color: rgba(173, 216, 230, 0.5);");
            airlineFlightNumBox.setStyle("-fx-background-color: rgba(173, 216, 230, 0.5);");
            departureTimeLabel.setText("Scheduled For Date: " + formattedEnteredDepartureDate);
        }

        departingTimeLabel.setText(String.format("Departure ⮕ %02d:%02d " + amOrPm, hours, minutes));
        departingAirportLabel.setText(departureLocationCode + " Airport" + " • Terminal " + departureGate);

         time = LocalTime.parse(arrivalTime, DateTimeFormatter.ofPattern("HH:mm"));
        // Extract hours and minutes
         hours = time.getHour();
         minutes = time.getMinute();
         amOrPm = (hours < 12) ? "AM" : "PM";
        hours = (hours > 12) ? hours - 12 : hours;

        arrivalTimeLabel.setText(String.format("Arrival ⮕ %02d:%02d " + amOrPm, hours, minutes));
        arrivalAirportLabel.setText(arrivalLocationCode + " Airport" + " • Terminal " + arrivalGate);


    }

    public Duration calculateTimeRemaining(String departureTime) {
        // Parse the departure time
        LocalTime time = LocalTime.parse(departureTime, DateTimeFormatter.ofPattern("HH:mm"));

        // Get the current time
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("America/Chicago"));

        // Combine the current date with the departure time
        LocalDateTime departureDateTime = LocalDateTime.of(currentTime.toLocalDate(), time);

        // Calculate the difference between current time and departure time
        Duration timeRemaining = Duration.between(currentTime, departureDateTime);

        return timeRemaining;
    }


}