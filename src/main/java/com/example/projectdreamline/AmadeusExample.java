package com.example.projectdreamline;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Airline;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AmadeusExample {

    // Custom code added to read from api_keys.xml
    public static String getClientID() {
        String clientID = "";
        try {
            File file = new File(
                    "src/main/resources/com/example/projectdreamline/api_keys.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            clientID = document.getElementsByTagName("client_id").item(0).getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientID;
    }

    public static String getClientSecret() {
        String clientSecret = "";
        try {
            File file = new File(
                    "src/main/resources/com/example/projectdreamline/api_keys.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            clientSecret = document.getElementsByTagName("client_secret").item(0).getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientSecret;
    }

    public static Map<String, String> getAirlineNamesAndCodes() {
        Map<String, String> airlineNamesAndCodes = new HashMap<>();
        // Initialize the Amadeus client with your API key and secret
        Amadeus amadeus = Amadeus.builder(getClientID(), getClientSecret()).build();

        try {
            // Make a request to Airlines API to fetch all airlines
            Airline[] airlines = amadeus.referenceData.airlines.get();

            // Process the response
            for (Airline airline : airlines) {
                String airlineCode = airline.getIataCode();
                String airlineName = airline.getBusinessName();
                airlineNamesAndCodes.put(airlineCode, airlineName);
            }
        } catch (ResponseException e) {
            // Handle errors
            e.printStackTrace();
        }
        return airlineNamesAndCodes;
    }
}





