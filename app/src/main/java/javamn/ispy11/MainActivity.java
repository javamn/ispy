package javamn.ispy11;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button playBtn;
    private Button nextBtn;
    private Button resetBtn;
    private TextView scoreView;
    private ImageView img;
    private ArrayList<Puzzle> puzzleList;
    private ArrayList<Integer> idxList;
    private int currentPuzzle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res = this.getResources();

        scoreView = (TextView) findViewById(R.id.textView2);
        playBtn = (Button) findViewById(R.id.button2);
        nextBtn = (Button) findViewById(R.id.button3);
        resetBtn = (Button) findViewById(R.id.button5);

        resetBtn.setActivated(false);
        resetBtn.setVisibility(scoreView.INVISIBLE);

        puzzleList = new ArrayList<Puzzle>();
        Bundle extras = getIntent().getExtras();

        idxList = new ArrayList<Integer>();
        idxList.add(0); idxList.add(1); idxList.add(2);

        currentPuzzle = 0;


        if (extras != null && extras.containsKey("curr") && extras.containsKey("list")){
            currentPuzzle = extras.getInt("curr");
            idxList = (ArrayList<Integer>) extras.get("list");
            if (idxList != null) {

                idxList.remove(Integer.valueOf(currentPuzzle));

                scoreView.setText("Progress: " + (3 - idxList.size()) + "/3");
                if (idxList.isEmpty()) {
                    resetBtn.setActivated(true);
                    resetBtn.setVisibility(View.VISIBLE);
                }
            }
        }

        puzzleList.add(new Puzzle(res.getIdentifier("test1", "drawable", this.getPackageName()),
                new LatLng(45.425974, -75.697673)));
        puzzleList.add(new Puzzle(res.getIdentifier("test2", "drawable", this.getPackageName()),
                new LatLng(45.323252, -75.667699)));
        puzzleList.add(new Puzzle(res.getIdentifier("test3", "drawable", this.getPackageName()),
                new LatLng(45.392062, -75.681799)));


        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreView.setText("Progress: 0/3");
                idxList.add(0); idxList.add(1); idxList.add(2);
                resetBtn.setActivated(false);
                resetBtn.setVisibility(scoreView.INVISIBLE);
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (puzzleList.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All puzzles are complete.", Toast.LENGTH_LONG).show();
                } else {

                    updatePuzzle();
                }
            }
        });


        img = (ImageView) findViewById(R.id.imageView2);
        updatePuzzle();
    }

    private void launchActivity(){
        Intent intent = new Intent(this, LocationSelectActivity.class);
        intent.putExtra("resID", puzzleList.get(currentPuzzle).getID());
        intent.putExtra("latLng", puzzleList.get(currentPuzzle).getLoc());
        intent.putExtra("curr", currentPuzzle);
        intent.putExtra("list", idxList);
        startActivity(intent);
    }

    /*private class Puzzle{
        int resID;
        LatLng latLng;

        Puzzle(int resID, LatLng latLng){
            this.resID = resID;
            this.latLng = latLng;
        }
    }*/

    public int getCurrentPuzzleImg(){
        return puzzleList.get(currentPuzzle).getID();
    }

    public LatLng getCurrentPuzzleLatLng(){
        return puzzleList.get(currentPuzzle).getLoc();
    }

    private void updatePuzzle(){
        if (!idxList.isEmpty()){
            int newIdx = currentPuzzle + 1;
            while(!idxList.contains(Integer.valueOf(newIdx))){
                newIdx++;
                if (newIdx > 2) newIdx = 0;
            }
            currentPuzzle = newIdx;

            img.setImageResource(puzzleList.get(currentPuzzle).getID());
        }
    }

}