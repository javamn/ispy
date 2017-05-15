package javamn.ispy11;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Austin on 2017-05-14.
 */

public class Puzzle{
    private int resID;
    private LatLng latLng;

    Puzzle(int resID, LatLng latLng){
        this.resID = resID;
        this.latLng = latLng;
    }


    public int getID(){return resID;}

    public LatLng getLoc(){return latLng;}
}
