package com.tennis.cli.tenn.cus.tennisapp.Fragments.Sheets;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.tennis.cli.tenn.cus.tennisapp.Activities.SignUpActivity;
import com.tennis.cli.tenn.cus.tennisapp.Application.TennisApp;
import com.tennis.cli.tenn.cus.tennisapp.Models.PaypalModel;
import com.tennis.cli.tenn.cus.tennisapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class BottomSheetPaypal extends BottomSheetDialogFragment implements View.OnClickListener {

    private PaypalModel paypalModel;
    private View itemView;
    private TextInputEditText emailEdit,passEdit;
    private AppCompatButton loginBtn;
    private AppCompatImageView back;


    public OnBottomSheetClick onBottomSheetClick;

    public interface OnBottomSheetClick{

        void onLoginClicked(AppCompatButton view,String email,String password);
        void onBackPaypalClicked();

    }

    public void setOnBottomSheetClick(OnBottomSheetClick onBottomSheetClick) {
        this.onBottomSheetClick = onBottomSheetClick;
    }

    public BottomSheetPaypal() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView =  inflater.inflate(R.layout.fragment_bottom_sheet_dialog_paypal, container, false);

        paypalModel  = TennisApp.getPaypalModel();

        back = itemView.findViewById(R.id.back);

        emailEdit = itemView.findViewById(R.id.email);
        passEdit = itemView.findViewById(R.id.password);

        loginBtn = itemView.findViewById(R.id.login_btn);

        if (paypalModel != null){

            emailEdit.setText(paypalModel.getEmail());
            passEdit.setText(paypalModel.getPassword());


        }

        back.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

        return itemView;

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.back:

                if (onBottomSheetClick != null){

                    onBottomSheetClick.onBackPaypalClicked();
                }


                break;

            case R.id.login_btn:

                String email = emailEdit.getText().toString().trim();
                String pass = passEdit.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getActivity(), "Email is missing", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(getActivity(), "Email is not valid", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(pass)){
                    Toast.makeText(getActivity(), "Password is missing", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.length() <= 8){
                    Toast.makeText(getActivity(), "Password length should be more then 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (onBottomSheetClick != null){

                    onBottomSheetClick.onLoginClicked(loginBtn,email,pass);
                }

                break;


            default:

                break;
        }
    }
}
