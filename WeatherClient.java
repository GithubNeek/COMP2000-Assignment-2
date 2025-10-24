// WeatherClient.java (NEW FILE)

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class WeatherClient implements Runnable {

    private static final String SERVER_URL = "http://13.238.167.130/weather";
    // We'll use the same list of observers as the old WeatherStream
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
            // 1. Establish the connection and setup the reader
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(new URL(SERVER_URL).openStream())
            )
        ) {
            // 2. Use a continuous Java Stream to process lines
            reader.lines()
                .forEach(line -> {
                    // Use a stream to parse and group all data arriving in the same timestamp
                    List<WeatherDataPoint> dataBatch = parseLine(line);
                    
                    if (!dataBatch.isEmpty()) {
                        // 4. Notify observers with the batch of parsed, usable data
                        // This happens asynchronously as data comes in.
                        notifyObservers(dataBatch);
                    }
                    
                    // Throttle the feed slightly to prevent UI flicker
                    try {
                        Thread.sleep(100); 
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            
        } catch (Exception e) {
            System.err.println("Weather Client Error: " + e.getMessage());
            // You should add logic here to handle disconnection (e.g., reconnect)
        }
    }

    /**
     * FIX: Use LAMBDAS and STREAMS to parse the incoming lines.
     * Since the server sends all attributes (rain, windX, windY, temp) for a single
     * timestamp/coordinate block-by-block, we'll collect a batch.
     * * NOTE: This simplified approach assumes the server sends one line at a time, 
     * but groups by timestamp (which isn't strictly true in the example but is
     * needed for stream-based line processing). The assignment example shows 
     * multiple lines with the same timestamp. For simplicity, we'll collect 
     * a batch of 4 lines if the line contains "temp", assuming that finishes a set.
     * * A more robust solution would require a custom collector. For the assignment, 
     * we'll stick to simple stream usage on the line itself.
     * * Because the server sends one line per call, we'll only parse that line.
     * The grid will aggregate the effects over time.
     */
    private List<WeatherDataPoint> parseLine(String line) {
        // Use stream to split, filter, and map the components of the line
        return List.of(line)
            .stream()
            // Lambda 1: Use map to split the line into parts (long, String, int, int, float)
            .map(s -> s.split(" ")) 
            // Filter out lines that don't have exactly 5 parts or have bad data
            .filter(parts -> parts.length == 5)
            // Lambda 2: Use map to create a WeatherDataPoint object
            .map(parts -> {
                try {
                    long ts = Long.parseLong(parts[0]);
                    String attr = parts[1];
                    int x = Integer.parseInt(parts[2]);
                    int y = Integer.parseInt(parts[3]);
                    float val = Float.parseFloat(parts[4]);
                    return new WeatherDataPoint(ts, attr, x, y, val);
                } catch (NumberFormatException e) {
                    return null; // Ignore malformed lines
                }
            })
            // Filter out null results from the try-catch block
            .filter(point -> point != null)
            // Collect the single parsed point into a List
            .collect(Collectors.toList());
    }
}