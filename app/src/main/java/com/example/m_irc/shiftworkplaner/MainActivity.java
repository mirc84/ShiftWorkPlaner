package com.example.m_irc.shiftworkplaner;

import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase _db;
    private CaldroidFragment _caldroidFragment;
    private Toolbar _toolbar;
    private Shift[] _shifts;
    private Shift _currentShift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

        setContentView(R.layout.activity_main);
        _caldroidFragment = new CaldroidFragment();

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            _caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday

            // Uncomment this line to use Caldroid in compact mode
            // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            _caldroidFragment.setArguments(args);
        }

        //setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar, _caldroidFragment);
        t.commit();

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                if (_currentShift == null)
                    return;


            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(getApplicationContext(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {
                if (_caldroidFragment.getLeftArrowButton() != null) {
                    Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();
                }
            }

        };

        // Setup Caldroid
        _caldroidFragment.setCaldroidListener(listener);



        Button button = new Button(this);
        button.setText("+");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewShift();
            }
        });

        _toolbar = this.findViewById(R.id.toolbar);

        _toolbar.addView(button);

        //AsyncTask task = new AsyncTask<Void, Void, List<Shift>>(){
//
        //    @Override
        //    protected List<Shift> doInBackground(Void... params) {
        //        ShiftDao dao = _db.shiftDao();
        //        List<Shift> shifts =  dao.getAll();
        //        return shifts;
        //    }
        //    @Override
        //    protected void onPostExecute(List<Shift> shifts) {
        //        for (Shift shift: shifts) {
        //            AddShift(shift);
        //        }
        //    }
        //};

        //task.execute();
    }

    private void AddShift(final Shift shift) {
        Button btn = new Button(this);
        btn.setBackgroundColor(shift.color);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UseShift(shift);
            }
        });
        int count = _toolbar.getChildCount();
        _toolbar.addView(btn, count - 1);
    }

    private void PopulateToolbar() {

        List<Shift> shifts = _db.shiftDao().getAll();

        for (final Shift shift : shifts) {
            final Shift currentShift = shift;
            Button btn = new Button(this);
            btn.setBackgroundColor(shift.color);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UseShift(currentShift);
                }
            });
            _toolbar.addView(btn);
        }



    }

    private void UseShift(final Shift shift) {
        _currentShift = shift;
    }

    private void AddNewShift() {
        AsyncTask<Void, Void, Shift> task = new AsyncTask<Void, Void, Shift>() {
            @Override
            protected Shift doInBackground(Void[] objects) {
                final Shift shift = getShift();

                return shift;
            }

            @Override
            protected void onPostExecute(Shift shift) {
                if (shift != null)
                    AddShift(shift);
            }
        };
        task.execute();
    }

    @Nullable
    private Shift getShift() {
        ShiftDao dao = _db.shiftDao();
        Log.d("Shift", "dao created.");

        final Shift currentShift = new Shift();
        currentShift.name = String.valueOf(Calendar.MILLISECONDS_IN_DAY);
        currentShift.color = Color.BLUE;
        Log.d("Shift", "new shift created.");

        Shift existing = dao.findByName(currentShift.name);
        if (existing != null) {
            Log.d("Shift", "existing shift loaded.");
            currentShift.uid = existing.uid;
            dao.updateShifts(currentShift);
            Log.d("Shift", "existing shift updated.");

        }
        else {
            Log.d("Shift", "inserting shift.");
            dao.insertAll(currentShift);
            Log.d("Shift", "inserted shift.");
            return currentShift;
        }
        return null;
    }
}

