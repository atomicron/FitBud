package com.chupe.fitbud.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.chupe.fitbud.R;

public class Circle extends View {
    public Circle(Context context) {
        super(context);
        init(context, null);
    }
    public Circle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public Circle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public Paint paint;
    int radius;

    private void init(Context context, @Nullable AttributeSet set)
    {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3.0f);

        TypedArray a = context.getTheme().obtainStyledAttributes(
            set,
            R.styleable.Circle,
            0, 0);

        try {
            radius = a.getInteger(R.styleable.Circle_radius, 0);
        } finally {
            a.recycle();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(getWidth()/2,getHeight()/2, radius, paint);
    }
}
