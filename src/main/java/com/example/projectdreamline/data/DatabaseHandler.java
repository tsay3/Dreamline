// Version 2.0

package com.example.projectdreamline.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

//import org.h2.jdbcx.JdbcDataSource;

import com.example.projectdreamline.DreamScore;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatabaseHandler {
    static final String DB_URL = "jdbc:h2:./src\\\\main\\\\java\\\\com\\\\example\\\\projectdreamline\\\\flight_data";
    static final String USER = "sa";
    static final String PASS = "";
    
//    public static void init() {
//        try {
//            JdbcDataSource ds = new JdbcDataSource();
//            ds.setUrl(DB_URL);
//            ds.setUser(USER);
//            ds.setPassword(PASS);
//            Connection conn = ds.getConnection();
//            PreparedStatement ps;
//            for (String q : Data.statements) {
//                ps = conn.prepareStatement(q);
//                ps.execute();
//            }
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private static Connection openConnection() throws Exception {
//        JdbcDataSource ds = new JdbcDataSource();
//        ds.setUrl(DB_URL);
//        ds.setUser(USER);
//        ds.setPassword(PASS);
////        Connection conn = ds.getConnection();
//        return ds.getConnection();
//    }

//    private static void closeConnection(Connection conn) throws Exception {
//        conn.close();
//    }

//    public static ArrayList<Map<String, Object>> processQuery(String sqlQuery) {
//        ArrayList<Map<String, Object>> resultList = new ArrayList<>();
//        try {
//            Connection conn = openConnection();
//            PreparedStatement ps = conn.prepareStatement(sqlQuery);
//            ResultSet rs = ps.executeQuery();
//            ResultSetMetaData rsmd = rs.getMetaData();
//            int num_cols = rsmd.getColumnCount();
//            while (rs.next()) {
//                Map<String, Object> obj = new HashMap<String, Object>();
//                for (int i = 1; i <= num_cols; i++) {
//                    String col_name = rsmd.getColumnName(i);
//                    System.out.println("Column " + i + ": " + col_name);
//                    obj.put(col_name, rs.getObject(i));
//                }
//                resultList.add(obj);
//            }
//            closeConnection(conn);
//        } catch (Exception e) {
//            System.err.println("Could not process query (" + sqlQuery + ").");
//            System.err.println("Returning empty result list.");
//        }
//        return resultList;
//    }

    public static ArrayList<FlightComfort> getFlightComfortData() {
        return getFlightComfortData(-1);
    }

    public static Integer getFlightIDMatchingAirline(String carrier, Integer flightNum) {
        return null;
    }

    public static ArrayList<FlightComfort> getFlightComfortData(int flightID) {
//        ArrayList<FlightComfort> resultList = new ArrayList<>();
//        String query = "SELECT * FROM flight_comfort";
//        if (flightID > 0) {
//            query = query + " WHERE flight_id = " + flightID;
//        }
//        ArrayList<Map<String, Object>> results = DatabaseHandler.processQuery(query);
//        for (Map<String, Object> item: results) {
//            FlightComfort result = new FlightComfort();
//            result.avg_heart_rate = (double) item.get("AVG_HEART_RATE");
//            result.avg_heart_rate_liftoff = (double) item.get("AVG_HEART_RATE_LIFTOFF");
//            result.avg_heart_rate_landing = (double) item.get("AVG_HEART_RATE_LANDING");
//            result.avg_oxygen_rate = (double) item.get("AVG_OXYGEN_RATE");
//            result.avg_oxygen_rate_landing = (double) item.get("AVG_OXYGEN_RATE_LANDING");
//            result.avg_oxygen_rate_liftoff = (double) item.get("AVG_OXYGEN_RATE_LIFTOFF");
//            result.flight_id = (int) item.get("FLIGHT_ID");
//            result.customer_review = (int) item.get("CUSTOMER_REVIEW");
//            result.highest_heart_rate = (double) item.get("HIGHEST_HEART_RATE");
//            result.highest_heart_rate_landing = (double) item.get("HIGHEST_HEART_RATE_LANDING");
//            result.highest_heart_rate_liftoff = (double) item.get("HIGHEST_HEART_RATE_LIFTOFF");
//            result.highest_oxygen_rate = (double) item.get("HIGHEST_OXYGEN_RATE");
//            result.highest_oxygen_rate_landing = (double) item.get("HIGHEST_OXYGEN_RATE_LANDING");
//            result.highest_oxygen_rate_liftoff = (double) item.get("HIGHEST_OXYGEN_RATE_LIFTOFF");
//            result.user_id = (int) item.get("USER_ID");
//            result.time_submitted = (Timestamp) item.get("TIME_SUBMITTED");
//            result.lowest_heart_rate = (double) item.get("LOWEST_HEART_RATE");
//            result.lowest_heart_rate_landing = (double) item.get("LOWEST_HEART_RATE_LANDING");
//            result.lowest_heart_rate_liftoff = (double) item.get("LOWEST_HEART_RATE_LIFTOFF");
//            result.lowest_oxygen_rate = (double) item.get("LOWEST_OXYGEN_RATE");
//            result.lowest_oxygen_rate_landing = (double) item.get("LOWEST_OXYGEN_RATE_LANDING");
//            result.lowest_oxygen_rate_liftoff = (double) item.get("LOWEST_OXYGEN_RATE_LIFTOFF");
//            resultList.add(result);
//        }
        ArrayList<FlightComfort> resultList =
                DataFaker.createFakeData(20, 4.5f, 150, flightID, 1);
        return resultList;
    }

    public static ObservableList<String> getFlights() {
        ObservableList<String> flights = 
            FXCollections.observableArrayList();
//        flights.add("90          American Airlines AA1      ORD to DFW");
//        flights.add("77           American Airlines AA2      SEA to DFW");
//        flights.add("52             American Airlines AA6      IND to BWI");
//        flights.add("85             United Airlines UA1            LAX to JFK");
//        flights.add("45          United Airlines UA5            EWR to LAS");
        String space = "          ";
        ArrayList<FlightComfort> allResults = getFlightComfortData();
        HashMap<Integer, ArrayList<FlightComfort>> resultsByFlight = new HashMap<>();
        for (FlightComfort fc : allResults) {
            if (resultsByFlight.containsKey(fc.flight_id)) {
                ArrayList<FlightComfort> subResult = resultsByFlight.get(fc.flight_id);
                subResult.add(fc);
            } else {
                ArrayList<FlightComfort> subResult = new ArrayList<>();
                subResult.add(fc);
                resultsByFlight.put(fc.flight_id, subResult);
            }
        }

        for (ArrayList<FlightComfort> resultList : resultsByFlight.values()) {
            Integer score = DreamScore.calculateFromUserReviews(resultList);
        }
        return flights;
    }

    public static Integer getScoreForID(int flightID) {
        ArrayList<FlightComfort> resultList = getFlightComfortData(flightID);
        return DreamScore.calculateFromUserReviews(resultList);
    }
}