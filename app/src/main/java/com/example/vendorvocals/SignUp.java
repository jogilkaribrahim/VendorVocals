package com.example.vendorvocals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    Button login, SignUp;
    AutoCompleteTextView autoCompleteTextView;
    TextView fullName, Email, Charges, Phone, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SignUpIntent = new Intent(SignUp.this, LoginActivity.class);
                startActivity(SignUpIntent);
                finish();
            }
        });

        FirebaseDatabase Services = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = Services.getReference("Services/");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<HashMap<String, Object>> ObGTY = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                Map<String, Object> ObjectMap = snapshot.getValue(ObGTY);
                ArrayList<Object> Values = new ArrayList<Object>(ObjectMap.values());

                autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
                String[] option = Values.toArray(new String[0]);
                ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, option);
                autoCompleteTextView.setText(arrayAdapter.getItem(0).toString(), false);

                autoCompleteTextView.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        SignUp = findViewById(R.id.SignUp);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullName = findViewById(R.id.name);
                Email = findViewById(R.id.Email);
                autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
                Charges = findViewById(R.id.Charges);
                Phone = findViewById(R.id.Phone);
                Password = findViewById(R.id.Password);

                String uName, uEmail, uCharges, uCategory, uPhn, uAddress, uPassword;

                uName = fullName.getText().toString().trim();
                uEmail = Email.getText().toString().trim();
                uCharges = Charges.getText().toString().trim();
                uCategory = autoCompleteTextView.getText().toString().trim();
                uPhn = Phone.getText().toString().trim();
                uPassword = Password.getText().toString().trim();
                String hashedPass;

                hashedPass = hashPass.hashedPass(uPassword);

                String regex = "^(.+)@(.+)$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(uEmail);

                if (uName.isEmpty()) {
                    Toast.makeText(SignUp.this, "Enter Your Name.", Toast.LENGTH_SHORT).show();
                } else {
                    if (uEmail.isEmpty()) {
                        Toast.makeText(SignUp.this, "Enter Your Email ID.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!matcher.matches()) {
                            Toast.makeText(SignUp.this, "Enter Valid Email ID.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (uCharges.isEmpty()) {
                                Toast.makeText(SignUp.this, "Charges Are Mandatory.", Toast.LENGTH_SHORT).show();
                            } else {
                                if (uPhn.isEmpty()) {
                                    Toast.makeText(SignUp.this, "Enter Your Phone Number", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (uPhn.length() < 10) {
                                        Toast.makeText(SignUp.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (uPhn.length() > 10) {
                                            Toast.makeText(SignUp.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (uPassword.isEmpty()) {
                                                Toast.makeText(SignUp.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (uPassword.length() < 8) {
                                                    Toast.makeText(SignUp.this, "Enter Minimum 8 Digit Password", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    verify verify = new verify(getApplicationContext());
                                                    if (verify.isNetworkConnected()) {
                                                        DatabaseReference Data;
                                                        Data = FirebaseDatabase.getInstance().getReference();

                                                        Data.child("Vendors").child(uPhn).child("phn").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                                String firePhn = String.valueOf(task.getResult().getValue()).trim();
                                                                if (!task.isSuccessful()) {
                                                                    Toast.makeText(SignUp.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    if (uPhn.equals(firePhn)) {
                                                                        Toast.makeText(SignUp.this, "User Already Exist.", Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        DatabaseReference uData;
                                                                        uData = FirebaseDatabase.getInstance().getReference();
                                                                        String sub = "";
                                                                        Vendor vendor = new Vendor(uName, uEmail, uCategory, uCharges, uPhn, sub, hashedPass);
                                                                        uData.child("Vendors").child(uPhn).setValue(vendor);
                                                                        Toast.makeText(SignUp.this, "Please Login", Toast.LENGTH_SHORT).show();
                                                                        startActivity(new Intent(SignUp.this, LoginActivity.class));
                                                                    }
                                                                }

                                                            }
                                                        });
                                                    } else {
                                                        Toast.makeText(SignUp.this, "Network Error", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        });
    }
    @IgnoreExtraProperties
    public static class Vendor {
        public String fullName;
        public String email;
        public String Category;
        public String Charges;
        public String phn;
        public String sub;
        public String password;

        public Vendor() {

        }
        public Vendor(String fullName, String email, String category, String charges, String phn,String sub, String password) {
            this.fullName = fullName;
            this.email = email;
            Category = category;
            Charges = charges;
            this.phn = phn;
            this.sub = sub;
            this.password = password;
        }

    }
}
