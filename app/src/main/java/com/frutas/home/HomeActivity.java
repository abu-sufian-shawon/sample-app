package com.frutas.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.frutas.R;
import com.frutas.home.view.FragmentHome;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setToolbar();
        // init view components

        //********** Construct Fragment ************\\
        FragmentHome fragmentHome = new FragmentHome();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragmentHome, FragmentHome.class.getSimpleName())
                .addToBackStack(FragmentHome.class.getSimpleName())
                .commit();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count <= 1) {
            finish();
        } else {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }


    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            ImageView ivBack = findViewById(R.id.ivBack);

            if (count > 1) ivBack.setVisibility(View.VISIBLE);
            else ivBack.setVisibility(View.GONE);

            ivBack.setOnClickListener(v -> getSupportFragmentManager().popBackStack());
        });




    }
}