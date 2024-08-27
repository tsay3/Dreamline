package com.example.projectdreamline;

import java.util.List;

public class FlightDataTerminalDisplay {

    public static void main(String[] args) {
        // Get flight data list
        List<FlightTrackingExample.FlightData> flightDataList = FlightTrackingExample.getFlightDataList("", "JFK", "MAD");

        
        // Display flight data in the terminal
        for (FlightTrackingExample.FlightData flightData : flightDataList) {
            System.out.println("Airline Code: " + flightData.getAirlineCode());
            System.out.println("Flight Number: " + flightData.getFlightNumber());
            System.out.println("Departure Date: " + flightData.getDepartureDate());
            System.out.println("Source Location Code: " + flightData.getSourceLocationCode());
            System.out.println("Destination Location Code: " + flightData.getDestinationLocationCode());
            System.out.println();
        }
    }
}