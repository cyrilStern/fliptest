package fr.tracky.cyrilstern.fliptest;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private ImageView iw;
    private RelativeLayout frameLayout;
    private Matrix matrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (RelativeLayout) findViewById(R.id.framebasetime);

        FlipNumber flipNumber = new FlipNumber(getApplicationContext());
        frameLayout.addView(flipNumber);
        //flipNumber.setPivotY(0);
        flipNumber.flipToBotton(1,1);
        //flipNumber.animate().translationY(10).start();
        //flipNumber.animate().rotationX(180).start();

    }

}
