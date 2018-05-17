package cn.edu.sicnu.coolweather.game.game2018;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by pby on 2017/5/29.
 */

public class MyTextView  extends TextView {
    private Paint paint = null;
    private String textString = null;
    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
    }
    public void setTextString(String textString)
    {
        this.textString = textString;
    }
    public String getTextString()
    {
        return textString;
    }

    public void setTextSize1(float size)
    {
        paint.setTextSize(size);
    }
    public void setTextColor1(int color)
    {
        paint.setColor(color);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Rect rect = new Rect();
        paint.getTextBounds(textString, 0, textString.length(), rect);
        int textWidth = rect.width();
        int textHeight = rect.height();
        int width = getWidth();
        int height = getHeight();
        canvas.drawText(textString, 0, textString.length(),(width - textWidth) / 2, (textHeight + height) / 2, paint);
    }
}
