package com.example.vendorvocals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    EditText subService, Price;
    Button AddService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        subService = findViewById(R.id.Sub);
        Price = findViewById(R.id.Price);

        AddService = findViewById(R.id.AddService);





        AddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subServiceName, subServicePrice;

                subServiceName = subService.getText().toString().trim();
                subServicePrice = Price.getText().toString().trim();

                if (subServiceName.isEmpty()){
                    Toast.makeText(AddActivity.this, "Enter Sub-Service Name.",Toast.LENGTH_SHORT).show();
                }else {
                    if (subServicePrice.isEmpty()){
                        Toast.makeText(AddActivity.this,"Enter Price.",Toast.LENGTH_SHORT).show();
                    }else {
                        verify verify = new verify(getApplicationContext());
                        if (verify.isNetworkConnected()){
                            SharedPreferences Username = getApplicationContext().getSharedPreferences("loggedIN", MODE_PRIVATE);
                            String Phone = Username.getString("phn", "");

                            DatabaseReference AdData ;
                            AdData = FirebaseDatabase.getInstance().getReference();

                            subService subService = new subService(subServiceName, subServicePrice);
                            Map<String, Object> sub = subService.toMap();
                            AdData.child("Vendors").child(Phone).child("sub").child(subServiceName).updateChildren(sub);

                            Toast.makeText(AddActivity.this, "Service Is Added.",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddActivity.this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();



                        }else{
                            Toast.makeText(AddActivity.this, "Network Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }
    @IgnoreExtraProperties
    public static class subService{
        String subName, subPrice;

        subService(){

        }
        subService(String subName, String subPrice){
            this.subName = subName;
            this.subPrice = subPrice;
        }

        @Exclude
        public Map<String, Object> toMap(){
            HashMap<String, Object> result = new HashMap<>();
            result.put("name", subName);
            result.put("price", subPrice);

            return result;
        }
    }
}