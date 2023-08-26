package carrot.app.Map;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;

import java.util.concurrent.TimeUnit;
public class DistanceCalculator {
    private final String apiKey = "AIzaSyBXL4uQMdmd7Ddc6U5mY3NXisUsbbFT13Q";

    public double calculateDistance(LatLng origin, LatLng destination) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();

        DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(context)
                .origins(origin)
                .destinations(destination);

        DistanceMatrix result = request.await();

        // Get the distance in meters
        long distanceInMeters = result.rows[0].elements[0].distance.inMeters;

        return distanceInMeters / 1000.0; // Convert to kilometers
    }
}

