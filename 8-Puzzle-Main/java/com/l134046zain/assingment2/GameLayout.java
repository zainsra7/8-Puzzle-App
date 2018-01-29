package com.l134046zain.assingment2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Zan on 10/12/2016.
 */
public class GameLayout extends ViewGroup {
    private static final int DEFAULT_COUNT = 3;

    private Paint mGridPaint;

    private int mColumnCount;
    private int mMaxChildren;
    Context c;

    public GameLayout(Context context) {
        this(context, null);
    }

    public GameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        c=context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GameLayout, 0, defStyle);

        int strokeWidth = a.getDimensionPixelSize(R.styleable.GameLayout_separatorWidth, 0);
        int strokeColor = a.getColor(R.styleable.GameLayout_separatorColor, Color.WHITE);
        mColumnCount = a.getInteger(R.styleable.GameLayout_numColumns, DEFAULT_COUNT);
        mMaxChildren = mColumnCount * mColumnCount;

        a.recycle();

        mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGridPaint.setStyle(Paint.Style.STROKE);
        mGridPaint.setColor(strokeColor);
        mGridPaint.setStrokeWidth(strokeWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize, heightSize;

        //Get the width based on the measure specs
        widthSize = getDefaultSize(0, widthMeasureSpec);

        //Get the height based on measure specs
        heightSize = getDefaultSize(0, heightMeasureSpec);

        int majorDimension = Math.min(widthSize, heightSize);
        //Measure all child views
        int blockDimension = majorDimension / mColumnCount;
        int blockSpec = MeasureSpec.makeMeasureSpec(blockDimension, MeasureSpec.EXACTLY);
        measureChildren(blockSpec, blockSpec);

        //MUST call this to save our own dimensions
        setMeasuredDimension(majorDimension, majorDimension);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int row, col, left, top;
        for (int i=0; i < getChildCount(); i++) {
            row = i / mColumnCount;
            col = i % mColumnCount;
            View child = getChildAt(i);
            left = col * child.getMeasuredWidth();
            top = row * child.getMeasuredHeight();




            child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());

        }

        //Here I am creating Random Values for each cell of the puzzle

        ArrayList<Integer> arr = new ArrayList<>();
        boolean isPuzzleSolvable=false;
        int count_inversions=0;


        while(isPuzzleSolvable==false) {

            for (int i = 0; i < 9; i++) {
                arr.add(new Integer(i));
            }

            Collections.shuffle(arr); //Built in Function that will shuffle the values





            //Checking Solvability of the puzzle
            // ( in my case the grid width is odd i.e 3 , so my formula is (grid width is odd && # of inversions are even) then the puzzle is solveable

            for (int i = 0; i < 9; i++) {
                int x = i + 1;
                while (x < arr.size()) {
                    if ((arr.get(i).intValue() > arr.get(x).intValue()) && (arr.get(i).intValue() != 0 && arr.get(x).intValue() != 0)) {
                        count_inversions++;
                    }
                    x++;

                }

            }




            if (count_inversions % 2 == 0) {

                //This puzzle is Solveable

                isPuzzleSolvable = true;
            }
            else arr.clear();
        }

        for(int i=0;i<9;i++)
        {

            //Setting Random Values for Children

            Integer value=arr.get(i);

            if(value.intValue()==0)
            {
                ((TextView) getChildAt(i)).setText(value.toString());
                getChildAt(i).setVisibility(INVISIBLE);
            }

            else {

                ((TextView) getChildAt(i)).setText(value.toString());
                getChildAt(i).setVisibility(VISIBLE);
            }
        }


    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //Let the framework do its thing
        super.dispatchDraw(canvas);

        //Draw the grid lines
        for (int i=0; i <= getWidth(); i += (getWidth() / mColumnCount)) {
            canvas.drawLine(i, 0, i, getHeight(), mGridPaint);
        }
        for (int i=0; i <= getHeight(); i += (getHeight() / mColumnCount)) {
            canvas.drawLine(0, i, getWidth(), i, mGridPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:


                //Calculating Correct Index of Child View
                float x=event.getX();
                float y=event.getY();

               int index=CalculateIndex(x,y);



            //Accessing Child
                View w=getChildAt(index);



                MakeMove(w,index);

                boolean isWinState=checkWinState();

                if(isWinState==true)
                {
                    Toast.makeText(c,"Game Won ! Puzzle Solved !",Toast.LENGTH_LONG).show();
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    public void MakeMove(View child, int index)
    {
        TextView ClickedView=(TextView) child;
        TextView TempView;


        if(ClickedView.getText().equals("0"))
        {
            //Invalid Move



        }
        else
        {
            if(index==0)
            {

                TempView=(TextView) getChildAt(1);
                if(TempView.getText().equals("0"))
                {
                    CharSequence c=ClickedView.getText();

                    ClickedView.setText("0");
                    ClickedView.setVisibility(INVISIBLE);
                    TempView.setText(c);
                    TempView.setVisibility(VISIBLE);
                }
                else
                {
                    TempView=(TextView) getChildAt(index+3);

                    if(TempView.getText().equals("0"))
                    {
                        CharSequence c=ClickedView.getText();
                        ClickedView.setText("0");
                        ClickedView.setVisibility(INVISIBLE);
                        TempView.setText(c);
                        TempView.setVisibility(VISIBLE);
                    }

                }
            }//end of index==0


            else if(index==1)
            {
                TempView=(TextView) getChildAt(0);
                if(TempView.getText().equals("0"))
                {
                    CharSequence c=ClickedView.getText();
                    Log.d("MY", "MakeMove: GetTextClickedView "+c);
                    ClickedView.setText("0");
                    ClickedView.setVisibility(INVISIBLE);
                    TempView.setText(c);
                    TempView.setVisibility(VISIBLE);
                }
                else
                {
                    TempView=(TextView) getChildAt(2);
                    if(TempView.getText().equals("0"))
                    {
                        CharSequence c=ClickedView.getText();
                        ClickedView.setText("0");
                        ClickedView.setVisibility(INVISIBLE);
                        TempView.setText(c);
                        TempView.setVisibility(VISIBLE);
                    }
                    else
                    {
                        TempView=(TextView) getChildAt(4);
                        if(TempView.getText().equals("0"))
                        {
                            CharSequence c=ClickedView.getText();
                            ClickedView.setText("0");
                            ClickedView.setVisibility(INVISIBLE);
                            TempView.setText(c);
                            TempView.setVisibility(VISIBLE);
                        }

                    }
                }
            }//end of index ==1

            else if(index==2)
            {
                TempView=(TextView) getChildAt(1);
                if(TempView.getText().equals("0"))
                {
                    CharSequence c=ClickedView.getText();
                    ClickedView.setText("0");
                    ClickedView.setVisibility(INVISIBLE);
                    TempView.setText(c);
                    TempView.setVisibility(VISIBLE);
                }
                else
                {
                    TempView=(TextView) getChildAt(5);

                    if(TempView.getText().equals("0"))
                    {
                        CharSequence c=ClickedView.getText();
                        ClickedView.setText("0");
                        ClickedView.setVisibility(INVISIBLE);
                        TempView.setText(c);
                        TempView.setVisibility(VISIBLE);
                    }

                }
            } //end of index==2

        else if(index==3)
            {

                TempView=(TextView) getChildAt(0);
                if(TempView.getText().equals("0"))
                {
                    CharSequence c=ClickedView.getText();
                    ClickedView.setText("0");
                    ClickedView.setVisibility(INVISIBLE);
                    TempView.setText(c);
                    TempView.setVisibility(VISIBLE);
                }
                else
                {
                    TempView=(TextView) getChildAt(4);

                    if(TempView.getText().equals("0"))
                    {
                        CharSequence c=ClickedView.getText();
                        ClickedView.setText("0");
                        ClickedView.setVisibility(INVISIBLE);
                        TempView.setText(c);
                        TempView.setVisibility(VISIBLE);
                    }
                    else
                    {
                        TempView=(TextView) getChildAt(6);

                        if(TempView.getText().equals("0"))
                        {
                            CharSequence c=ClickedView.getText();
                            ClickedView.setText("0");
                            ClickedView.setVisibility(INVISIBLE);
                            TempView.setText(c);
                            TempView.setVisibility(VISIBLE);
                        }
                    }

                }

            } //end of index==3


        else if(index==4)
            {

                TempView=(TextView) getChildAt(1);
                if(TempView.getText().equals("0"))
                {
                    CharSequence c=ClickedView.getText();
                    ClickedView.setText("0");
                    ClickedView.setVisibility(INVISIBLE);
                    TempView.setText(c);
                    TempView.setVisibility(VISIBLE);
                }
                else
                {
                    TempView=(TextView) getChildAt(3);

                    if(TempView.getText().equals("0"))
                    {
                        CharSequence c=ClickedView.getText();
                        ClickedView.setText("0");
                        ClickedView.setVisibility(INVISIBLE);
                        TempView.setText(c);
                        TempView.setVisibility(VISIBLE);
                    }
                    else
                    {
                        TempView=(TextView) getChildAt(5);

                        if(TempView.getText().equals("0"))
                        {
                            CharSequence c=ClickedView.getText();
                            ClickedView.setText("0");
                            ClickedView.setVisibility(INVISIBLE);
                            TempView.setText(c);
                            TempView.setVisibility(VISIBLE);
                        }
                        else
                        {
                            TempView=(TextView) getChildAt(7);

                            if(TempView.getText().equals("0"))
                            {
                                CharSequence c=ClickedView.getText();
                                ClickedView.setText("0");
                                ClickedView.setVisibility(INVISIBLE);
                                TempView.setText(c);
                                TempView.setVisibility(VISIBLE);
                            }
                        }
                    }

                }


            }//end of index==4

            else if(index==5)
            {
                TempView=(TextView) getChildAt(2);
                if(TempView.getText().equals("0"))
                {
                    CharSequence c=ClickedView.getText();
                    ClickedView.setText("0");
                    ClickedView.setVisibility(INVISIBLE);
                    TempView.setText(c);
                    TempView.setVisibility(VISIBLE);
                }
                else
                {
                    TempView=(TextView) getChildAt(8);

                    if(TempView.getText().equals("0"))
                    {
                        CharSequence c=ClickedView.getText();
                        ClickedView.setText("0");
                        ClickedView.setVisibility(INVISIBLE);
                        TempView.setText(c);
                        TempView.setVisibility(VISIBLE);
                    }

                    else
                    {
                        TempView=(TextView) getChildAt(4);

                        if(TempView.getText().equals("0"))
                        {
                            CharSequence c=ClickedView.getText();
                            ClickedView.setText("0");
                            ClickedView.setVisibility(INVISIBLE);
                            TempView.setText(c);
                            TempView.setVisibility(VISIBLE);
                        }

                    }


                }



            }//end of index==5

            else if(index==6)
            {

                TempView=(TextView) getChildAt(3);
                if(TempView.getText().equals("0"))
                {
                    CharSequence c=ClickedView.getText();
                    Log.d("MY", "MakeMove: GetTextClickedView "+c);
                    ClickedView.setText("0");
                    ClickedView.setVisibility(INVISIBLE);
                    TempView.setText(c);
                    TempView.setVisibility(VISIBLE);
                }
                else
                {
                    TempView = (TextView) getChildAt(7);
                    if (TempView.getText().equals("0")) {
                        CharSequence c = ClickedView.getText();
                        ClickedView.setText("0");
                        ClickedView.setVisibility(INVISIBLE);
                        TempView.setText(c);
                        TempView.setVisibility(VISIBLE);
                    }
                }

            }//end of index==6

            else if (index==7)
            {

                TempView=(TextView) getChildAt(4);
                if(TempView.getText().equals("0"))
                {
                    CharSequence c=ClickedView.getText();
                    Log.d("MY", "MakeMove: GetTextClickedView "+c);
                    ClickedView.setText("0");
                    ClickedView.setVisibility(INVISIBLE);
                    TempView.setText(c);
                    TempView.setVisibility(VISIBLE);
                }
                else {
                    TempView = (TextView) getChildAt(6);
                    if (TempView.getText().equals("0")) {
                        CharSequence c = ClickedView.getText();
                        ClickedView.setText("0");
                        ClickedView.setVisibility(INVISIBLE);
                        TempView.setText(c);
                        TempView.setVisibility(VISIBLE);
                    }
                    else
                    {
                        TempView = (TextView) getChildAt(8);
                        if (TempView.getText().equals("0")) {
                            CharSequence c = ClickedView.getText();
                            ClickedView.setText("0");
                            ClickedView.setVisibility(INVISIBLE);
                            TempView.setText(c);
                            TempView.setVisibility(VISIBLE);
                        }
                    }
                }

            }//end of index==7


            else if(index==8)
            {

                TempView=(TextView) getChildAt(7);
                if(TempView.getText().equals("0"))
                {
                    CharSequence c=ClickedView.getText();
                    Log.d("MY", "MakeMove: GetTextClickedView "+c);
                    ClickedView.setText("0");
                    ClickedView.setVisibility(INVISIBLE);
                    TempView.setText(c);
                    TempView.setVisibility(VISIBLE);
                }
                else {
                    TempView = (TextView) getChildAt(5);
                    if (TempView.getText().equals("0")) {
                        CharSequence c = ClickedView.getText();
                        ClickedView.setText("0");
                        ClickedView.setVisibility(INVISIBLE);
                        TempView.setText(c);
                        TempView.setVisibility(VISIBLE);
                    }

                }


            }//end of index==8

        }//end of else


    }

public int CalculateIndex(float x,float y)
{
    int index=0;
    int height=getChildAt(0).getMeasuredHeight();
    int width=getChildAt(0).getMeasuredWidth();


    int i=(int) x/width;
    int j= (int) y/height;

     index= (i*3)+j;

    if(index==7){index=5;}
    else if(index==5) {index=7;}
    else if(index==2){index=6;}
    else if(index==6){index=2;}
    else if(index==3){index=1;}
    else if(index==1){index=3;}


    return index;
}


    public boolean checkWinState()
    {

        if  (((TextView)getChildAt(0)).getText().equals("1") &&
                ((TextView)getChildAt(1)).getText().equals("2") &&
        ((TextView)getChildAt(2)).getText().equals("3") &&
        ((TextView)getChildAt(3)).getText().equals("4") &&
        ((TextView)getChildAt(4)).getText().equals("5") &&
        ((TextView)getChildAt(5)).getText().equals("6") &&
        ((TextView)getChildAt(6)).getText().equals("7") &&
        ((TextView)getChildAt(7)).getText().equals("8") &&
        ((TextView)getChildAt(8)).getText().equals("0")
                )
        {

            return true;
        }
         return false;

    }


    @Override
    public void addView(View child) {
        if (getChildCount() > mMaxChildren-1) {
            throw new IllegalStateException("BoxGridLayout cannot have more than "+mMaxChildren+" direct children");
        }

        super.addView(child);
    }


    @Override
    public void addView(View child, int index) {
        if (getChildCount() > mMaxChildren-1) {
            throw new IllegalStateException("BoxGridLayout cannot have more than "+mMaxChildren+" direct children");
        }

        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (getChildCount() > mMaxChildren-1) {
            throw new IllegalStateException("BoxGridLayout cannot have more than "+mMaxChildren+" direct children");
        }

        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, LayoutParams params) {
        if (getChildCount() > mMaxChildren-1) {
            throw new IllegalStateException("BoxGridLayout cannot have more than "+mMaxChildren+" direct children");
        }

        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        if (getChildCount() > mMaxChildren-1) {
            throw new IllegalStateException("BoxGridLayout cannot have more than "+mMaxChildren+" direct children");
        }

        super.addView(child, width, height);
    }
}
