package com.dateviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.dateviewdemo.Util.DensityUtils;

import java.util.Calendar;

public class DateV extends View {

    int COL = 7, ROW = 6;
    int currentMonthDays;//当前月的天数
    int year, month, day;// 年月日
    int widthSize, heightSize;//单元格的宽高
    int firstweek;//当前1号是周几
    Paint mTextPaint;
    String[] weekName = new String[]{"日", "一", "二", "三", "四", "五", "六"};
    boolean isCurrentMonth = true, isNextMonth = false, isCurrentYear = true, isClickToday = true;
    int distanceDays;//当日与最后一天的差值
    int nextdistanceDays = 0;//下一个月有几天可以选
    int clickDay = -1;
    int twoweek = 7;
    int thismonth = 0;
    int clickX, clickY;

    private String drawText = "00";//绘制字体的

    public DateV(Context context, AttributeSet attrs) {
        super(context, attrs);
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DATE);
        distanceDays = getCurrentMonthDays(year, month) - day+1;
        if (distanceDays < twoweek) {
            nextdistanceDays = twoweek - distanceDays;
        }

        init();
    }

    private void init() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.GRAY);
        mTextPaint.setTextSize(DensityUtils.dp2px(getContext(), 12));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        initSize();
        currentMonthDays = getCurrentMonthDays(year, month);
        firstweek = getFirstWeek(year, month);
        int middistanceDays = distanceDays;//中间值
        int midnextdistanceDays = nextdistanceDays;//中间值
        //画周一到周日
        /*for(int i=0;i<weekName.length;i++){
			canvas.drawText(weekName[i], widthSize*i+(widthSize-mTextPaint.measureText(weekName[i]))/2, 
					mTextPaint.measureText(weekName[i])*2, mTextPaint);
		}*/
        int column = 0, row = 0;
        for (int i = 0; i < currentMonthDays; i++) {
            String dayString = i + 1 + "";
            column = (i + firstweek - 1) % 7;
            row = (i + firstweek - 1) / 7;
            int startX = (int) (widthSize * column + (widthSize - mTextPaint.measureText(dayString)) / 2);
            int startY;
            startY = (int) (heightSize * row + (heightSize - mTextPaint.measureText(drawText) * 2.5));

            if (midnextdistanceDays > 0 && isNextMonth && isCurrentYear) { //下个月可以预约的天数

                /*if (column == 0 || column == 6) {
                    mTextPaint.setColor(Color.RED);
                } else {
                    mTextPaint.setColor(Color.GRAY);
                }*/
                canvas.drawText(dayString, startX, startY, mTextPaint);

                midnextdistanceDays--;
            } else {
                if(year<Calendar.getInstance().get(Calendar.YEAR)){ //去年的
                    mTextPaint.setColor(Color.GRAY);
                }else if(year==Calendar.getInstance().get(Calendar.YEAR)){
                    if((month< Calendar.getInstance().get(Calendar.MONTH))||(month== Calendar.getInstance().get(Calendar.MONTH) &&i < (day - 1))) {
                        mTextPaint.setColor(Color.GRAY);
                    }
                }else{
                    mTextPaint.setColor(Color.BLACK);
                }
                canvas.drawText(dayString, startX, startY, mTextPaint);
            }

            if (i == day - 1 && isCurrentMonth && isCurrentYear) { //今天
                if (isClickToday) {

                    canvas.drawCircle(startX + mTextPaint.measureText(day + "") / 2, startY - mTextPaint.measureText(day + "") / 3, mTextPaint.measureText(drawText), mTextPaint);
                    mTextPaint.setColor(Color.WHITE);
                }
                canvas.drawText(dayString, startX, startY, mTextPaint);
                mTextPaint.setColor(Color.GRAY);
                canvas.drawText("今天", startX - mTextPaint.measureText("今天") / 3, startY + mTextPaint.measureText("今天"), mTextPaint);
            }
            if (middistanceDays < twoweek && i >= day - 1 && isCurrentMonth && isCurrentYear) { //这个月可以预约的天数,但是下个月还有

               /* if (column == 0 || column == 6) {
                    mTextPaint.setColor(Color.RED);
                } else {
                    mTextPaint.setColor(Color.GRAY);
                }*/
                if (i == day - 1) { //表示今天的
                    if (!isClickToday) {
                        canvas.drawText(dayString, startX, startY, mTextPaint);
                    }
                } else {
                    canvas.drawText(dayString, startX, startY, mTextPaint);

                }

                middistanceDays--;
            }
            if (middistanceDays >= twoweek && thismonth < twoweek && i >= day - 1 && isCurrentMonth && isCurrentYear) {//这个月可以预约的天数,但是下个月没有
                if (column == 0 || column == 6) {
                    mTextPaint.setColor(Color.RED);
                } else {
                    mTextPaint.setColor(Color.GRAY);
                }
                if (i == day - 1) { //表示今天的
                    if (!isClickToday) {
                        canvas.drawText(dayString, startX, startY, mTextPaint);
                    }
                } else {
                    canvas.drawText(dayString, startX, startY, mTextPaint);
                }
                thismonth++;
            }
            mTextPaint.setColor(Color.BLACK);
            if (clickDay == i + 1) {
                clickX = startX;
                clickY = startY;
            }
        }
        thismonth = 0;
        if (clickDay != -1) {
            if (clickDay < 10) {
                canvas.drawCircle(clickX + mTextPaint.measureText(clickDay + "") / 2, clickY - mTextPaint.measureText(clickDay + "") / 3, mTextPaint.measureText(clickDay + "0"), mTextPaint);

            } else {
                canvas.drawCircle(clickX + mTextPaint.measureText(clickDay + "") / 2, clickY - mTextPaint.measureText(clickDay + "") / 3, mTextPaint.measureText(clickDay + ""), mTextPaint);
            }

            mTextPaint.setColor(Color.WHITE);
            canvas.drawText(clickDay + "", clickX, clickY, mTextPaint);

            mTextPaint.setColor(Color.GRAY);
        }

        //画线
        for (int i = 0; i <= 5; i++) {
            //+heightSize/3;
            canvas.drawLine(0, heightSize * (row)-mTextPaint.measureText(drawText)/3, getWidth(), heightSize * (row)-mTextPaint.measureText(drawText)/3, mTextPaint);
            Log.i("col", column + "====" + row);
            row--;
        }

    }

    //获取每一行，每一列的宽度
    public void initSize() {
        widthSize = getWidth() / COL;
        heightSize = getHeight() / ROW;
    }

    //返回某年某月的天数
    public int getCurrentMonthDays(int year, int month) {

        int[] arr = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (year % 4 == 0 && (year % 100 != 0) || (year % 400 == 0)) {
            arr[1] = 29;
        }
        return arr[month];
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (onClickItemListener != null) {
                    if (getClickDay(x, y) != -1) {
                        clickDay = getClickDay(x, y);
                        if(year>Calendar.getInstance().get(Calendar.YEAR) &&Calendar.getInstance().get(Calendar.MONTH)!=11 ){
                            onClickItemListener.onClickItem(getClickDay(x, y));
                            isClickToday = false;
                            invalidate();
                            return true;
                        }
                        if (distanceDays >= twoweek) {//本月
                            if ((year == Calendar.getInstance().get(Calendar.YEAR) && month == Calendar.getInstance().get(Calendar.MONTH))) {
                                if (clickDay > (twoweek + day - 1)) {
                                    onClickItemListener.onClickItem(getClickDay(x, y));
                                    isClickToday = false;
                                    invalidate();
                                    return true;
                                }
                            }else if((year == Calendar.getInstance().get(Calendar.YEAR) && month > Calendar.getInstance().get(Calendar.MONTH))
                                    ||(year > Calendar.getInstance().get(Calendar.YEAR))){
                                if(clickDay>=1){
                                    onClickItemListener.onClickItem(getClickDay(x, y));
                                    isClickToday = false;
                                    invalidate();
                                    return true;
                                }
                            }
                        }else if(Calendar.getInstance().get(Calendar.MONTH)!= 11){
                            if ((year == Calendar.getInstance().get(Calendar.YEAR) && month == Calendar.getInstance().get(Calendar.MONTH) + 1)) {
                                if ( clickDay > (twoweek - distanceDays)) {
                                    onClickItemListener.onClickItem(getClickDay(x, y));
                                    invalidate();
                                    isClickToday = false;
                                    return true;
                                }
                            }
                        }else{
                            if (year == Calendar.getInstance().get(Calendar.YEAR) + 1 && month == 0) {
                                if ( clickDay > (twoweek - distanceDays)) {
                                    onClickItemListener.onClickItem(getClickDay(x, y));
                                    invalidate();
                                    isClickToday = false;
                                    return true;
                                }
                            }
                        }
                    }
                }
                clickDay = -1;
                return false;
            case MotionEvent.ACTION_MOVE:
                return false;
            case MotionEvent.ACTION_UP:
			/*clickDay=-1;
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					invalidate();
				}
			}, 200);*/

                break;

        }
        return super.onTouchEvent(event);
    }

    public int getClickDay(int x, int y) {
        int day = -1;
        int indexi = 0, indexj = 0;
        for (; x > widthSize; ) {
            x -= widthSize;
            indexi++;
        }
        for (; y > heightSize; ) {
            y -= heightSize;
            indexj++;
        }
        day = indexj * COL + indexi - getFirstWeek(year, month) + 2;
        if (day > currentMonthDays || day == 0) {
            day = -1;
        }
        Log.i("index", indexi + "====" + indexj+"==="+day);

        return day;
    }


    //返回1号是周几
    public int getFirstWeek(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    private OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void onClickItem(int day);
    }

    //下一页
    public void nextPage() {
        Log.i("month", month + "===");
        if (month == 11) {
            month = 0;
            year += 1;

        } else {
            month++;
        }
        isClickToday = true;
        clickDay = -1;
        //今年 或者明年
        isCurrentYear = year == Calendar.getInstance().get(Calendar.YEAR) || (year== Calendar.getInstance().get(Calendar.YEAR)+1);
        //本月
        isCurrentMonth = month == Calendar.getInstance().get(Calendar.MONTH) && year== Calendar.getInstance().get(Calendar.YEAR);
        //下个月
        isNextMonth = month == Calendar.getInstance().get(Calendar.MONTH) + 1 || ((year== Calendar.getInstance().get(Calendar.YEAR)+1) &&month==0 );
        Log.i("nextmonth", month + "==" + isNextMonth + "==" + distanceDays);
        invalidate();
    }

    //上一页
    public void prePage() {
        if (month == 0) {
            month = 11;
            year -= 1;
            isCurrentYear = false;
        } else {
            month--;
        }
        isClickToday = true;
        clickDay = -1;
        //今年或者明年
        isCurrentYear = year == Calendar.getInstance().get(Calendar.YEAR)|| (year== Calendar.getInstance().get(Calendar.YEAR)+1);
        //本月
        isCurrentMonth = month == Calendar.getInstance().get(Calendar.MONTH) && year== Calendar.getInstance().get(Calendar.YEAR);

        //下个月
        isNextMonth = month == Calendar.getInstance().get(Calendar.MONTH) + 1 ||((year== Calendar.getInstance().get(Calendar.YEAR)+1) && month==0);
        Log.i("nextmonth", month + "==" + isNextMonth + "==" + distanceDays);
        invalidate();
    }
}
