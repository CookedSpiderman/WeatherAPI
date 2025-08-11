import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jibble.pircbot.*;

public class MyBotMain {
    static MyBot bot = new MyBot();
    public static void main(String[] args) throws Exception {
        
        // Now start our bot up.
        
        
        // Enable debugging output.
        bot.setVerbose(true);
        
        // Connect to the IRC server.
        bot.connect("irc.libera.chat", 6667); // Connect to Libera.Chat
        bot.joinChannel("#Testchanneldeh"); // Join a channel

        // Join the #pircbot channel.
        
    }

    public static void getWeather(String city,String channel){
        String apiKey = "a7e86eebbbf0df7c577d3d18e11ff88b";
        String baseGeoUrl = "http://api.openweathermap.org/geo/1.0/direct?q=";          //data to build url strings
        String baseWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat=";

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
            bot.sendMessage(channel,"City not found!");
            return;
        }
        JsonObject geoJson = geoArray.get(0).getAsJsonObject();
        double lattitude = geoJson.get("lat").getAsDouble();                //get lat and lon from json
        double longitude = geoJson.get("lon").getAsDouble();
        System.out.println("lattitude: " + lattitude + "\nlongitude: " + longitude);
        bot.sendMessage(channel,"lattitude: " + lattitude + " longitude: " + longitude);

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
        bot.sendMessage(channel, "Temperature: " + KelvinToFahrenheit(temperature));
    }

    public static StringBuilder getJsonData(String fullUrl) throws Exception{
        
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