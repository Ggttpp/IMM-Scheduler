package com.zulfian.immscheduler;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listViewSchedule;

    List<Schedule> schedules;
    DatabaseReference databaseSchedule;

    private TextView textDate ;
    private TextView textHour ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseSchedule = FirebaseDatabase.getInstance().getReference("schedules");
        schedules = new ArrayList<>();

        listViewSchedule = findViewById(R.id.listSchedule);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                showAddDialog();
            }
        });

        listViewSchedule.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Schedule schedule = schedules.get(i);
                showUpdateDeleteDialog(schedule.getId(), schedule.getName());
                return true;
            }
        });
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
    protected void onStart() {
        super.onStart();

        databaseSchedule.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                schedules.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Schedule schedule = postSnapshot.getValue(Schedule.class);
                    //adding artist to the list
                    schedules.add(schedule);
                }

                ScheduleList scheduleAdapter = new ScheduleList(MainActivity.this, schedules);
                //attaching adapter to the listview
                listViewSchedule.setAdapter(scheduleAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean updateArtist(String id, String name, String location, String note, String date, String hour) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("schedules").child(id);

        //updating artist
        Schedule schedule = new Schedule(id, name, location, note, date, hour);
        dR.setValue(schedule);
        Toast.makeText(getApplicationContext(), "Schedule Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private void showUpdateDeleteDialog(final String sheduleId, String scheduleName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editName = (EditText) dialogView.findViewById(R.id.editNameUp);
        final EditText editLocation = (EditText) dialogView.findViewById(R.id.editLocationUp);
        final EditText editNote = (EditText) dialogView.findViewById(R.id.editNoteUp);
        final TextView textDate = (TextView) dialogView.findViewById(R.id.textDateUp);
        final TextView textHour = (TextView) dialogView.findViewById(R.id.textHourUp);

        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateArtist);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteArtist);

        dialogBuilder.setTitle(scheduleName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString().trim();
                String location = editLocation.getText().toString().trim();
                String note = editNote.getText().toString().trim();
                String date = textDate.getText().toString().trim();
                String hour = textHour.getText().toString().trim();

                if (!TextUtils.isEmpty(name)) {
                    updateArtist(sheduleId, name, location, note, date, hour);
                    b.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteArtist(sheduleId);
                b.dismiss();
            }
        });

    }

    private boolean deleteArtist(String id) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("schedules").child(id);

        //removing artist
        dR.removeValue();

        Toast.makeText(getApplicationContext(), "Schedule Deleted", Toast.LENGTH_LONG).show();

        return true;
    }

    private void showAddDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editName = (EditText) dialogView.findViewById(R.id.editNameAdd);
        final EditText editLocation = (EditText) dialogView.findViewById(R.id.editLocationAdd);
        final EditText editNote = (EditText) dialogView.findViewById(R.id.editNoteAdd);
        textDate = (TextView) dialogView.findViewById(R.id.textDateAdd);
        textHour = (TextView) dialogView.findViewById(R.id.textHourAdd);

        final Button buttonAdd = (Button) dialogView.findViewById(R.id.buttonAddSchedule);

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        textHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        dialogBuilder.setTitle("Create Schedule");
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString().trim();
                String location = editLocation.getText().toString().trim();
                String note = editNote.getText().toString().trim();
                String date = textDate.getText().toString().trim();
                String hour = textHour.getText().toString().trim();

                if (!TextUtils.isEmpty(name)) {
                    addSchedule(name, location, note, date, hour);
                    b.dismiss();
                }

                editName.setText("");
                editLocation.setText("");
                editNote.setText("");
                textDate.setText("");
                textHour.setText("");

            }
        });
    }

    private void addSchedule(String name, String location, String note, String date, String hour) {

        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseSchedule.push().getKey();

            //creating an Artist Object
            Schedule schedule = new Schedule(id, name, location, note, date, hour);

            //Saving the Artist
            databaseSchedule.child(id).setValue(schedule);


            //displaying a success toast
            Toast.makeText(this, "Schedule added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please Fill The Blank", Toast.LENGTH_LONG).show();
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setTextHour(int hourOfDay, int minute) {
        SimpleDateFormat ft = new SimpleDateFormat("kk:mm a");
        Calendar cal = Calendar.getInstance();
        cal.set(0,0,0, hourOfDay, minute);
        Date date = cal.getTime();

        this.textHour.setText(ft.format(date));
    }

    public void setTextDate(int day, int month, int year) throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat("EE dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        Date date = cal.getTime();

        this.textDate.setText(ft.format(date));
    }



}
