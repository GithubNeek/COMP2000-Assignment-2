
import java.util.List;


public interface WeatherObserver {
    
    public void updateWeather(List<WeatherDataPoint> weatherData);
    
    
    public default void updateLegacyWeather(WeatherEvent event) {
    }
}
