package com.example.desmond.clinc.Controller;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.desmond.clinc.Controller.Actions.DiagnosisAdapter;
import com.example.desmond.clinc.Controller.Actions.FragmentUtils;
import com.example.desmond.clinc.Controller.Actions.MedicinesListAdapter;
import com.example.desmond.clinc.Controller.Actions.OnBackPressedListener;
import com.example.desmond.clinc.Controller.Actions.PatientsDialog;
import com.example.desmond.clinc.Controller.Actions.ShowAppointmentsAdapter;
import com.example.desmond.clinc.Model.Appointment;
import com.example.desmond.clinc.Model.CallBacks.DiagnosisListCallback;
import com.example.desmond.clinc.Model.CallBacks.UserCallback;
import com.example.desmond.clinc.Model.Diagnosis;
import com.example.desmond.clinc.Model.Firebase.AuthenticationDB;
import com.example.desmond.clinc.Model.Firebase.DBFacade;
import com.example.desmond.clinc.Model.Firebase.DiagnosisDB;
import com.example.desmond.clinc.Model.Firebase.UserDB;
import com.example.desmond.clinc.Model.ModelFacade;
import com.example.desmond.clinc.Model.User;
import com.example.desmond.clinc.R;
import com.example.desmond.clinc.View.AddMedicines;
import com.example.desmond.clinc.View.CreateDiagnosis;
import com.example.desmond.clinc.View.HomeFragment;
import com.example.desmond.clinc.View.HomeScreen;
import com.example.desmond.clinc.View.ShowDiagnosis;
import com.example.desmond.clinc.View.ShowListDiagnosis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Desmond on 12/18/2017.
 */

public class DiagnosisController {


    CreateDiagnosis createDiagnosis;
    ShowDiagnosis showDiagnosis;
    ShowListDiagnosis diagnosisView;
    AddMedicines addMedicinesView;

    Context context;
    Diagnosis diagnosis;
    DBFacade db;
    RecyclerView.Adapter adapter;


    public DiagnosisController(CreateDiagnosis createDiagnosis, Diagnosis diagnosis) {
        this.createDiagnosis = createDiagnosis;
        this.context = this.createDiagnosis.getActivity();
        this.diagnosis = diagnosis;
        this.createDiagnosis.addNextBtnListener(nextDiagnosisBtnListener());
        onCreateFragmentBackPressed();
    }

    public DiagnosisController(AddMedicines addMedicinesView, Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
        this.addMedicinesView = addMedicinesView;
        this.context = this.addMedicinesView.getActivity();
        this.addMedicinesView.addpushBtnListener(pushBtnListener());
        this.addMedicinesView.addEditTextWatcher(addEditTextWatcher());
        db = DBFacade.getINSTANCE(context);

    }

    public DiagnosisController(ShowListDiagnosis diagnosisView) {
        this.diagnosisView = diagnosisView;
        this.context = this.diagnosisView.getContext();
        this.diagnosisView.addCreateBtnListener(createDiagnosisBtnListener());
        db = DBFacade.getINSTANCE(context);
    }

    public DiagnosisController(ShowDiagnosis showDiagnosis, Diagnosis diagnosis) {
        this.showDiagnosis = showDiagnosis;
        this.context = this.showDiagnosis.getContext();
        this.diagnosis = diagnosis;
        onShowFragmentBackPressed();

    }


    // CREATE DIAGNOSIS....

    private void onCreateFragmentBackPressed() {
        ((HomeScreen) createDiagnosis.getActivity()).setOnBackPressedListener(new OnBackPressedListener() {
            @Override
            public void doBack() {
                FragmentUtils.replaceFragment(createDiagnosis.getActivity(), R.id.container, true, new ShowListDiagnosis());
            }
        });
    }

