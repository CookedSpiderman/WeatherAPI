
//API KEY : 98dfb1f99a13cf034df71f17a1c3629e
/*
api call for turning city to lat lon:
 * http://api.openweathermap.org/geo/1.0/direct?q={city name},{state code},{country code}&appid={API key}

api call for lat and long to weather data:
https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
 */
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherAPI {
    public static void main(String[] args) {
        String apiKey = "a7e86eebbbf0df7c577d3d18e11ff88b";
        String baseGeoUrl = "http://api.openweathermap.org/geo/1.0/direct?q=";          //data to build url strings
        String baseWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat=";

        System.out.print("Enter a city: ");
        Scanner scnr = new Scanner(System.in);                          //prompt user input
        String city = scnr.nextLine();
        scnr.close();

        String fullGeoUrl = baseGeoUrl + city + "&appid=" + apiKey;     //build full url string
        //System.out.println(fullGeoUrl);
        StringBuilder geoJsonData = null;
        try{
            geoJsonData = getJsonData(fullGeoUrl);
            //System.out.println("JSON Data: " + geoJsonData);            //get json data into string using tryblock
        }
        catch(Exception e){System.err.println(e);}

        //JsonObject geoJson = JsonParser.parseString(geoJsonData.toString()).getAsJsonObject();

        JsonArray geoArray = JsonParser.parseString(geoJsonData.toString()).getAsJsonArray(); //store data as jsonarray
        if (geoArray.size() == 0) {
            System.out.println("City not found!");
            return;
        }
        JsonObject geoJson = geoArray.get(0).getAsJsonObject();
        double lattitude = geoJson.get("lat").getAsDouble();                //get lat and lon from json
        double longitude = geoJson.get("lon").getAsDouble();
        System.out.println("lattitude: " + lattitude + "\nlongitude: " + longitude);

        //build full url string for weather
        String fullWeatherUrl = baseWeatherUrl + lattitude + "&lon=" + longitude + "&appid=" + apiKey;
        //System.out.println(fullWeatherUrl);
        StringBuilder weatherJsonData = null;
        try{
            weatherJsonData = getJsonData(fullWeatherUrl);
        }
        catch(Exception e){System.out.println(e);}

        JsonObject weatherJson = JsonParser.parseString(weatherJsonData.toString()).getAsJsonObject();
        double temperature = weatherJson.getAsJsonObject("main").get("temp").getAsDouble();
        System.out.printf("Temperature: %.1fF", KelvinToFahrenheit(temperature));





    }

    public static StringBuilder getJsonData(String fullUrl) throws Exception
    {
        
        URL url = new URL(fullUrl);                       //create url object

        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //cast URL open connect to HttpURLconnection
        connection.setRequestMethod("GET");                 //GET request

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); //create buffered reader
        StringBuilder result = new StringBuilder();
        String temp;
        while ((temp = reader.readLine()) != null) {            //loop thru stream and append to result
            result.append(temp);
        }

        reader.close();

        return result;
    }

    public static double KelvinToFahrenheit(double K){
        return (K - 273.15) * 9/5 + 32;
    }
}
