package com.example.vendorvocals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AdminMenuActivity extends AppCompatActivity {
    TextView vendorverification;
    TextView report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        TextView mvendorverification = (TextView) findViewById(R.id.VendorVerification);
        mvendorverification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vendorverificationIntent = new Intent(AdminMenuActivity.this, VendorVerification.class);
                startActivity(vendorverificationIntent);

            }
        });

        TextView mreport = (TextView) findViewById(R.id.Report);
        mreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reportIntent = new Intent(AdminMenuActivity.this, ReportActivity.class);
                startActivity(reportIntent);

            }
        });

    }
}