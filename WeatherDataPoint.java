// WeatherDataPoint.java (NEW FILE)

/**
 * Represents a single weather measurement received from the server.
 * Format: timestamp attribute x-coordinate y-coordinate value
 * Note: x/y coordinates from the server are centered at (0,0).
 */
public class WeatherDataPoint {
    private final long timestamp;
    private final String attribute; // e.g., "rain", "windx", "temp"
    private final int x;
    private final int y;
    private final float value;

    public WeatherDataPoint(long timestamp, String attribute, int x, int y, float value) {
        this.timestamp = timestamp;
        this.attribute = attribute;
        this.x = x;
        this.y = y;
        this.value = value;
    }

    // Getters
    public long getTimestamp() { return timestamp; }
    public String getAttribute() { return attribute; }
    public int getX() { return x; }
    public int getY() { return y; }
    public float getValue() { return value; }

    @Override
    public String toString() {
        return String.format("%s at (%d, %d) = %.2f", attribute, x, y, value);
    }
}