package com.example.david.bookgoalapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.david.bookgoalapp.BookGoalMySQLiteDBDiffinition.BookGoalTableDiffinition.POS_TYPES;
import com.thebluealliance.spectrum.SpectrumDialog;

public class BookGoalViewActivity extends AppCompatActivity {

    public static final String BookGoalId = "BookGoalId";

    private IBackend database = null;
    private int bookGoalId=-1;
    private boolean addingBookGoalFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,0);
        setContentView(R.layout.activity_book_goal_view);
        database = Backend_SQLite.getInstance(this.getApplicationContext());

        //get date from intent, sent by whom ever started this activity
        Intent intent = getIntent();
        int id  = intent.getIntExtra(BookGoalId,-1);
        if(id != -1 ) {// edit book goal

            Pair<Integer,BookGoal> p = database.getBookGoalById(id);
            if(p.first != 0) { //error
                Toast.makeText(getApplicationContext(),getString(R.string.error_while_loading) + getResources().getString(p.first),Toast.LENGTH_SHORT).show();
                //exit
                goBack(null);
                return;
            }
            addingBookGoalFlag = false;
            BookGoal b = p.second;
            this.bookGoalId = id;
            disableEdit();
            initView();
            fiilBookGoalData(b);
        } else { //add bookGoal

            enableEdit();
            initView();
            addingBookGoalFlag = true;

        }

    }
    // ----------------- my functions
    private void enableEdit() {

        //disable edit button
        ((FloatingActionButton) findViewById(R.id.btnEdit)).setEnabled(false);
        //enable save button
        ((FloatingActionButton) findViewById(R.id.btnSave)).setEnabled(true);

        ((EditText)           findViewById(R.id.etxtName))      .setEnabled(true);
        ((EditText)           findViewById(R.id.etxtStartPos))  .setEnabled(true);
        ((EditText)           findViewById(R.id.etxtEndPos))    .setEnabled(true);
        ((EditText)           findViewById(R.id.etxtCurPos))    .setEnabled(true);
        ((Spinner)            findViewById(R.id.spnrPos_Types)) .setEnabled(true);
        ((EditText)           findViewById(R.id.etxtRate))      .setEnabled(true);
        ((EditText)           findViewById(R.id.etxtStartDate)) .setEnabled(true);
        ((TextView)        findViewById(R.id.txtvColorSelected)).setEnabled(true);
        ((EditText)           findViewById(R.id.etxtEveryDayAt)).setEnabled(true);
        ((CheckBox)           findViewById(R.id.chkbxEnabled))  .setEnabled(true);
        ((EditText)           findViewById(R.id.etxtmNote))     .setEnabled(true);
    }
    private void disableEdit() {

        //enable edit button
        ((FloatingActionButton) findViewById(R.id.btnEdit)).setEnabled(true);
        //disable save button
        ((FloatingActionButton) findViewById(R.id.btnSave)).setEnabled(false);

        ((EditText)           findViewById(R.id.etxtName))      .setEnabled(false);
        ((EditText)           findViewById(R.id.etxtStartPos))  .setEnabled(false);
        ((EditText)           findViewById(R.id.etxtEndPos))    .setEnabled(false);
        ((EditText)           findViewById(R.id.etxtCurPos))    .setEnabled(false);
        ((Spinner)            findViewById(R.id.spnrPos_Types)) .setEnabled(false);
        ((EditText)           findViewById(R.id.etxtRate))      .setEnabled(false);
        ((EditText)           findViewById(R.id.etxtStartDate)) .setEnabled(false);
        ((TextView)        findViewById(R.id.txtvColorSelected)).setEnabled(false);
        ((EditText)           findViewById(R.id.etxtEveryDayAt)).setEnabled(false);
        ((CheckBox)           findViewById(R.id.chkbxEnabled))  .setEnabled(false);
        ((EditText)           findViewById(R.id.etxtmNote))     .setEnabled(false);
    }
    private void initView() {
        //fill spinner with general data

        //TODO:: Alert!!! make lis in this order ONLY. else will be a problem with setting the PosType in view
        List<String> lis = new ArrayList<String>();
        for (POS_TYPES p:POS_TYPES.values()) {
            lis.add(p.toString());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,lis);
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.spnrPos_Types)) .setAdapter(dataAdapter);
    }
    private void fiilBookGoalData(BookGoal b) {


        ((EditText)           findViewById(R.id.etxtName))        .setText(b.getName());
        ((TextView)           findViewById(R.id.txtvwName))        .setText(b.getName());
        ((EditText)           findViewById(R.id.etxtStartPos))    .setText(String.valueOf(b.getStart_pos()));
        ((EditText)           findViewById(R.id.etxtEndPos))      .setText(String.valueOf(b.getEnd_pos()));
        ((EditText)           findViewById(R.id.etxtCurPos))      .setText(String.valueOf(b.getCur_pos()));

       //TODO:: find another way to get selected
        int i;
        for(i = 0; i <POS_TYPES.values().length; i++ ) {
            if(b.getPos_type() == POS_TYPES.values()[i])
                break;
        }
        //what if not found? - will just show one of the other values.

        //now set specific data for spinner
        ((Spinner) findViewById(R.id.spnrPos_Types)).setSelection(i);

        ((EditText)           findViewById(R.id.etxtRate))     .setText(String.valueOf(b.getRate()));
        ((EditText)           findViewById(R.id.etxtStartDate)).setText(b.getShortStarting_date());
        ((TextView)           findViewById(R.id.txtvColorSelected)).setBackgroundColor(b.getColor());
        ((EditText)         findViewById(R.id.etxtEveryDayAt)) .setText(b.getAt_time().toString());
        ((CheckBox)           findViewById(R.id.chkbxEnabled)) .setChecked(b.isEnabled());
        ((EditText)           findViewById(R.id.etxtmNote))    .setText(b.getNote());

    }

    /**
     * get a BookGoal object from text boxes. If data is invalid, will retun null.
     * @return
     */
    private BookGoal getBookGoalFromView(){

        BookGoal b = new BookGoal();
        b.setId(this.bookGoalId); //not really needed, but why not? may save us some trouble in the future
        b.setEnabled(((CheckBox)                  findViewById(R.id.chkbxEnabled)).isChecked());
        b.setName(((EditText)                     findViewById(R.id.etxtName)).getText().toString());
        b.setNote(((EditText)                     findViewById(R.id.etxtmNote)).getText().toString());

        //not needed
        //b.setId(Integer.valueOf(((TextView)       findViewById(R.id.txtvID)).getText().toString()));

        b.setRate(Integer.valueOf(((EditText)     findViewById(R.id.etxtRate)).getText().toString()));
        b.setStart_pos(Integer.valueOf(((EditText)findViewById(R.id.etxtStartPos)).getText().toString()));
        b.setEnd_pos(Integer.valueOf(((EditText)  findViewById(R.id.etxtEndPos)).getText().toString()));
        b.setCur_pos(Integer.valueOf(((EditText)  findViewById(R.id.etxtCurPos)).getText().toString()));
        if(b.getEnd_pos() < b.getStart_pos() || b.getCur_pos() < b.getStart_pos() || b.getCur_pos() > b.getEnd_pos()) { //error
            Toast.makeText(getApplicationContext(), R.string.invalid_pos_fields, Toast.LENGTH_SHORT).show();
            return null;
        }
        try {
            b.setPos_type(((Spinner)            findViewById(R.id.spnrPos_Types)).getSelectedItem().toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),getString(R.string.invalid_pos_type) + e.getMessage(),Toast.LENGTH_SHORT).show();
            return null;
        }
        try {
            b.setStating_date(((EditText)           findViewById(R.id.etxtStartDate)).getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),getString(R.string.invalid_date) + e.getMessage(),Toast.LENGTH_SHORT).show();
            return null;
        }
       // ((EditText)           findViewById(R.id.etxtColor))
        try {
            b.setAt_time(new Time(((EditText)  findViewById(R.id.etxtEveryDayAt)).getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),getString(R.string.invalid_time) + e.getMessage(),Toast.LENGTH_SHORT).show();
            return null;
        }

        ColorDrawable cd = (ColorDrawable) ((TextView)  findViewById(R.id.txtvColorSelected)).getBackground();
        b.setColor(cd.getColor());

        return b;
    }
    private boolean isFullWithData() {
        return

                (((EditText)    findViewById(R.id.etxtName))            .getText().length() != 0) &&
                (((EditText)    findViewById(R.id.etxtStartPos))        .getText().length() != 0) &&
                (((EditText)    findViewById(R.id.etxtEndPos))          .getText().length() != 0) &&
                (((EditText)    findViewById(R.id.etxtCurPos))          .getText().length() != 0) &&
                (((EditText)    findViewById(R.id.etxtRate))            .getText().length() != 0) &&
                (((EditText)    findViewById(R.id.etxtStartDate))       .getText().length() != 0) &&
                (((EditText)    findViewById(R.id.etxtEveryDayAt))      .getText().length() != 0) ;

    }
    public void chooseDate(View v) {
        Log.d("tag","messag0");
        new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        Log.d("tag","messag1");
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR,year);
                        c.set(Calendar.MONTH,month);
                        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        DateFormat df = new SimpleDateFormat(BookGoalMySQLiteDBDiffinition.BookGoalTableDiffinition.DATE_FORMAT);
                        ((EditText) findViewById(R.id.etxtStartDate)).setText(df.format(c.getTime()));
                        Log.d("tag","message2");
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                .show();
    }
    public void chooseTime(View v) {

        TimePickerDialog tpd = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                        //make sure to allways show 2-digit number time. (09:00)
                        String textTimeToShow = "";
                        if(hour < 10)
                            textTimeToShow += "0";
                        textTimeToShow += hour;
                        textTimeToShow += ":";
                        if(minute < 10)
                            textTimeToShow += "0";
                        textTimeToShow += minute;

                        ((EditText) findViewById(R.id.etxtEveryDayAt)).setText(textTimeToShow);
                    }
                },
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),
                android.text.format.DateFormat.is24HourFormat(this));
        tpd.show();
    }
    public void chooseColor(View view) {

        SpectrumDialog.Builder bu = new SpectrumDialog.Builder(this);
        bu.setTitle(R.string.choose_color);
        bu.setColors(R.array.myColors);
        ColorDrawable cd = (ColorDrawable) findViewById(R.id.txtvColorSelected).getBackground();
        int color = cd.getColor();
        bu.setSelectedColor(color);
        bu.setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
            @Override
            public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                ((TextView) findViewById(R.id.txtvColorSelected)).setBackgroundColor(color);
            }
        });
        bu.build().show(getSupportFragmentManager(),"tag");

    }

    public void editBookGoal(View v) {
        enableEdit();
    }
    public void SaveBookGoal(View view) {
        try {
            BookGoal b;
            if (!isFullWithData()) {
                Toast.makeText(this, R.string.some_data_flds_still_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            b = getBookGoalFromView();
            if(b == null)  return;

            disableEdit();

            if (addingBookGoalFlag == false) {
                //check that there is data to collect
                int res = database.editBookGoal(b);

                if (0 == res) { //edit successful
                    Toast.makeText(this, R.string.book_edited_sucesfuly, Toast.LENGTH_SHORT).show();
                    //change name heading
                    String newName = ((EditText) findViewById(R.id.etxtName)).getText().toString();
                    ((TextView) findViewById(R.id.txtvwName)).setText(newName);
                } else //error
                    Toast.makeText(this, getString(R.string.error_while_updating) + getString(res), Toast.LENGTH_SHORT).show();

            } else { //if this is adding command
                int res = database.addBookGoal(b);
                if (0 == res) { //adding successful
                    Toast.makeText(getApplicationContext(), R.string.bookgoal_added_successfully, Toast.LENGTH_SHORT).show();
                    //change name heading
                    String newName = ((EditText) findViewById(R.id.etxtName)).getText().toString();
                    ((TextView) findViewById(R.id.txtvwName)).setText(newName);
                    addingBookGoalFlag = false; //its not adding anymore - from here on its editing
                    //get out form activity

                    //if won't use go back, so need to pull it back from the data base to
                    // get the id. otherwise on deletion there will be an error because
                    // bookGoalId wan't load
                    goBack(null);
                } else //error
                    Toast.makeText(this, getString(R.string.error_while_adding) + getString(res), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void deleteBookGoal(View view) {
        //delete and exit view

        int res = database.deleteBookGoal(this.bookGoalId);
        if(0 == res) { //delete successful
            Toast.makeText(getApplicationContext(), R.string.bookgoal_deleted_successfully,Toast.LENGTH_SHORT).show();
            goBack(null);
        }
    }

    /**
     * Go back to previous activity.
     * Watch  out! some other functions may use this function to exit the activity,
     * sending @View as null
     * @param view
     */
    public void goBack(View view) {

        finish();
    }


}
