package org.jackyonline.cameraandphotodemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {
    private TableLayout mTlTable,mTlTableTime,mTlTableRoom;
    private float mx, my;
    private float curX, curY;
    private VScroll mSlLeft;
    private HScroll mSlTop;

    private ScrollView vScroll;
    private HorizontalScrollView hScroll;
    private String[] mTimes = {"08:00 AM","08:30","09:00","09:30","10:00","10:30","11:00","11:30",
                                "12:00 PM", "12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00",
                                "17:30","18:00","18:30","19:00","19:30","20:00","20:30","21:00"};
    private String[] mRooms = {
            "一层209A","一层209B","一层209C", "二层209A","二层209B","二层209C","三层209A","三层209B","三层209C","四层209A","四层209B","四层209C"
    };
    private List<String> mTimeList;
    private List<String> mRoomsList;
    private int mTableWidth;
    private int mTableHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        mSlLeft = (VScroll)findViewById(R.id.sv_left);
        mSlTop = (HScroll)findViewById(R.id.sv_top);

        mTlTable = (TableLayout) findViewById(R.id.tl_table);
        mTlTableTime =  (TableLayout) findViewById(R.id.tl_table_time);
        mTlTableRoom =  (TableLayout) findViewById(R.id.tl_table_room);

        mTlTable.setStretchAllColumns(true);
        mTlTable.setDividerDrawable(getResources().getDrawable(R.drawable.black));
        vScroll = (ScrollView) findViewById(R.id.vScroll);
        hScroll = (HorizontalScrollView) findViewById(R.id.hScroll);

        mTimeList = new ArrayList<String>();
        mRoomsList = new ArrayList<String>();

        for(int i=0; i<mTimes.length; i++) {
            mTimeList.add(mTimes[i]);
        }

        for(int i=0; i<mRooms.length; i++) {
            mRoomsList.add(mRooms[i]);
        }

        BuildTableTime(mTlTableTime, mTimes.length, 1, mTimeList);
        BuildTableRoom(mTlTableRoom,1,mRooms.length,mRoomsList);
        BuildTable(mTlTable,mTimes.length,mRooms.length);
    }

    private void BuildTable(TableLayout tableLayout, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            TableRow row = new TableRow(this);
            TableLayout.LayoutParams params =  new TableLayout.LayoutParams(160, 100);
            for (int j = 1; j < cols; j++) {
                TextView tv = new TextView(this);
                tv.setLayoutParams(new TableRow.LayoutParams(160, 100));
                tv.setBackgroundResource(android.R.color.white);
                tv.setTextSize(12f);
                row.addView(tv);

                if(i==3 && j == 9) {

                }
            }
            tableLayout.addView(row,params);
        }
    }

    private void BuildTableTime(TableLayout tableLayout, int rows, int cols, List<String> times) {
        for (int i = 0; i < times.size(); i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            row.setBackgroundColor(getResources().getColor(R.color.white));
            for (int j = 0; j < cols; j++) {
                TextView tv = new TextView(this);
                tv.setLayoutParams(new TableRow.LayoutParams(160, 100));
                tv.setBackgroundResource(android.R.color.white);
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(12f);
                tv.setText(times.get(i));
                row.addView(tv);
            }
            tableLayout.addView(row);
        }
    }

    private void BuildTableRoom(TableLayout tableLayout, int rows, int cols, List<String> rooms) {
        for (int i = 0; i < rows; i++) {
            TableRow row = new TableRow(this);
            TableLayout.LayoutParams params =  new TableLayout.LayoutParams(160, 100);
            row.setLayoutParams(params);
            row.setBackgroundColor(getResources().getColor(R.color.white));
            for (int j = 0; j < rooms.size(); j++) {
                TextView tv = new TextView(this);
                tv.setLayoutParams(new TableRow.LayoutParams(160, 100));
                tv.setBackgroundResource(android.R.color.white);
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(12f);
                tv.setText(rooms.get(j));
                row.addView(tv);
            }
            tableLayout.addView(row, params);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float curX, curY;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mx = event.getX();
                my = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                curX = event.getX();
                curY = event.getY();
                vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                mSlLeft.scrollBy((int) (mx - curX), (int) (my - curY));
                mSlTop.scrollBy((int) (mx - curX), (int) (my - curY));
                mx = curX;
                my = curY;
                break;
            case MotionEvent.ACTION_UP:
                curX = event.getX();
                curY = event.getY();
                vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                mSlLeft.scrollBy((int) (mx - curX), (int) (my - curY));
                mSlTop.scrollBy((int) (mx - curX), (int) (my - curY));
                break;
        }
        return true;
    }


}
