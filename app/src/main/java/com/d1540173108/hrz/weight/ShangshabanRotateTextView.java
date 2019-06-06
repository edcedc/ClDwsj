package com.d1540173108.hrz.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by edison on 2019/1/13.
 */

public class ShangshabanRotateTextView extends android.support.v7.widget.AppCompatTextView {
    public ShangshabanRotateTextView(Context context) {
        super(context);
    }

    public ShangshabanRotateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //倾斜度45,上下左右居中
        canvas.rotate(5, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        super.onDraw(canvas);
    }

}
