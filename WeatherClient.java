
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class WeatherClient implements Runnable {

    private static final String SERVER_URL = "http://13.238.167.130/weather";
    private final List<WeatherObserver> observers = new CopyOnWriteArrayList<>();

    public void addObserver(WeatherObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(List<WeatherDataPoint> data) {
        for (WeatherObserver observer : observers) {
            observer.updateWeather(data);
        }
    }

    @Override
    public void run() {
        System.out.println("Connecting to weather server: " + SERVER_URL);

        try (
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(new URL(SERVER_URL).openStream())
            )
        ) {
            reader.lines()
                .forEach(line -> {
                    List<WeatherDataPoint> dataBatch = parseLine(line);
                    
                    if (!dataBatch.isEmpty()) {
                        notifyObservers(dataBatch);
                    }
                    
                    try {
                        Thread.sleep(100); 
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            
        } catch (Exception e) {
            System.err.println("Weather Client Error: " + e.getMessage());
        }
    }

  
    private List<WeatherDataPoint> parseLine(String line) {
        return List.of(line)
            .stream()
            .map(s -> s.split(" ")) 
            .filter(parts -> parts.length == 5)
            .map(parts -> {
                try {
                    long ts = Long.parseLong(parts[0]);
                    String attr = parts[1];
                    int x = Integer.parseInt(parts[2]);
                    int y = Integer.parseInt(parts[3]);
                    float val = Float.parseFloat(parts[4]);
                    return new WeatherDataPoint(ts, attr, x, y, val);
                } catch (NumberFormatException e) {
                    return null; 
                }
            })
            .filter(point -> point != null)
            .collect(Collectors.toList());
    }
}
