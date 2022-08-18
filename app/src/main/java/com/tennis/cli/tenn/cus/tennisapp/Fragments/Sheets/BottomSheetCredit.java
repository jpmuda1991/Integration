package com.tennis.cli.tenn.cus.tennisapp.Fragments.Sheets;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tennis.cli.tenn.cus.tennisapp.Application.TennisApp;
import com.tennis.cli.tenn.cus.tennisapp.Application.TennisCommon;
import com.tennis.cli.tenn.cus.tennisapp.Models.CreditModel;
import com.tennis.cli.tenn.cus.tennisapp.Models.PaypalModel;
import com.tennis.cli.tenn.cus.tennisapp.R;

public class BottomSheetCredit extends BottomSheetDialogFragment implements View.OnClickListener {

    private CreditModel creditModel;
    private View itemView;
    private AppCompatImageView back;
    private TextInputLayout creditCardLayout,mmLayout,yyLayout,cvvLayout,fullNameLayout,addressLayout;
    private TextInputEditText creditNumEdit,fullNameEdit,addressEdit,mmEdit,yyEdit,cvvEdit;
    private AppCompatButton loginBtn;
    private RadioGroup paymentGrp;
    private String visaOrMaster = "";
    private TennisCommon tennisCommon;
    private AppCompatRadioButton visaRad,masterRad;


    public OnBottomSheetCreditClick onBottomSheetCreditClick;

    public interface OnBottomSheetCreditClick{

        void onCreditAddClicked(AppCompatButton view,String type,String cNum,String mm,String yy, String cvv,String fullName,String address);
        void onCreditBackIsClicked();

    }


    public void setOnBottomSheetCreditClick(OnBottomSheetCreditClick onBottomSheetCreditClick) {
        this.onBottomSheetCreditClick = onBottomSheetCreditClick;
    }

    public BottomSheetCredit() {
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
        itemView =  inflater.inflate(R.layout.fragment_bottom_sheet_dialog_credit, container, false);

        tennisCommon = new TennisCommon();

        creditModel  = TennisApp.getCreditModel();

        back = itemView.findViewById(R.id.back);

        creditCardLayout = itemView.findViewById(R.id.c_card_lyt);
        mmLayout = itemView.findViewById(R.id.mm_lyt);
        yyLayout = itemView.findViewById(R.id.yy_lyt);
        cvvLayout = itemView.findViewById(R.id.cvv_lyt);
        fullNameLayout = itemView.findViewById(R.id.name_lyt);
        addressLayout = itemView.findViewById(R.id.address_lyt);

        paymentGrp = itemView.findViewById(R.id.payment_grp);
        visaRad = itemView.findViewById(R.id.visa);
        masterRad = itemView.findViewById(R.id.master);

        creditNumEdit = itemView.findViewById(R.id.cnumber);
        mmEdit = itemView.findViewById(R.id.month);
        yyEdit = itemView.findViewById(R.id.year);
        cvvEdit = itemView.findViewById(R.id.cvv);

        fullNameEdit = itemView.findViewById(R.id.full_name);
        addressEdit = itemView.findViewById(R.id.address);

        loginBtn = itemView.findViewById(R.id.login_btn);

        if (creditModel != null){

            if (creditModel.getType().contentEquals("visa")){

                visaOrMaster = "visa";

                visaRad.setChecked(true);


            }else{

                visaOrMaster = "master";
                masterRad.setChecked(true);
            }

            creditNumEdit.setText(creditModel.getCard_nmber());
            mmEdit.setText(creditModel.getMm());
            yyEdit.setText(creditModel.getYy());
            cvvEdit.setText(creditModel.getCvv());
            fullNameEdit.setText(creditModel.getFullname());
            addressEdit.setText(creditModel.getAddress());
        }

        visaRad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                visaOrMaster  = "visa";
            }
        });

        masterRad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                visaOrMaster  = "master";
            }
        });

        back.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

        return itemView;

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.back:

                if (onBottomSheetCreditClick != null){

                    onBottomSheetCreditClick.onCreditBackIsClicked();
                }


                break;

            case R.id.login_btn:

                int id  = paymentGrp.getCheckedRadioButtonId();

                if (TextUtils.isEmpty(visaOrMaster)){
                    tennisCommon.inFormUser(getActivity(),"Select card type");
                    return;
                }

                if (id == R.id.visa){

                    visaOrMaster = "visa";

                }else{

                    visaOrMaster = "master";

                }

                String card = creditNumEdit.getText().toString().trim();
                String mm = mmEdit.getText().toString().trim();
                String yy = yyEdit.getText().toString().trim();
                String cvv = cvvEdit.getText().toString().trim();
                String fullName = fullNameEdit.getText().toString().trim();
                String address = addressEdit.getText().toString().trim();


                if (TextUtils.isEmpty(card)){
                    creditCardLayout.setError("Card Number is missing");
                    return;
                }

                if (card.length() != 12){
                    creditCardLayout.setError("Card Number is not valid");
                    return;
                }

                creditCardLayout.setError(null);

                if (TextUtils.isEmpty(mm)){
                    mmLayout.setError("missing");
                    return;
                }


                int mmInt = Integer.parseInt(mmEdit.getText().toString().trim());

                if (mmInt <= 0){
                    mmLayout.setError("invalid");
                    return;
                }

                if (mmInt > 12){
                    mmLayout.setError("invalid");
                    return;
                }


                mmLayout.setError(null);

                if (TextUtils.isEmpty(yy)){
                    yyLayout.setError("missing");
                    return;
                }


                int myynt = Integer.parseInt(yyEdit.getText().toString().trim());

                if (myynt < 19){
                    yyLayout.setError("invalid");
                    return;
                }

                if (myynt > 30){
                    yyLayout.setError("invalid");
                    return;
                }


                yyLayout.setError(null);

                if (TextUtils.isEmpty(cvv)){
                    cvvLayout.setError("missing");
                    return;
                }

                cvvLayout.setError(null);

                if (TextUtils.isEmpty(fullName)){
                    fullNameLayout.setError("Full name is missing");
                    return;
                }

                fullNameLayout.setError(null);

                if (TextUtils.isEmpty(address)){
                    addressLayout.setError("Address is missing");
                    return;
                }


                addressLayout.setError(null);
                if (onBottomSheetCreditClick != null){

                    onBottomSheetCreditClick.onCreditAddClicked(loginBtn,visaOrMaster,card,mm,yy,cvv,fullName,address);
                }

                break;


            default:

                break;
        }
    }
}
