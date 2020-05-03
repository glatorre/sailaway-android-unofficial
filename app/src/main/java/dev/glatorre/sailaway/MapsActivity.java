package dev.glatorre.sailaway;

import androidx.fragment.app.FragmentActivity;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SailawayHandler {
    private GoogleMap mMap;
    private boolean setInitialPosition = true;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        new SailawayService(this);
    }

    @Override
    public void onBoatsReceived(Boat[] boats) {
        mMap.clear();
        for(int i=0; i<boats.length; i++){
            Boat boat = boats[i];
            LatLng boatPosition = new LatLng(boat.getLatitude(), boat.getLongitude());
            String boatDescr = boat.getName();
            Double boatSpeed = new Double(boat.getSpeed());
            boatSpeed = BigDecimal.valueOf(boatSpeed)
                    .setScale(1, RoundingMode.HALF_UP)
                    .doubleValue();
            boatDescr += " (" + boatSpeed + "kn)";
            mMap.addMarker(new MarkerOptions().position(boatPosition).title(boatDescr));

            PolylineOptions directionOptions = new PolylineOptions();
            double newLat = boat.getLatitude()  + 0.5 * boatSpeed * Math.cos(Math.toRadians(boat.getCog()));
            double newLon = boat.getLongitude() + 0.5 * boatSpeed * Math.sin(Math.toRadians(boat.getCog()));
            directionOptions.add(boatPosition, new LatLng(newLat, newLon))
                    .pattern(PATTERN_POLYGON_ALPHA)
                    .width(3)
                    .color(Color.RED);
            mMap.addPolyline(directionOptions);

            if(i == 0 && setInitialPosition){
                mMap.moveCamera(CameraUpdateFactory.newLatLng(boatPosition));
                setInitialPosition = false;
            }
        }
    }
}
