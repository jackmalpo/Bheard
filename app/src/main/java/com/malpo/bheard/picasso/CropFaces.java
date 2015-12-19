package com.malpo.bheard.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.squareup.picasso.Transformation;

/**
 * Based on some code in the Picasso library for centerCrop.
 */
public class CropFaces implements Transformation {

    private int mWidth;
    private int mHeight;
    private Context mContext;

    public CropFaces(Context context, int width, int height) {
        mContext = context;
        mWidth = width;
        mHeight = height;
    }

    @Override public Bitmap transform(Bitmap source) {
        float facePosX = 0;

        mWidth = mWidth == 0 ? source.getWidth() : mWidth;
        mHeight = mHeight == 0 ? source.getHeight() : mHeight;

        //FaceDetection
        FaceDetector detector = new FaceDetector.Builder(mContext).setTrackingEnabled(false).build();
        Frame frame = new Frame.Builder().setBitmap(source).build();
        SparseArray<Face> faces = detector.detect(frame);
        detector.release();

        for (int i = 0; i < faces.size(); ++i) {
            Face face = faces.valueAt(i);
            facePosX += face.getPosition().x;
        }

        if(faces.size() > 0) {
            facePosX = facePosX / faces.size();
        }

        int inWidth = source.getWidth();
        int inHeight = source.getHeight();

        int drawX = 0;
        int drawY = 0;
        int drawWidth = inWidth;
        int drawHeight = inHeight;

        Matrix matrix = new Matrix();

        // Keep aspect ratio if one dimension is set to 0
        float widthRatio =
                mWidth != 0 ? mWidth / (float) inWidth : mHeight / (float) inHeight;
        float heightRatio =
                mHeight != 0 ? mHeight / (float) inHeight : mWidth / (float) inWidth;

        float scaleX, scaleY;

        int newSize = (int) Math.ceil(inHeight * (heightRatio / widthRatio));

        drawHeight = newSize;
        scaleX = widthRatio;
        scaleY = mHeight / (float) drawHeight;

        drawY = facePosX == 0 ? ((inHeight - newSize) / 2) : Math.round(facePosX / drawHeight);

        matrix.preScale(scaleX, scaleY);


        Bitmap newResult =
                Bitmap.createBitmap(source, drawX, drawY, drawWidth, drawHeight, matrix, true);
        if (newResult != source) {
            source.recycle();
            source = newResult;
        }

        return source;
    }

    @Override public String key() {
        return "top";
    }





}