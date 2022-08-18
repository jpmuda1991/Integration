package com.tennis.cli.tenn.cus.tennisapp.Fragments;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.tennis.cli.tenn.cus.tennisapp.Activities.MainActivity;
import com.tennis.cli.tenn.cus.tennisapp.Adapters.TopMaleAdapter;
import com.tennis.cli.tenn.cus.tennisapp.Application.TennisApp;
import com.tennis.cli.tenn.cus.tennisapp.Application.TennisCommon;
import com.tennis.cli.tenn.cus.tennisapp.Models.PlayersModel;
import com.tennis.cli.tenn.cus.tennisapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PlayersFragment extends Fragment {

    private View view;
    private TextInputEditText playerEdit;
    private AppCompatAutoCompleteTextView countryAutoComplete;
    private ArrayAdapter<String> countriesAdapter;
    private AppCompatButton enterBtn;
    private RadioGroup genderGrp;
    private AppCompatRadioButton mRBtn,fRBtn,bRbtn;
    private RecyclerView recyclerView;
    private TopMaleAdapter topMaleAdapter;
    private String toSearchName = "";
    private String togenderSearch = "";
    private List<String> countryNames;
    private String selectedCountry;
    private TennisCommon tennisCommon;

    private List<PlayersModel> playersModelList;




    public PlayersFragment() {
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
        view  = inflater.inflate(R.layout.fragment_players, container, false);

        tennisCommon = new TennisCommon();

        playersModelList = new ArrayList<>();

        countryNames = new ArrayList<>();

        playerEdit = view.findViewById(R.id.pl_name);
        countryAutoComplete = view.findViewById(R.id.country_autocomplete);
        enterBtn = view.findViewById(R.id.enter_btn);
        genderGrp = view.findViewById(R.id.gender_grp);
        mRBtn = view.findViewById(R.id.m_rBtn);
        fRBtn = view.findViewById(R.id.w_rBtn);
        bRbtn = view.findViewById(R.id.b_rBtn);

//        if(Build.VERSION.SDK_INT >= 21)
//        {
//            ColorStateList colorStateList = new ColorStateList(
//                    new int[][]
//                            {
//                                    new int[]{-android.R.attr.state_enabled}, // Disabled
//                                    new int[]{android.R.attr.state_enabled}   // Enabled
//                            },
//                    new int[]
//                            {
//                                    Color.BLACK, // disabled
//                                    Color.BLUE   // enabled
//                            }
//            );
//
//            mRBtn.setButtonTintList(colorStateList); // set the color tint list
//            fRBtn.setButtonTintList(colorStateList); // set the color tint list
//            bRbtn.setButtonTintList(colorStateList); // set the color tint list
//
////            fRBtn.invalidate(); // Could not be necessary
//        }

        recyclerView = view.findViewById(R.id.s_pRView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        topMaleAdapter = new TopMaleAdapter(getActivity());
        recyclerView.setAdapter(topMaleAdapter);


        topMaleAdapter.setOnItemClickListener(new TopMaleAdapter.OnItemClickListener() {
            @Override
            public void onFlagIsClicked(PlayersModel playersModel) {

                if (MainActivity.getNavController() != null && MainActivity.getNavController().getCurrentDestination().getId() == R.id.nav_players){

                    TennisApp.setSamelMaleOrFemale(togenderSearch);

                    TennisApp.setSameCountry(playersModel.getCountry());

                    TennisApp.setSameCityFrom("PLAYERS");

                    MainActivity.getNavController().navigate(R.id.action_nav_players_to_sameCtyPlayersFragment);
                }


            }

            @Override
            public void onPlayerClicked(PlayersModel playersModel) {

                System.out.println("MALE CARD IS CLICKED");

                if (MainActivity.getNavController() != null && MainActivity.getNavController().getCurrentDestination().getId() == R.id.nav_players){

                    TennisApp.setFrom("PLAYERS");
                    TennisApp.setSelectedPlayerModel(playersModel);
                    MainActivity.getNavController().navigate(R.id.action_nav_players_to_profileFragment);
                }

            }
        });


        mRBtn.setChecked(true);
        togenderSearch = "MALE";

        if (TennisApp.getMaleRankingsList() != null && TennisApp.getMaleRankingsList().size() > 0){

            if (countryNames.size() > 0){

                countryNames.clear();
            }

            selectedCountry = "";
            togenderSearch = "MALE";

            ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Preparing countries....");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            for (int i = 0 ; i < TennisApp.getMaleRankingsList().size() ; i++){

                if (!countryNames.contains(TennisApp.getMaleRankingsList().get(i).getCompetitor().getCountry())){
                    countryNames.add(TennisApp.getMaleRankingsList().get(i).getCompetitor().getCountry());
                }
            }


            if(dialog.isShowing()) {
                dialog.dismiss();
            }

           Collections.sort(countryNames,String.CASE_INSENSITIVE_ORDER);

            for (int i = 0; i < countryNames.size(); i++){

                System.out.println("index: " + i + " " + countryNames.get(i));
            }

            countriesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,countryNames);
            countryAutoComplete.setAdapter(countriesAdapter);


            countryAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    selectedCountry = adapterView.getItemAtPosition(i).toString();

                }
            });
        }


        mRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TennisApp.getMaleRankingsList() != null && TennisApp.getMaleRankingsList().size() > 0){

                    if (countryNames.size() > 0){

                        countryNames.clear();
                    }

                    selectedCountry = "";
                    togenderSearch = "MALE";

                    ProgressDialog dialog = new ProgressDialog(getActivity());
                    dialog.setMessage("Preparing countries....");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    for (int i = 0 ; i < TennisApp.getMaleRankingsList().size() ; i++){

                        if (!countryNames.contains(TennisApp.getMaleRankingsList().get(i).getCompetitor().getCountry())){
                            countryNames.add(TennisApp.getMaleRankingsList().get(i).getCompetitor().getCountry());
                        }
                    }


                    if(dialog.isShowing()) {
                        dialog.dismiss();
                    }


                    Collections.sort(countryNames,String.CASE_INSENSITIVE_ORDER);

                    for (int i = 0; i < countryNames.size(); i++){

                        System.out.println("index: " + i + " " + countryNames.get(i));
                    }

                    countriesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,countryNames);
                    countryAutoComplete.setAdapter(countriesAdapter);


                    countryAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            selectedCountry = adapterView.getItemAtPosition(i).toString();

                        }
                    });
                }
            }
        });


        fRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (TennisApp.getFemaleRankingsList() != null && TennisApp.getFemaleRankingsList().size() > 0){

                    if (countryNames.size() > 0){

                        countryNames.clear();
                    }

                    selectedCountry = "";
                    togenderSearch = "FEMALE";

                    ProgressDialog dialog = new ProgressDialog(getActivity());
                    dialog.setMessage("Preparing countries....");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    for (int i = 0 ; i < TennisApp.getFemaleRankingsList().size() ; i++){

                        if (!countryNames.contains(TennisApp.getFemaleRankingsList().get(i).getCompetitor().getCountry())) {
                            countryNames.add(TennisApp.getFemaleRankingsList().get(i).getCompetitor().getCountry());
                        }
                    }


                    if(dialog.isShowing()) {
                        dialog.dismiss();
                    }


                    Collections.sort(countryNames,String.CASE_INSENSITIVE_ORDER);

                    for (int i = 0; i < countryNames.size(); i++){

                        System.out.println("index: " + i + " " + countryNames.get(i));
                    }

                    countriesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,countryNames);
                    countryAutoComplete.setAdapter(countriesAdapter);


                    countryAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            selectedCountry = adapterView.getItemAtPosition(i).toString();

                        }
                    });
                }

            }
        });


        bRbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (TennisApp.getMaleRankingsList() != null && TennisApp.getMaleRankingsList().size() > 0){

                    if (countryNames.size() > 0){

                        countryNames.clear();
                    }

                    selectedCountry = "";
                    togenderSearch = "BOTH";

                    ProgressDialog dialog = new ProgressDialog(getActivity());
                    dialog.setMessage("Preparing countries....");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    for (int i = 0 ; i < TennisApp.getFemaleRankingsList().size() ; i++){

                        if (!countryNames.contains(TennisApp.getFemaleRankingsList().get(i).getCompetitor().getCountry())) {
                            countryNames.add(TennisApp.getFemaleRankingsList().get(i).getCompetitor().getCountry());
                        }
                    }

                    for (int i = 0 ; i < TennisApp.getMaleRankingsList().size() ; i++){

                        if (!countryNames.contains(TennisApp.getMaleRankingsList().get(i).getCompetitor().getCountry())) {
                            countryNames.add(TennisApp.getMaleRankingsList().get(i).getCompetitor().getCountry());
                        }
                    }


                    if(dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    Collections.sort(countryNames,String.CASE_INSENSITIVE_ORDER);

                    for (int i = 0; i < countryNames.size(); i++){

                        System.out.println("index: " + i + " " + countryNames.get(i));
                    }

                    countriesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,countryNames);
                    countryAutoComplete.setAdapter(countriesAdapter);


                    countryAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            selectedCountry = adapterView.getItemAtPosition(i).toString();

                        }
                    });
                }

            }
        });

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toSearchName = playerEdit.getText().toString().trim();
                selectedCountry  = countryAutoComplete.getText().toString().trim();

                if (TextUtils.isEmpty(toSearchName)){
                    tennisCommon.inFormUser(getActivity(),"Name is missing");
                    return;
                }

                if (TextUtils.isEmpty(togenderSearch)){
                    tennisCommon.inFormUser(getActivity(),"Gender is missing");
                    return;
                }


                int id = genderGrp.getCheckedRadioButtonId();

                if (id == R.id.m_rBtn){

                    togenderSearch = "MALE";

                }else if (id == R.id.b_rBtn){

                    togenderSearch = "BOTH";

                }else{

                    togenderSearch = "FEMALE";

                }

                if (playersModelList != null && playersModelList.size() > 0){

                    playersModelList.clear();
                }

                if (togenderSearch.contentEquals("MALE")){

                    for (int i = 0 ; i < TennisApp.getMaleRankingsList().size() ; i++){

                        if (!TextUtils.isEmpty(selectedCountry)){

                            if (TennisApp.getMaleRankingsList().get(i).getCompetitor().getCountry().contentEquals(selectedCountry)){

                                System.out.println("CFL: " + TennisApp.getMaleRankingsList().get(i).getCompetitor().getCountry() + " CS: " + selectedCountry);

                                if (TennisApp.getMaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contains(toSearchName.toLowerCase()) || TennisApp.getMaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contentEquals(toSearchName.toLowerCase())){

                                    System.out.println("PNFL: " + TennisApp.getMaleRankingsList().get(i).getCompetitor().getName().toLowerCase() + " PS: " + toSearchName.toLowerCase());

                                    playersModelList.add(new PlayersModel(TennisApp.getMaleRankingsList().get(i).getCompetitor().getId(),"","",TennisApp.getMaleRankingsList().get(i).getCompetitor().getName(),"",TennisApp.getMaleRankingsList().get(i).getPoints()+"",TennisApp.getMaleRankingsList().get(i).getCompetitor().getCountry(),"M"));
                                }
                            }

                        }else{

                            if (TennisApp.getMaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contains(toSearchName.toLowerCase()) || TennisApp.getMaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contentEquals(toSearchName.toLowerCase())){

                                System.out.println("PNFL: " + TennisApp.getMaleRankingsList().get(i).getCompetitor().getName().toLowerCase() + " PS: " + toSearchName.toLowerCase());

                                playersModelList.add(new PlayersModel(TennisApp.getMaleRankingsList().get(i).getCompetitor().getId(),"","",TennisApp.getMaleRankingsList().get(i).getCompetitor().getName(),"",TennisApp.getMaleRankingsList().get(i).getPoints()+"",TennisApp.getMaleRankingsList().get(i).getCompetitor().getCountry(),"M"));
                            }
                        }

                    }

                    System.out.println("SIZE OF PLAYER LIST IS : " + playersModelList.size());

                    if (playersModelList.size() == 0){

                        tennisCommon.inFormUser(getActivity(),"No player found");

                        topMaleAdapter.submitList(playersModelList);
                        topMaleAdapter.notifyDataSetChanged();

                    }else{


                        topMaleAdapter.submitList(playersModelList);
                        topMaleAdapter.notifyDataSetChanged();
                    }

                }else if (togenderSearch.contentEquals("BOTH")){


                    System.out.println("BOTH IS WORKING //");
                    for (int i = 0 ; i < TennisApp.getMaleRankingsList().size() ; i++){

                        if (!TextUtils.isEmpty(selectedCountry)){

                            if (TennisApp.getMaleRankingsList().get(i).getCompetitor().getCountry().contentEquals(selectedCountry)){

                                System.out.println("CFL: " + TennisApp.getMaleRankingsList().get(i).getCompetitor().getCountry() + " CS: " + selectedCountry);

                                System.out.println("PNFLC: " + TennisApp.getMaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contains(toSearchName.toLowerCase()) + " PNFL: " + TennisApp.getMaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contentEquals(toSearchName.toLowerCase()));

                                if (TennisApp.getMaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contains(toSearchName.toLowerCase()) || TennisApp.getMaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contentEquals(toSearchName.toLowerCase())){

                                    System.out.println("PNFL: " + TennisApp.getMaleRankingsList().get(i).getCompetitor().getName().toLowerCase() + " PS: " + toSearchName.toLowerCase());

                                    playersModelList.add(new PlayersModel(TennisApp.getMaleRankingsList().get(i).getCompetitor().getId(),"","",TennisApp.getMaleRankingsList().get(i).getCompetitor().getName(),"",TennisApp.getMaleRankingsList().get(i).getPoints()+"",TennisApp.getMaleRankingsList().get(i).getCompetitor().getCountry(),"M"));
                                }
                            }

                        }else{


                            if (TennisApp.getMaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contains(toSearchName.toLowerCase()) || TennisApp.getMaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contentEquals(toSearchName.toLowerCase())){

                                System.out.println("PNFL: " + TennisApp.getMaleRankingsList().get(i).getCompetitor().getName().toLowerCase() + " PS: " + toSearchName.toLowerCase());

                                playersModelList.add(new PlayersModel(TennisApp.getMaleRankingsList().get(i).getCompetitor().getId(),"","",TennisApp.getMaleRankingsList().get(i).getCompetitor().getName(),"",TennisApp.getMaleRankingsList().get(i).getPoints()+"",TennisApp.getMaleRankingsList().get(i).getCompetitor().getCountry(),"M"));
                            }

                        }

                    }

                    for (int i = 0 ; i < TennisApp.getFemaleRankingsList().size() ; i++){

                        if (!TextUtils.isEmpty(selectedCountry)){

                            if (TennisApp.getFemaleRankingsList().get(i).getCompetitor().getCountry().contentEquals(selectedCountry)){

                                System.out.println("CFL: " + TennisApp.getFemaleRankingsList().get(i).getCompetitor().getCountry() + " CS: " + selectedCountry);

                                if (TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contains(toSearchName.toLowerCase()) || TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contentEquals(toSearchName.toLowerCase())){

                                    System.out.println("PNFL: " + TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName().toLowerCase() + " PS: " + toSearchName.toLowerCase());

                                    playersModelList.add(new PlayersModel(TennisApp.getFemaleRankingsList().get(i).getCompetitor().getId(),"","",TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName(),"",TennisApp.getFemaleRankingsList().get(i).getPoints()+"",TennisApp.getFemaleRankingsList().get(i).getCompetitor().getCountry(),"F"));
                                }
                            }

                        }else{

                            if (TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contains(toSearchName.toLowerCase()) || TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contentEquals(toSearchName.toLowerCase())){

                                System.out.println("PNFL: " + TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName().toLowerCase() + " PS: " + toSearchName.toLowerCase());

                                playersModelList.add(new PlayersModel(TennisApp.getFemaleRankingsList().get(i).getCompetitor().getId(),"","",TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName(),"",TennisApp.getFemaleRankingsList().get(i).getPoints()+"",TennisApp.getFemaleRankingsList().get(i).getCompetitor().getCountry(),"F"));
                            }
                        }

                    }

                    System.out.println("SIZE OF BOTH PLAYER LIST IS : " + playersModelList.size());

                    if (playersModelList.size() == 0){

                        tennisCommon.inFormUser(getActivity(),"No player found");

                        topMaleAdapter.submitList(playersModelList);
                        topMaleAdapter.notifyDataSetChanged();

                    }else{


                        topMaleAdapter.submitList(playersModelList);
                        topMaleAdapter.notifyDataSetChanged();
                    }



                }else{


                    for (int i = 0 ; i < TennisApp.getFemaleRankingsList().size() ; i++){

                        if (!TextUtils.isEmpty(selectedCountry)){

                            if (TennisApp.getFemaleRankingsList().get(i).getCompetitor().getCountry().contentEquals(selectedCountry)){

                                System.out.println("CFL: " + TennisApp.getFemaleRankingsList().get(i).getCompetitor().getCountry() + " CS: " + selectedCountry);

                                if (TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contains(toSearchName.toLowerCase()) || TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contentEquals(toSearchName.toLowerCase())){

                                    System.out.println("PNFL: " + TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName().toLowerCase() + " PS: " + toSearchName.toLowerCase());

                                    playersModelList.add(new PlayersModel(TennisApp.getFemaleRankingsList().get(i).getCompetitor().getId(),"","",TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName(),"",TennisApp.getFemaleRankingsList().get(i).getPoints()+"",TennisApp.getFemaleRankingsList().get(i).getCompetitor().getCountry(),"F"));
                                }
                            }

                        }else{

                            if (TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contains(toSearchName.toLowerCase()) || TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName().toLowerCase().contentEquals(toSearchName.toLowerCase())){

                                System.out.println("PNFL: " + TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName().toLowerCase() + " PS: " + toSearchName.toLowerCase());

                                playersModelList.add(new PlayersModel(TennisApp.getFemaleRankingsList().get(i).getCompetitor().getId(),"","",TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName(),"",TennisApp.getFemaleRankingsList().get(i).getPoints()+"",TennisApp.getFemaleRankingsList().get(i).getCompetitor().getCountry(),"F"));
                            }
                        }

                    }

                    System.out.println("SIZE OF PLAYER LIST IS : " + playersModelList.size());

                    if (playersModelList.size() == 0){

                        tennisCommon.inFormUser(getActivity(),"No player found");

                        topMaleAdapter.submitList(playersModelList);
                        topMaleAdapter.notifyDataSetChanged();

                    }else{


                        topMaleAdapter.submitList(playersModelList);
                        topMaleAdapter.notifyDataSetChanged();
                    }


                }



            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        topMaleAdapter = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        topMaleAdapter = null;
    }


}