package com.tennis.cli.tenn.cus.tennisapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.module.GlideModule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.tennis.cli.tenn.cus.tennisapp.Application.TennisCommon;
import com.tennis.cli.tenn.cus.tennisapp.R;
import com.tennis.cli.tenn.cus.tennisapp.Utils.GIFView;

public class LoginActivity extends TennisCommon {

    private AppCompatTextView signUpTxt;
    private TextInputEditText emailEdit,passEdit;
    private AppCompatButton loginBtn;
    private String email,pass;
    private LottieAnimationView animationViewLottie;
    private FirebaseAuth mAuth;
    private SharedPreferences login_preferences;
    private boolean isDefaultValue;
//    private String devGmail;
    private FirebaseFirestore firebaseFirestore;
    private String image,name;

    private AppCompatImageView gifView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getAppSharedPreferences();

        // tO SHOW ACTIVITY ON FULL SCREEN //
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mAuth = FirebaseAuth.getInstance();

//        img = findViewById(R.id.imag);
        firebaseFirestore = FirebaseFirestore.getInstance();

        signUpTxt = findViewById(R.id.sign_uptxt);
//        forgetPassTxt = findViewById(R.id.forget_password);

        emailEdit = findViewById(R.id.email);
        passEdit = findViewById(R.id.password);

        loginBtn = findViewById(R.id.login_btn);
        animationViewLottie = findViewById(R.id.animation_view);

        gifView = findViewById(R.id.gif);


        Glide.with(LoginActivity.this).load("https://i.pinimg.com/originals/72/d0/87/72d08779db9cbf8b94312ba301b737eb.gif").into(gifView);
//        gifView.loadGIFResource(LoginActivity.this,R.drawable.start);

        signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoSignUpActivity();

            }
        });

//        forgetPassTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                gotoForgetPassActivity();
//
//            }
//        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailEdit.getText().toString().trim();
                pass = passEdit.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    inFormUser(LoginActivity.this,"Email is missing");
                    return;
                }

                if (email != null && email.contains(" ")){
                    inFormUser(LoginActivity.this,"Remove spaces from email");
                    return;
                }

                if (TextUtils.isEmpty(pass)){
                    inFormUser(LoginActivity.this,"Password is missing");
                    return;
                }

                if (pass != null && pass.contains(" ")){
                    inFormUser(LoginActivity.this,"Remove spaces from pass");
                    return;
                }

                if (pass != null && pass.length() < 6){
                    inFormUser(LoginActivity.this,"Password should be atleast of 6 characters");
                    return;
                }

                if (isNetWorkAvailable(LoginActivity.this)) {

                    exitReveal(loginBtn);

                    animationViewLottie.setVisibility(View.VISIBLE);
                    animationViewLottie.setAnimation(R.raw.loading);
                    animationViewLottie.playAnimation();


                    if (mAuth != null){

                        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){

                                    if (animationViewLottie != null) {

                                            animationViewLottie.setVisibility(View.GONE);
                                    }

                                    enterReveal(loginBtn);
                                    login_preferences.edit().putBoolean("FLAG", true).apply();

                                    gotoMainActivity();

//                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

//                                    if(firebaseUser != null && firebaseUser.isEmailVerified()){
//
//                                        String user_id = firebaseUser.getUid();
//
//
//
//                                    }else{
//
//                                        if (animationViewLottie != null) {
//
//                                            animationViewLottie.setVisibility(View.GONE);
//                                        }
//
//                                        enterReveal(loginBtn);
//
//
//                                        login_preferences.edit().putBoolean("FLAG", false).apply();
//                                        showSnackBar(loginBtn,"Your email is not verified");
//                                    }


                                }else{

                                    if (animationViewLottie != null) {

                                        animationViewLottie.setVisibility(View.GONE);
                                    }

                                    enterReveal(loginBtn);

                                    String error = task.getException().getMessage();
                                    showSnackBar(loginBtn,error);

                                }
                            }
                        });
                    }


                }

            }
        });
    }

//    private void gotoUserFormActivity() {
//
//        Intent mainIntent = new Intent(LoginActivity.this, DriverFormActivity.class);
//        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(mainIntent);
//        finish();
//
//    }

    private void gotoSignUpActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(mainIntent);
    }

    private void gotoForgetPassActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, ForgetPassActivity.class);
        startActivity(mainIntent);
    }

    private void gotoMainActivity() {

        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();
    }

    private void getAppSharedPreferences() {

        // Retrieving shared Preferences here //

        login_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isDefaultValue = login_preferences.getBoolean("FLAG", false);

//        gmail_preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        devGmail = gmail_preferences.getString(getResources().getString(R.string.preferences_gmail),"dummy@gmail.com");

    }
}