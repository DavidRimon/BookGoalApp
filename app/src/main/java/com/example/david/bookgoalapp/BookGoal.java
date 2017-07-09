package com.example.david.bookgoalapp;

import android.graphics.Color;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.example.david.bookgoalapp.BookGoalMySQLiteDBDiffinition.BookGoalTableDiffinition.POS_TYPES;
import java.util.Date;

import static com.example.david.bookgoalapp.BookGoalMySQLiteDBDiffinition.BookGoalTableDiffinition.POS_TYPES.AMOD;
import static com.example.david.bookgoalapp.BookGoalMySQLiteDBDiffinition.BookGoalTableDiffinition.POS_TYPES.DAF;
import static com.example.david.bookgoalapp.BookGoalMySQLiteDBDiffinition.BookGoalTableDiffinition.POS_TYPES.PAGE;
import static com.example.david.bookgoalapp.BookGoalMySQLiteDBDiffinition.BookGoalTableDiffinition.POS_TYPES.PEREK;

/**
 * Created by David on 24-Jun-17.
 */

public class BookGoal {
    private int id;
    private String name;
    private int start_pos,
            end_pos,
            cur_pos,
            rate;
    private POS_TYPES pos_type;
    private Calendar starting_date;

    /**
     * needed for some calculations in main activity. not a real value of the object
     */
    private Calendar endingDate = null;
    private  Time at_time; //at time is not a date, but a time class
    private int color;
    private boolean enabled;
    private String note;


    public BookGoal() {}

    /**
     *
     * @param Name
     * @param Start_pos
     * @param End_pos
     * @param Cur_pos
     * @param Rate
     * @param Pos_type
     * @param Starting_date
     * @param At_time
     * @param COlor
     */
    public BookGoal(String Name, int Start_pos, int End_pos, int Cur_pos,
                    int Rate, POS_TYPES Pos_type, Calendar Starting_date,
                    Time At_time, int COlor, boolean Enabled, String Note) {
        this.name = Name;
        this.start_pos = Start_pos;
        this.end_pos = End_pos;
        this.cur_pos = Cur_pos;
        this.rate = Rate;
        this.pos_type = Pos_type;
        this.starting_date = (Calendar) Starting_date.clone();
        this.at_time = new Time(At_time);
        this.color = COlor;
        this.enabled = Enabled;
        this.note = Note;
    }

