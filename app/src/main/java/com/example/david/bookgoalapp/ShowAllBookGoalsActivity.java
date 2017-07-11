package com.example.david.bookgoalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ConfigurationHelper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowAllBookGoalsActivity extends AppCompatActivity {

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
        boolean enableds =  ((CheckBox) this.findViewById(R.id.cbEnableds)).isChecked(),
                disableds = ((CheckBox) this.findViewById(R.id.cbDisableds)).isChecked();

        updateLayout(enableds,disableds);
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
            Toast.makeText(this,"Error while loading: " + getResources().getString(p.first),Toast.LENGTH_SHORT).show();
            return;
        }

        fillLayout(p.second);


    }

    private void fillLayout(ArrayList<BookGoal> bookGoals) {
        for(BookGoal b: bookGoals) {
            MainActivityBookGoalSummaryFragment fragment  =  MainActivityBookGoalSummaryFragment.newInstance(
                    b.getId(),b.getCur_pos(),b.getColor(),b.toString(),b.isEnabled());
            getSupportFragmentManager().beginTransaction().add(R.id.lnlyShowAll,fragment).commit();
        }
    }

    private void emptyLayout() {

        ((LinearLayout)this.findViewById(R.id.lnlyShowAll)).removeAllViews();
    }
}
