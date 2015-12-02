package org.jackyonline.cameraandphotodemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Created by Jacky on 2015/12/1.
 */
public class MyTableScheduleView extends ViewGroup {

    public MyTableScheduleView(Context ctx){
        super(ctx);
    }

    public MyTableScheduleView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }

    public MyTableScheduleView(Context ctx, AttributeSet attrs, int defStyleAttr){
        super(ctx,attrs,0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }
}
