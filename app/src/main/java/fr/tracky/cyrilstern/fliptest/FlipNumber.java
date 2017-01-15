package fr.tracky.cyrilstern.fliptest;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Arrays;
import java.util.jar.Attributes;

/**
 * Created by cyrilstern1 on 14/01/2017.
 */

public class FlipNumber extends ImageView {

    private final int CAMERA_DISTANCE = 8000;
    private final int MIN_FLIP_DURATION = 300;
    private final int VELOCITY_TO_DURATION_CONST = 5;
    private final int MAX_FLIP_DURATION = 1000;
    private final int ANTIALIAS_BORDER = 1;
    private FrameLayout frameLayout;
    private Matrix horizontFlipmatrix;
    private BitmapDrawable mFrontBitmapDrawable,mBackBitmaDrawable;
    private boolean mInFrontShowing = true;
    private boolean mIsHorizontakkyFlipped = false;
    private BitmapDrawable mCurrentBitmapDrawable;

    public FlipNumber(Context context) {
        super(context);
        initCtx(context);
    }
    public FlipNumber(Context context, AttributeSet attributeSet) {
        super(context);
        initCtx(context);
    }

    private void initCtx(Context context){
        mFrontBitmapDrawable = new BitmapDrawable(getResources(),BitmapFactory.decodeResource(getResources(),R.drawable.onetop));
        mBackBitmaDrawable  =  new BitmapDrawable(getResources(),BitmapFactory.decodeResource(getResources(),R.drawable.onebotton));
        horizontFlipmatrix = new Matrix();
        setPivotY(mFrontBitmapDrawable.getBitmap().getHeight());
        Log.i("thisis the height", String.valueOf((mFrontBitmapDrawable.getBitmap().getHeight())));
        setCameraDistance(CAMERA_DISTANCE);
        updateDrawableBitmap();
    }

    private BitmapDrawable bitmapDrawablewithBorder(BitmapDrawable bitmapDrawable){
        Bitmap bitmapDrawablewithBorder = Bitmap.createBitmap(bitmapDrawable.getIntrinsicWidth() +
                ANTIALIAS_BORDER * 2,bitmapDrawable.getIntrinsicHeight() + ANTIALIAS_BORDER * 2, Bitmap.Config.ARGB_8888);
        return new BitmapDrawable(getResources(),bitmapDrawablewithBorder);

    }
    public void flipToBotton(int numberPile, int velocity){
        flipVertical(numberPile,false,velocity);
    }

    public void flipVertical (int numbertI, boolean clockwise, int velocity){
        PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat(View.ROTATION_X,clockwise ? 180 : -180);
        PropertyValuesHolder xOffset = PropertyValuesHolder.ofFloat(View.TRANSLATION_X,0);
        PropertyValuesHolder yOffset = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,0);

        ObjectAnimator timeAnimator = ObjectAnimator.ofPropertyValuesHolder(this,rotation,xOffset,yOffset);
        timeAnimator.setDuration(MAX_FLIP_DURATION - Math.abs(velocity)/VELOCITY_TO_DURATION_CONST);
        timeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
               if(valueAnimator.getAnimatedFraction() >  0.5 && mInFrontShowing ){
                   toggleFrontShowing();
                    toggleIsHorizontalFlipped();
                    updateDrawableBitmap();
                }
            }
        });
        timeAnimator.start();
        Keyframe shadowKeyFrameStart = Keyframe.ofFloat(0,0);
        Keyframe shadowKeyFrameMid = Keyframe.ofFloat(0.5f,1);
        Keyframe shadowKeyFrameEnd = Keyframe.ofFloat(1,1);
        PropertyValuesHolder shadowsValueHolder = PropertyValuesHolder.ofKeyframe("shadow",shadowKeyFrameStart,shadowKeyFrameMid,shadowKeyFrameEnd);
        ObjectAnimator objanimatorColor = ObjectAnimator.ofPropertyValuesHolder(this,shadowsValueHolder);

        AnimatorSet set = new AnimatorSet();
        int duration = MAX_FLIP_DURATION - Math.abs(velocity)/VELOCITY_TO_DURATION_CONST;
        duration = duration < MIN_FLIP_DURATION ? MIN_FLIP_DURATION :duration;
        set.setDuration(duration);
        set.playTogether(timeAnimator,objanimatorColor);
        set.setInterpolator(new AccelerateDecelerateInterpolator());

        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                updateDrawableBitmap();
               // updateLayoutParams();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.start();


    }
    public void toggleFrontShowing(){
        mInFrontShowing = !mInFrontShowing;
    }
    public void toggleIsHorizontalFlipped(){

        mIsHorizontakkyFlipped = !mIsHorizontakkyFlipped;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        horizontFlipmatrix.setScale(1, -1, w/2,h/2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mIsHorizontakkyFlipped) canvas.concat(horizontFlipmatrix);
        super.onDraw(canvas);
    }
    public void updateLayoutParams(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        params.leftMargin = (int) (params.leftMargin + ((Math.abs(getRotationX())) %360/180)*(2*getPivotX() - getHeight()));
        setRotationX(0);
        setRotationY(0);
        setLayoutParams(params);
    }

    public void updateDrawableBitmap(){
        mCurrentBitmapDrawable = mInFrontShowing ? mFrontBitmapDrawable : mBackBitmaDrawable;
        setImageDrawable(mCurrentBitmapDrawable);
    }

    public void updateTranslation(int numinpile){
        //setTranslationX();
    }

}
