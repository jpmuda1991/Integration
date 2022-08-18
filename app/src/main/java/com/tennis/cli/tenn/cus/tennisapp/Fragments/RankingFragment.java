package com.tennis.cli.tenn.cus.tennisapp.Fragments;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.ProgressDialog;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.tennis.cli.tenn.cus.tennisapp.Activities.MainActivity;
import com.tennis.cli.tenn.cus.tennisapp.Adapters.TopMaleAdapter;
import com.tennis.cli.tenn.cus.tennisapp.Application.TennisApp;
import com.tennis.cli.tenn.cus.tennisapp.Application.TennisCommon;
import com.tennis.cli.tenn.cus.tennisapp.Models.CModel.CListModel;
import com.tennis.cli.tenn.cus.tennisapp.Models.CModel.CountryModel;
import com.tennis.cli.tenn.cus.tennisapp.Models.CompetitorsArr;
import com.tennis.cli.tenn.cus.tennisapp.Models.PlayersModel;
import com.tennis.cli.tenn.cus.tennisapp.Models.RankingsArr;
import com.tennis.cli.tenn.cus.tennisapp.Models.RankingsModel;
import com.tennis.cli.tenn.cus.tennisapp.R;
import com.tennis.cli.tenn.cus.tennisapp.RetrofitUtils.Api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RankingFragment extends Fragment {

    private View view;
    private RecyclerView recyclerViewMale,recyclerViewFemale;
    private TopMaleAdapter topMaleAdapter;
    private TopMaleAdapter topFemaleAdapter;
    private List<PlayersModel> playersModelListMale,playersModelListFemale;
    private TennisCommon tennisCommon;
    private NestedScrollView scrollView;
    private AppCompatTextView maleTxt,femaleTxt;
    private boolean ismaleImgVisible = false;
    private boolean isfemaleImgVisible = false;
    private String genM,genF;

    public RankingFragment() {
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
        view =  inflater.inflate(R.layout.fragment_ranking, container, false);



        tennisCommon = new TennisCommon();

        if (playersModelListMale == null){

            playersModelListMale = new ArrayList<>();
        }

        if (playersModelListFemale == null){

            playersModelListFemale = new ArrayList<>();
        }


        scrollView = view.findViewById(R.id.scrollView);
        maleTxt = view.findViewById(R.id.top_male_txt);
        femaleTxt = view.findViewById(R.id.top_female_txt);


        recyclerViewMale = view.findViewById(R.id.recyclerView_top_male);
        recyclerViewMale.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewMale.setHasFixedSize(true);

        topMaleAdapter = new TopMaleAdapter(getActivity());

        recyclerViewMale.setAdapter(topMaleAdapter);

        recyclerViewFemale= view.findViewById(R.id.recyclerView_top_female);
        recyclerViewFemale.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFemale.setHasFixedSize(true);

        topFemaleAdapter = new TopMaleAdapter(getActivity());
        recyclerViewFemale.setAdapter(topFemaleAdapter);

        topMaleAdapter.submitList(playersModelListMale);
        topFemaleAdapter.submitList(playersModelListFemale);

        topMaleAdapter.setOnItemClickListener(new TopMaleAdapter.OnItemClickListener() {
            @Override
            public void onFlagIsClicked(PlayersModel playersModel) {

                if (MainActivity.getNavController() != null && MainActivity.getNavController().getCurrentDestination().getId() == R.id.nav_rankings){

                    TennisApp.setSamelMaleOrFemale("MALE");


                    TennisApp.setSameCountry(playersModel.getCountry());
                    TennisApp.setSameCityFrom("RANKING");
                    MainActivity.getNavController().navigate(R.id.action_nav_rankings_to_sameCtyPlayersFragment2);
                }


            }

            @Override
            public void onPlayerClicked(PlayersModel playersModel) {

                System.out.println("MALE CARD IS CLICKED");

                if (MainActivity.getNavController() != null && MainActivity.getNavController().getCurrentDestination().getId() == R.id.nav_rankings){

                    TennisApp.setFrom("RANKING");
                    TennisApp.setSelectedPlayerModel(playersModel);
                    MainActivity.getNavController().navigate(R.id.action_nav_rankings_to_profileFragment);
                }

            }
        });

        topFemaleAdapter.setOnItemClickListener(new TopMaleAdapter.OnItemClickListener() {
            @Override
            public void onFlagIsClicked(PlayersModel playersModel) {

                if (MainActivity.getNavController() != null && MainActivity.getNavController().getCurrentDestination().getId() == R.id.nav_rankings){

                    TennisApp.setSamelMaleOrFemale("FEMALE");
                    TennisApp.setSameCountry(playersModel.getCountry());
                    TennisApp.setSameCityFrom("RANKING");
                    MainActivity.getNavController().navigate(R.id.action_nav_rankings_to_sameCtyPlayersFragment2);
                }


            }

            @Override
            public void onPlayerClicked(PlayersModel playersModel) {

                System.out.println("FEMALE CARD IS CLICKED");

                if (MainActivity.getNavController() != null && MainActivity.getNavController().getCurrentDestination().getId() == R.id.nav_rankings){

                    TennisApp.setFrom("RANKING");
                    TennisApp.setSelectedPlayerModel(playersModel);
                    MainActivity.getNavController().navigate(R.id.action_nav_rankings_to_profileFragment);
                }

            }
        });

        getDataFromTennisV3();


//        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//
//                Rect scrollBounds = new Rect();
//                scrollView.getHitRect(scrollBounds);
//
//                if (maleImg.getLocalVisibleRect(scrollBounds)) {
//
//                    ismaleImgVisible = true;
//
//                    if (ismaleImgVisible){
//
//                        ObjectAnimator animation = ObjectAnimator.ofFloat(maleImg, "translationX", 80f);
//                        animation.setInterpolator(new AccelerateDecelerateInterpolator());
//                        animation.setDuration(1000);
//                        animation.start();
//
//                        ObjectAnimator animationTxt = ObjectAnimator.ofFloat(maleTxt, "translationX", 30f);
//                        animationTxt.setInterpolator(new AccelerateDecelerateInterpolator());
//                        animationTxt.setDuration(1000);
//                        animationTxt.start();
//
//                        ismaleImgVisible = false;
//
//                    }
//
//
//
//                } else {
//
//
//                    if (!ismaleImgVisible){
//
//                        ObjectAnimator animation = ObjectAnimator.ofFloat(maleImg, "translationX", 0f);
//                        animation.setDuration(2000);
//                        animation.start();
//
//                        ObjectAnimator animationTxt = ObjectAnimator.ofFloat(maleTxt, "translationX", 0f);
//                        animationTxt.setDuration(2000);
//                        animationTxt.start();
//
//                        ismaleImgVisible = true;
//                    }
//
//                }
//
//                if (femaleImg.getLocalVisibleRect(scrollBounds)) {
//
//                    isfemaleImgVisible = true;
//
//
//                    if (isfemaleImgVisible){
//
//                        ObjectAnimator animation = ObjectAnimator.ofFloat(femaleImg, "translationX", -80f);
//                        animation.setInterpolator(new AccelerateDecelerateInterpolator());
//                        animation.setDuration(1000);
//                        animation.start();
//
//                        ObjectAnimator animationTxt = ObjectAnimator.ofFloat(femaleTxt, "translationX", -30f);
//                        animationTxt.setInterpolator(new AccelerateDecelerateInterpolator());
//                        animationTxt.setDuration(1000);
//                        animationTxt.start();
//
//                        isfemaleImgVisible = false;
//                    }
//
//                } else {
//
//                    if (!isfemaleImgVisible){
//
//                        ObjectAnimator animation = ObjectAnimator.ofFloat(femaleImg, "translationX", 0f);
//                        animation.setDuration(2000);
//                        animation.start();
//
//                        ObjectAnimator animationTxt = ObjectAnimator.ofFloat(femaleTxt, "translationX", 0f);
//                        animationTxt.setDuration(2000);
//                        animationTxt.start();
//
//
//                        isfemaleImgVisible = true;
//                    }
//
//                }
//            }
//        });
//

        return view;
    }


    private void getDataFromTennisV3() {

        if (tennisCommon.isNetWorkAvailable(getActivity())) {

            System.out.println("INITIAL SIZE OF LIST IS: " + TennisApp.getMaleRankingsList().size());

            if (TennisApp.getFlagsList() != null && TennisApp.getFlagsList().size() <= 0){

                OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(40, TimeUnit.SECONDS).readTimeout(40,TimeUnit.SECONDS).build();

                String BASE_URL =  "https://cdn.jsdelivr.net/npm/country-flag-emoji-json@2.0.0/";

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();


                Api service = retrofit.create(Api.class);

                service.getConFlags("dist/index.json").enqueue(new Callback<List<CountryModel>>() {
                    @Override
                    public void onResponse(Call<List<CountryModel>> call, Response<List<CountryModel>> response) {

                        List<CountryModel> list = response.body();
                        System.out.println("COUNTRY LIST SIZE IS: " + list.size());
                        TennisApp.setFlagsList(list);
                    }

                    @Override
                    public void onFailure(Call<List<CountryModel>> call, Throwable t) {

                        System.out.println("FLAG EXCEPTION IS: " + t.getMessage());

                    }
                });



            }

            if (TennisApp.getMaleRankingsList() != null && TennisApp.getMaleRankingsList().size() <= 0) {

                ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Getting Rankings....");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(40, TimeUnit.SECONDS).readTimeout(40,TimeUnit.SECONDS).build();

                String BASE_URL =  "https://api.sportradar.com/tennis/trial/v3/en/";

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Api service = retrofit.create(Api.class);

                service.rankings("rankings.json?api_key=bsrp4bft6arabtrrdwjupf25").enqueue(new Callback<RankingsModel>() {
                    @Override
                    public void onResponse(Call<RankingsModel> call, Response<RankingsModel> response) {

                        RankingsModel rankingsModel = response.body();

                        try {

                            List<RankingsArr> rankingsList =  rankingsModel.getRankings();

                            System.out.println("LIST OF Competitor_rankings size : " + rankingsList.get(0).getCompetitor_rankings().size());



                            TennisApp.setMaleRankingsList(rankingsList.get(0).getCompetitor_rankings());
                            TennisApp.setFemaleRankingsList(rankingsList.get(1).getCompetitor_rankings());

                            if (playersModelListMale == null){

                                playersModelListMale = new ArrayList<>();
                            }

                            if (playersModelListMale.size() > 0){

                                playersModelListMale.clear();
                            }

                            if (playersModelListFemale == null){

                                playersModelListFemale = new ArrayList<>();
                            }

                            if (playersModelListFemale.size() > 0){

                                playersModelListFemale.clear();
                            }




                            for (int i = 0 ; i < 20 ; i++){

                                playersModelListMale.add(new PlayersModel(TennisApp.getMaleRankingsList().get(i).getCompetitor().getId(),"","",TennisApp.getMaleRankingsList().get(i).getCompetitor().getName(),"",TennisApp.getMaleRankingsList().get(i).getPoints()+"",TennisApp.getMaleRankingsList().get(i).getCompetitor().getCountry(),"M"));
                            }

                            for (int i = 0 ; i < 20 ; i++){

                                playersModelListFemale.add(new PlayersModel(TennisApp.getFemaleRankingsList().get(i).getCompetitor().getId(),"","",TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName(),"",TennisApp.getFemaleRankingsList().get(i).getPoints()+"",TennisApp.getFemaleRankingsList().get(i).getCompetitor().getCountry(),"F"));
                            }


                            topMaleAdapter.notifyDataSetChanged();
                            topFemaleAdapter.notifyDataSetChanged();

                            if(dialog.isShowing()) {
                                dialog.dismiss();
                            }


                        }catch (NullPointerException ex){

                            ex.printStackTrace();

                            if(dialog.isShowing()) {
                                dialog.dismiss();
                            }

                        }


                    }

                    @Override
                    public void onFailure(Call<RankingsModel> call, Throwable t) {

                        if(dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        tennisCommon.inFormUser(getActivity(),t.getMessage());
                    }
                });


            }else{

                // set already getted list to adapter //

                if (playersModelListMale == null){

                    playersModelListMale = new ArrayList<>();
                }

                if (playersModelListMale.size() > 0){

                    playersModelListMale.clear();
                }

                if (playersModelListFemale == null){

                    playersModelListFemale = new ArrayList<>();
                }

                if (playersModelListFemale.size() > 0){

                    playersModelListFemale.clear();
                }


                for (int i = 0 ; i < 20 ; i++){

                    playersModelListMale.add(new PlayersModel(TennisApp.getMaleRankingsList().get(i).getCompetitor().getId(),"","",TennisApp.getMaleRankingsList().get(i).getCompetitor().getName(),"",TennisApp.getMaleRankingsList().get(i).getPoints()+"",TennisApp.getMaleRankingsList().get(i).getCompetitor().getCountry(),"M"));
                }

                for (int i = 0 ; i < 20 ; i++){

                    playersModelListFemale.add(new PlayersModel(TennisApp.getFemaleRankingsList().get(i).getCompetitor().getId(),"","",TennisApp.getFemaleRankingsList().get(i).getCompetitor().getName(),"",TennisApp.getFemaleRankingsList().get(i).getPoints()+"",TennisApp.getFemaleRankingsList().get(i).getCompetitor().getCountry(),"F"));
                }

                topMaleAdapter.submitList(playersModelListMale);
                topFemaleAdapter.submitList(playersModelListFemale);

            }

        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        topMaleAdapter = null;
        topFemaleAdapter = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        topMaleAdapter = null;
        topFemaleAdapter = null;
    }
}