    public void checkDiagnosisData() {

        String diseaseName = createDiagnosis.getDiseasesName().getText().toString();
        String diagnosisPrice = createDiagnosis.getDiagnosisPrice().getText().toString();
        String diagnosisBlank = createDiagnosis.getDiagnosisBlank().getText().toString();

        boolean dataValidation = diseaseName.trim().equals("") || diagnosisBlank.trim().equals("")
                || diagnosisPrice.trim().equals("");

        if (dataValidation) {
            showToast("Invalid Diagnosis Data....");
        } else {
            diagnosis.setDiagnosisBlank(diagnosisBlank);
            diagnosis.setDiagnosisPrice(diagnosisPrice);
            diagnosis.setDiseasesName(diseaseName);
            diagnosis.setDoctor(getCurrentDoctor());
            diagnosis.setPatient(getChosenPatient());

            createDiagnosis.getActivity().getIntent().putExtra("Diagnosis", diagnosis);
            FragmentUtils.replaceFragment(createDiagnosis.getActivity(), R.id.container, true, new AddMedicines());

            System.out.println("Diagnosis : Patient {" + diagnosis.getPatient().getFirstName()
                    + " " + diagnosis.getPatient().getLastName() + "  " + diagnosis.getPatient().getEmail() + "}");

        }

    }

