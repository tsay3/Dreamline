// Version 2.0.1

package com.example.projectdreamline;

// import javafx.application.Application;
// import javafx.scene.Group;
// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.paint.Color;
// import javafx.scene.paint.Paint;
// import javafx.scene.text.Font;
// import javafx.stage.Stage;
// import javafx.scene.Parent;
// import javafx.scene.control.Tab;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.resources.DatedFlight;
import com.amadeus.resources.FlightOfferSearch;
import com.example.projectdreamline.data.FlightComfort;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.example.projectdreamline.FlightTrackingExample.FlightData;

public class DreamScore {

    TextField airline_field;
    TextField flightnum_field;
    DatePicker date_field;
    TextField src_field;
    TextField dest_field;
    Label error_msg;
    ObservableList<ViewData> scores;
    private ListView<String> suggestionsListView;
    private ObservableList<String> suggestions = FXCollections.observableArrayList();

    class ScoreComparator implements Comparator<ViewData> {
        @Override
        public int compare(ViewData entry1, ViewData entry2) {
//            Integer score1 = Integer.parseInt(entry1.substring(entry1.length() - 3, entry1.length()).trim());
//            Integer score2 = Integer.parseInt(entry2.substring(entry2.length() - 3, entry2.length()).trim());
            Integer score1 = entry1.score.get();
            Integer score2 = entry2.score.get();
            return score2.compareTo(score1);
        }
    }

    public class ViewData {
        private StringProperty airline;
        public void setAirline(String value) { airlineProperty().set(value); }
        public String getAirline() { return airlineProperty().get(); }
        public StringProperty airlineProperty() {
            if (airline == null) airline = new SimpleStringProperty(this, "airline");
            return airline;
        }
        private StringProperty route;
        public void setRoute(String value) { routeProperty().set(value); }
        public String getRoute() { return routeProperty().get(); }
        public StringProperty routeProperty() {
            if (route == null) route = new SimpleStringProperty(this, "route");
            return route;
        }
        private IntegerProperty score;
        public void setScore(Integer value) { scoreProperty().set(value); }
        public Integer getScore() { return scoreProperty().get(); }
        public IntegerProperty scoreProperty() {
            if (score == null) score = new SimpleIntegerProperty(this, "score");
            return score;
        }
//        public String airline;
//        public String route;
//        public int score;
    }
    private Map<String, String> airlineNamesAndCodes = AmadeusExample.getAirlineNamesAndCodes();
    static final HashMap<String, String> flights = new HashMap(Map.of(
       "AA", "American Airlines",
            "UA", "United Airlines",
            "AC", "Air Canada"
    ));