    /**
     *
     * @param bookGoal
     */
    public BookGoal (BookGoal bookGoal){
        this(bookGoal.name,bookGoal.start_pos,
                bookGoal.end_pos,bookGoal.cur_pos,
                bookGoal.rate,bookGoal.pos_type,
                bookGoal.starting_date,bookGoal.at_time,
                bookGoal.color,bookGoal.isEnabled(),bookGoal.getNote());
        this.id = bookGoal.getId();
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof BookGoal)) return false;
        BookGoal b = (BookGoal) o;
        if(
                this.id == b.id &&
                this.name == b.name &&
                this.start_pos == b.start_pos &&
                this.end_pos == b.end_pos &&
                this.cur_pos == b.cur_pos &&
                this.rate == b.rate &&
                this.pos_type == b.pos_type &&
                this.starting_date.equals(b.starting_date) &&
                this.at_time.equals(b.at_time) &&
                this.color == b.color &&
                this.isEnabled() == b.isEnabled() &&
                this.note == b.getNote()
                )
            return true;
        else return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStart_pos() {
        return start_pos;
    }

    public void setStart_pos(int start_pos) {
        this.start_pos = start_pos;
    }

    public int getEnd_pos() {
        return end_pos;
    }

    public void setEnd_pos(int end_pos) {
        this.end_pos = end_pos;
    }

    public int getCur_pos() {
        return cur_pos;
    }
    private String intToHebLetters(int num){ //gimatrya
        //get thousands
        if(num >=1000) return intToHebLetters(num % 1000) + "'" + intToHebLetters(num / 1000);
        //bellow 1000
        if      (num >= 400) return  "ת" + intToHebLetters(num - 400);
        else if (num >= 300) return  "ש" + intToHebLetters(num - 300);
        else if (num >= 200) return  "ר" + intToHebLetters(num - 200);
        else if (num >= 100) return  "ק" + intToHebLetters(num - 100);
        else if (num >= 90)  return  "צ" + intToHebLetters(num -  90);
        else if (num >= 80)  return  "פ" + intToHebLetters(num -  80);
        else if (num >= 70)  return  "ע" + intToHebLetters(num -  70);
        else if (num >= 60)  return  "ס" + intToHebLetters(num -  60);
        else if (num >= 50)  return  "נ" + intToHebLetters(num -  50);
        else if (num >= 40)  return  "מ" + intToHebLetters(num -  40);
        else if (num >= 30)  return  "ל" + intToHebLetters(num -  30);
        else if (num >= 20)  return  "כ" + intToHebLetters(num -  20);
        else if (num == 16)  return "טו";
        else if (num == 15)  return "טז";
        else if (num >= 10)  return  "י" + intToHebLetters(num -  10);
        switch (num) {
            case 9: return "ט";
            case 8: return "ח";
            case 7: return "ז";
            case 6: return "ו";
            case 5: return "ה";
            case 4: return "ד";
            case 3: return "ג";
            case 2: return "ב";
            case 1: return "א";
        }

        return "ERROR";
    }
    private String processOnePos(int pos) {
        switch (this.pos_type) {
            case AMOD:
                //calc half of cur_pos as hebrew letters
                String page = intToHebLetters(pos / 2);
                if (pos % 2 == 1) page = page + " עמוד א";
                if (pos % 2 == 0) page = page + " עמוד ב";
                return page;
            case DAF: case PEREK: return intToHebLetters(pos);
            case PAGE:  return ((Integer)pos).toString();
        }
        return "ERROR";
    }
    public String getProcessedCur_pos() {
        String pos = this.processOnePos(this.cur_pos);
        if(rate > 1) pos = this.processOnePos(this.cur_pos + this.rate - 1) + " - " + pos;
        return pos;
    }
    public void setCur_pos(int cur_pos) {
        this.cur_pos = cur_pos;
    }
    public void advanceCur_pos() {
        this.cur_pos += rate;
    }
    public void undoAdvanceCur_pos() {this.cur_pos -= rate;}
    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public POS_TYPES getPos_type() {
        return pos_type;
    }

    public void setPos_type(POS_TYPES pos_type) {
        this.pos_type = pos_type;
    }

    public void setPos_type(String pos) {
        if(pos.equals(AMOD .toString())) this.pos_type = AMOD;
        if(pos.equals(DAF  .toString())) this.pos_type = DAF;
        if(pos.equals(PEREK.toString())) this.pos_type = PEREK;
        if(pos.equals(PAGE .toString())) this.pos_type = PAGE;
    }

    public Calendar getStarting_date() {
        return starting_date;
    }
    public String getShortStarting_date() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(BookGoalMySQLiteDBDiffinition.BookGoalTableDiffinition.DATE_FORMAT);
        String str = dateFormat.format(this.getStarting_date().getTime());
        return str;
    }
    public void setStarting_date(Calendar starting_date) {
        this.starting_date = (Calendar) starting_date.clone();
    }

    /**
     * Works with BookGoal project.
     * @param str string date. format: dd-MM-yy
     */
    public void setStating_date(String str) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(BookGoalMySQLiteDBDiffinition.BookGoalTableDiffinition.DATE_FORMAT);
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(dateFormat.parse(str));
        } catch (ParseException e) {
            throw new Exception(String.valueOf(R.string.couldn_t_parse_time_got_from_data_base));
        }
        this.starting_date = cal;
    }
    public Calendar getEndingDate() {
        if(endingDate == null) { //if not calculated already
            int numdays = (end_pos - start_pos);
                    //end =  ceil(end/rate)
                numdays = (numdays + rate -1) /rate;
            endingDate = starting_date;
            endingDate.add(Calendar.DATE,numdays);
        }
        return endingDate;
    }

    public Time getAt_time() {
        return at_time;
    }

    public void setAt_time(Time at_time) {
        this.at_time = new Time(at_time);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String Note) {
        note = Note;
    }

    public String toString() {
        //return summary of book goal
        return this.name + " " + getProcessedCur_pos() + ", " + at_time.toString();
    }
}