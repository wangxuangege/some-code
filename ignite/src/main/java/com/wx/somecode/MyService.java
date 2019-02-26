package com.wx.somecode;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyService {

    /**
     * 服务网格
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        try (Ignite ignite = Ignition.start()) {
            ignite.services().deployClusterSingleton("WeatherService", new WeatherServiceImpl());

            WeatherService service = ignite.services().service("WeatherService");
            String forecast = service.getCurrentTemperature("London", "UK");
            System.out.println("Weather forecast in London:" + forecast);
        }
    }

    public interface WeatherService extends Service {
        String getCurrentTemperature(String countryCode, String cityName)
                throws Exception;
    }

    public static class WeatherServiceImpl implements WeatherService {
        /**
         * Weather service URL.
         */
        private static final String WEATHER_URL = "http://samples.openweathermap.org/data/2.5/weather?";
        /**
         * Sample app ID.
         */
        private static final String appId = "ca7345b4a1ef8c037f7749c09fcbf808";

        /**
         * {@inheritDoc}.
         */
        @Override
        public void init(ServiceContext ctx) throws Exception {
            System.out.println("Weather Service is initialized!");
        }

        /**
         * {@inheritDoc}.
         */
        @Override
        public void execute(ServiceContext ctx) throws Exception {
            System.out.println("Weather Service is started!");
        }

        /**
         * {@inheritDoc}.
         */
        @Override
        public void cancel(ServiceContext ctx) {
            System.out.println("Weather Service is stopped!");
        }

        /**
         * {@inheritDoc}.
         */
        @Override
        public String getCurrentTemperature(String cityName,
                                            String countryCode) throws Exception {
            System.out.println(">>> Requested weather forecast [city="
                    + cityName + ", countryCode=" + countryCode + "]");
            String connStr = WEATHER_URL + "q=" + cityName + ","
                    + countryCode + "&appid=" + appId;
            URL url = new URL(connStr);
            HttpURLConnection conn = null;
            try {
                // Connecting to the weather service.
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                // Read data from the weather server.
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    StringBuilder builder = new StringBuilder();
                    while ((line = reader.readLine()) != null)
                        builder.append(line);
                    return builder.toString();
                }
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
        }
    }
}
