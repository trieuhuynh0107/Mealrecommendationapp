package com.example.mealrecommendationapp.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class RulerView extends View {

    private Paint linePaint;
    private Paint centerPaint;
    private Paint textPaint;

    private int min = 100;
    private int max = 220;
    private int currentValue = 170;

    private float offset = 0;
    private float lastX;

    private static final int STEP_WIDTH = 20;

    public interface OnValueChangeListener {
        void onValueChange(int value);
    }

    private OnValueChangeListener listener;

    public void setOnValueChangeListener(OnValueChangeListener l) {
        this.listener = l;
    }

    public void setRange(int min, int max, int defaultValue) {
        this.min = min;
        this.max = max;
        this.currentValue = defaultValue;
        invalidate();
    }

    public RulerView(Context context) {
        super(context);
        init();
    }

    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(3);

        centerPaint = new Paint();
        centerPaint.setColor(Color.WHITE);
        centerPaint.setStrokeWidth(6);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(28);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int height = getHeight();

        for (int i = min; i <= max; i++) {
            float x = centerX + (i - currentValue) * STEP_WIDTH + offset;

            if (x < 0 || x > getWidth()) continue;

            if (i % 10 == 0) {
                canvas.drawLine(x, 0, x, height, linePaint);
                canvas.drawText(String.valueOf(i), x, height - 10, textPaint);
            } else if (i % 5 == 0) {
                canvas.drawLine(x, height / 4f, x, height * 3 / 4f, linePaint);
            } else {
                canvas.drawLine(x, height / 3f, x, height * 2 / 3f, linePaint);
            }
        }

        canvas.drawLine(centerX, 0, centerX, height, centerPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                return true;

            case MotionEvent.ACTION_MOVE:
                float dx = event.getX() - lastX;
                offset += dx;

                if (Math.abs(offset) >= STEP_WIDTH) {
                    int change = (int) (offset / STEP_WIDTH);
                    currentValue -= change;
                    offset %= STEP_WIDTH;

                    if (currentValue < min) currentValue = min;
                    if (currentValue > max) currentValue = max;

                    if (listener != null) {
                        listener.onValueChange(currentValue);
                    }
                }

                lastX = event.getX();
                invalidate();
                return true;
        }

        return super.onTouchEvent(event);
    }
    public int getCurrentValue() {

        return currentValue;
    }
}