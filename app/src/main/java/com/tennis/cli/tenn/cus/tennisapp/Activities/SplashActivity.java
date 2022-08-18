package com.tennis.cli.tenn.cus.tennisapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.tennis.cli.tenn.cus.tennisapp.Application.TennisApp;
import com.tennis.cli.tenn.cus.tennisapp.Models.StatisModels.Top;
import com.tennis.cli.tenn.cus.tennisapp.Models.StatisModels.TopTwnModels;
import com.tennis.cli.tenn.cus.tennisapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private SharedPreferences login_preferences;
    private boolean isDefaultValue;
    private ConstraintLayout constraintLayout;
    private FirebaseFirestore firebaseFirestore;


    private LottieAnimationView animationViewLottie;
    private List<TopTwnModels> topTwnModelsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSharedPreferences();

        firebaseFirestore = FirebaseFirestore.getInstance();

        // tO SHOW ACTIVITY ON FULL SCREEN //
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        animationViewLottie = findViewById(R.id.animation_view);

        topTwnModelsList = new ArrayList<>();

        animationViewLottie.setAnimation(R.raw.tennis_loading);
        animationViewLottie.playAnimation();

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {

                    sleep(4000);

                    if (isDefaultValue){

                        mAuth = FirebaseAuth.getInstance();

                        gotoMainActivity();


//                        try {
//
//                            JSONObject jsonObject = new JSONObject(topMaleJson);
//                            JSONArray arr = jsonObject.getJSONArray("rankings");
//
//                            System.out.println("ARRAY LENGTH IS: " + arr.length());
//                            for (int i = 0 ; i < arr.length(); i++){
//
//                                JSONObject jsonObject1 = (JSONObject) arr.get(i);
//                                String name = jsonObject1.getString("name");
//                                String dob = jsonObject1.getString("dob");
//                                String abbr = jsonObject1.getString("abbr");
//
//                                topTwnModelsList.add(new TopTwnModels(name,dob,abbr));
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                        TennisApp.setTopTwnModelsList(topTwnModelsList);



                    }else{

                        gotoLoginActivity();


//                        try {
//
//                            JSONObject jsonObject = new JSONObject(topMaleJson);
//                            JSONArray arr = jsonObject.getJSONArray("rankings");
//
//                            System.out.println("ARRAY LENGTH IS: " + arr.length());
//                            for (int i = 0 ; i < arr.length(); i++){
//
//                                JSONObject jsonObject1 = (JSONObject) arr.get(i);
//                                String name = jsonObject1.getString("name");
//                                String dob = jsonObject1.getString("dob");
//                                String abbr = jsonObject1.getString("abbr");
//
//                                topTwnModelsList.add(new TopTwnModels(name,dob,abbr));
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                        TennisApp.setTopTwnModelsList(topTwnModelsList);
//
////                        gotoMainActivity();

                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }


    private void gotoMainActivity() {

        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();
    }

    private void gotoLoginActivity() {

        Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
        finish();
    }


    public void getSharedPreferences(){

        login_preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        isDefaultValue = login_preferences.getBoolean("FLAG",false);

    }


}