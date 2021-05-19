package com.example.vendorvocals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button forgetPassword;
    Button Login;
    MaterialButton newUser;
    TextView phn , password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences Username = getApplicationContext().getSharedPreferences("loggedIN", MODE_PRIVATE);
        String session = Username.getString("uName", "");
        if (!session.equals("")){
            startActivity(new Intent(LoginActivity .this, HomePage.class));
            finish();
        }else{
            System.out.println("hello");
        }

        forgetPassword = findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ForgetPasswordIntent = new Intent(LoginActivity.this, SendOTP.class);
                startActivity(ForgetPasswordIntent);

            }
        });

        phn = findViewById(R.id.Phn);
        password = findViewById(R.id.Password);



        Login =findViewById(R.id.Login);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uPhn = phn.getText().toString().trim();
                String pass = password.getText().toString().trim();

                String hashedPass = hashPass.hashedPass(pass);

                if (uPhn.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Enter Your Phone Number.",Toast.LENGTH_SHORT).show();
                }else{
                    if (uPhn.length() != 10){
                        Toast.makeText(LoginActivity.this, "Invalid Phone Number",Toast.LENGTH_SHORT).show();
                    }else {
                        if (pass.isEmpty()){
                            Toast.makeText(LoginActivity.this, "Enter Your Password.",Toast.LENGTH_SHORT).show();
                        }else{
                            verify verify = new verify(getApplicationContext());
                            if (verify.isNetworkConnected()){
                                FirebaseDatabase mFData = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = mFData.getReference("Vendors/"+uPhn);
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Map<String, Object> objectMap = (Map<String, Object>) snapshot.getValue();
                                        String name = (String) objectMap.get("fullName");
                                        String password = (String) objectMap.get("password");
                                        String Category = (String) objectMap.get("Category");
                                        String MinCharge = (String) objectMap.get("Charges");
                                        String Phone = (String) objectMap.get("phn");
                                        String Email = (String) objectMap.get("email");


                                        if (hashedPass.equals(password)){
                                            SharedPreferences userDetails = getSharedPreferences("loggedIN", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = userDetails.edit();
                                            editor.putString("uName", name);
                                            editor.putString("Category", Category);
                                            editor.putString("phn", Phone);
                                            editor.putString("email", Email);
                                            editor.putString("Charges",MinCharge);

                                            editor.apply();
                                            startActivity(new Intent(LoginActivity.this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                            finish();
                                        }else{
                                            Toast.makeText(LoginActivity.this,"Wrong Username Or Password",Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }else{
                                Toast.makeText(LoginActivity.this, "Network Error",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }



            }
        });

        newUser = findViewById(R.id.newUser);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SignUpIntent = new Intent(LoginActivity.this , SignUp.class);
                startActivity(SignUpIntent);
            }
        });


    }
}