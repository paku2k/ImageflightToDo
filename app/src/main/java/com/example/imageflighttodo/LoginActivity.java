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

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView register;
    private Button login;
    private EditText email, password;
    private RelativeLayout relativeLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        login = findViewById(R.id.button_sign_in);
        email = findViewById(R.id.text_email_login);
        password = findViewById(R.id.edit_text_password_login);
        relativeLayout = findViewById(R.id.relLayoutLogin);
        progressBar = findViewById(R.id.progressbarRegister);

        email.setText(getIntent().getStringExtra("email"));
        password.setText(getIntent().getStringExtra("password"));




        login.setOnClickListener(new View.OnClickListener() {
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
                    loginUser(mail, pw);
                }

            }
        });


        register=findViewById(R.id.text_view_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    private void loginUser(String mail, String pw) {
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(mail,pw)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK ).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }

                        else {
                            Log.d("AuthFailed", "onComplete: "+task.getException());
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK ).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }

    }
}
