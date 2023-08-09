package com.frutas;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.frutas.home.HomeActivity;

public class MainActivity extends AppCompatActivity {
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        handler.postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, HomeActivity.class),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        }, 1500);
    }

    @Override
    protected void onStop() {
        handler.removeCallbacksAndMessages(null);
        finishAffinity();
        super.onStop();
    }
}