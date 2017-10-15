package websocket.chat;

public class Position {
    public static final double BAD_VALUE = -999.0;
    
    private double mLatitude = BAD_VALUE;
    private double mLongitude = BAD_VALUE;
    
    public Position(final double lat, final double lon) {
        mLatitude = lat;
        mLongitude = lon;
    }

    Position() {
    }
    
    public void setLat(final double lat) {
        mLatitude = lat;
    }
    
    public void setLon(final double lon) {
        mLongitude = lon;
    }
    
    public void setLatLon(final double lat, final double lon) {
        mLatitude = lat;
        mLongitude = lon;
    }
    
    public double getLat() {
        return mLatitude;
    }
    
    public double getLon() {
        return mLongitude;
    }
} 
