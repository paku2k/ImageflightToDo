package com.example.imageflighttodo;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private TextView login;
    private Button register_btn;
    private EditText email, password;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    private CollectionReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users");


        login = findViewById(R.id.text_view_login);
        register_btn = findViewById(R.id.button_register);
        email = findViewById(R.id.edit_text_email_register);
        password = findViewById(R.id.edit_text_password_register);
        relativeLayout = findViewById(R.id.relLayoutRegister);
        progressBar = findViewById(R.id.progressbarRegister);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString().trim();
                String pw = password.getText().toString().trim();

                if(mail.isEmpty()){
                    email.setError("Email Required");
                    Snackbar.make(v, "Email Required", Snackbar.LENGTH_SHORT).show();
                }

                else if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                    email.setError("Entered Email not Valid");
                }

                else if (pw.isEmpty()){
                    password.setError("Please enter a Password");

                }
                else if(pw.length()<6){
                    password.setError("Minimum 6 Characters for your password");
                }

                else {
                    registerUser(mail, pw);
                }
                
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }

    private void registerUser(final String mail, final String pw) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(mail,pw)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            HashMap<String, Object> user = new HashMap<>();
                            user.put("email", mail);
                            user.put("password", pw);

                            String uid = mAuth.getUid();
                            Log.d("CurrentUser", "current User: "+uid);
                            userRef.document(uid).set(user);
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class)
                                    .putExtra("email", mail).putExtra("password", pw)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK ).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }
                        else {
                            Snackbar.make(relativeLayout, "Creating Account failed ", Snackbar.LENGTH_SHORT).show();

                            Log.d("AuthFailed", "onComplete: "+task.getException());

                        }

                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(RegisterActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK ).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }

    }
}