    public void showPatientsDialog() {

        db.getPatients(new UserCallback() {
            @Override
            public void currentUser(User user) {

            }

            @Override
            public void getUseres(final ArrayList<User> users) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose a Patient")
                        .setItems(setPatientsName(users), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String username = users.get(which).getFirstName() + " " + users.get(which).getLastName();
                                String email = users.get(which).getEmail();
                                createDiagnosis.getUsername().setText(username);
                                createDiagnosis.getUserEmail().setText(email);

                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);

                DialogFragment patientsFragment = new PatientsDialog(alertDialog);
                patientsFragment.show(createDiagnosis.getActivity().getFragmentManager(), "Patients Names");


            }
        });


    }

    public View.OnClickListener nextDiagnosisBtnListener() {
        return new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                checkDiagnosisData();
            }
        };
    }


    //ADD MEDICINES....

    public void showMedicinesList(ArrayList<String> medicines, RecyclerView recyclerView) {

        adapter = new MedicinesListAdapter(context, medicines);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

    }

    public View.OnClickListener pushBtnListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPushBtnBehavior();
            }
        };
    }

    public TextWatcher addEditTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.toString().trim().equals("")) {
                    addMedicinesView.getPushBtn().setRotation(90);
                } else {
                    addMedicinesView.getPushBtn().setRotation(0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private void setPushBtnBehavior() {

        ImageView pushBtn = addMedicinesView.getPushBtn();

        if (pushBtn.getRotation() == 90) {
            saveDiagnosisData();
        } else {
            addMedicineName();
        }
    }

    private void addMedicineName() {
        String mediceneName = addMedicinesView.getMedicineName().getText().toString();
        ((MedicinesListAdapter) adapter).getMedicines().add(mediceneName);
        addMedicinesView.getMedicineName().setText("");
        adapter.notifyDataSetChanged();
    }

    private void saveDiagnosisData() {
        diagnosis.setMedicines(((MedicinesListAdapter) adapter).getMedicines());
        diagnosis.setDateCreated(getCurrentDate());
        db.setDiagnosis(diagnosis);
        FragmentUtils.replaceFragment(addMedicinesView.getActivity(), R.id.container, true, new ShowListDiagnosis());
    }


    //SHOW DIAGNOSIS LIST......

    public void showDiagnosisList() {

        if (AuthenticationDB.USER_TYPE.equals("patient")) {
            showPatientDiagnosis();
        } else if (AuthenticationDB.USER_TYPE.equals("doctor")) {
            showDoctorDiagnosis();
        }


    }

    private void showDoctorDiagnosis() {
        db.getAllDoctorDiagnosis(new DiagnosisListCallback() {
            @Override
            public void sucess(ArrayList<Diagnosis> diagnoses) {
                initializeDiagnosisList(diagnoses);
            }

            @Override
            public void fail() {

            }
        });
    }

    private void showPatientDiagnosis() {
        diagnosisView.getcreateBtn().setVisibility(View.GONE);

        db.getAllPatientDiagnosis(new DiagnosisListCallback() {
            @Override
            public void sucess(ArrayList<Diagnosis> diagnoses) {
                initializeDiagnosisList(diagnoses);
            }

            @Override
            public void fail() {

            }
        });
    }

    public View.OnClickListener createDiagnosisBtnListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtils.replaceFragment(diagnosisView.getActivity(), R.id.container, true, new CreateDiagnosis());

            }
        };
    }

    private void initializeDiagnosisList(ArrayList<Diagnosis> diagnoses) {

        adapter = new DiagnosisAdapter(context, diagnosisView.getActivity(), diagnoses);
        RecyclerView recyclerView = diagnosisView.getRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        if (AuthenticationDB.USER_TYPE.equals("doctor")) {
            addRecyclerViewTouchHelper(recyclerView);
        }
        recyclerView.setHasFixedSize(true);
    }

    //SHOW DIAGNOSIS...

    private void onShowFragmentBackPressed() {
        ((HomeScreen) showDiagnosis.getActivity()).setOnBackPressedListener(new OnBackPressedListener() {
            @Override
            public void doBack() {
                FragmentUtils.replaceFragment(showDiagnosis.getActivity(), R.id.container, true, new ShowListDiagnosis());
            }
        });
    }

    public void showDiagnosisData() {

        showDiagnosis.getDiseaseName().setText(diagnosis.getDiseasesName());

        String username;
        if (AuthenticationDB.USER_TYPE.equals("patient")) {
            username = "Doctor : "+diagnosis.getDoctor().getFirstName() + " " + diagnosis.getDoctor().getLastName();
            showDiagnosis.getUserName().setText(username);
        } else {
            username = "Patient : "+diagnosis.getPatient().getFirstName() + " " + diagnosis.getPatient().getLastName();
            showDiagnosis.getUserName().setText(username);
        }

        String price = "(" + diagnosis.getDiagnosisPrice() + "$)";
        showDiagnosis.getDiagnosisPrice().setText(price);
        showDiagnosis.getDiagnosisBlank().setText(diagnosis.getDiagnosisBlank());
        showMedicinesList(diagnosis.getMedicines(), showDiagnosis.getMedicinesList());
    }

    // Utilises Methods

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private String[] setPatientsName(ArrayList<User> patients) {
        String[] patientsName = new String[patients.size()];
        int i = 0;
        for (User user : patients) {
            patientsName[i] = (user.getFirstName() + " " + user.getLastName());
            i++;
        }
        return patientsName;
    }

    private User getCurrentDoctor() {

        User doctor = null;
        try {
            doctor = ModelFacade.createUser("Doctor");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(UserDB.USER_DATA, Context.MODE_PRIVATE);

        doctor.setFirstName(sharedPreferences.getString("firstName", ""));
        doctor.setLastName(sharedPreferences.getString("lastName", ""));
        doctor.setEmail(sharedPreferences.getString("email", ""));
        doctor.setUserId(AuthenticationDB.USER_ID);
        diagnosis.setDoctor(doctor);
        return doctor;
    }

    private User getChosenPatient() {
        User patient = null;

        try {
            patient = ModelFacade.createUser("Patient");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


        patient.setEmail(createDiagnosis.getUserEmail().getText().toString());
        String username = createDiagnosis.getUsername().getText().toString();
        patient.setFirstName(username.split("\\s")[0]);
        patient.setLastName(username.split("\\s")[1]);
        return patient;
    }

    private String getCurrentDate() {

        DateFormat dateFormat = new SimpleDateFormat("E, dd-MM-yyyy@hh:mm a");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);

    }

    private void addRecyclerViewTouchHelper(RecyclerView recyclerView) {

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
                DiagnosisAdapter currentAdapter = ((DiagnosisAdapter) adapter);
                Diagnosis diagnosis = currentAdapter.getDiagnoses().get(position);
                currentAdapter.getDiagnoses().remove(position);
                db.deleteDiagnosis(diagnosis);
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
                        icon = BitmapFactory.decodeResource(diagnosisView.getResources(), R.drawable.ic_delete);
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
