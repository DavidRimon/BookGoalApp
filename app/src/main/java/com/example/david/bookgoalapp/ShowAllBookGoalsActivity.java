package com.example.david.bookgoalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ConfigurationHelper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowAllBookGoalsActivity extends AppCompatActivity implements MainActivityBookGoalSummaryFragment.OnIsDoneChangedListener {

    private IBackend database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_book_goals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),BookGoalViewActivity.class);
                startActivity(intent);
            }
        });

        database = Backend_SQLite.getInstance(getApplicationContext());
        //add to Linear Layout child layout all bookGoals in data base
        boolean enables =  ((CheckBox) this.findViewById(R.id.cbEnableds)).isChecked(),
                disables = ((CheckBox) this.findViewById(R.id.cbDisableds)).isChecked();

        final CheckBox cb1 = (CheckBox) findViewById(R.id.cbEnableds),
                 cb2 = (CheckBox) findViewById(R.id.cbDisableds);

        CompoundButton.OnCheckedChangeListener ocl = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateLayout(cb1.isChecked(),cb2.isChecked());
            }
        };
        cb1.setOnCheckedChangeListener(ocl);
        cb2.setOnCheckedChangeListener(ocl);


        updateLayout(enables,disables);
    }


    private void updateLayout(boolean showEnableds, boolean showDisableds) {

        //TODO:: do in sperate thread?

        Pair<Integer,ArrayList<BookGoal>> p;
        if(showEnableds && showDisableds)
            p = database.getAllBookGoals();
        else if(showEnableds)
            p = database.getAllEnabledBookGoals();
        else if(showDisableds)
            p = database.getAllDisabledBookGoals();
        else {
            emptyLayout();
            return;
        }

        if(p.first !=0 ) { //error
            emptyLayout();
            Toast.makeText(this,getResources().getString(R.string.error_while_loading) + getResources().getString(p.first),Toast.LENGTH_SHORT).show();
            return;
        }

        fillLayout(p.second);
    }

    private void fillLayout(ArrayList<BookGoal> bookGoals) {
        ((LinearLayout) findViewById(R.id.lnlyShowAll)).removeAllViews();

        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        for(BookGoal b: bookGoals) {
            MainActivityBookGoalSummaryFragment fragment  =  MainActivityBookGoalSummaryFragment.newInstance(
                    b.getId(),b.getCur_pos(),b.getColor(),b.getName(),b.isEnabled());
            LinearLayout lnlyShowAll  = (LinearLayout) findViewById(R.id.lnlyShowAll);
            fragmentTransaction.add(R.id.lnlyShowAll,fragment);
        }
        fragmentTransaction.commit();
    }

    private void emptyLayout() {

        ((LinearLayout)this.findViewById(R.id.lnlyShowAll)).removeAllViews();
    }

    /**
     *
     * @param checkBox
     * @param isEnabled is enabled. TODO:: change this to something that does make sence and specific for this case!! cus' the source is 'isDone'
     * @param bookGoalId
     * @param pos cur_pos of sending fragment
     */
    @Override
    public void onFragmentInteraction(CompoundButton checkBox, boolean isEnabled, int bookGoalId, int pos) {

        //update in data base
        int res = database.fastSetEnabledById(bookGoalId,isEnabled);
        if (res != 0) {
            //error occurred
            Toast.makeText(this,res,Toast.LENGTH_SHORT).show();
        }
        //TODO:: update in view ?

    }
}
