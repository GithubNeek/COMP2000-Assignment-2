
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeatherStream implements Runnable {
    
    private static final int INTERVAL_MS = 5000; 
    private List<WeatherObserver> observers = new ArrayList<>(); 
    private Random random = new Random();

    public void addObserver(WeatherObserver observer) {
        observers.add(observer);
    }

 
    private void notifyObservers(WeatherEvent event) {
        for (WeatherObserver observer : observers) {
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

                notifyObservers(newEvent); 

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Random weather stream interrupted.");
                break;
            }
        }
    }

    private WeatherEvent generateRandomEvent() {
        WeatherEvent[] events = WeatherEvent.values(); 
        return events[random.nextInt(events.length)];
    }
}
