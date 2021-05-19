package com.example.vendorvocals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AdminPanelActivity extends AppCompatActivity {
    ImageView adminMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        ImageView madminMenu = (ImageView) findViewById(R.id.AdminMenu);
        madminMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adminMenuIntent = new Intent(AdminPanelActivity.this, AdminMenuActivity.class);
                startActivity(adminMenuIntent);

            }
        });

    }
}