package com.example.mealrecommendationapp.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CaloriesArcView extends View {

    private Paint backgroundPaint;
    private Paint progressPaint;

    private RectF rectF;

    private float progress = 75f;

    public CaloriesArcView(Context context) {
        super(context);
        init();
    }

    public CaloriesArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CaloriesArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(26f);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        backgroundPaint.setColor(Color.parseColor("#E7CFCB"));

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(26f);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setColor(Color.parseColor("#F57D5B"));

        rectF = new RectF();
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        float padding = 35f;

        rectF.set(
                padding,
                padding,
                getWidth() - padding,
                getHeight() - padding
        );

        // background arc

        canvas.drawArc(
                rectF,
                180,
                180,
                false,
                backgroundPaint
        );

        // progress arc

        canvas.drawArc(
                rectF,
                180,
                180 * (progress / 100f),
                false,
                progressPaint
        );
    }
}