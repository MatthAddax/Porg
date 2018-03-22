package com.mmeunier.porg;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.mmeunier.porg.utils.PDFPagerAdapter;
import com.mmeunier.porg.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PDFSubSampleTest extends AppCompatActivity {

    /**
     * A {@VerticalViewPager} to scroll vertical within the pdf
     */
    @BindView(R.id.pager) ViewPager pager;

    /**
     * Shows the page count
     */
    @BindView(R.id.pages)
    TextView pages;

    /**
     * The animator to switch between views.
     */
    @BindView(R.id.animator)
    ViewAnimator animator;

    /**
     * The {@PDFPagerAdapter} implemented in this example
     */
    private PDFPagerAdapter pagerAdapter;

    /**
     * The current position within the pdf
     */
    protected Integer currentPosition = 0;
    private String pdfName = "mypdf.pdf";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfsub_sample_test);
        ButterKnife.bind(this);

        setPDF();

        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int index) {
                PDFSubSampleTest.this.onPageSelected(index);
            }
        });

    }


    /**
     * Callback method for the {@link #onPageSelected(int)} to handle setting the
     * page number in the activity
     * @param position the current position in the pager
     */
    private void onPageSelected(int position) {
        this.currentPosition = position;
        updatePageCounter();
    }

    /**
     * Sets the pdf file to the {@PDFPagerAdapter} and then sets the adapter to the
     * {@VerticalViewPager}
     */
    private void setPDF() {
        animator.setDisplayedChild(0);
        pagerAdapter = new PDFPagerAdapter(this, Utils.getFileFromAssets(this, pdfName));
        pager.setAdapter(pagerAdapter);
        updatePageCounter();
    }


    /**
     * updates the page counter while scrolling through the pdf
     */
    public void updatePageCounter() {
        pages.setText(currentPosition + 1 + "/" + pagerAdapter.getCount());
        pages.clearAnimation();
        AlphaAnimation animation1 = new AlphaAnimation(1.0f, 0.0f);
        animation1.setDuration(1000);
        animation1.setStartOffset(4000);
        animation1.setFillAfter(true);
        pages.startAnimation(animation1);
    }
}
