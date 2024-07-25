package com.mostafatamer.tasktogether;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitmapHelper {
     public static Bitmap getBitmap(VectorDrawable vectorDrawable) {

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }
    public static Bitmap uriToBitmap(Uri uri, ContentResolver contentResolver) throws IOException {
        InputStream inputStream = contentResolver.openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        assert inputStream != null;
        inputStream.close();
        return bitmap;
    }

    public static File saveBitmap(ContentResolver contentResolver, File fileToWrite, Uri imageUri) throws IOException {
        Bitmap bitmap = BitmapHelper.uriToBitmap(imageUri, contentResolver);
        OutputStream outputStream = new FileOutputStream(fileToWrite);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.close();
        return fileToWrite;
    }

    public static File writeBitmapOnFile(Bitmap bitmap, String outputFilePath) throws Exception {
        File outputFile = new File(outputFilePath);
        OutputStream outputStream = new FileOutputStream(outputFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.close();
        return outputFile;
    }


    public static Bitmap reduceBitmapDimensions(String imagePath, int maxWidth, int maxHeight) {
        // Decode the image file with scaling options
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // This will only get the image dimensions
        BitmapFactory.decodeFile(imagePath, options);

        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        int scaleFactor = 1;
        if (imageWidth > maxWidth || imageHeight > maxHeight) {
            final int widthRatio = Math.round((float) imageWidth / (float) maxWidth);
            final int heightRatio = Math.round((float) imageHeight / (float) maxHeight);
            scaleFactor = Math.min(widthRatio, heightRatio);
        }

        options.inSampleSize = scaleFactor;
        options.inJustDecodeBounds = false; // Decode the full bitmap now

        return BitmapFactory.decodeFile(imagePath, options);
    }
}
