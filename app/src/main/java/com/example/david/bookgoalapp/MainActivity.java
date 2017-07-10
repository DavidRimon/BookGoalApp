package com.example.david.bookgoalapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.david.bookgoalapp.BookGoalMySQLiteDBDiffinition.BookGoalTableDiffinition.POS_TYPES;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import static com.example.david.bookgoalapp.R.id.fab;

public class MainActivity extends AppCompatActivity implements MainActivityBookGoalSummaryFragment.OnIsDoneChangedListener {

    //TODO: should make backendOnRam and that will work with the SQLBackend, so all activities canrefrens that ramDB instead of the DB been here in MainActivvity
    private IBackend database;
    private ArrayList<BookGoal> bookGoals;
    /**
     * Array list for a list of bookgoals idxs (in @this.bookGoals element) for each day in the month.
     * Index zero shouldn't be used! because this array is 1 indexed (like days in the month)
     */
    private ArrayList<ArrayList<Integer>> bookGoalsIdxsForThisMonth;
    private CaldroidFragment cl;
    private Calendar curDate = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        database = Backend_SQLite.getInstance(this.getApplicationContext());
        try {
            Pair<Integer, ArrayList<BookGoal>> p = database.getAllEnabledBookGoals();
            if(p.first != 0) {
                Toast.makeText(getApplicationContext(),"Couldn't load data: " + getResources().getString(p.first),Toast.LENGTH_LONG).show();
            }
            else this.bookGoals = p.second;
        } catch (Exception e) {
            e.printStackTrace();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),BookGoalViewActivity.class);
                startActivity(intent);
            }
        });


        cl = new CaldroidFragment();

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE,true);
        args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL,true);
        cl.setArguments(args);
        //TODO: set on moth changed
        //TODO: set on day selected, show list of bok goals for that day.
                //it will be a lisft of fragments, each one will get id, IsDone, and textToShow params.
                //when IsDone over there will be checked, it will singnal this activity to advance cur_pos in bookGoals list, and in the data base.
        cl.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
               //TODO:: show fragments list
                //TODO:: set selected date with backround
                Calendar day = Calendar.getInstance();
                day.setTime(date);
                ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.holo_blue_dark));
                cl.setBackgroundDrawableForDate(cd,day.getTime());
                //Month is 0 indexed
                if(day.get(Calendar.MONTH) + 1 > cl.getMonth()) { // if the day selected is in the next month
                    cl.nextMonth();
                    fillDaySummary(day);
                }
                else if(day.get(Calendar.MONTH) + 1 < cl.getMonth()) {// if the day selected is in the prev month
                    cl.prevMonth();
                    fillDaySummary(day);
                }
                else
                    fillDaySummary(day);
            }

            @Override
            public void onChangeMonth(int month, int year) {
                //TODO:: recalc
                CalcBookGoalsIdxsForThisMonth(month,year);
            }
        });

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendarView2, cl);
        t.commit();

    }

    /**
     * Reload all date
     * TODO:: if we have onlineRAM data base, this is not needed...
     */
    public void reloadData() {
        try {
            Pair<Integer, ArrayList<BookGoal>> p = database.getAllEnabledBookGoals();
            if(p.first != 0) {
                Toast.makeText(getApplicationContext(),"Couldn't load data: " + getResources().getString(p.first),Toast.LENGTH_LONG).show();
            }
            else this.bookGoals = p.second;
        } catch (Exception e) {
            e.printStackTrace();
        }
        CalcBookGoalsIdxsForThisMonth(cl.getMonth(),cl.getYear());
    }
    @Override
    public void onResume() {
        super.onResume();
        //TODO: is this needed?? its for safety, but if we have onlineRAM synchrinized backend, this is not needed.
        reloadData();
        fillDaySummary(curDate);

    }
    /**
     * fill bookGoalsIdxs for selected month in the calander.
     * TODO: Also filles it with drawables????
     *
     * @param month
     * @param year
     */
    private void CalcBookGoalsIdxsForThisMonth(int month,int year) {
        //TODO: also generate Drawables for Caldroid calander
        //TODO: remember bookGolasIdxs is 1 indexed and not 0-indexed!!!
        //TODO fix error that in next month isnot working very well, has something to do with Calander.Month is 0- indexed

        Calendar monthStart = Calendar.getInstance(),
                 monthEnd   = Calendar.getInstance();
        monthStart.set(Calendar.DAY_OF_MONTH,1);
        int monthLength = monthStart.getActualMaximum(Calendar.DAY_OF_MONTH);
        monthEnd.set(Calendar.DAY_OF_MONTH,monthLength);


        bookGoalsIdxsForThisMonth = new ArrayList<ArrayList<Integer>>();
        //init var
        for(int i = 0; i <= monthStart.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            bookGoalsIdxsForThisMonth.add(new ArrayList<Integer>());
        }


        //check for each book goal 6 cases, so you know what idx are in this month.
        for(int bookIdx = 0; bookIdx< bookGoals.size(); bookIdx++) {
            BookGoal b = bookGoals.get(bookIdx);

            if(b.getStarting_date().before(monthStart)) {
                if(b.getEndingDate().before(monthStart)){
                    //do nothing....
                } else if(b.getEndingDate().after(monthEnd)){
                    //then this whole month is filled with this book goal
                    for(int day = 1; day <= monthLength; day++)
                        bookGoalsIdxsForThisMonth.get(day).add(bookIdx); //add this bookGOal index to that day
                } else { //endDate is inside this month
                    for(int day = 1; day <= b.getEndingDate().get(Calendar.DAY_OF_MONTH); day++)
                        bookGoalsIdxsForThisMonth.get(day).add(bookIdx); //add this bookGOal index to that day
                }
            } else if(b.getStarting_date().after(monthEnd)) {
                //do nothing...
            } else { //if starting date is inside month
                if(b.getEndingDate().after(monthEnd)) {
                    //add to all days statring at starting date
                    for(int day = b.getStarting_date().get(Calendar.DAY_OF_MONTH); day <= monthLength; day++)
                        bookGoalsIdxsForThisMonth.get(day).add(bookIdx);
                } else if(b.getEndingDate().before(monthStart)) {
                    //TODO: throw error or not???
                } else {//if end date is also in mont
                    //add days form start date to end date
                    for(int day = b.getStarting_date().get(Calendar.DAY_OF_MONTH); day <= b.getEndingDate().get(Calendar.DAY_OF_MONTH); day++)
                        bookGoalsIdxsForThisMonth.get(day).add(bookIdx);
                }
            }

        }
        int a = 5+5;
        a *=3;
    }

    /**
     * Function to be used by fragmnets, when done checkbox over there is checked
     * TODO:: make revert for this
     * @param id
     */
    private void advanceCur_posForBookGoalId(int id) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(CompoundButton checkBox, boolean isDone, int bookGoalId, int pos) {

        //TODO:: what to do if error happens???
        if(isDone) {
            //advance bookGoalId cur pos
            int res = database.advanceBookGoalToCur_posById(pos,bookGoalId);
            if(res != 0) { //error occurred - revert
                checkBox.setChecked(false);
                //TODO: bigger mmessgae??
                Toast.makeText(getApplicationContext(),getResources().getString(res),Toast.LENGTH_SHORT).show();
                return;
            }
            //TODO::should reload all instead??
            //find the book goal with the id and advance it
            for(BookGoal b: this.bookGoals) {
                if(b.getId() == bookGoalId) {
                    b.setCur_pos(pos);
                    break;
                }
            }
        } else { //revert
            int res = database.undoAdvanceBookGoalToCur_posById(pos,bookGoalId);
            if(res != 0) {//error - revert
                checkBox.setChecked(true);
                Toast.makeText(getApplicationContext(),getResources().getString(res),Toast.LENGTH_SHORT).show();
                return;
            }
            //TODO::should reload all instead??
            //find the book goal with the id and advance it
            for(BookGoal b: this.bookGoals) {
                if(b.getId() == bookGoalId) {
                    b.setCur_pos(pos - b.getRate());
                    break;
                }
            }
        }
    }

    public void fillDaySummary(Calendar day) {
        //first empty what is there already
        ((LinearLayout) findViewById(R.id.lnlyDaySummary)).removeAllViews();
        if(day == null)return;

        //get all bookGoals from bookGoalsIdxsForThisMonth
        ArrayList<Integer> idxs = this.bookGoalsIdxsForThisMonth.get(day.get(Calendar.DAY_OF_MONTH));
        //gen fragments for all idxs
        for(int i: idxs) {
            BookGoal b = this.bookGoals.get(i);
            MainActivityBookGoalSummaryFragment fragment  =  MainActivityBookGoalSummaryFragment.newInstance(
                                    b.getId(),b.getCur_posForDate(day),b.getColor(),b.getStringSummaryForDay(day),b.isDone(day));
            getSupportFragmentManager().beginTransaction().add(R.id.lnlyDaySummary,fragment).commit();
        }



    }

}
