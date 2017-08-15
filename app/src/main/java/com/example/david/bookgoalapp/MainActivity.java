package com.example.david.bookgoalapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.david.bookgoalapp.BookGoalMySQLiteDBDiffinition.BookGoalTableDiffinition.POS_TYPES;
import com.github.clans.fab.FloatingActionMenu;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.roomorama.caldroid.CaldroidListener;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import static com.example.david.bookgoalapp.R.id.fab;
import static com.example.david.bookgoalapp.R.id.weekday_gridview;

public class MainActivity extends AppCompatActivity implements MainActivityBookGoalSummaryFragment.OnIsDoneChangedListener {

    //TODO: should make backendOnRam and that will work with the SQLBackend, so all activities canrefrens that ramDB instead of the DB been here in MainActivvity
    private IBackend database;
    //TODO:: all of book goal data here is not really needed, because BookGoalActivityView loads it anywas from SQL db
    private ArrayList<BookGoal> bookGoals;

    /**
     * Array list for a list of bookgoals idxs (in @this.bookGoals element) for each day in the month.
     * Index zero shouldn't be used! because this array is 1 indexed (like days in the month)
     */
    private ArrayList<ArrayList<Integer>> bookGoalsIdxsForThisMonth;
    private CaldroidFragment cl;
    /**
     * currently selected date
     */
    private Calendar curDate = null;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO::: make custom text for caldroid. has todo with, caldroid grid adapter, look for cutome text on caldroid online.
        //TODO:: make activity to show all book goals
        //TODO:: make activity for predefined bookGoals
        //TODO:: make floating menu work with api 19
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        updateLanguage();

        loadData();

        setUpFloatingMenu();

        setUpCaldroid();

