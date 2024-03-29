package com.example.imageflighttodo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.RequestOptions;
import com.example.imageflighttodo.adapters.ProjectViewAdapter;
import com.example.imageflighttodo.adapters.ToDoViewAdapter;
import com.example.imageflighttodo.model.Project;
import com.example.imageflighttodo.model.ToDoItem;
import com.example.imageflighttodo.util.userUIDTranslator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProjectList2 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProjectViewAdapter adapter;
    private ArrayList<Project> projects = new ArrayList<>();
    private TextView logout;
    private EditText dialogTitle, dialogDescription, assignmentEditText;
    private Button dialogSave,search;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private CollectionReference userRef, projectRef;
    private String uid;
    private ArrayList<String> DBprojects;
    private ArrayList<String> usersAdded = new ArrayList<>();
    //private ArrayList<String> userEmails = new ArrayList<>();
    private ArrayList<DocumentSnapshot> userDocs = new ArrayList<>();
    private ArrayList<String> nameArray= new ArrayList<>();


    private ListView listViewUsersAdded;
    private ArrayAdapter arrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,usersAdded);

        mAuth= FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users");
        projectRef = db.collection("projects");





//        Client client = new Client("SFFBTENJCW", "70317295f7b7de1f720d40aaa10a3e8d");
//        Index index = client.getIndex("todo_users");




        projectRef.whereArrayContains("collaborators", mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot document : task.getResult()) {
                        if(document.exists()){


                            nameArray=(ArrayList < String >) document.get("collaborators_names");
                            if(nameArray!= null) {
                                Log.d("DBQuery", "nameArray for project : " + nameArray);
                                String title = document.getString("title");
                                Date date = document.getDate("dateOfCreation");
                                projects.add(new Project(title, document.getId(), nameArray, date));
                            }
                        }

                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("DBQuery", "onComplete: Task Failed");
                    Toast.makeText(ProjectList2.this,"Getting Projects Failed", Toast.LENGTH_LONG).show();
                }
            }
        });






        logout = findViewById(R.id.logout_textView_2);
        recyclerView = findViewById(R.id.recyclerViewProject);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        adapter = new ProjectViewAdapter(this, projects, new ProjectViewAdapter.OnProjectClickListener() {
            @Override
            public void onProjectClicked(Project p) {
                String uid = p.getUid();
                Intent intent = new Intent(ProjectList2.this, MainActivity.class).putExtra("projectUid", uid).putExtra("userList", p.getUsers());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        FloatingActionButton fab = findViewById(R.id.fabAddProject);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createDialog();
                HashMap<String, Object> projInitData=new HashMap<>();
                projInitData.put("timeOfCreation", FieldValue.serverTimestamp());
                //projInitData.put("")


                //projectRef.document()

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(ProjectList2.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK ).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK));

            }
        });

    }

    private void createDialog() {

        final AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(ProjectList2.this);
        View view1 = getLayoutInflater().inflate(R.layout.popup_project, null);


        listViewUsersAdded = (ListView) view1.findViewById(R.id.ListViewAddedUsers);
        search = view1.findViewById(R.id.btn_search);
        dialogTitle = view1.findViewById(R.id.itemTitle);
        dialogSave = view1.findViewById(R.id.saveItemButton);
        assignmentEditText = view1.findViewById(R.id.itemAssignmentEditText);

        listViewUsersAdded.setAdapter(arrayAdapter);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignmentEditText.setError(null);
                String enteredEmail = assignmentEditText.getText().toString().trim();
                if (enteredEmail.length()>0) {
                    Query query = userRef.whereEqualTo("email", enteredEmail);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    if (document.getId().trim().equals(mAuth.getUid().trim())){
                                        assignmentEditText.setError("You cant add yourself");
                                    }
                                    else if((document.getString("email")!=null)){
                                        if(!usersAdded.contains(document.getString("email"))) {
                                            userDocs.add(document);
                                            usersAdded.add(document.getString("email"));

                                            arrayAdapter.notifyDataSetChanged();
                                        }
                                    }
                                    else{
                                        assignmentEditText.setError("user not found");

                                    }
                                }
                            } else {
                                assignmentEditText.setError("user not found");

                            }
                        }
                    });

                }
            }
        });

        listViewUsersAdded.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                usersAdded.remove(i);
                arrayAdapter.notifyDataSetChanged();
            }
        });
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

                userRef.document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document!=null) {
                                userDocs.add(document);
                                usersAdded.add(document.getString("email"));
                            }
                        }

                        String titleString = dialogTitle.getText().toString().trim();
                        if(titleString.length()>0){
                            Calendar c = Calendar.getInstance();
                            Date now = c.getTime();
                            HashMap<String, Object> hm = new HashMap<>();
                            HashMap<String, String> userUIDuserName =new HashMap<>();
                            ArrayList<String> userUIDs = new ArrayList<>();
                            hm.put("title", titleString);
                            hm.put("dateOfCreation", now);
                            for (DocumentSnapshot userDoc : userDocs){
                                userUIDs.add(userDoc.getId());
                                userUIDuserName.put(userDoc.getId(), userDoc.getString("email"));
                            }
                            hm.put("collaboratorsMap", userUIDuserName);
                            hm.put("collaborators", userUIDs);
                            hm.put("collaborators_names", usersAdded );
                            DocumentReference newProjectReference = projectRef.document();
                            newProjectReference.set(hm);
                            projects.add(new Project(titleString,newProjectReference.getId(), userUIDuserName, now));
                            dialog.dismiss();
                            usersAdded.clear();
                            userUIDs.clear();
                            userDocs.clear();
                            adapter.notifyDataSetChanged();


                        }
                        else{
                            dialogTitle.setError("Please enter a Title");
                        }
                    }
                });


                }});


                }





    @Override
    protected void onStart() {
        super.onStart();

        userRef.document(uid).update("lastLogin", FieldValue.serverTimestamp());
    }
}
