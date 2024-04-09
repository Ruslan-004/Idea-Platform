package com.company;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.List;

public class FlightStatus {

    public static void main(String[] args) {
        JSONParser parser = new JSONParser();

        try {
            JSONArray flightsAndForecast = (JSONArray) parser.parse(new FileReader("flights_and_forecast.json"));

            for (Object flightObj : flightsAndForecast) {
                JSONObject flight = (JSONObject) flightObj;

                String flightNo = (String) flight.get("no");
                String departureCity = (String) flight.get("from");
                String destinationCity = (String) flight.get("to");

                JSONArray flights = (JSONArray) flight.get("flights");

                boolean isCancelled = false;
                for (Object flightInfoObj : flights) {
                    JSONObject flightInfo = (JSONObject) flightInfoObj;

                    long departureTime = (long) flightInfo.get("departure");
                    long duration = (long) flightInfo.get("duration");

                    boolean isFlyingWeather = isFlyingWeather(flightInfo, departureCity, destinationCity);

                    if (!isFlyingWeather) {
                        isCancelled = true;
                        break;
                    }
                }

                String status = isCancelled ? "отменен" : "по расписанию";
                System.out.println(flightNo + " | " + departureCity + " -> " + destinationCity + " | " + status);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isFlyingWeather(JSONObject flightInfo, String departureCity, String destinationCity) {
        long windSpeed = (long) flightInfo.get("forecast.wind");
        long visibility = (long) flightInfo.get("forecast.visibility");

        return windSpeed <= 30 && visibility >= 200;
    }
}