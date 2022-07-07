package com.example.desihand;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button btn_login;
    String email, password;
    EditText edt_1, edt_2;
    TextView txtregister;
    private FirebaseAuth mAuth;
    public static final String SHARED_PREFS = "shared_prefs";
    // key for storing email.
    public static final String EMAIL_KEY = "email_key";
    // key for storing password.
    public static final String PASSWORD_KEY = "password_key";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        btn_login = findViewById(R.id.btn_login);
        edt_1 = findViewById(R.id.EdtemailAdd);
        edt_2 = findViewById(R.id.Edtpassword);
        txtregister = findViewById(R.id.Txtregister);
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedpreferences.getString(EMAIL_KEY, null);
        password = sharedpreferences.getString(PASSWORD_KEY, null);

        txtregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = edt_1.getText().toString().trim();
                password = edt_2.getText().toString().trim();
                if (email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edt_1.setError("Please Enter Correct Email address");
                }
                if (email.isEmpty() && password.length() < 6) {
                    edt_2.setError("Please Enter Correct Password with Length Six Digit");
                } else
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Here is the code for verificatin of email
                              //  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                //if (user.isEmailVerified()) {
                                    //Move to Profile

                                   /* SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(EMAIL_KEY, edt_1.getText().toString());
                                    editor.putString(PASSWORD_KEY, edt_2.getText().toString());
                                    editor.apply();*/
                                    Intent intent = new Intent(MainActivity.this, Desi_Dashboard.class);
                                    Toast.makeText(MainActivity.this, "Yeah..!!Login Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();
                                    edt_1.setText("");
                                    edt_2.setText("");
                                //}
                        //    else {
                            //        user.sendEmailVerification();
                              //      Toast.makeText(MainActivity.this, "Check your email address to Verify Account..!:", Toast.LENGTH_SHORT).show();
                                    edt_1.setText("");
                                    edt_2.setText("");
                               // }
                            } else {
                                Toast.makeText(MainActivity.this, "Login Failed,Provide correct login details!!..!:", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            }
        });


    }

    /*@Override
    protected void onStart() {
        super.onStart();
        if (email != null && password != null) {
            Intent i = new Intent(MainActivity.this, Desi_Dashboard.class);
            startActivity(i);
        }*/
    //}
}