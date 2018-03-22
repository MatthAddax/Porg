package com.mmeunier.porg.utils;

import android.app.Activity;

import com.mmeunier.porg.PDFSubSampleTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Matthieu Meunier on 22-03-18.
 * Project Porg
 */

public class Utils {
    public static File getFileFromAssets(Activity activity, String filename) {
        File f = new File(activity.getCacheDir() + filename);
        if (!f.exists()) {
            try {
                InputStream is = activity.getAssets().open(filename);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();

                FileOutputStream fos = new FileOutputStream(f);
                fos.write(buffer);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }
}
