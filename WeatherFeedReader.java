import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.stream.Stream;

public class WeatherFeedReader implements Runnable {
    private static final String WEATHER_URL = "http://13.238.167.130/weather";
    private final Grid gameGrid; 

    public WeatherFeedReader(Grid grid) { 
        this.gameGrid = grid; 
    }

    @Override
    public void run() {
        try {
            URL url = new URL(WEATHER_URL);
            
            try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    processLineUsingStreams(inputLine);
                }
            }
        } catch (IOException e) {
            System.err.println("Weather feed connection failed: " + e.getMessage());
        }
    }

    private void processLineUsingStreams(String line) {
        String[] parts = line.split(" ");
        
        Optional<WeatherData> data = Stream.of(parts)
            .filter(p -> parts.length == 5) 
            .map(p -> { 
                try {
                    return new WeatherData(
                        Long.parseLong(parts[0]),
                        Integer.parseInt(parts[2]), // X
                        Integer.parseInt(parts[3]), // Y
                        parts[1], // Attribute
                        Double.parseDouble(parts[4])
                    );
                } catch (NumberFormatException e) {
                    return null; 
                }
            })
            .filter(d -> d != null) 
            .findFirst();

        data.ifPresent(gameGrid::handleWeatherUpdate); 
    }
}