package com.tennis.cli.tenn.cus.tennisapp.Fragments;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.util.Preconditions;
import com.tennis.cli.tenn.cus.tennisapp.Activities.MainActivity;
import com.tennis.cli.tenn.cus.tennisapp.Application.TennisApp;
import com.tennis.cli.tenn.cus.tennisapp.Application.TennisCommon;
import com.tennis.cli.tenn.cus.tennisapp.Models.CModel.CountryModel;
import com.tennis.cli.tenn.cus.tennisapp.Models.PlayersModel;
import com.tennis.cli.tenn.cus.tennisapp.Models.Profile.CprofileModel;
import com.tennis.cli.tenn.cus.tennisapp.R;
import com.tennis.cli.tenn.cus.tennisapp.RetrofitUtils.Api;
import com.tennis.cli.tenn.cus.tennisapp.Utils.GlideApp;
import com.tennis.cli.tenn.cus.tennisapp.Utils.GlideRequests;
import com.tennis.cli.tenn.cus.tennisapp.Utils.SvgSoftwareLayerSetter;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProfileFragment extends Fragment {


    private View view;
    private String from;
    private AppCompatImageView backImg;
    private TennisCommon tennisCommon;
    private PlayersModel playersModel;
    private String player_id;

    private CircleImageView pImg,flagImg;
    private AppCompatTextView nameHeaderTxt,nameInfoTxt,ageInfoTxt,dobInfoTxt,weightTxt,heightTxt,countryTxt;

    private RequestBuilder<PictureDrawable> requestBuilder;

    private String USAFLAG = "data:image/png;base64,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";
    private String CREPFLAG = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAoHCBQTERcREREXERcRERAUERAXFxAYGRAZFxMYGBcXFxcaICwjGhwoHRkXJDUkKS0vMjIyGSI4PTgxPCwxMi8BCwsLDw4PGhERFzwoIyA8NDExMTI8MjE3Ly8vMS8xMS8yMzI8MTMvMzIxLzExLzIvLzQ0LzExMTEyPC8xMTEvMf/AABEIAOMA3gMBIgACEQEDEQH/xAAcAAEAAQUBAQAAAAAAAAAAAAAAAQIDBAUHBgj/xABFEAABAgQCBAoFCQgCAwAAAAABAAIDERIhBDEyQWFxBQYHEyJRgZGh0UJys9PwFiQ0UlR0sbLBFCNDU2JzguFj8WSj0v/EABsBAQACAwEBAAAAAAAAAAAAAAACBQEDBAYH/8QANhEAAgEBBQMJCAEFAAAAAAAAAAECAwQRITFREkGRBRMVIjJhcYHwFDNTkqGxwdHhBiNCUtL/2gAMAwEAAhEDEQA/AOvPdUJDejX0iRzRzabjPJGtDhM5oCGtpud1kLajUMvJSx1Vnb1BcWmkZICXOqsN90a6kUnPzRzabt3I1tQqOfkgIa2m53WQtqNQy8kY6qzt6FxaaRkgJc6qw33RrqRSc/NHNpu3cjW1Co5+SAhrabndZHNmahl5I01WdvRxINIy80BLjVYar3UtcGik5+ah4pu3XZS1oIqOfkgKWtouddrIWzNQy8kaarO3o4kGkZeaAlxqsNV7oHSFJzy70eKbt12QNmKjnn3ICGii519SFszXqz7kYa7O1KS4g06rDvQBzqrDfdGOpFJzRzabjcjWhwqOaAhrabndZC2o1DLyUsdVZ29Q4lppGXmgJc6qw33RrqRSc/NHNpu3cjWzFRz8kBDW0XOu1lLhVcblDTVZ29S402G9AQxlNzushZUah4o0lxk7JHEtMm5ICXOqsN90a6kUnPZtRzQ27c8utGtDhM5oCGtpud1kLajUMvJGkus7LPqRxINIyQEudVYb7o11IpOezajm03bu60a0OEzmgIa2m53WQtqNQy8kaS6zss+pHEg0jLzQEuNVhqvdGupFJz80cKbt3daNaCKjn5ICGii512shbUahl5I01Wd5I4kGkZeaAlxqsNV7o11IpOfmjhTdu7rRrQRUc/JAQ0UXOu1kLZmsZZ7bI01Wdq7EJINIysO/NAS41WGrrQOkKNeWy6OFOjr7UDQRUc8+5AQ1tNzushZUahltRri4ydln1I5xaZDJAS51VhvujXUik5+aPbTdueXWjWgiZzQENbTc7rIW1GoZeSNNVnb+pHEg0jLzQEuNVhqvdGmmx32Rwpu3d1o1odd2eXUgBdVYb7o11PRPguGxOM8fCY3Eta7nYYxmL/cvJIH79+g7NnZbYvb8A8ZcPi5NY7m4krwXyDv8Tk8bu0Bb50JxW1u9Z+riCmm7j3TW03O6yFlRqHjsWiENViCtBM3bnVWG+6NdSKTns2rScynMoDdNbTc7rIW1GoeOxaXmU5lAbtzqrDfdGupFJz81pOZTmUBumtpud1kLajUMvGy0vMpzKA3bjVYar3Rrg0UnPwutJzKcygN00UXOu1kLajUMvGy0vMpzKA3bjVYar3QOkKNeWy60nMpzKA3TRRc67WQtma9We2y0vMpzKA3ZdVYb7o11IpPgtJzKcygN01tNzushbUahlt2LS8ynMoDdudVYb7o11IpOfhdaQwVQYaA3rRRc67WUkVXG6688Wq9hLOt9U/iEBwvh8fPMT98xft3rBbnMGRBBBGYIyIKzuH/pmJ+94r2z1hBXlPso45Zs9nxd49xYMoeJBxEOwrtzrBvNn9sjtXTeCOEoGKh85h4oiASqAs5h6ntN2neuAgLIwWLiQYgiwYjob25PaZHcesbDZaatjhPGOD+hKNVrPE+hebU82vA8XOUhrpQse2g5DEMBpP8AcYLt3tmNgXQ8PEZEYIkN7YjHibXtIc1w6wRmq2pSnTd0kdEZKWRa5tRzayqEpWskY3NpzayaUpQGLzac2sqlKUBi82p5tZNKUoDF5tTzayaUpQGNzajm1lUpSgMbm1HNrKpSlAY3NpzayaUpQGLzac2sqlKUBiuhq09izXMVl7EBr3sVeCMnH1T+IVyK1W8IBWZ/VP4hZBwnh8/PcT98xft3rCCzeMH03E/fMX7d6wgryl2Eccs2VBSoClbSAW14E4fxGDfVh4paCZvhHpQ4nrM69okdq1SI0pK5rAX3HaOLfH/DYqUONLDRTIUuP7uIf6IhyOx0j1TXspL5lXqOLfHbE4OTCefhCQ5mITNg/wCN+bd1xsCr61h30+H6N8K26R3KSiS0nF/jRhsaP3MSmIBN0B8mvb1yHpDaJhbtV0ouLuaxN6aavQkklKLBkiSSUogIkklKICJJJSiAiSSUogIkklKICJJJSiApcFYeFkOVh6AxIoVnDNm8+qfxCvxVYw5NZl9U/iFkHCOHx89xI6sZi/bvWEFm8PfTMT97xXtnrCCvKXYRxSzZUFKgKVtREIiLICIiArY4tIc0lrmmbXAkFp6wRcFe84t8pEWFKFjQY7LARmyEVnrDKIO4714BFrqUoVFdJGYycXej6O4L4Tg4mGImHitiMOtpu09Tmm7TsIWYvnDg/hCLh4giwIroTx6TTmOpwycNhBC6Zxb5SmRJQsc0QnZCO2fNu9YZs33G5VlaxzhjDFfX+fI6I1k88DoaKmFEa9oexwe1wBa5pBDgciCLEKpcZuChSiAIiIAiIgCIiAIiICHKw9X3Kw9AYsVWcM6Tz6p/EK9FVnCgVmf1T+IWQcH4fPz3E7cZi/bvWEFm8YPpuJ++Yv271hBXlLsI45ZsrCKApW1EAiIsgIiIAiIgCIiA3PAPGTE4J04MToEzdBfN0N3X0fRO1sjvXVuLXHrDYuUN5/Z4pkOaeRS8/wDHEyduMjsXEFJC561mhVxeD19Zk41HE+mUXEuLfHzE4SUOKTiYQkKHnpwx/Q837HTG5dX4B4xYbGNqgRZuABfBd0YkPe3q2iY2qrrWedLFrDU6YVFI2yIi0EwihSgCKFKAIiICHKw9X3Kw9AYsVWcM2bz6p/EK9FVjDzrMvqn8Qsg4Rw+PnuJHVjMX7d6wgszh/wCmYn73ivbPWGFeUuwjjlmyoKVAUraiAREWQEREAREQBERAERZEPDH0rDqsT3LVVrQox2pu5estTfZrLWtM9ijG9/bxeS8/uWAJ2F1m4WGYb2xA9zXsM2lpLSw9YcLhXGNDdES6uvtKhUVp5VnO+NJbK13/AKXlxPXWH+nqVK6doe3LT/Ff9eeHcbz5WY77U/47E+VmO+1P+OxaVUqr2nr9WXfs1H4cflRvPlZjvtT/AI7E+VmO+1P+OxaNE2nq+LHs1D4cflRvPlZjvtT/AI7E+VmO+1P+OxaVUptPV8WPZqHw4/KjefKzHfan/HYnysx32p/x2LRom1LV8WPZqHw4/KjdnjZj/tT+/wD0qHca8f8Aan9/+lqCqCs7UtXxZF2ah8OPyr9G1fxoxv2l/wAdi3fE7h3ExMQ4RYz3gQXkCYsa2Xy2leMcvQ8RZftDp/yHfnYpRlK/M4bXZ6SpSugllklqjznD5+e4nbjMX7d6wgs3h/6bifvmL9u9YQXrafYR4KWbKgpUBStqIBERZAREQBEVUNhNgJrEpKKvbwRKMZTkoxV7eSWbKVdhwS7Kw+ssiFAa25FR8O8K6T2qntPKyWFFX97y8lv88PE9LYf6dlK6dqdy/wBVn5vd4K996ZTDhhuVz9e4nu6lKIqOpUnUltTd7PV0aNOjBQpxSS3L1j4vEIiKBsCIiAIiIAiIgCIiAFUlVFUlZMSLbl6HiMJ4h39h352Lzzl6LiPP9odL+QfzMU4nBbPdP1vR5zh8fPcTsxmL9u9YQWZw99MxP3vFe2esML11PsI+eSzZUFKgKVtRAIiLICkBXoWGJzNI1Dr7FlMaG2aJbRr7f0VfaeUqVHCPWlosl4v8LEurDyHaLTdKfUjq834R/d2qvLEPCgad/wClZA6gCP6QVCLz9otVWu75vy3L133s9hY7BZ7JG6lHHe3i35/hXLuCIi5zsCIiAIiIAiIgCIiAIiIAiIgBVJVZVBWTEihy9BxGMsQ7+w78zF59y9DxFl+0On/Id+dimtxwWz3UvL7nnOHz89xJ68Zi/bvWEFm8YPpuJ++Yv271gAr11LsI+eSzZcClGNJyusqFAaNLpbsh5rXXtdKguu8dFn673cdVj5Pr2t/244b28EvPf4K8tQoRdlb+rqWTDhAZXPXr7lVPwUKhtPKFWt1V1Y6L8vf4Zdx7Cw8jWey3SfWnq93gt3i733lc0mqJpNcFxcbRXNJqhEuG0VzSaoRLhtFc0mqES4bRXNJqhJpcNormk1RNEuG0VzSaomk0uG0VzSaomiXDaK5pNUIlw2ioqkoSqSVki2Q5eg4jCeId/Zd+di864r0PEaf7Q6X8l352KUczhtfun63nneMH03Ej/wAzFj/3vWOyBLSt/TYy3rssbk7wr4kSK98cGNFiRHSdCkHPeXECbCZTJVB5NMH6L45HXVA/+FcVrXVuUaSu79/8esjyVkpWOPXrtyeiWHnlf9F4nJWkDK3x49qmpdZdya4P0Ykcn1oPu1I5NcH6UXEA9VUH3aq3Sm3e9/eegjyvZopJXpLuOTVJUusN5NMJ6UXEAetA92h5NMHqiYgjrqge7TmZ6EumbNq+ByepKl1h3JrgzoxcQT60H3akcmuD9KLiAeqqD7tOZmOmbNq+ByapKl1hvJphPSi4gD1oHu0PJpg9UTEEddUD3aczPQdM2bV8Dk9SVLrDuTXB+jFxB/yg+7Ujk1weuLiAeqqD2fw05mWg6Zs2r4HJqkqXWG8mmE9KLiB/lA92h5NMJqiYgjrqgdv8NOYnoOmbNq+ByepKl1h3Jrg/Ri4g/wCUH3aDk1wcrxcQD1VQez+GnMz0HTNm1fA5PUlS6w3k0wnpRcQP8oHu0PJrg52fHl11QO30E5meg6Zs2r4HJ6kqXWHcmuD9GLiD19KD7tSOTXByvFxE+qqDnq/hpzMx0zZtXwOTVJUusN5NMJ6UXEAetA92h5NMJ6MTEEddUD3aczPQdM2bV8Dk1SmpdYdya4P0YuIJ9aD7tSOTXB64uIB6qoPu05meg6Zs2r4HJalBcutN5NMJ6UXEAetA92h5NcHqfHI66oHb6CczPQx0xZ+/gckJXo+IxliHf2XfnYvcO5NcH6MTEH/KD7tZvBPEjDYaIYjIkaZaWycYREiWmdmC82rKoz0NNblSzzg4pvgeoD67Za1BfT0c1LiDo57LIwgCTs9t12HmwW03F9SBtXSy2blDARp5bb3QgkzblssgJDqrG2v470L6ejnt3o4g6Gey1kaQBJ2ffuugBFNxedkDaulls3KGAjTy23uhBJm3LZbwQEh1VjbX8d6F9PRz270cQdDPZayNIAk7Pv3XQAim4vOyBtXS8NyhgI08tt7oQSZjR+J2QEh1djaV0L6ej470cZ6GeuVkaQBJ2fxK6AEU3F52QNq6XhuUNBGnltuhBJmNH4nZASDXY2ldRVLodk9//alxnodsrICJSOl47LoARTcXmlM+n2y3KG20+yd0IM5jR8Ja7ICQ+u2WtC+no57d6OIOhnssjSAJOz23QAim4vqQNq6WWzcoYCNPLbe6EEmbcu7fZASHVWNtaF9PRz270cQdDPZayNIAk7P4ldAC2m4vOyAVXy1KGgjTy23UumdDLZZAC2i4vqQNq6RsoaCDN2XejgSZty7kAa6uxtrQup6Iv/tS4g2bn3I0gCTs+/xQAim4vq+O5A2rpeG5Q0EXfltvdCCTNuXdvsgAdXY2ldC6noi/+1LiDoZ7LI0gCTs+/wAUALabi+pA2rpeG5Q0EXfltvdCCTNuXdvsgAdXY2ldC+no+O9S4g6GeyyNIAkc/iV0AIpuLzsgbV0vDcoaJafZO6lwJMxl8TsgIDq7G0roX09Hx3qXEHQz2WRpAEjn8SugBFNxedkDZ9Ptlu/6UNEtPsndCCTMaNv92QAGuxtK6VyNHZPepcZ6HbKyAiUjpfrqugBbTcX1IG1dI2/0oaCLuy70IJM25d3ggDXV2NtaF9PRz271LiDZmey1kaQBJ2ff4oAW03F9SBtXS8NyhgI08tt7qXAkzGXxOyAgOrsbSupJpsL60eZ6GeyyNIFnZ96AqxGj2hIGj2lEQFvDZ9nkkbS7kRAV4jLt/QqYOj3oiAow+fZ+qiNpdyIgK8Rl2/oVMLR71KIC3h8+z9VETT7QiICvEZDephaHYfxKIgLeHzO5Imn2j9ERAV4jIb1LNDsP6oiAow+Z3KHafaP0REBcxGj2pA0e9EQFvDZ9n6hI2l3IiArxGXb+imFod6IgLeHzO5MRpdiIgP/Z";


    public ProfileFragment() {
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
        view =  inflater.inflate(R.layout.fragment_profile, container, false);

        requestBuilder =
                GlideApp.with(getActivity())
                        .as(PictureDrawable.class)
                        .placeholder(R.drawable.ic_flag)
                        .listener(new SvgSoftwareLayerSetter());

        playersModel = TennisApp.getSelectedPlayerModel();

        tennisCommon = new TennisCommon();

        if (playersModel != null){

            player_id  = playersModel.getPlayer_id();
        }

        System.out.println("PLAYER ID IS: "  + player_id);

        pImg = view.findViewById(R.id.profile_img);
        flagImg = view.findViewById(R.id.flag_img);


        nameHeaderTxt = view.findViewById(R.id.name_txt);
        nameInfoTxt = view.findViewById(R.id.name_txt_info);
        ageInfoTxt = view.findViewById(R.id.age_txt_info);
        dobInfoTxt = view.findViewById(R.id.dob_txt_info);
        heightTxt = view.findViewById(R.id.height_txt_info);
        weightTxt = view.findViewById(R.id.weight_txt_info);
        countryTxt = view.findViewById(R.id.con_txt_info);

        if (playersModel !=  null){

            if (playersModel.getGender().contentEquals("M")){

                pImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.male_tenn));
//                Glide.with(getActivity()).load(getActivity().getResources().getDrawable(R.drawable.male)).into(pImg);
            }else{
                pImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.female_tenn));
