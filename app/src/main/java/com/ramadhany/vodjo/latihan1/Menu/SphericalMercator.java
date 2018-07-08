package com.ramadhany.vodjo.latihan1.Menu;


public final class SphericalMercator {

    private static final double MIN_LATITUDE = -85.0511287798;
    private static final double MAX_LATITUDE = 85.0511287798;

    private SphericalMercator() {
    }

    public static double fromLatitude(double latitude) {
        double radians = Math.toRadians(latitude + 90) / 2;
        return Math.toDegrees(Math.log(Math.tan(radians)));
    }

    public static double toLatitude(double mercator) {
        double radians = Math.atan(Math.exp(Math.toRadians(mercator)));
        return Math.toDegrees(2 * radians) - 90;
    }

    /**
     * @param latitude to be scaled
     * @return value in the range [0, 360)
     */
    public static double scaleLatitude(double latitude) {
        if (latitude < MIN_LATITUDE) {
            latitude = MIN_LATITUDE;
        } else if (latitude > MAX_LATITUDE) {
            latitude = MAX_LATITUDE;
        }
        return fromLatitude(latitude) + 180.0;
    }

    /**
     * @param longitude to be scaled
     * @return value in the range [0, 360)
     */
    public static double scaleLongitude(double longitude) {
        return longitude + 180.0;
    }
}