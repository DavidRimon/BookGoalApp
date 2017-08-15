package com.example.david.bookgoalapp;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by David on 15-Aug-17.
 */

public class MyFloatingActionMenu extends com.github.clans.fab.FloatingActionMenu {

    public static final int LABELS_POSITION_LEFT = 0;
    public static final int LABELS_POSITION_RIGHT = 1;

    private int mLabelsPosition;
    public int getLabelsPosition() {
        return mLabelsPosition;
    }

    /**
     *
     * @param pos LABELS_POSITION_LEFT or LABELS_POSITION_RIGHT
     */
    public void setLabelsPosition(int pos) {
        mLabelsPosition = pos;
    }
    public MyFloatingActionMenu(Context context) {
        super(context);

    }

    public MyFloatingActionMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFloatingActionMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



}
