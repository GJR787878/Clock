package com.gjr.clock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import java.util.Calendar;

public class GlassClockFaceView extends View {
    private final Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mHourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mMinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mSecPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final float mDensity;
    private final Calendar mCalendar = Calendar.getInstance();
    private float mRadius;
    private boolean mNight = true;

    public GlassClockFaceView(Context context) {
        this(context, null);
    }

    public GlassClockFaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlassClockFaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDensity = getResources().getDisplayMetrics().density;
        initPaint();
        applyColors();
    }

    private void initPaint() {
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(3f * mDensity);

        mHourPaint.setStrokeWidth(5f * mDensity);
        mHourPaint.setStrokeCap(Paint.Cap.ROUND);

        mMinPaint.setStrokeWidth(3f * mDensity);
        mMinPaint.setStrokeCap(Paint.Cap.ROUND);

        mSecPaint.setStrokeWidth(1.5f * mDensity);
        mSecPaint.setStrokeCap(Paint.Cap.ROUND);

        mDotPaint.setStyle(Paint.Style.FILL);
    }

    private void applyColors() {
        if (mNight) {
            mCirclePaint.setColor(0x66FFFFFF);
            mHourPaint.setColor(0xFFFFFFFF);
            mMinPaint.setColor(0xFFDDDDDD);
        } else {
            mCirclePaint.setColor(0x66000000);
            mHourPaint.setColor(0xFF111111);
            mMinPaint.setColor(0xFF444444);
        }
        mSecPaint.setColor(0xFF0A84FF);
        mDotPaint.setColor(0xFF0A84FF);
    }

    public void setNight(boolean night) {
        mNight = night;
        applyColors();
        invalidate();
    }

    public void updateTime() {
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float availW = w - getPaddingLeft() - getPaddingRight();
        float availH = h - getPaddingTop() - getPaddingBottom();
        mRadius = Math.min(availW, availH) / 2f - 8f * mDensity;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float cx = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight()) / 2f;
        float cy = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom()) / 2f;

        canvas.drawCircle(cx, cy, mRadius, mCirclePaint);

        int h = mCalendar.get(Calendar.HOUR); // 已经是 0-11
        int m = mCalendar.get(Calendar.MINUTE);
        int s = mCalendar.get(Calendar.SECOND);

        float hourAngle = (h + m / 60f) * 30f;
        float minAngle = (m + s / 60f) * 6f;
        float secAngle = s * 6f;

        drawHand(canvas, cx, cy, hourAngle, mRadius * 0.55f, mHourPaint);
        drawHand(canvas, cx, cy, minAngle, mRadius * 0.70f, mMinPaint);
        drawHand(canvas, cx, cy, secAngle, mRadius * 0.80f, mSecPaint);

        canvas.drawCircle(cx, cy, 4f * mDensity, mDotPaint);
    }

    private void drawHand(Canvas canvas, float cx, float cy, float angle, float len, Paint paint) {
        double rad = Math.toRadians(angle - 90);
        float x2 = cx + (float) (Math.cos(rad) * len);
        float y2 = cy + (float) (Math.sin(rad) * len);
        canvas.drawLine(cx, cy, x2, y2, paint);
    }
}
