public class WeatherData {
    private final long timestamp;
    private final int x, y;
    private final String attribute;
    private final double value;

    public WeatherData(long timestamp, int x, int y, String attribute, double value) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.attribute = attribute;
        this.value = value;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public String getAttribute() { return attribute; }
    public double getValue() { return value; }
    public long getTimestamp() { return timestamp; } 
}