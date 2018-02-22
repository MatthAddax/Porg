package com.mmeunier.porg;

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubSamplingImageTest extends AppCompatActivity {
    @BindView(R.id.subSamplingImageView)
    SubsamplingScaleImageView subsamplingScaleImageView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_sampling_image_test);
        ButterKnife.bind(this);

        subsamplingScaleImageView.setImage(ImageSource.resource(R.drawable.floor_plan));

        subsamplingScaleImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                PointF test = ((SubsamplingScaleImageView)view).viewToSourceCoord(new PointF(motionEvent.getX(), motionEvent.getY()));
                return false;
            }
        });
    }
}