        setUpNotification();

    }

    private void updateLanguage() {
        Locale locale = new Locale(MyApplication.mlang);
        Locale.setDefault(locale);
        Configuration configuration = getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            configuration.setLocale(locale);
        else configuration.locale = locale;

        getBaseContext().getResources().updateConfiguration(configuration,
                                        getBaseContext().getResources().getDisplayMetrics());
    }

    private void loadData() {
        database = Backend_SQLite.getInstance(this.getApplicationContext());
        try {
            Pair<Integer, ArrayList<BookGoal>> p = database.getAllEnabledBookGoals();
            if(p.first != 0) {
                Toast.makeText(getApplicationContext(),getString(R.string.couldnt_load_data) + getResources().getString(p.first),Toast.LENGTH_LONG).show();
            }
            else this.bookGoals = p.second;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setUpFloatingMenu() {

        MyFloatingActionMenu fabm = (MyFloatingActionMenu) findViewById(R.id.fabmMenu);

        com.github.clans.fab.FloatingActionButton fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),BookGoalViewActivity.class);
                startActivity(intent);
            }
        });
        fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabShowAll);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ShowAllBookGoalsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * set up the caldroid calander fragment
     */
    private void setUpCaldroid() {
        cl = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1); //caldroid uses 1-indexed month number
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

                //clear prev date selection
                ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.caldroid_holo_blue_dark));
                if(curDate !=null)
                    cl.clearBackgroundDrawableForDate(curDate.getTime());

                //set new day selection
                cd.setColor(getResources().getColor(R.color.caldroid_holo_blue_light));
                cl.setBackgroundDrawableForDate(cd,date);
                if(curDate == null)
                    curDate = Calendar.getInstance();

                curDate.setTime(date);


                //Month is 0 indexed
                if(curDate.get(Calendar.MONTH) + 1 > cl.getMonth()) { // if the day selected is in the next month
                    cl.nextMonth();
                    fillDaySummary(curDate);
                }
                else if(curDate.get(Calendar.MONTH) + 1 < cl.getMonth()) {// if the day selected is in the prev month
                    cl.prevMonth();
                    fillDaySummary(curDate);
                }
                else
                    fillDaySummary(curDate);

                cl.refreshView();
            }

            @Override
            public void onChangeMonth(int month, int year) {
                //TODO:: recalc
                //TODO:: explain why 1 helps here / fix it: because month hre given is the prev month, that we are moving FROM?
                CalcBookGoalsIdxsForThisMonth(month-1,year);
                //clear day summary view - if needed it will be filled up again.
                fillDaySummary(null);
            }
        });
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendarView2, cl);
        t.commit();
    }
    /**
     * set up the notification for the app. Must be launch after caldroid is set for this month! - only once at app startup!
     */
    private void setUpNotification() {
        Intent resultIntent = new Intent(this,MainActivity.class);
        PendingIntent  resultPendingIntent = PendingIntent.getActivity(this,0,
                resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        String shortSummary = "", longSummary = "";
        Calendar today = Calendar.getInstance();
        CalcBookGoalsIdxsForThisMonth(today.get(Calendar.MONTH),today.get(Calendar.YEAR));
        //get today's bookGoals
        ArrayList<Integer> bookGoalsIdxs = bookGoalsIdxsForThisMonth.get(today.get(Calendar.DAY_OF_MONTH));
        for(int idx : bookGoalsIdxs) {
            shortSummary += bookGoals.get(idx).getName() + " ,";
            longSummary  += bookGoals.get(idx).getStringSummaryForDay(today) +"\n";
        }
        //TODO:: this shortSummary.toCharArray()[shortSummary.length() - 1 ] = '.';
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_stat_name);

//TODO:: make a string from resource
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                                                .setSmallIcon(R.drawable.ic_stat_name3)
                                                .setColor(getResources().getColor(R.color.caldroid_holo_blue_light))
                                                .setContentTitle(getString(R.string.learn_today))
                                                .setContentText(shortSummary)
                                                .setStyle(new NotificationCompat.BigTextStyle().bigText(longSummary))
                                                .setContentIntent(resultPendingIntent);
        NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mgr.notify(001,mBuilder.build());
    }

    /**
     * Reload all date
     * TODO:: if we have onlineRAM data base, this is not needed...
     */
    public void reloadData() {
        try {
            Pair<Integer, ArrayList<BookGoal>> p = database.getAllEnabledBookGoals();
            if(p.first != 0)
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.couldnt_load_data) + getResources().getString(p.first), Toast.LENGTH_LONG).show();
            else this.bookGoals = p.second;
        } catch (Exception e) {
            e.printStackTrace();
        }
        CalcBookGoalsIdxsForThisMonth(cl.getMonth() - 1,cl.getYear()); // CalcBooks uses 0-indexed month number
    }
    @Override
    public void onResume() {
        super.onResume();
        //TODO: is this needed?? its for safety, but if we have onlineRAM synchrinized backend, this is not needed.
        reloadData();
        fillDaySummary(curDate);

    }

    /**
     * Make floating action menu close when tuched outside of its area
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent (MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            com.github.clans.fab.FloatingActionMenu fabm = (FloatingActionMenu) findViewById(R.id.fabmMenu);
            if(fabm.isOpened()) {
                Rect outRect = new Rect();
                fabm.getGlobalVisibleRect(outRect);

                if(!outRect.contains((int)event.getRawX(),(int)event.getRawY()))
                    fabm.close(true);
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * fill bookGoalsIdxs for selected month in the calander.
     * TODO: Also filles it with drawables????
     *
     * @param month 0-indexed month number
     * @param year
     */
    private void CalcBookGoalsIdxsForThisMonth(final int month,int year) {
        //TODO: also generate Drawables for Caldroid calander
        //TODO: remember bookGolasIdxs is 1 indexed and not 0-indexed!!!
        //TODO fix error that in next month isnot working very well, has something to do with Calander.Month is 0- indexed

        Calendar monthStart = Calendar.getInstance(),
                 monthEnd   = Calendar.getInstance();
        monthStart.set(Calendar.DAY_OF_MONTH,1);
        monthStart.set(Calendar.MONTH,month);
        monthStart.set(Calendar.YEAR,year);

        int monthLength = monthStart.getActualMaximum(Calendar.DAY_OF_MONTH);
        monthEnd.set(Calendar.DAY_OF_MONTH,monthLength);
        monthEnd.set(Calendar.MONTH,month);
        monthEnd.set(Calendar.YEAR,year);


        bookGoalsIdxsForThisMonth = new ArrayList<ArrayList<Integer>>();
        //init var
        for(int i = 0; i <= monthStart.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            bookGoalsIdxsForThisMonth.add(new ArrayList<Integer>());
        }


        //check for each book goal 6 cases, so you know what idx are in this month.
        for(int bookIdx = 0; bookIdx< bookGoals.size(); bookIdx++) {
            BookGoal b = bookGoals.get(bookIdx);

            //TODO:: what about date times?? do they matter here???
            if(b.getStarting_date().before(monthStart)) {
                if(b.getEndingDate().before(monthStart)){
                    //do nothing....
                } else if(b.getEndingDate().after(monthEnd)){
                    //then this whole month is filled with this book goal
                    for(int day = 1; day <= monthLength; day++) {
                        bookGoalsIdxsForThisMonth.get(day).add(bookIdx); //add this bookGOal index to that day
                    }
                } else { //endDate is inside this month
                    for(int day = 1; day <= b.getEndingDate().get(Calendar.DAY_OF_MONTH); day++)
                        bookGoalsIdxsForThisMonth.get(day).add(bookIdx); //add this bookGOal index to that day
                }
            } else if(b.getStarting_date().after(monthEnd)) {
                //do nothing...
            } else if(b.getStarting_date().after(monthStart)){
                if( b.getStarting_date().after(monthEnd)) {
                    //do nothing....
                }
                else if(b.getEndingDate().after(monthEnd)) {//if starting date is inside month
                    //add to all days statring at starting date
                    for(int day = b.getStarting_date().get(Calendar.DAY_OF_MONTH); day <= monthLength; day++)
                        bookGoalsIdxsForThisMonth.get(day).add(bookIdx);
                } else if(b.getEndingDate().before(monthStart)) {
                    //TODO: throw error or not???
                } else {//if end date is also in month - b.getEndingDate < monthEnd
                    //add days form start date to end date/יעש
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

        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        for(int i: idxs) {
            BookGoal b = this.bookGoals.get(i);
            //TODO: reuse fragments instead of creating new ones to speed up the app
            MainActivityBookGoalSummaryFragment fragment  =  MainActivityBookGoalSummaryFragment.newInstance(
                                    b.getId(),b.getCur_posForDate(day),b.getColor(),b.getStringSummaryForDay(day),b.isDone(day));
            fragmentTransaction.add(R.id.lnlyDaySummary,fragment);
        }
        fragmentTransaction.commit();
    }

}
