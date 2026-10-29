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

    private float mRadius;
    private Calendar mCalendar = Calendar.getInstance();

    public GlassClockFaceView(Context context) {
        this(context, null);
    }

    public GlassClockFaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlassClockFaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        // 玻璃外圈
        mCirclePaint.setColor(0x40FFFFFF);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(6f);

        // 时针
        mHourPaint.setColor(0xFFFFFFFF);
        mHourPaint.setStrokeWidth(10f);
        mHourPaint.setStrokeCap(Paint.Cap.ROUND);
        // 分针
        mMinPaint.setColor(0xFFDDDDDD);
        mMinPaint.setStrokeWidth(6f);
        mMinPaint.setStrokeCap(Paint.Cap.ROUND);
        // 秒针
        mSecPaint.setColor(0xFF0A84FF);
        mSecPaint.setStrokeWidth(3f);
        mSecPaint.setStrokeCap(Paint.Cap.ROUND);
        // 中心点
        mDotPaint.setColor(0xFF0A84FF);
        mDotPaint.setStyle(Paint.Style.FILL);
    }

    public void updateTime() {
        mCalendar = Calendar.getInstance();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = Math.min(w, h) / 2f - 24f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;

        // 外圈玻璃环
        canvas.drawCircle(cx, cy, mRadius, mCirclePaint);

        int h = mCalendar.get(Calendar.HOUR);
        int m = mCalendar.get(Calendar.MINUTE);
        int s = mCalendar.get(Calendar.SECOND);

        float hourAngle = (h % 12 + m / 60f) * 30f;
        float minAngle = (m + s / 60f) * 6f;
        float secAngle = s * 6f;

        drawLine(canvas, cx, cy, hourAngle, mRadius * 0.55f, mHourPaint);
        drawLine(canvas, cx, cy, minAngle, mRadius * 0.70f, mMinPaint);
        drawLine(canvas, cx, cy, secAngle, mRadius * 0.80f, mSecPaint);

        canvas.drawCircle(cx, cy, 8f, mDotPaint);
    }

    private void drawLine(Canvas canvas, float cx, float cy, float angle, float len, Paint paint) {
        double rad = Math.toRadians(angle - 90);
        float x2 = cx + (float) (Math.cos(rad) * len);
        float y2 = cy + (float) (Math.sin(rad) * len);
        canvas.drawLine(cx, cy, x2, y2, paint);
    }
}
