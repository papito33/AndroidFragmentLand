package com.example.thomas.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.thomas.fragmentapplication.R;

/**
 * Created by thomas on 26/03/15.
 */
public class ViewTest extends View {

    //circle and text colors
    private int rectangleColor;
    private int labelTextColor;
    //label text
    private String labelText;
    //paint for drawing custom view
    private Paint rectanglePaint;

    public int getRectangleColor() {
        return rectangleColor;
    }

    public void setRectangleColor(int rectangleColor) {
        this.rectangleColor = rectangleColor;
        //redraw the view
        invalidate();
        requestLayout();
    }

    public int getLabelTextColor() {
        return labelTextColor;
    }

    public void setLabelTextColor(int labelTextColor) {
        this.labelTextColor = labelTextColor;
        //redraw the view
        invalidate();
        requestLayout();
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
        //redraw the view
        invalidate();
        requestLayout();
    }



    public ViewTest(Context context) {
        super(context);
    }

    public ViewTest(Context context, AttributeSet attrs) {
        super(context, attrs);
        rectanglePaint = new Paint();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.testView,0,0);

        try {
            //get the text and colors specified using the names in attrs.xml
            labelText = a.getString(R.styleable.testView_rectangleLabel);
            rectangleColor = a.getInteger(R.styleable.testView_rectangleColor, 0);//0 is default
            labelTextColor = a.getInteger(R.styleable.testView_labelColor, 0);

            Log.v("labelText" , "labelText " +  labelText);
            Log.v("rectangleColor" , "rectangleColor " +String.valueOf(rectangleColor));
            Log.v("labelTextColor" , "labelTextColor " +String.valueOf(labelTextColor));
        } finally {
            a.recycle();
        }

    }

    public ViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw the View
        int viewWidth = this.getMeasuredWidth();
        int viewHeight = this.getMeasuredHeight();

        rectanglePaint.setStyle(Paint.Style.FILL);
        rectanglePaint.setColor(rectangleColor);

        canvas.drawRect(0,0,viewWidth,70,rectanglePaint);

        rectanglePaint.setTextAlign(Paint.Align.CENTER);
        rectanglePaint.setTextSize(50);
        rectanglePaint.setColor(labelTextColor);
        canvas.drawText(labelText, this.getMeasuredWidth()/2, 50, rectanglePaint);
    }


}
