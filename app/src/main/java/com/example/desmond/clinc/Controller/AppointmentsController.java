package com.example.desmond.clinc.Controller;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.desmond.clinc.Controller.Actions.AppointmentParent;
import com.example.desmond.clinc.Controller.Actions.FragmentUtils;
import com.example.desmond.clinc.Controller.Actions.MakeAppointmentsAdapter;
import com.example.desmond.clinc.Controller.Actions.OnBackPressedListener;
import com.example.desmond.clinc.Controller.Actions.OpenDatePickerFragment;
import com.example.desmond.clinc.Controller.Actions.OpenTimePickerFragment;
import com.example.desmond.clinc.Controller.Actions.ShowAppointmentsAdapter;
import com.example.desmond.clinc.Model.Appointment;
import com.example.desmond.clinc.Model.AppointmentDate;
import com.example.desmond.clinc.Model.CallBacks.AppointmentsCallback;
import com.example.desmond.clinc.Model.CallBacks.CheckCallback;
import com.example.desmond.clinc.Model.CallBacks.MadeAppointmentCallback;
import com.example.desmond.clinc.Model.Firebase.AppointmentDB;
import com.example.desmond.clinc.Model.Firebase.AuthenticationDB;
import com.example.desmond.clinc.Model.Firebase.DBFacade;
import com.example.desmond.clinc.R;
import com.example.desmond.clinc.View.CreateAppointment;
import com.example.desmond.clinc.View.HomeScreen;
import com.example.desmond.clinc.View.MakeAppointment;
import com.example.desmond.clinc.View.ShowAppointments;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Desmond on 12/11/2017.
 */

public class AppointmentsController {


    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    AppointmentDate appDate;
    DBFacade db;
    Context context;


    CreateAppointment createView;
    MakeAppointment makeView;
    ShowAppointments appointmentsView;


    public AppointmentsController(ShowAppointments appointmentsView) {
        this.appointmentsView = appointmentsView;
        this.context = this.appointmentsView.getActivity();
        db = DBFacade.getINSTANCE(context);
        this.appointmentsView.addAddBtnListener(addFloatingButtonListener());

    }

    public AppointmentsController(MakeAppointment makeView) {
        this.makeView = makeView;
        context = this.makeView.getActivity();
        onMakeFragmentBackPressed();
        db = DBFacade.getINSTANCE(context);
    }

    public AppointmentsController(CreateAppointment createView, AppointmentDate appDate) {

        this.createView = createView;
        this.appDate = appDate;
        context = this.createView.getActivity();
        db = DBFacade.getINSTANCE(context);
        this.createView.pickDateBtnListener(openDatePickerDialog());
        this.createView.pickTimeBtnListener(openTimePickerDialog());
        this.createView.doneBtnListener(addAppointment());
        onCreateFragmentBackPressed();


    }


    // CREATE APPOINTMENT......

    private void onCreateFragmentBackPressed() {
        ((HomeScreen) createView.getActivity()).setOnBackPressedListener(new OnBackPressedListener() {
            @Override
            public void doBack() {
                FragmentUtils.replaceFragment(createView.getActivity(), R.id.container, true, new ShowAppointments());
            }
        });
    }