//                Glide.with(getActivity()).load(getActivity().getResources().getDrawable(R.drawable.female)).into(pImg);
            }


            String flagurl = getFlagUrl(TennisApp.getFlagsList(),playersModel.getCountry());


            if (playersModel.getCountry().contentEquals("USA")){

                GlideApp.with(getActivity()).load(USAFLAG).into(flagImg);

            }else if (playersModel.getCountry().contentEquals("Czech Republic")){

                GlideApp.with(getActivity()).load(CREPFLAG).into(flagImg);

            }else{

                if (TextUtils.isEmpty(flagurl)){

                    flagImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_flag));

                }else{

                    loadNet(flagurl,flagImg);
                }


            }


        }


        backImg = view.findViewById(R.id.back);

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                from = TennisApp.getFrom();

                if (MainActivity.getNavController() != null && MainActivity.getNavController().getCurrentDestination().getId() == R.id.profileFragment){

                    if (from.contentEquals("RANKING")){

                        MainActivity.getNavController().navigate(R.id.action_profileFragment_to_nav_rankings);

                    }else if (from.contentEquals("PLAYERS")){

                        MainActivity.getNavController().navigate(R.id.action_profileFragment_to_nav_players);

                    }else{
                        MainActivity.getNavController().navigate(R.id.action_profileFragment_to_sameCtyPlayersFragment);
                    }
                }

            }
        });


        loadProfileData();

        return view;
    }

    private void loadProfileData() {

        if (tennisCommon.isNetWorkAvailable(getActivity())) {

            ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Getting player data....");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(40, TimeUnit.SECONDS).readTimeout(40,TimeUnit.SECONDS).build();

            String BASE_URL =  "https://api.sportradar.com/tennis/trial/v3/en/competitors/"+player_id+"/";

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Api service = retrofit.create(Api.class);

            service.profile("profile.json?api_key=bsrp4bft6arabtrrdwjupf25").enqueue(new Callback<CprofileModel>() {
                @Override
                public void onResponse(Call<CprofileModel> call, Response<CprofileModel> response) {

                    CprofileModel cprofileModel  = response.body();

                    if(dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    if (cprofileModel != null) {

                        int age = calculateAge(cprofileModel.getInfo().getDate_of_birth());
                        System.out.println("AGE PREVIOUS IS: " + age);

//                        int age = getPerfectAgeInYears(cprofileModel.getInfo().getDate_of_birth());
//                        System.out.println("AGE NEW IS: " + age);

                        nameHeaderTxt.setText(cprofileModel.getCompetitor().getName());
                        nameInfoTxt.setText(cprofileModel.getCompetitor().getName());
                        ageInfoTxt.setText(age + " y.o");
                        dobInfoTxt.setText(cprofileModel.getInfo().getDate_of_birth());

                        if (cprofileModel.getInfo().getHeight() != 0) {
                            heightTxt.setText(cprofileModel.getInfo().getHeight() + " cm");
                        }else{
                            heightTxt.setText("not found");
                        }

                        if (cprofileModel.getInfo().getWeight() != 0){
                            weightTxt.setText(cprofileModel.getInfo().getWeight() + " lbs");
                        }else{
                            weightTxt.setText("not found");
                        }

                        countryTxt.setText(cprofileModel.getCompetitor().getCountry());
//                    coachTxt.setText(cprofileModel.getCompetitor().getCountry());

                    }



                }

                @Override
                public void onFailure(Call<CprofileModel> call, Throwable t) {

                    if(dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    System.out.println("EXCEPTION: " + t.getMessage());
                    tennisCommon.inFormUser(getActivity(),t.getMessage());

                }
            });

        }
    }


    public static int calculateAge(String date)
    {

        String[] arr = date.split("-");
        int year = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]);
        int day = Integer.parseInt(arr[2]);

//        LocalDate birthDate = LocalDate.of(year,month,day);
        LocalDate now = LocalDate.now();						//Today's date

        System.out.println("BIRTH YEAR IS: " + year);
        System.out.println("NOW YEAR IS: " + now.getYear());

        int age = now.getYear() - year;

        System.out.println("BIRTH DAY IS: " + day);
        System.out.println("NOW DAY IS: " + now.getDayOfYear());

        if (now.getDayOfYear() < day){
            age--;
        }

        System.out.println("Second cal: " + age);

//        Period period = Period.between(birthDate, now);

       return age;


    }


