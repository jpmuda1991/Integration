package com.tennis.cli.tenn.cus.tennisapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.tennis.cli.tenn.cus.tennisapp.Application.TennisCommon;
import com.tennis.cli.tenn.cus.tennisapp.R;

public class ForgetPassActivity extends TennisCommon {


    private AppCompatImageView backImg;
    private FirebaseAuth mAuth;
    private TextInputEditText emailEdit;
    private String email;
    private AppCompatButton sendNowBtn;
    private LottieAnimationView animationViewLottie;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        // tO SHOW ACTIVITY ON FULL SCREEN //
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mAuth = FirebaseAuth.getInstance();


        backImg = findViewById(R.id.back_img);

        emailEdit = findViewById(R.id.email);
        sendNowBtn = findViewById(R.id.send_now_btn);
        animationViewLottie = findViewById(R.id.animation_view);


        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoLoginActivity();
            }
        });

        sendNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailEdit.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    inFormUser(ForgetPassActivity.this, "Email is missing");
                    return;
                }

                if (email != null && email.contains(" ")) {
                    inFormUser(ForgetPassActivity.this, "Remove spaces from email");
                    return;
                }

                if(isNetWorkAvailable(ForgetPassActivity.this)){

                    exitReveal(sendNowBtn);

                    animationViewLottie.setVisibility(View.VISIBLE);
                    animationViewLottie.setAnimation(R.raw.loading);
                    animationViewLottie.playAnimation();


                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                if (animationViewLottie != null) {

                                    animationViewLottie.setVisibility(View.GONE);
                                }

                                enterReveal(sendNowBtn);

                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPassActivity.this,R.style.MyDialogTheme);

                                builder.setTitle("Check Your Email");
                                builder.setMessage("Please check your email in order to change your password");
                                builder.setCancelable(false);

                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (alertDialog != null){

                                            alertDialog.dismiss();
                                        }

                                        gotoLoginActivity();
                                    }

                                });

                                alertDialog =  builder.create();
                                alertDialog.show();

                            }else{

                                String error = task.getException().getMessage();
                                showSnackBar(sendNowBtn,error);
                            }
                        }
                    });


                }

            }
        });


    }


    private void gotoLoginActivity() {
        Intent signInIntent = new Intent(ForgetPassActivity.this, LoginActivity.class);
        signInIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(signInIntent);
        finish();
    }

}