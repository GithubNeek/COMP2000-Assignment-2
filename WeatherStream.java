// WeatherStream.java (FIXED)

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeatherStream implements Runnable {
    
    private static final int INTERVAL_MS = 5000; // Event every 5 seconds
    // Use CopyOnWriteArrayList for thread safety if observers are added/removed while running
    private List<WeatherObserver> observers = new ArrayList<>(); 
    private Random random = new Random();

    public void addObserver(WeatherObserver observer) {
        observers.add(observer);
    }

    /**
     * FIX 1: Implement the logic to notify observers using the new 
     * updateLegacyWeather method from the WeatherObserver interface.
     */
    private void notifyObservers(WeatherEvent event) {
        for (WeatherObserver observer : observers) {
            // Call the default method implemented in the observer interface
            observer.updateLegacyWeather(event); 
        }
    }

    @Override
    public void run() {
        System.out.println("Random Weather Stream started. New event every " + INTERVAL_MS + "ms.");

        WeatherEvent[] events = WeatherEvent.values(); 
        
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(INTERVAL_MS);

                WeatherEvent newEvent = events[random.nextInt(events.length)];
                System.out.println("Random Weather Event: " + newEvent);

                // FIX 2: Correctly call the notification method
                notifyObservers(newEvent); 

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Random weather stream interrupted.");
                break;
            }
        }
    }

    // This method is redundant but left for completeness if it's used elsewhere.
    private WeatherEvent generateRandomEvent() {
        WeatherEvent[] events = WeatherEvent.values(); 
        return events[random.nextInt(events.length)];
    }
}