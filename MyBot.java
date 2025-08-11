

import org.jibble.pircbot.*;

public class MyBot extends PircBot {
    
    public MyBot() {
        this.setName("MyBot");
    }

    public void onMessage(String channel, String sender,String login, String hostname, String message) {
        if (message.equalsIgnoreCase("time")) {
            String time = new java.util.Date().toString();
            sendMessage(channel, sender + ": The time is now " + time);
        }
        else if(message.equalsIgnoreCase("hi")){
            sendMessage(channel,"hi");
        }
        else if(message.contains("weather:")){
            String city = message.substring(8);
            sendMessage(channel, "fethching weather for " + city + "...");
            MyBotMain.getWeather(city,channel);
        }
    }

    

    
}