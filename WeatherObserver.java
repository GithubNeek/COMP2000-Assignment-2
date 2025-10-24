// WeatherObserver.java (FINAL UPDATE)

import java.util.List;

/**
 * Observer interface for components that react to weather updates.
 */
public interface WeatherObserver {
    
    // The Assignment 2 method: handles continuous numerical data
    public void updateWeather(List<WeatherDataPoint> weatherData);
    
    // The Legacy method (for the SUNNY/RAIN/SNOW enum)
    // We use a default implementation so classes implementing the new method 
    // don't break if they don't care about the old enum events.
    public default void updateLegacyWeather(WeatherEvent event) {
        // Default does nothing
    }
}