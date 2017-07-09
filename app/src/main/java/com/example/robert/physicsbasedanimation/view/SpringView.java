package com.example.robert.physicsbasedanimation.view;

/**
 * Created by Robert on 2017/7/7.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.robert.physicsbasedanimation.R;

/**
 * The view that draws the spring as it reacts (i.e. expands/compresses) to the user touch.
 */
public class SpringView extends View {
    final Paint mPaint = new Paint();
    private float mLastHeight = 175;

    public SpringView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        mPaint.setColor(context.getResources().getColor(R.color.springColor));
        mPaint.setStrokeWidth(10);
    }

    /**
     * Sets the other end of the spring.
     *
     * @param height height of the mass, which is used to derive how to draw the spring
     */
    public void setMassHeight(float height) {
        mLastHeight = height;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Draws the spring
        // 30px long, 15 sections
        int num = 20;
        float sectionLen = 150; // px
        final float x = canvas.getWidth() / 2;
        float y = 0;
        float sectionHeight = mLastHeight / num;
        float sectionWidth = (float) Math.sqrt(sectionLen * sectionLen
                - sectionHeight * sectionHeight);
        canvas.drawLine(x, 0, x + sectionWidth / 2, sectionHeight / 2, mPaint);
        float lastX = x + sectionWidth / 2;
        float lastY = sectionHeight / 2;
        for (int i = 1; i < num; i++) {
            canvas.drawLine(lastX, lastY, 2 * x - lastX, lastY + sectionHeight, mPaint);
            lastX = 2 * x - lastX;
            lastY = lastY + sectionHeight;
        }
        canvas.drawLine(lastX, lastY, x, mLastHeight, mPaint);
    }
}
