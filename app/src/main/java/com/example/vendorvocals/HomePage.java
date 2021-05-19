package com.example.vendorvocals;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.fonts.FontFamily;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class HomePage extends AppCompatActivity {

    ImageView Menu;
    ImageView ServicesAdder;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Menu = findViewById(R.id.menu);
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MenuIntent = new Intent(HomePage.this, MenuActivity.class);
                startActivity(MenuIntent);

            }
        });

        ServicesAdder = findViewById(R.id.ServicesAdder);
        ServicesAdder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ServicesIntent = new Intent(HomePage.this, AddActivity.class);
                startActivity(ServicesIntent);

            }
        });
        linearLayout = findViewById(R.id.linearLayout);
        SharedPreferences Username = getApplicationContext().getSharedPreferences("loggedIN", MODE_PRIVATE);
        String Phone = Username.getString("phn", "");
        System.out.println(Phone);

        FirebaseDatabase data = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = data.getReference("Vendors/"+Phone+"/sub/");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Sub> subList = new ArrayList<>();
                System.out.println(snapshot.getValue());
                subList.clear();
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                    Sub sub = dataSnapshot.getValue(Sub.class);
                    subList.add(sub);



                    RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                    params.height = 300;
                    params.rightMargin = 10;
                    params.topMargin = 10;
                    params.bottomMargin = 10;
                    params.leftMargin = 10;
                    relativeLayout.setBackgroundResource(R.drawable.location_bar_button);
                    relativeLayout.setLayoutParams(params);
                    linearLayout.addView(relativeLayout);
                    TableRow.LayoutParams txtParams = new TableRow.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                    TextView textView = new TextView(getApplicationContext());
                    textView.setId(R.id.textView);
                    textView.setPadding(70, 90, 100, 100);
                    textView.setTextColor(BLACK);
                    textView.setTextSize(30);
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    textView.setText(sub.getName());
                    textView.setLayoutParams(txtParams);
                    relativeLayout.addView(textView);
                    ImageView imageView = new ImageView(getApplicationContext());
                    relativeLayout.addView(imageView);
                    RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                    imgParams.height = 250;
                    imgParams.width = 250;
                    imgParams.topMargin = 45;
                    imgParams.rightMargin = 35;
                    imgParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    imgParams.addRule(RelativeLayout.LEFT_OF, R.id.textView);
                    imageView.setBackgroundResource(R.drawable.admin);
                    imageView.setLayoutParams(imgParams);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @IgnoreExtraProperties
    static class Sub{

        String name;

        String price;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}