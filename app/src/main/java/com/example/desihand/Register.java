package com.example.desihand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText edt_1,edt_2,edt_3,edt_4,edt_5,edt_6;
    Button btn_signUp;
    String name,email,city,address,pass,number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        edt_3=findViewById(R.id.Edtcity);
        edt_2=findViewById(R.id.Edtemail);
        edt_1=findViewById(R.id.Edtname);
        edt_4=findViewById(R.id.Edtaddress);
        edt_5=findViewById(R.id.Edtpass);
        edt_6=findViewById(R.id.Edtnum);
        btn_signUp=findViewById(R.id.btn_signUp);

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                email=edt_2.getText().toString().trim();
                city =edt_3.getText().toString().trim();
                address =edt_4.getText().toString().trim();
                name= edt_1.getText().toString().trim();
                pass= edt_5.getText().toString().trim();
                number= edt_6.getText().toString().trim();

                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(pass) && pass.length() < 6 && TextUtils.isEmpty(city)  && TextUtils.isEmpty(email) && TextUtils.isEmpty(number) && number.length()<10 )
                {
                    Toast.makeText(Register.this, "All Fields are Mandatory,Please add details..!:", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                userdetails users=new userdetails(name,email,city,address,pass,number);
                                FirebaseDatabase.getInstance().getReference("userdetails").child(FirebaseAuth.
                                        getInstance().getCurrentUser().getUid()).setValue(users).
                                        addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Register.this, "Successfully Register, Please Login..!!.", Toast.LENGTH_SHORT).show();
                                                    Intent intent= new Intent(Register.this,MainActivity.class);
                                                    String str = edt_1.getText().toString();
                                                    intent.putExtra("message_key", str);
                                                    startActivity(intent);
                                                    edt_1.setText("");
                                                    edt_2.setText("");
                                                    edt_3.setText("");
                                                    edt_4.setText("");
                                                    edt_5.setText("");
                                                    edt_6.setText("");
                                                    finish();
                                                }else{
                                                    Toast.makeText(Register.this, "Register Failed!!.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }else{
                                Toast.makeText(Register.this, "Register Failed!!.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }
}