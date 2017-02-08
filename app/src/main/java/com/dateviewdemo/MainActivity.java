package com.dateviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dateviewdemo.Util.DateUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements DateV.OnClickItemListener{

    ImageView btn_pre;
    ImageView btn_next;
    TextView txt_date;
    DateV dateV;
    int currentMonth;//当前月份
    int currentYear;//当前年
    int showMonth;//显示的月份
    int showYear;//显示的年
    int date;//当天
    Map<Integer, Integer> yearAndmonth = new HashMap<>();
    int currentMonthDays = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_pre= (ImageView) findViewById(R.id.btn_pre);
        btn_next= (ImageView) findViewById(R.id.btn_next);
        dateV= (DateV) findViewById(R.id.date);
        txt_date= (TextView) findViewById(R.id.txt_date);
        try {
            setListener();
            initData();
            currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
            currentYear = Calendar.getInstance().get(Calendar.YEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setListener() {
        txt_date = (TextView) findViewById(R.id.txt_date);
        dateV = (DateV) findViewById(R.id.date);
        dateV.setOnClickItemListener(this);
        btn_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dateV.nextPage();
                if (showMonth == 12) {
                    showMonth = 1;
                    showYear++;
                } else {
                    showMonth++;
                }
                String monthString = "";
                if (showMonth < 10) {
                    monthString = "0" + showMonth;
                } else {
                    monthString = showMonth + "";
                }
                txt_date.setText(showYear + "年" + monthString + "月");
            }

        });
        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dateV.prePage();
                if (showMonth == 1) {
                    showMonth = 12;
                    showYear--;
                } else {
                    showMonth--;
                }
                String monthString = "";
                if (showMonth < 10) {
                    monthString = "0" + showMonth;
                } else {
                    monthString = showMonth + "";
                }
                txt_date.setText(showYear + "年" + monthString + "月");
            }
        });
    }
    public void initData() {
        currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        date = Calendar.getInstance().get(Calendar.DATE);
        showMonth = currentMonth;
        showYear = currentYear;
        currentMonthDays = getCurrentMonthDays(currentYear, currentMonth);
        yearAndmonth.put(currentYear, currentMonth);
        String monthString = "";
        if (currentMonth < 10) {
            monthString = "0" + currentMonth;
        } else {
            monthString = currentMonth + "";
        }
        txt_date.setText(currentYear + "年" + monthString + "月");

    }

    //返回某年某月的天数
    public int getCurrentMonthDays(int year, int month) {

        int[] arr = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (year % 4 == 0 && (year % 100 != 0) || (year % 400 == 0)) {
            arr[1] = 29;
        }
        return arr[month-1];
    }

    @Override
    public void onClickItem(int day) {
        Toast.makeText(this,day+"",Toast.LENGTH_SHORT).show();
    }
}
