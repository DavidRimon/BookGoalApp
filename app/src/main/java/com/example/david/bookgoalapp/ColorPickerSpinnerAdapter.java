package com.example.david.bookgoalapp;

/**
 * Created by David on 07-Jul-17.
 */

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.List;

/**
 * gets a list of color ints
 */
public class ColorPickerSpinnerAdapter extends ArrayAdapter implements SpinnerAdapter{

    private final List<Integer> colors;
    private final int myLayout;
    public ColorPickerSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Integer> colors) {
        super(context, resource, colors);
        this.colors = colors;
        myLayout = resource;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        super.getDropDownView(position,convertView,parent);

        View rowView = convertView;

        if (rowView == null) {
            // Get a new instance of the row layout view
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(myLayout, null);

            rowView.setBackgroundColor(colors.get(position));

        } else {
            rowView.setBackgroundColor(colors.get(position));
        }


        return rowView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;


        if (rowView == null) {
            // Get a new instance of the row layout view
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(myLayout, null);

            rowView.setBackgroundColor(colors.get(position));

        } else {
            rowView.setBackgroundColor(colors.get(position));
        }


        return rowView;
    }
}
