package com.example.vendorvocals;

import android.content.Context;
import android.net.ConnectivityManager;

public class verify {
    Context context;

    verify(){

    }
    verify(Context context){
        this.context = context;
    }

    public boolean isNetworkConnected(){

        ConnectivityManager Connection = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Connection.getActiveNetworkInfo() != null && Connection.getActiveNetworkInfo().isConnected();
    }
}
