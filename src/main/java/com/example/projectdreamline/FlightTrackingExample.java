package com.example.projectdreamline;

import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.Amadeus;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FlightTrackingExample {
    static class FlightData {
        private String airlineCode;
        private String flightNumber;
        private String departureDate;
        private String sourceLocationCode;
        private String destinationLocationCode;

        private String departureTime;

        private String arrivalTime;

        private String departureGate;
        private String arrivalGate;

        public FlightData(String airlineCode, String flightNumber, String departureDate, String sourceLocationCode,
                          String destinationLocationCode, String departureTime, String arrivalTime, String departureGate,
                          String arrivalGate) {
            this.airlineCode = airlineCode;
            this.flightNumber = flightNumber;
            this.departureDate = departureDate;
            this.sourceLocationCode = sourceLocationCode;
            this.destinationLocationCode = destinationLocationCode;
            this.departureTime =  departureTime;
            this.arrivalTime = arrivalTime;
            this.departureGate = departureGate;
            this.arrivalGate = arrivalGate;
        }

        // Getters
        public String getAirlineCode() {
            return airlineCode;
        }
        public String getFlightNumber() {
            return flightNumber;
        }
        public String getDepartureDate() {
            return departureDate;
        }
        public String getSourceLocationCode() { return sourceLocationCode; }
        public String getDestinationLocationCode() { return destinationLocationCode; }
        public String getDepartureTime() { return departureTime; }

        public String getArrivalTime() { return arrivalTime; }

        public String getDepartureGate() { return departureGate; }

        public String getArrivalGate() { return arrivalGate; }
    }
    public static List<FlightData> getFlightDataList() {
        return getFlightDataList("", "", "");
    }
    public static List<FlightData> getFlightDataList(String s1) {
        return getFlightDataList(s1, "", "");
    }
    public static List<FlightData> getFlightDataList(String s1, String s2) {
        return getFlightDataList(s1, s2, "");
    }
    public static List<FlightData> getFlightDataList(String s1, String s2, String s3) {
        List<FlightData> flightDataList = new ArrayList<>();

        // Initialize the Amadeus client with your API key and secret
        Amadeus amadeus = Amadeus.builder(AmadeusExample.getClientID(), AmadeusExample.getClientSecret()).build();

        try {
            // Get the current date
            LocalDate currentDate = LocalDate.now();

            // Format the dates in the required format (YYYY-MM-DD)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Create an array to store the departure dates for today and the next two days
            String[] departureDates = new String[3];

            // Add today's date
            departureDates[0] = currentDate.format(formatter);

            // Add the next two days' dates
            for (int i = 1; i < 3; i++) {
                LocalDate nextDay = currentDate.plusDays(i);
                departureDates[i] = nextDay.format(formatter);
            }

            // From here
            LocalDate current_Date = LocalDate.now();

            String currentDateString = current_Date.toString();

            // Make a request to the Flight Offers Search API
            FlightOfferSearch[] flightOffers = amadeus.shopping.flightOffersSearch.get(
                    Params.with("originLocationCode", "JFK")
                            .and("destinationLocationCode", "MAD")
                            .and("departureDate", currentDateString)
                            .and("adults", 1));

            // to here

            // Process the response and store flight data
            for (FlightOfferSearch offer : flightOffers) {
                String airlineCode = offer.getItineraries()[0].getSegments()[0].getCarrierCode();
                String flightNumber = offer.getItineraries()[0].getSegments()[0].getNumber();
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
                flightDataList.add(flightData);
            }

        } catch (ResponseException e) {
            // Handle errors
            e.printStackTrace();
        }

        return flightDataList;
    }
}