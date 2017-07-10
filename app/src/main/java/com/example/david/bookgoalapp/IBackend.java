package com.example.david.bookgoalapp;

import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by David on 24-Jun-17.
 */
//TODO: make description for all functions
public interface IBackend{

    //on succssess returns 0, else error code

    /**
     *
     * @param bookGoal
     * @return on success returns 0, else R.string error code
     */
    public int addBookGoal(BookGoal bookGoal);

    /**
     * if Id doesn't exist return error
     * @param bookGoal
     * @return on success returns 0, else R.string error code
     */
    public int editBookGoal(BookGoal bookGoal);

    /**
     * Advance bookGoal's current position by rate, until end position.
     * @param bookGoalId bookGoal id  of bookGoal to channge
     * @return
     */
    public int advanceBookGoalCur_posById(int bookGoalId);

    /**
     * Advance bookGoal's current position to @pos
     * @param bookGoalId
     * @param pos
     * @return
     */
    public int advanceBookGoalToCur_posById(int pos,int bookGoalId);
    /**
     * Go back in @cur_pos of a book goal by @rate of that bookGoal
     * @param bookGoalId id of bookGoal to change
     * @return
     */
    public int undoAdvanceBookGoalCur_posById(int bookGoalId);

    /**
     * Go back in @cur_pos to before given pos
     * @param pos
     * @param bookGoalId
     * @return
     */
    public int undoAdvanceBookGoalToCur_posById(int pos,int bookGoalId);
    /**
     *
     * @param bookGoalId
     * @return on success returns 0, else R.string error code
     */
    public int deleteBookGoal(int bookGoalId);
    //on succssess Lest is 0 and R is data, else , first part is error code
    public Pair<Integer,BookGoal> getBookGoalById(int bookGoalId);
    public Pair<Integer,BookGoal> getBookGoalByName(String name);
    public Pair<Integer,ArrayList<BookGoal>> getAllBookGoals();
    public Pair<Integer,ArrayList<BookGoal>> getAllEnabledBookGoals();
}
