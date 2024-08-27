// Version 2.0

package com.example.projectdreamline.data;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DataFaker {

    // creates an ArrayList of fake FlightComfort data for a flight of a given length,
    // Takes a total number of samples
    public static ArrayList<FlightComfort> createFakeData
            (Integer totalSamples, Float trueQuality, Integer flightLength, Integer flightID, Integer baseID) {
        ArrayList<FlightComfort> allData = new ArrayList<>();

        for (int i = 0; i < totalSamples; i++) {
            Timestamp time = new Timestamp((long) (Math.random() * 1000000));
            // 0.0 quality produces anxiety ranges from 0.5 to 1.0 -- avg is 0.75
            // 50.0 quality produces anxiety ranges from 0.0 to 1.0 -- avg is 0.5
            // 100.0 quality produces anxiety ranges from 0.0 to 0.25 -- avg is 0.125
            // linear in between -- can be estimated with the equation quality = -0.2(anxiety)^2 - 0.4(anxiety) + .75
            // calculating DreamScore is effectively estimating the trueQuality from the average anxiety
            // The best way to calculate anxiety is through anxiety_at_liftoff and anxiety_at_landing
            // Alternatively, we can calculate it (and calibrate our variables) through the customer review.
            double anxiety_min = Math.max(0, 0.5f - trueQuality);
            double anxiety_max = Math.min(1.75f + (trueQuality * -1.5f), 1.0f);
            double anxiety = (anxiety_max - anxiety_min) * Math.random() + anxiety_min;
//            System.out.println("###### ANXIETY: " + anxiety);
            double anxiety_at_liftoff = (1 - anxiety) * Math.random() + anxiety;   // random, from anxiety to 1.0
            double anxiety_at_landing = (1 - anxiety) * Math.random() + anxiety;
            double lowest_heart_rate = getLowestHeartRate();   // FLOAT, 65 - 90 -- avg is 77.5
            double lowest_heart_rate_liftoff = lowest_heart_rate + 5 * anxiety_at_liftoff * Math.random();   // FLOAT,
            double lowest_heart_rate_landing = lowest_heart_rate + 5 * anxiety_at_landing * Math.random();   // FLOAT,
            double highest_heart_rate_liftoff = lowest_heart_rate_liftoff + Math.random() * 20 + anxiety_at_liftoff * 5;
            double highest_heart_rate_landing = lowest_heart_rate_landing + Math.random() * 20 + anxiety_at_landing * 5;
            double highest_heart_rate = Math.max(highest_heart_rate_landing, highest_heart_rate_liftoff) + Math.max(0, Math.random() - 0.5) * 2 * 4;
            double avg_heart_rate = (highest_heart_rate + lowest_heart_rate) / 2 + (Math.random() - 0.5 + anxiety) * 4;
            double avg_heart_rate_liftoff = (highest_heart_rate_liftoff + lowest_heart_rate_liftoff) / 2 + Math.random() * anxiety_at_liftoff * 2;
            double avg_heart_rate_landing = (lowest_heart_rate_landing + highest_heart_rate_landing) / 2 + Math.random() * anxiety_at_landing * 2;
            double lowest_oxygen_rate = getLowestOxygenRate();   // FLOAT, 90 - 95
            double highest_oxygen_rate = getHighestOxygenRate();   // FLOAT, 98 - 100
            double avg_oxygen_rate = (lowest_oxygen_rate + highest_oxygen_rate) / 2 + anxiety - 0.5 * Math.random();
            double lowest_oxygen_rate_liftoff = lowest_oxygen_rate + Math.max(0, (anxiety_at_liftoff - 0.5)) * Math.random();
            double highest_oxygen_rate_liftoff = highest_oxygen_rate - Math.max(0, (Math.random() - 0.5));
            double avg_oxygen_rate_liftoff = (highest_oxygen_rate_liftoff + lowest_oxygen_rate_liftoff) / 2;
            double lowest_oxygen_rate_landing = lowest_oxygen_rate + Math.max(0, (anxiety_at_landing - 0.5));
            double highest_oxygen_rate_landing = highest_oxygen_rate - Math.max(0, (Math.random() - 0.5));
            double avg_oxygen_rate_landing = (highest_oxygen_rate_landing + lowest_oxygen_rate_landing) / 2;
            int customer_review = (int) (5 - Math.max(0, Math.floor(anxiety * 5 - Math.random() * 2 * (1 - anxiety))));

            String sql = "";
            String time_string = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(time);
            sql = sql + String.format("INSERT INTO flight_comfort (%d, %d, %d, '%s',",
                    i + baseID, flightID, i + 1, time_string);
            sql = sql + String.format("  %f, %f, %f,",
                    lowest_heart_rate, avg_heart_rate, highest_heart_rate);
            sql = sql + String.format("  %f, %f, %f,",
                    lowest_oxygen_rate, avg_oxygen_rate, highest_oxygen_rate);
            sql = sql + String.format("  %f, %f, %f,",
                    lowest_heart_rate_liftoff, avg_heart_rate_liftoff, highest_heart_rate_liftoff);
            sql = sql + String.format("  %f, %f, %f,",
                    lowest_oxygen_rate_liftoff, avg_oxygen_rate_liftoff, highest_oxygen_rate_liftoff);
            sql = sql + String.format("  %f, %f, %f,",
                    lowest_heart_rate_landing, avg_heart_rate_landing, highest_heart_rate_landing);
            sql = sql + String.format("  %f, %f, %f, %d);",
                    lowest_oxygen_rate_landing, avg_oxygen_rate_landing, highest_oxygen_rate_landing, customer_review);

            FlightComfort fc = new FlightComfort();
            fc.flight_id = flightID;
            fc.user_id = i + 1;
            fc.time_submitted = time;
            fc.lowest_heart_rate = lowest_heart_rate;
            fc.lowest_heart_rate_landing = lowest_heart_rate_landing;
            fc.lowest_heart_rate_liftoff = lowest_heart_rate_liftoff;
            fc.avg_heart_rate = avg_heart_rate;
            fc.avg_heart_rate_landing = avg_heart_rate_landing;
            fc.avg_heart_rate_liftoff = avg_heart_rate_liftoff;
            fc.highest_heart_rate = highest_heart_rate;
            fc.highest_heart_rate_landing = highest_heart_rate_landing;
            fc.highest_heart_rate_liftoff = highest_heart_rate_liftoff;
            fc.lowest_oxygen_rate = lowest_oxygen_rate;
            fc.lowest_oxygen_rate_landing = lowest_oxygen_rate_landing;
            fc.lowest_oxygen_rate_liftoff = lowest_oxygen_rate_liftoff;
            fc.avg_oxygen_rate = avg_oxygen_rate;
            fc.avg_oxygen_rate_landing = avg_oxygen_rate_landing;
            fc.avg_oxygen_rate_liftoff = avg_oxygen_rate_liftoff;
            fc.highest_oxygen_rate = highest_oxygen_rate;
            fc.highest_oxygen_rate_landing = highest_oxygen_rate_landing;
            fc.highest_oxygen_rate_liftoff = highest_oxygen_rate_liftoff;
            fc.customer_review = customer_review;
            allData.add(fc);
        }

        return allData;
    }

    private static double getHighestOxygenRate() {
        return 100 - Math.random() * 2;
    }

    private static double getLowestOxygenRate() {
        return Math.random() * 5 + 90;
    }

    private static double getLowestHeartRate() {
        return Math.random() * 25 + 65;
    }
}
