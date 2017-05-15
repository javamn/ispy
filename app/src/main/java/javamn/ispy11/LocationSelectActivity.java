package javamn.ispy11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class LocationSelectActivity extends AppCompatActivity implements OnMapReadyCallback{


    private GoogleMap mMap;
    private LatLng selectedPoint = null;
    private Button selectBtn;
    private Button viewImgBtn;
    private ArrayList<Integer> idxList;
    private int curr;
    private ImageView img;
    private int resID;
    private LatLng latLng;
    private boolean imgShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select);

        Bundle extras = getIntent().getExtras();
        this.resID = extras.getInt("resID");
        this.latLng = (LatLng) extras.get("latLng");
        this.idxList = (ArrayList<Integer>) extras.get("list");
        this.curr = extras.getInt("curr");


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        img = (ImageView) findViewById(R.id.imageView);

        selectBtn = (Button) findViewById(R.id.button3);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationSelected();
            }
        });

        viewImgBtn = (Button) findViewById(R.id.viewimgbtn);
        viewImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgShowing) {
                    img.setImageResource(android.R.color.transparent);
                    viewImgBtn.setText("View Image");
                } else {
                    img.setImageResource(resID);
                    viewImgBtn.setText("Cancel");
                }
                imgShowing = !imgShowing;
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Add a marker in Sydney and move the camera
        LatLng OTT_DEFAULT = new LatLng(45.4215, -75.6972);
        //mMap.addMarker(new MarkerOptions().position(test1).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(OTT_DEFAULT, 11.0f));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // For development **
                //Toast.makeText(getApplicationContext(), point.toString(), Toast.LENGTH_SHORT).show();
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(point));
                selectedPoint = point;
            }
        });

    }

    private void locationSelected(){
        if (selectedPoint == null) {
            Toast.makeText(getApplicationContext(), "Please select a location", Toast.LENGTH_LONG).show();
        } else {
            double dist = CalculationByDistance(selectedPoint, latLng);
            if (dist <= 0.1){
                findIsSuccessful();
                //Toast.makeText(getApplicationContext(), "You got it! Only "+dist*1000+" meters off.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    private double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    private void findIsSuccessful(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("puzzleSolved", true);
        intent.putExtra("list", idxList);
        intent.putExtra("curr", curr);
        startActivity(intent);
    }
}