    private void initialize() {
        // Enable auto-suggestion for airline text field
        airline_field.textProperty().addListener((observable, oldValue, newValue) -> {
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
                airline_field.setText(selected);
                airline_field.positionCaret(selected.length());
                suggestionsListView.setVisible(false);
                airline_field.requestFocus();
            }
        });
    }

    private float setFakeScore(FlightData f) {
//        return origin.charAt(0) < 'M' ? .7f : 1.0f;
        float factor = (float) Math.random();
        return 100 - (factor * factor * factor) * 100;
    }

    private List<FlightData> getFlightDataList(String origin, String dest, String airline, String flightNum, String date) {
        List<FlightData> flightDataList = new ArrayList<>();

        if (!origin.equals("")) {
            System.out.println("Origin: " + origin);
        }
        if (!dest.equals("")) {
            System.out.println("Destination: " + dest);
        }
        if (!airline.equals("")) {
            System.out.println("Airline: " + airline);
        }
        if (!flightNum.equals("")) {
            System.out.println("Flight #: " + flightNum);
        }

        String enteredCode = "";
        if (airline.indexOf(")") == -1) {
            // try converting it to an airline code
            enteredCode = (airlineNamesAndCodes.get(airline));
            enteredCode = (enteredCode != null) ? enteredCode : airline;
        } else {
            enteredCode = airline.substring(airline.indexOf("(") + 1, airline.indexOf(")"));
        }
        error_msg.setText("");

        Amadeus amadeus = Amadeus.builder("ZPeflw5GDnxraU9hFpdk6UAYPQm7jsay", "CQYi8IGjusYdu6Xb").build();

        if ((!enteredCode.equals("")) && (!flightNum.equals(""))) {
            System.out.println("schedule.flights: " + enteredCode + flightNum + " " + date);
            try {
                DatedFlight[] flightOffers = amadeus.schedule.flights.get(
                        Params.with("carrierCode", enteredCode)
                                .and("flightNumber", flightNum)
                                .and("scheduledDepartureDate", date));
                System.out.println("Found " + flightOffers.length + " flights");
                for (DatedFlight offer : flightOffers) {
                    FlightData flightData = DatedFlightToData(offer);
                    flightDataList.add(flightData);
                }
                if (flightDataList.size() == 0) {
                    error_msg.setText("No flights found.");
                }
            } catch (Exception e) {
                // Handle errors
                error_msg.setText("Please validate carrier code and number.");
                e.printStackTrace();
            }
        } else if ((!origin.equals("")) && (!dest.equals(""))) {
            System.out.println("flightOffersSearch");
            try {
                // Make a request to the Flight Offers Search API
                FlightOfferSearch[] flightOffers = amadeus.shopping.flightOffersSearch.get(
                        Params.with("originLocationCode", origin)
                                .and("destinationLocationCode", dest)
                                .and("departureDate", date)
                                .and("adults", 1));
                System.out.println("Flights found: " + flightOffers.length);
                // Process the response and store flight data
                for (FlightOfferSearch offer : flightOffers) {
                    String airlineCode = offer.getItineraries()[0].getSegments()[0].getCarrierCode();
                    String flightNumber = offer.getItineraries()[0].getSegments()[0].getNumber();
                    boolean matchesAirline = (enteredCode.equals("") || (airlineCode.matches(enteredCode) ||
                            airlineNamesAndCodes.get(airlineCode).matches(enteredCode)));
                    boolean matchesFlightNum = (flightNum.equals("") || (flightNumber.equals(flightNum)));
                    boolean directFlight = Arrays.stream(offer.getItineraries()).anyMatch(n -> n.getSegments().length == 1);
                    System.out.println("Input: (" + enteredCode + "), (" + flightNum + ")");
                    System.out.println("Processed: (" + airlineCode + "), (" + flightNumber + ")");
                    System.out.println("Results: " + String.valueOf(matchesAirline) + String.valueOf(matchesFlightNum) + String.valueOf(directFlight));
                    if (matchesAirline && matchesFlightNum && directFlight) {
                        FlightData flightData = FlightOfferToData(offer, airlineCode, flightNumber);
                        flightDataList.add(flightData);
                    }
                }
                if (flightDataList.size() == 0) {
                    error_msg.setText("No flights found.");
                }
            } catch (Exception e) {
                error_msg.setText("Valid 3-letter airport codes required");
                e.printStackTrace();
            }
        } else {
            error_msg.setText("Must input a flight or a starting airport.");
        }
        return flightDataList;
    }

    private static FlightData FlightOfferToData(FlightOfferSearch offer, String airlineCode, String flightNumber) {
        String flightDuration = offer.getItineraries()[0].getDuration();

        String departureDateTime = offer.getItineraries()[0].getSegments()[0].getDeparture().getAt();
        String departureGate = offer.getItineraries()[0].getSegments()[0].getDeparture().getTerminal();

        String arrivalDateTime = offer.getItineraries()[0].getSegments()[0].getArrival().getAt();
        String arrivalGate = offer.getItineraries()[0].getSegments()[0].getArrival().getTerminal();

        String arrivalTime = arrivalDateTime.split("T")[1];

        // parsing to get date only and removing time
        LocalDate departureDate = LocalDate.parse(departureDateTime.split("T")[0]);
        // Extracting time separately
        String departureTime = departureDateTime.split("T")[1];

        String sourceLocationCode = offer.getItineraries()[0].getSegments()[0].getDeparture().getIataCode();
        String destinationLocationCode = offer.getItineraries()[0].getSegments()[0].getArrival().getIataCode();

        // Create a FlightData object and add it to the list
        FlightData flightData = new FlightData(airlineCode, flightNumber, departureDate.toString(), sourceLocationCode, destinationLocationCode, departureTime,
                arrivalTime, departureGate, arrivalGate);
        return flightData;
    }

    private static FlightData DatedFlightToData(DatedFlight offer) {
        String airlineCode = offer.getFlightDesignator().getCarrierCode();
        String flightNumber = String.valueOf(offer.getFlightDesignator().getFlightNumber());
        String departureString = offer.getScheduledDepartureDate();
        String sourceLocationCode = offer.getFlightPoints()[0].getIataCode();
        String destinationLocationCode = offer.getFlightPoints()[1].getIataCode();
        String departureTime = offer.getFlightPoints()[0].getDeparture().toString();
        String arrivalTime = offer.getFlightPoints()[1].getArrival().toString();
        String departureGate = "X";
        String arrivalGate = "X";
//                    System.out.println(offer.toString());
//                    String departureString = departureDate.toString();
        FlightData flightData = new FlightData(airlineCode, flightNumber, departureString,
                sourceLocationCode, destinationLocationCode, departureTime,
                arrivalTime, departureGate, arrivalGate);
        return flightData;
    }

    private List<FlightData> getFlights() {
        String departureDate;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now().plusDays(1);
        LocalDate daySelected = date_field.getValue();
        if (daySelected == null || daySelected.isBefore(currentDate)) {
            departureDate = currentDate.format(formatter);
        } else {
            departureDate = daySelected.format(formatter);
        }
        return getFlightDataList(src_field.getText(), dest_field.getText(),
                airline_field.getText(), flightnum_field.getText(), departureDate);
//        ArrayList<FlightData> results = new ArrayList<>();
//        String[] airlines = {"AA","AA","AA","UA","UA"};
//        int[] flight_nums = {1, 2, 6, 1, 5};
//        String[] origin = {"ORD", "SEA", "IND", "LAX", "EWR"};
//        String[] dest = {"DFW", "DFW", "BWI", "JFK", "LAS"};
//        for (int i = 0; i < dest.length; i++) {
//            FlightData f = new FlightData(
//                    airlines[i], String.valueOf(flight_nums[i]),
//                    departureDates[0],
//                    origin[i], dest[i], Instant.now().toString(),
//                    Instant.now().toString(), "1", "1");
//            results.add(f);
//        }
//        return results;
    }

    private void searchAction(Event e) {
        flightScores();
    }

    private void flightScores() {
        scores.clear();
        List<FlightData> results = getFlights();
        if (results != null) {
            for (int i = 0; i < results.size(); i++) {
                ViewData entry = FlightToString(results.get(i));
                scores.add(entry);
            }
        }
        scores.sort(new ScoreComparator());
    }

    private ViewData FlightToString(FlightData f) {
        ViewData vd = new ViewData();
        float trueScore = setFakeScore(f);
//                ArrayList<FlightComfort> fake = DataFaker.createFakeData(20,
//                        trueScore, 150, i, 20 * i);
//                int value = DreamScore.calculateFromUserReviews(fake);
        String airline_name = airlineNamesAndCodes.get(f.getAirlineCode()) +
                " " + f.getAirlineCode() + f.getFlightNumber();
        vd.setAirline(airline_name);
        vd.setRoute(f.getSourceLocationCode() + " to " + f.getDestinationLocationCode());
        vd.setScore((int) trueScore);
        return vd;
    }

    public void dreamScoreScreen(Stage stage) {
        Label airline_label = new Label("Airline");
        Label flightnum_label = new Label("Flight #");
        Label date_label = new Label("Departure Date");
        Label src_label = new Label("Origin IATA");
        Label dest_label = new Label("Destination IATA");

        airline_field = new TextField();
        flightnum_field = new TextField();
        date_field = new DatePicker();
        src_field = new TextField();
        dest_field = new TextField();

        suggestionsListView = new ListView<>();
        suggestionsListView.getStyleClass().add("suggestions-list-view");
        suggestionsListView.setVisible(false);

        error_msg = new Label();
        error_msg.setStyle("-fx-text-fill:red; -fx-font-size: 8px");

        Button search_btn = new Button("Search");
        search_btn.setOnAction(event -> searchAction(event));
        Button back_btn = new Button("Back");
        back_btn.setOnAction(event -> backButtonAction(event));

        GridPane button_layout = new GridPane();
        button_layout.setPadding(new Insets(10));
        button_layout.add(airline_label, 0, 0);
        GridPane.setColumnSpan(airline_label, 2);
        button_layout.add(flightnum_label, 2, 0);
        button_layout.add(airline_field, 0, 1);
        GridPane.setColumnSpan(airline_field, 2);
        button_layout.add(flightnum_field, 2, 1);
        button_layout.add(date_label, 0, 2);
        button_layout.add(src_label, 1, 2);
        button_layout.add(dest_label, 2, 2);
        button_layout.add(date_field, 0, 3);
        button_layout.add(src_field, 1, 3);
        button_layout.add(dest_field, 2, 3);
        button_layout.add(error_msg, 0, 4);
        button_layout.add(search_btn, 1, 4);
        button_layout.add(suggestionsListView, 0, 2);
        GridPane.setRowSpan(suggestionsListView, 3);

        Label results_label = new Label("Top Dreamtime Scores");
        results_label.setFont(new Font(25));

        scores = FXCollections.observableArrayList();
        TableView<ViewData> tableView = new TableView<>();
        tableView.setItems(scores);
        TableColumn<ViewData, String> airlineCol = new TableColumn<>("Airline");
        airlineCol.setCellValueFactory(new PropertyValueFactory("airline"));
        airlineCol.setPrefWidth(300);
        TableColumn<ViewData, String> routeCol = new TableColumn<>("Route");
        routeCol.setCellValueFactory(new PropertyValueFactory("route"));
        routeCol.setMinWidth(150);
        TableColumn<ViewData, Integer> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(new PropertyValueFactory("score"));
        scoreCol.setMinWidth(50);
        tableView.getColumns().setAll(airlineCol, routeCol, scoreCol);
        tableView.setPrefWidth(400);
        tableView.setStyle("-fx-font-size: 1.5em ; -fx-control-inner-background: azure;");

        initialize();

        VBox root = new VBox(back_btn, button_layout, results_label, tableView);

        Scene scene = new Scene(root, 600, 600, Color.MINTCREAM);
        scene.getStylesheets().add(DreamScore.class.getResource(
                "styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    private void backButtonAction(Event e) {
        // get reference to the current stage

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close(); // close the current stage

        // create a new stage for the dreamline window
        Stage mainStage = new Stage();
        try {
            // initialize and show the dreamline window
            Menu mainMenu = new Menu();
            mainMenu.start(mainStage);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
    public static Integer calculateDreamScore(ArrayList<FlightComfort> r) {
        ArrayList<Double> avg_heart_rate = new ArrayList<Double>();
        ArrayList<Double> avg_heart_takeoff = new ArrayList<Double>();
        ArrayList<Double> avg_heart_landing = new ArrayList<Double>();
        ArrayList<Double> avg_oxy_rate = new ArrayList<Double>();
        ArrayList<Double> avg_oxy_takeoff = new ArrayList<Double>();
        ArrayList<Double> avg_oxy_landing = new ArrayList<Double>();
        for (FlightComfort fc: r) {
            avg_heart_rate.add(fc.avg_heart_rate);
            avg_heart_takeoff.add(fc.avg_heart_rate_liftoff);
            avg_heart_landing.add(fc.avg_heart_rate_landing);
            avg_oxy_rate.add(fc.avg_heart_rate);
            avg_oxy_takeoff.add(fc.avg_heart_rate_liftoff);
            avg_oxy_landing.add(fc.avg_heart_rate_landing);
        }
        // idea behind dream score:
        // a little tension is normal, but any more indicates an unsatisfying ride
        // A perfect score of 100 comes when:
        //    * everyone's heart rates is the same, with only < 5 difference on liftoff and landing;
        //    * everyone's oxygen rates is the same, with only < 1 difference on liftoff and landing.
        // A disastrous score of 0 comes when:
        //    * Nearly everyone had a dramatic difference (> 30) in heart rate on liftoff and landing;
        //    * Nearly everyone had a dramatic difference (> 5) in oxygen rate on liftoff and landing.
        //
        int total_riders = avg_heart_rate.size();
        double heart_tolerance = 5;
        double oxy_tolerance = 1;
        double max_heart_distress = 30;
        double max_oxy_distress = 5;
        double heart_distress = 0.0;
        double oxy_distress = 0.0;
        for (int i = 0; i < total_riders; i++) {
            double diff1 = Math.abs(avg_heart_takeoff.get(i) - avg_heart_rate.get(i));
            double diff2 = Math.abs(avg_heart_landing.get(i) - avg_heart_rate.get(i));
            double max_diff = Double.max(diff1, diff2);
            if (max_diff > heart_tolerance) {
                heart_distress += Double.min(max_diff, (double) max_heart_distress);
            }
            diff2 = Math.abs(avg_oxy_landing.get(i) - avg_oxy_rate.get(i));
            diff1 = Math.abs(avg_oxy_takeoff.get(i) - avg_oxy_rate.get(i));
            max_diff = Double.max(diff1, diff2);
            if (max_diff > oxy_tolerance) {
                oxy_distress += Double.min(max_diff, (double) max_oxy_distress);
            }
        }
        if (total_riders == 0) {
            return null;
        }
        // map heart_distress from 0 to max_heart_distress * total_riders -> 0 to -50
        // map oxy_distress from 0 to max_oxy_distress * total_riders -> 0 to -50
        System.out.println("Total heart distress: " + heart_distress);
        System.out.println("Max heart distress: " + max_heart_distress);
        System.out.println("Total oxygen distress: " + oxy_distress);
        System.out.println("Max oxygen distress: " + max_oxy_distress);
        return (int) (100 - 50 * heart_distress / (max_heart_distress * total_riders)
                - 50 * oxy_distress / (max_oxy_distress * total_riders));
    }

    public static Integer calculateFromUserReviews(ArrayList<FlightComfort> d){
        // scores range from 0 to 5
        // get average, map from 0.0 to 5.0 -> 0 to 100
        int total_riders = d.size();
        int total_stars = 0;
        for (FlightComfort i : d) {
            total_stars += i.customer_review;
        }
        return total_stars * 20 / total_riders;
    }

    public DreamScore() {
        // super("View Dream Score");
        // Stage stage;
        // Scene scene = stage.getScene();
        // Button exitDreamScore = new Button("Exit");
        // exitDreamScore.setOnAction(ae -> {stage.setScene(scene);});

    }
}