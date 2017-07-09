package com.example.robert.physicsbasedanimation.fragment;

import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.robert.physicsbasedanimation.R;
import com.example.robert.physicsbasedanimation.view.SpringView;

/**
 * Created by Robert on 2017/7/7.
 */

public class BallSpringFragment extends Fragment {
    private View root;
    private View img;
    private SpringView mSpringView;
    private SeekBar dr;
    private SeekBar stiff;
    private TextView drTxt;
    private TextView nfTxt;

    private float mDampingRatio;
    private float mStiffness;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_ball_spring, container, false);
        mSpringView = root.findViewById(R.id.actual_spring);
        img = root.findViewById(R.id.imageView);
        dr = root.findViewById(R.id.damping_ratio);
        stiff = root.findViewById(R.id.stiffness);
        drTxt = root.findViewById(R.id.damping_ratio_txt);
        nfTxt = root.findViewById(R.id.stiffness_txt);
        setupSeekBars();
        final SpringAnimation anim = new SpringAnimation(img, DynamicAnimation.TRANSLATION_Y,
                0 /* final position */);
        anim.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float v, float v1) {
                // Update the drawing of the spring.
                mSpringView.setMassHeight(img.getY());
            }
        });

        ((View) img.getParent()).setOnTouchListener(new View.OnTouchListener() {
            public float touchOffset;
            public VelocityTracker vt;
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    // check whether the touch happens inside of the img view.
                    boolean inside = motionEvent.getX() >= img.getX()
                            && motionEvent.getX() <= img.getX() + img.getWidth()
                            && motionEvent.getY() >= img.getY()
                            && motionEvent.getY() <= img.getY() + img.getHeight();

                    anim.cancel();

                    if (!inside) {
                        return false;
                    }
                    // Apply this offset to all the subsequent events
                    touchOffset = img.getTranslationY() - motionEvent.getY();
                    vt = VelocityTracker.obtain();
                    vt.clear();
                }

                vt.addMovement(motionEvent);

                if (motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE) {
                    img.setTranslationY(motionEvent.getY() + touchOffset);
                    // Updates the drawing of the spring.
                    mSpringView.setMassHeight(img.getY());
                } else if (motionEvent.getActionMasked() == MotionEvent.ACTION_CANCEL
                        || motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
                    // Compute the velocity in unit: pixel/second
                    vt.computeCurrentVelocity(1000);
                    float velocity = vt.getYVelocity();
                    anim.getSpring().setDampingRatio(mDampingRatio).setStiffness(mStiffness);
                    anim.setStartVelocity(velocity).start();
                    vt.recycle();
                }
                return true;
            }
        });
        return root;
    }
    // Setup seek bars so damping ratio and stiffness for the spring can be modified through the UI.
    void setupSeekBars() {
        dr.setMax(130);
        dr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 80) {
                    mDampingRatio = i / 80.0f;
                } else if (i > 90) {
                    mDampingRatio = (float) Math.exp((i - 90) / 10.0);
                } else {
                    mDampingRatio = 1;
                }
                drTxt.setText(String.format("%.4f", (float) mDampingRatio));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        stiff.setMax(110);
        stiff.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float stiffness = (float) Math.exp(i / 10d);
                mStiffness = stiffness;
                nfTxt.setText(String.format("%.3f", (float) stiffness));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        dr.setProgress(40);
        stiff.setProgress(60);
    }


}
