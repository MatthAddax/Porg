package com.mmeunier.porg;

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ortiz.touch.TouchImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagePinchActivity extends AppCompatActivity implements View.OnTouchListener{

    @BindView(R.id.ortizTouchImageView)
    TouchImageView touchImageView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pinch);
        ButterKnife.bind(this);

        touchImageView.setImageDrawable(getDrawable(R.drawable.floor_plan));

        touchImageView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        PointF scrollMiddlePosition = touchImageView.getScrollPosition();
        Float currentZoom = touchImageView.getCurrentZoom();
        Float x = motionEvent.getX();
        Float y = motionEvent.getY();
        Float viewX = view.getX();
        Float viewY = view.getY();
        Integer viewHeight = view.getHeight();
        Integer viewWidth = view.getWidth();
        Float relativeTouchX = x / viewWidth;
        Float relativeTouchY = y / viewHeight;
        float[] dest = new float[2];
        touchImageView.getImageMatrix().mapPoints(dest, new float[]{x, y});
        int tivWidth = touchImageView.getWidth();
        int drawableHeight = touchImageView.getDrawable().getIntrinsicHeight();
        int drawableWidth = touchImageView.getDrawable().getIntrinsicWidth();
        //float touchedXForTheWholescreen
        Log.d("POSITIONS ", String.format("(x,y) => (%f, %f)", x, y));

        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
