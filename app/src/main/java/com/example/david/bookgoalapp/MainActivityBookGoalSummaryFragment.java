package com.example.david.bookgoalapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainActivityBookGoalSummaryFragment.OnIsDoneChangedListener} interface
 * to handle interaction events.
 * Use the {@link MainActivityBookGoalSummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivityBookGoalSummaryFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String IsDone = "IsDone";
    public static final String BookGoalId = "BookGoalId";
    public static final String Color = "Color";
    public static final  String MyCurPos ="My current position";
    public static final String TextToShow = "TextToShow";

    private int bookGoalId;
    private int myCurPos;
    private int color;
    private boolean isDone;
    private String textToShow;

    private OnIsDoneChangedListener mListener;

    public MainActivityBookGoalSummaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MainActivityBookGoalSummaryFragment.
     */
    public static MainActivityBookGoalSummaryFragment newInstance(int bookGoalId,int mCurPos,int color,String textToShow,boolean isDone) {
        MainActivityBookGoalSummaryFragment fragment = new MainActivityBookGoalSummaryFragment();
        Bundle args = new Bundle();
        args.putInt(BookGoalId, bookGoalId);
        args.putInt(MyCurPos,mCurPos);
        args.putInt(Color,color);
        args.putString(TextToShow, textToShow);
        args.putBoolean(IsDone,isDone);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.isDone = getArguments().getBoolean(IsDone);
            this.bookGoalId = getArguments().getInt(BookGoalId);
            this.myCurPos = getArguments().getInt(MyCurPos);
            this.color = getArguments().getInt(Color);
            this.textToShow = getArguments().getString(TextToShow);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_main_activity_book_goal_summary, container, false);
        //set the background color
        ((LinearLayout)v.findViewById(R.id.lnlySummary)).setBackgroundColor(this.color);
        //set the text
        ((TextView) v.findViewById(R.id.txtvSummary)).setText(this.textToShow);
        //set the edit button
        ((FloatingActionButton)v.findViewById(R.id.btnSummaryEdit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                Intent intent = new Intent(getContext(),BookGoalViewActivity.class);
                intent.putExtra(BookGoalViewActivity.BookGoalId,MainActivityBookGoalSummaryFragment.this.bookGoalId);
                startActivity(intent);
            }
        });
        //set the checkbox ischecked
        ((CheckBox)v.findViewById(R.id.cbSummaryIsDone)).setChecked(this.isDone);
        //set the on checked button listener
        ((CheckBox)v.findViewById(R.id.cbSummaryIsDone)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
             MainActivityBookGoalSummaryFragment.this.mListener.onFragmentInteraction(checkBox,isChecked,
                                                        MainActivityBookGoalSummaryFragment.this.bookGoalId,
                                                        MainActivityBookGoalSummaryFragment.this.myCurPos);
            }
        });

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnIsDoneChangedListener) {
            mListener = (OnIsDoneChangedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnIsDoneChangedListener {
        /**
         *
         * @param checkBox
         * @param isDone
         * @param bookGoalId
         * @param pos cur_pos of sending fragment
         */
        void onFragmentInteraction(CompoundButton checkBox,boolean isDone,int bookGoalId,int pos);
    }
}
