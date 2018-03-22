package com.mmeunier.porg.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.support.v4.view.PagerAdapter;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.mmeunier.porg.PinView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.number42.subsampling_pdf_decoder.PDFDecoder;
import de.number42.subsampling_pdf_decoder.PDFRegionDecoder;

/**
 * Created by Matthieu Meunier on 22-03-18.
 * Project Porg
 */

public class PDFPagerAdapter extends PagerAdapter {
    /**
     * context for the view
     */
    private Context context;

    /**
     * pdf file to show
     */
    private File file;

    /**
     * file descriptor of the PDF
     */
    private ParcelFileDescriptor mFileDescriptor;

    /**
     * this scale sets the size of the {@link PdfRenderer.Page} in the {@link
     * PDFRegionDecoder}.
     * Since it rescales the picture, it also sets the possible zoom level.
     */
    private float scale;

    PointF touchedPointOnMapCoord;
    /**
     * this renderer is only used to count the pages
     */
    private PdfRenderer renderer;

    /**
     * @param file the pdf file
     */
    public PDFPagerAdapter(Context context, File file) {
        super();
        this.context = context;
        this.file = file;
        this.scale = 8;
        try {
            mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            renderer = new PdfRenderer(mFileDescriptor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Instantiate an item. Therefor a {@link SubsamplingScaleImageView} with special decoders is
     * initialized and rendered.
     *
     * @param container isn't used here
     * @param position the current pdf page position
     */
    public Object instantiateItem(ViewGroup container, int position) {

        PinView imageView = new PinView(context);

        // the smaller this number, the smaller the chance to get an "outOfMemoryException"
        // still, values lower than 100 really do affect the quality of the pdf picture
        int minimumTileDpi = 120;
        imageView.setMinimumTileDpi(minimumTileDpi);

        //sets the PDFDecoder for the imageView
        imageView.setBitmapDecoderFactory(() -> new PDFDecoder(position, file, scale));

        //sets the PDFRegionDecoder for the imageView
        imageView.setRegionDecoderFactory(() -> new PDFRegionDecoder(position, file, scale));

        ImageSource source = ImageSource.uri(file.getAbsolutePath());

        imageView.setImage(source);
        /*Create the gesture detector to ... detect... the... Gesture*/
        final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                /*
                 * Draw pin on touchedPointOnMapCoord coordinates or on relative coordinates??
                 *
                 * */
                int width = imageView.getSWidth();
                int height = imageView.getSHeight();
                float relativeX = touchedPointOnMapCoord.x / width;
                float relativeY = touchedPointOnMapCoord.y / height;

                if (touchedPointOnMapCoord.x < 0 || touchedPointOnMapCoord.x > imageView.getSWidth()
                        || touchedPointOnMapCoord.y < 0 || touchedPointOnMapCoord.y > imageView.getSHeight()) {
                    Toast.makeText(context, "T'es en dehors de la map", Toast.LENGTH_LONG).show();
                } else {
                    imageView.setPin(touchedPointOnMapCoord);
                }

                super.onLongPress(e);
            }

            @SuppressLint("DefaultLocale")
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                List<PointF> pins = imageView.getPinList();
                float x = touchedPointOnMapCoord.x;
                float y = touchedPointOnMapCoord.y;
                float pinWidth = imageView.getPin().getWidth();
                float pinHeight = imageView.getPin().getHeight();
                for (PointF pin : pins) {
                    if (x > pin.x - (pinWidth / 2) && x < pin.x + (pinWidth / 2)) {
                        if (y > pin.y - (pinHeight / 2) && y < pin.y + (pinHeight / 2)) {
                            Toast.makeText(context, String.format("Blablabla pin clicked on (%fl, %fl)", pin.x, pin.y), Toast.LENGTH_LONG).show();

                        }
                    }
                }
                return super.onSingleTapConfirmed(e);
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                PinView subsamplingScaleImageView = imageView;
                touchedPointOnMapCoord = subsamplingScaleImageView.viewToSourceCoord(new PointF(motionEvent.getX(), motionEvent.getY()));
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
        container.addView(imageView);
        return imageView;
    }

    /**
     * gets the pdf site count
     *
     * @return pdf site count
     */
    public int getCount() {
        return renderer.getPageCount();
    }

    @Override public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}