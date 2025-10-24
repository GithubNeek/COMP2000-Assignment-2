
public class WeatherDataPoint {
    private final long timestamp;
    private final String attribute; 
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
