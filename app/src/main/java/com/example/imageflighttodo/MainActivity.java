package com.example.imageflighttodo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.imageflighttodo.adapters.ToDoViewAdapter;
import com.example.imageflighttodo.model.ToDoItem;
import com.example.imageflighttodo.util.EmployeeTranslator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private ToDoViewAdapter adapter;
    private List<ToDoItem> items = new ArrayList<>();
    private TextView dialogDateSet,logout;
    private EditText dialogTitle, dialogDescription;
    private Button dialogSave;
    private Spinner spinner;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String projectUID;
    private CollectionReference todosRef;
    private List<String> userArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mAuth= FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        logout = findViewById(R.id.logout_textView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        adapter = new ToDoViewAdapter(this, items);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        projectUID = getIntent().getStringExtra("projectUid");
        userArray=(List<String>) getIntent().getStringArrayListExtra("userList");

        todosRef = db.collection("projects").document(projectUID).collection("todos");

        todosRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("ToDoItem", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("ToDoItem", "Error getting documents: ", task.getException());
                        }
                    }
                });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK ).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK));

            }
        });


        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();

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
    private void createDialog() {
        final AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.popup, null);
        dialogDateSet = view1.findViewById(R.id.itemDate);
        dialogTitle = view1.findViewById(R.id.itemTitle);
        dialogDescription = view1.findViewById(R.id.itemDescription);
        dialogSave = view1.findViewById(R.id.saveItemButton);
        spinner = view1.findViewById(R.id.itemAssignmentSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, userArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        builder.setView(view1);
        dialog = builder.create();
        dialog.show();

        dialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if((dialogTitle.getText().toString().length()>0)&&(dialogDescription.getText().toString().length()>0)&&(spinner.getSelectedItem()))
            }
        });

        dialogDateSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dp = new DatePickerFragment();
                dp.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dialogDateSet.setText(DateFormat.getDateInstance().format(c.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
