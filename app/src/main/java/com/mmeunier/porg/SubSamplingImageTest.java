package com.mmeunier.porg;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubSamplingImageTest extends AppCompatActivity {
    @BindView(R.id.subSamplingImageView)
    PinView subsamplingScaleImageView;


    PointF touchedPointOnMapCoord;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_sampling_image_test);
        ButterKnife.bind(this);
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) {
                /*
                 * Draw pin on touchedPointOnMapCoord coordinates or on relative coordinates??
                 *
                 * */
                int width = subsamplingScaleImageView.getSWidth();
                int height = subsamplingScaleImageView.getSHeight();
                float relativeX = touchedPointOnMapCoord.x / width;
                float relativeY = touchedPointOnMapCoord.y / height;

                if(touchedPointOnMapCoord.x < 0 || touchedPointOnMapCoord.x > subsamplingScaleImageView.getSWidth()
                        || touchedPointOnMapCoord.y < 0 ||touchedPointOnMapCoord.y > subsamplingScaleImageView.getSHeight()){
                    Toast.makeText(SubSamplingImageTest.this, "T'es en dehors de la map", Toast.LENGTH_LONG).show();
                }
                else{
                    subsamplingScaleImageView.setPin(touchedPointOnMapCoord);
                }

                super.onLongPress(e);
            }

            @SuppressLint("DefaultLocale")
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                List<PointF> pins = subsamplingScaleImageView.getPinList();
                float x = touchedPointOnMapCoord.x;
                float y = touchedPointOnMapCoord.y;
                float pinWidth = subsamplingScaleImageView.getPin().getWidth();
                float pinHeight = subsamplingScaleImageView.getPin().getHeight();
                for(PointF pin : pins){
                    if(x > pin.x - (pinWidth/2) && x < pin.x + (pinWidth/2)){
                        if(y > pin.y - (pinHeight/2) && y < pin.y + (pinHeight/2)){
                            Toast.makeText(SubSamplingImageTest.this, String.format("Blablabla pin clicked on (%fl, %fl)", pin.x, pin.y), Toast.LENGTH_LONG).show();

                        }
                    }
                }
                return super.onSingleTapConfirmed(e);
            }
        });


        subsamplingScaleImageView.setImage(ImageSource.resource(R.drawable.floor_plan));

        subsamplingScaleImageView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                PinView subsamplingScaleImageView = SubSamplingImageTest.this.subsamplingScaleImageView;
                touchedPointOnMapCoord = subsamplingScaleImageView.viewToSourceCoord(new PointF(motionEvent.getX(), motionEvent.getY()));
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

    }
}
