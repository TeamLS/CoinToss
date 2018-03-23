package com.example.tasosxak.cointoss;

/**
 * Created by Thanasis on 22/3/2018.
 *
 * With help from these sources:
 * https://gist.github.com/methodin/5678214
 * https://2cupsoftech.wordpress.com/2012/09/18/3d-flip-between-two-view-or-viewgroup-on-android/
 *
 */

import android.view.animation.Animation;

import android.view.animation.Transformation;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.widget.ImageView;

public class Rotate3dAnimation extends Animation {
    private final float fromXDegrees;
    private final float toXDegrees;
    private final float fromYDegrees;
    private final float toYDegrees;
    private final float fromZDegrees;
    private final float toZDegrees;
    private Camera camera;
    private int width = 0;
    private int height = 0;
    private ImageView imageView;
    private int curDrawable;
    private int nextDrawable;
    private int numOfRepetition = 0;

    private float repeatCount;

    public Rotate3dAnimation(ImageView imageView, int curDrawable, int nextDrawable, float fromXDegrees, float toXDegrees, float fromYDegrees, float toYDegrees, float fromZDegrees, float toZDegrees) {
        this.fromXDegrees = fromXDegrees;
        this.toXDegrees = toXDegrees;
        this.fromYDegrees = fromYDegrees;
        this.toYDegrees = toYDegrees;
        this.fromZDegrees = fromZDegrees;
        this.toZDegrees = toZDegrees;
        this.imageView = imageView;
        this.curDrawable = curDrawable;
        this.nextDrawable = nextDrawable;
    }

    @Override
    public void setRepeatCount(int repeatCount){
        super.setRepeatCount(repeatCount);
        this.repeatCount = repeatCount+1;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        this.width = width / 2;
        this.height = height / 2;
        camera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float xDegrees = fromXDegrees + ((toXDegrees - fromXDegrees) * interpolatedTime);
        float yDegrees = fromYDegrees + ((toYDegrees - fromYDegrees) * interpolatedTime);
        float zDegrees = fromZDegrees + ((toZDegrees - fromZDegrees) * interpolatedTime);

        final Matrix matrix = t.getMatrix();


        // ----------------- ZOOM ----------------- //

        if ((numOfRepetition + interpolatedTime) / (repeatCount/2) <= 1){
            imageView.setScaleX(1 + (numOfRepetition + interpolatedTime) / (repeatCount/2));
            imageView.setScaleY(1 + (numOfRepetition + interpolatedTime) / (repeatCount/2));
        } else if (numOfRepetition < repeatCount){
            imageView.setScaleX(3 - (numOfRepetition + interpolatedTime) / (repeatCount/2));
            imageView.setScaleY(3 - (numOfRepetition + interpolatedTime) / (repeatCount/2));
        }


        // ----------------- ROTATE ----------------- //


        if (interpolatedTime >= 0.5f && interpolatedTime < 1f)
        {
            imageView.setImageResource(nextDrawable);
            xDegrees -= 180f;


        } else if (interpolatedTime == 1f) {

            int temp = curDrawable;
            curDrawable = nextDrawable;
            nextDrawable = temp;
            xDegrees -= 180f;

            numOfRepetition++;

        } else {
            imageView.setImageResource(curDrawable);
        }

        camera.save();
        camera.rotateX(-xDegrees);
        camera.rotateY(yDegrees);
        camera.rotateZ(zDegrees);
        camera.getMatrix(matrix);
        camera.restore();



        matrix.preTranslate(-this.width, -this.height);
        matrix.postTranslate(this.width, this.height);
    }

}
