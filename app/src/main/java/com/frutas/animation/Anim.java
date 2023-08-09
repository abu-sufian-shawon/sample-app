package com.frutas.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;

import com.frutas.R;

public class Anim extends android.view.animation.Animation {

    public static Animation animationHide(@NonNull View view){
        android.view.animation.Animation animationHide =
                AnimationUtils.loadAnimation(view.getContext(),
                R.anim.search_bar_gone);
        animationHide.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) { }

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) { }
        });

        return animationHide;
    }



    public static Animation animationShow(@NonNull View view) {

        android.view.animation.Animation animationShow = AnimationUtils.loadAnimation(view.getContext(),
                R.anim.search_bar_visible);


        animationShow.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {
            }
        });
        return animationShow;
    }
}
