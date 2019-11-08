package com.example.imageflighttodo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.FirebaseFirestore;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToDoItem item1 = new ToDoItem("Test", "Dies ist kein Test", EmployeeTranslator.SIMON);
        items.add(item1);



        mAuth= FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        logout = findViewById(R.id.logout_textView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        adapter = new ToDoViewAdapter(this, items);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ToDoItem item2 = new ToDoItem("Test2", "Dies ist ein zweiter  Test", EmployeeTranslator.WILLY);
        items.add(item2);
        adapter.notifyDataSetChanged();

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
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.popup, null);
        dialogDateSet = view1.findViewById(R.id.itemDate);
        dialogTitle = view1.findViewById(R.id.itemTitle);
        dialogDescription = view1.findViewById(R.id.itemDescription);
        dialogSave = view1.findViewById(R.id.saveItemButton);
        spinner = view1.findViewById(R.id.itemAssignmentSpinner);
       // ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, array, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener(this);


        builder.setView(view1);
        dialog = builder.create();
        dialog.show();

        dialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    if()
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
