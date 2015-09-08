package com.example.sergbek.googlemapsl18.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.sergbek.googlemapsl18.activity.MainActivity;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.IOException;

public class Utils {

    public static BitmapDescriptor convertUriToBitmapDescriptor(Uri imgUri) {
        BitmapDescriptor icon = null;
        if (imgUri!=null){
            int radius = 100;
            int stroke = 2;

            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            Bitmap newBitmap = Bitmap.createBitmap( radius, radius + 25, conf);
            Canvas canvas = new Canvas(newBitmap);
            try {
                Bitmap oldBitmap = MediaStore.Images.Media.getBitmap(MainActivity.getContext().getContentResolver(), imgUri);

                oldBitmap = ThumbnailUtils.extractThumbnail(oldBitmap,  radius - stroke,  radius - stroke, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                BitmapShader shader = new BitmapShader(oldBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

                drawCircle(radius, stroke, canvas, shader);

                icon=BitmapDescriptorFactory.fromBitmap(newBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return icon;
    }

    private static void drawCircle(int radius, int stroke, Canvas canvas, BitmapShader shader) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xff464646);
        paint.setStyle(Paint.Style.FILL);

        int pointedness = 20;
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(radius / 2, radius + 15);
        path.lineTo(radius / 2 + pointedness, radius - 10);
        path.lineTo(radius / 2 - pointedness, radius - 10);
        canvas.drawPath(path, paint);

        RectF rect = new RectF(0, 0, radius, radius);
        canvas.drawRoundRect(rect, radius / 2, radius / 2, paint);

        paint.setShader(shader);
        rect = new RectF(stroke, stroke, radius - stroke, radius - stroke);
        canvas.drawRoundRect(rect, (radius - stroke) / 2, (radius - stroke) / 2, paint);
    }


    /**
     * Conversion method, without adding frames
     *
     * @param imgUri
     * @return
     */
    public BitmapDescriptor convertUriImageToBitmapDescriptor(Uri imgUri) {
        BitmapDescriptor icon = null;
        if (imgUri != null) {
            try {
                Bitmap bitmap;
                bitmap = MediaStore.Images.Media.getBitmap(MainActivity.getContext().getContentResolver(), imgUri);
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, 100, 100);
                icon = BitmapDescriptorFactory.fromBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return icon;
    }
}
