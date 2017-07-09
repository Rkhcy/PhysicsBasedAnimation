package com.example.robert.physicsbasedanimation.fragment;

import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.robert.physicsbasedanimation.R;

/**
 * Created by Robert on 2017/7/7.
 */

public class ChainedSpringFragment extends Fragment {
    private float mDampingRatio = 1.0f;
    private float mStiffness = 50.0f;

    private View root;
    private View lead;
    private View follow1;
    private View follow2;
    private SeekBar dr;
    private SeekBar stiff;
    private TextView drTxt;
    private TextView nfTxt;

    private SpringAnimation animate1X;
    private SpringAnimation animate1Y;
    private SpringAnimation animate2X;
    private SpringAnimation animate2Y;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_chained_spring, container, false);
        lead = root.findViewById(R.id.lead);
        follow1 = root.findViewById(R.id.follow1);
        follow2 = root.findViewById(R.id.follow2);
        dr = root.findViewById(R.id.damping_ratio);
        stiff = root.findViewById(R.id.stiffness);
        drTxt = root.findViewById(R.id.damping_ratio_txt);
        nfTxt = root.findViewById(R.id.stiffness_txt);

        animate1X = new SpringAnimation(follow1, DynamicAnimation.TRANSLATION_X, lead.getTranslationX());
        animate1Y = new SpringAnimation(follow1, DynamicAnimation.TRANSLATION_Y, lead.getTranslationY());
        animate2X = new SpringAnimation(follow2, DynamicAnimation.TRANSLATION_X, follow2.getTranslationX());
        animate2Y = new SpringAnimation(follow2, DynamicAnimation.TRANSLATION_Y, follow2.getTranslationY());

        animate1X.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float value, float velocity) {
                animate2X.animateToFinalPosition(value);
            }
        });
        animate1Y.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float value, float velocity) {
                animate2Y.animateToFinalPosition(value);
            }
        });

        ((View)lead.getParent()).setOnTouchListener(new View.OnTouchListener() {
            private float firstDownX = 0;
            private float firstDownY = 0;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {

                    if (motionEvent.getX() < lead.getX()
                            || motionEvent.getX() > lead.getX() + lead.getWidth()
                            || motionEvent.getY() < lead.getY()
                            || motionEvent.getY() > lead.getY() + lead.getHeight()) {
                        return false;
                    }

                    animate1X.getSpring().setDampingRatio(mDampingRatio).setStiffness(mStiffness);
                    animate1Y.getSpring().setDampingRatio(mDampingRatio).setStiffness(mStiffness);
                    animate2X.getSpring().setDampingRatio(mDampingRatio).setStiffness(mStiffness);
                    animate2Y.getSpring().setDampingRatio(mDampingRatio).setStiffness(mStiffness);

                    firstDownX = motionEvent.getX() - lead.getTranslationX();
                    firstDownY = motionEvent.getY() - lead.getTranslationY();
                } else if (motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE) {
                    float deltaX = motionEvent.getX() - firstDownX;
                    float deltaY = motionEvent.getY() - firstDownY;

                    lead.setTranslationX(deltaX);
                    lead.setTranslationY(deltaY);

                    animate1X.animateToFinalPosition(deltaX);
                    animate1Y.animateToFinalPosition(deltaY);
                    
                }
                return true;
            }
        });

        setupSeekBars();
        return root;
    }

    private void setupSeekBars() {
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
        dr.setProgress(80);
        stiff.setProgress(60);

    }
}