//    public static int getPerfectAgeInYears(String date) {
//
//        String[] arr = date.split("-");
//        int year = Integer.parseInt(arr[0]);
//        int month = Integer.parseInt(arr[1]);
//        int day = Integer.parseInt(arr[2]);
//
//
//        Calendar dobCalendar = Calendar.getInstance();
//
//        dobCalendar.set(Calendar.YEAR, year);
//        dobCalendar.set(Calendar.MONTH, month);
//        dobCalendar.set(Calendar.DATE, day);
//
//        int ageInteger = 0;
//
//        Calendar today = Calendar.getInstance();
//
//        ageInteger = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);
//
//        if (today.get(Calendar.MONTH) == dobCalendar.get(Calendar.MONTH)) {
//
//            if (today.get(Calendar.DAY_OF_MONTH) < dobCalendar.get(Calendar.DAY_OF_MONTH)) {
//
//                ageInteger = ageInteger - 1;
//            }
//
//        } else if (today.get(Calendar.MONTH) < dobCalendar.get(Calendar.MONTH)) {
//
//            ageInteger = ageInteger - 1;
//
//        }
//
//        return ageInteger;
//    }


    private String getFlagUrl(List<CountryModel> flagsList, String country) {

        String url = "";

        for (int i = 0; i < flagsList.size(); i++){

            if (flagsList.get(i).getName().contentEquals(country)){
                url = flagsList.get(i).getImage();
                return url;
            }
        }

        return url;
    }

    private void loadNet(String url, CircleImageView view) {

        System.out.println("LOAD NET IS CALLED: " + url);
        Uri uri = Uri.parse(url);
        requestBuilder.load(uri).into(view);
    }


}