    private View.OnClickListener openTimePickerDialog() {

        return new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                if (isDateDataSet()) {
                    DialogFragment timeFragment = new OpenTimePickerFragment();
                    ((OpenTimePickerFragment) timeFragment).setTimeSetListener(setTimeFormat());
                    timeFragment.show(createView.getActivity().getFragmentManager(), "TimePicker");
                } else {
                    showToast("Please Set The Date First");
                }


            }
        };


    }

    private View.OnClickListener openDatePickerDialog() {

        return new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                DialogFragment dateFragment = new OpenDatePickerFragment();
                ((OpenDatePickerFragment) dateFragment).setDateSetListener(setDateFormat());
                dateFragment.show(createView.getActivity().getFragmentManager(), "DatePicker");

            }
        };


    }

    private View.OnClickListener addAppointment() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDataSet()) {
                    db.insertAppointment(appDate, new CheckCallback() {
                        @Override
                        public void success() {
                            // FragmentUtils.replaceFragment(createView.getActivity(), R.id.container, true, new HomeFragment());
                            showToast("Appointment Added");
                        }

                        @Override
                        public void fail() {
                            showToast("Failed Appointment Adding");
                        }
                    });
                }


            }
        };
    }

    public TimePickerDialog.OnTimeSetListener setTimeFormat() {

        return new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                // TODO Auto-generated method stub

                String appointmentTime = timeFormat(hourOfDay, minute);

                if (isPickedDateSameCurrent()) {
                    if (!isPickedTimeSameOrBeforeCurrent(hourOfDay, minute)) {
                        appDate.setAppointmentTime(appointmentTime);
                        createView.getPickedTime().setText(appointmentTime);
                    } else {
                        showToast("Choose Good Time For Appointment");
                    }
                } else {
                    appDate.setAppointmentTime(appointmentTime);
                    createView.getPickedTime().setText(appointmentTime);
                }


            }
        };


    }

    public DatePickerDialog.OnDateSetListener setDateFormat() {

        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                String appointmentDate = new SimpleDateFormat("E, dd-MM-yyyy").format(calendar.getTime());
                appDate.setAppointmentDate(appointmentDate);
                createView.getPickedDate().setText(appointmentDate);
            }


        };


    }

    private boolean checkDataSet() {

        String pickedTime = createView.getPickedTime().getText().toString().trim();
        if (pickedTime.equals("") || !isDateDataSet()) {
            showToast("Cannot set Appointment");
            return false;
        }
        return true;
    }

    private boolean isDateDataSet() {
        return !(createView.getPickedDate().getText().toString().equals(""));
    }

    private boolean isPickedDateSameCurrent() {

        DateFormat dateFormat = new SimpleDateFormat("E, dd-MM-yyyy");
        Date currentDate = new Date();
        Date userDate = new Date();

        String dateValue2 = dateFormat.format(currentDate);
        String dateValue = createView.getPickedDate().getText().toString();

        try {
            userDate = dateFormat.parse(dateValue);
            currentDate = dateFormat.parse(dateValue2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return userDate.equals(currentDate);
    }

    private static boolean isPickedTimeSameOrBeforeCurrent(int hour, int min) {

        DateFormat dateFormat = new SimpleDateFormat("HH:mm a");
        Date currentDate = new Date();
        Date userDate = new Date();

        String dateValue2 = dateFormat.format(currentDate);
        String dateValue = hour + ":" + min + " AM";

        try {
            userDate = dateFormat.parse(dateValue);
            currentDate = dateFormat.parse(dateValue2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return userDate.before(currentDate);

    }

    // "05:40 PM"
    public String timeFormat(int hour, int minutes) {

        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String min = "";
        if (minutes < 10)
            min = "0" + minutes;
        else
            min = String.valueOf(minutes);

        String appointmentTime = hour + ":" + min + " " + timeSet;
        return appointmentTime;

    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    //MAKE APPOINTMENT.....

    public void showAllAvailableAppointments() {


        db.getAllAvailableAppointments(new AppointmentsCallback() {
            @Override
            public void success(ArrayList<AppointmentParent> appointments) {
                setAvailableAppointment(appointments);
            }

            @Override
            public void fail() {

            }
        });


    }

    private void setAvailableAppointment(ArrayList<AppointmentParent> appointments) {

        adapter = new MakeAppointmentsAdapter(context, appointments);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView = makeView.getMakeRecyclerView();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    private void onMakeFragmentBackPressed() {
        ((HomeScreen) makeView.getActivity()).setOnBackPressedListener(new OnBackPressedListener() {
            @Override
            public void doBack() {
                FragmentUtils.replaceFragment(makeView.getActivity(), R.id.container, true, new ShowAppointments());
            }
        });
    }

    // SHOW APPOINTMENTS

    public FloatingActionButton.OnClickListener addFloatingButtonListener() {

        if (AuthenticationDB.USER_TYPE.equals("patient")) {
            return makeAppointmentListener();
        } else if (AuthenticationDB.USER_TYPE.equals("doctor")) {
            return createAppointmentListener();
        } else {
            return null;
        }

    }

    private FloatingActionButton.OnClickListener makeAppointmentListener() {
        return new FloatingActionButton.OnClickListener() {


            @Override
            public void onClick(View view) {
                FragmentUtils.replaceFragment(appointmentsView.getActivity(), R.id.container, true, new MakeAppointment());
            }
        };
    }

    private FloatingActionButton.OnClickListener createAppointmentListener() {
        return new FloatingActionButton.OnClickListener() {


            @Override
            public void onClick(View view) {
                FragmentUtils.replaceFragment(appointmentsView.getActivity(), R.id.container, true, new CreateAppointment());
            }
        };
    }

    public void showAppointments() {

        System.out.println("User Type : " + AuthenticationDB.USER_TYPE);
        if (AuthenticationDB.USER_TYPE.equals("patient")) {
            showPatientAppointments();
        } else if (AuthenticationDB.USER_TYPE.equals("doctor")) {
            showDoctorAppointments();
        }

    }

    private void showDoctorAppointments() {

        db.getAllDoctorAppointments(new MadeAppointmentCallback() {
            @Override
            public void success(ArrayList<Appointment> appointments) {

                if (appointments.size() > 0) {

                    initializeRecyclerView(appointments);
                }
            }

            @Override
            public void fail() {
                showToast("Showing Appointments Failed");
            }
        });


    }

    private void showPatientAppointments() {

        db.getAllPatientAppointments(new MadeAppointmentCallback() {
            @Override
            public void success(ArrayList<Appointment> appointments) {
                if (appointments.size() > 0) {

                    initializeRecyclerView(appointments);
                }
            }

            @Override
            public void fail() {
                showToast("Showing Appointments Failed");
            }
        });
    }

    private void initializeRecyclerView(ArrayList<Appointment> appointments) {

        adapter = new ShowAppointmentsAdapter(context, appointments);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView = appointmentsView.getAppointmentsList();
        addRecyclerViewTouchHelper();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

    }

    private void addRecyclerViewTouchHelper() {

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return super.getMovementFlags(recyclerView, viewHolder);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }

            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                ShowAppointmentsAdapter currentAdapter = ((ShowAppointmentsAdapter) adapter);
                Appointment appointment = currentAdapter.getAppointments().get(position);
                currentAdapter.getAppointments().remove(position);
                db.deleteAppointment(appointment);
                currentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                try {

                    Bitmap icon;
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                        View itemView = viewHolder.itemView;
                        float height = (float) itemView.getBottom() - (float) itemView.getTop();
                        float width = height / 3;
                        Paint paint = new Paint();
                        paint.setColor(Color.parseColor("#D32F2F"));

                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, paint);
                        icon = BitmapFactory.decodeResource(appointmentsView.getResources(), R.drawable.ic_delete);
                        RectF destination = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);

                        c.drawBitmap(icon, null, destination, paint);
                    } else {
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


}
