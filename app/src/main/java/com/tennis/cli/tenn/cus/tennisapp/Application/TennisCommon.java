package com.tennis.cli.tenn.cus.tennisapp.Application;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.tennis.cli.tenn.cus.tennisapp.R;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class TennisCommon extends AppCompatActivity {

    private static final String TAG = "AlphaMoversCommon";
    public AnimatorSet animatorSet;
    public SharedPreferences permissionLocationStatus, permissionCall, permissionReadWriteStatus,permissionAudioStatus,permissionactivityRecogStatus;
    public boolean sentToSettings = false;

    // Constant Values Used in Permissions //
    public static final int PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;
    public static final int PERMISSION_REQUEST_CODE_ACTIVITY_RECOGNITION = 21;

    public static final int ERROR_DIALOG_REQUEST = 15;
    public static final int PERMISSION_REQUEST_CODE_ENABLE_GPS = 14;
    public static final int PERMISSION_REQUEST_CODE_LOCATION = 13;
    public static final int REQUEST_READ_WRITE_PERMISSION_SETTING = 15;
    public static final int REQUEST_ACTIVITY_RECOG_PERMISSION_SETTING = 20;

    public static final int REQUEST_lOCATION_PERMISSION_SETTING = 16;
    // Constant Values Used in Permissions //
    public static final int PERMISSION_REQUEST_CODE_CALL = 100;
    public static final int REQUEST_CALL_PERMISSION_SETTING = 1800;

    public static final int PERMISSION_REQUEST_CODE_AUDIO = 107;
    public static final int REQUEST_AUDIO_PERMISSION_SETTING = 108;


    public ConnectivityManager manager = null;
    public NetworkInfo networkInfo;

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Function For Checking NetWork Available Or Not //
    public boolean isNetWorkAvailable(Context context) {

        manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

        if (manager != null) {

            networkInfo = manager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;

            } else {
                inFormUser(context, "Please Check Your Network Connection");
                return false;
            }
        }
        return false;
    }


    // checking if already have permission or not //
    public void callPermissionChecking(final Activity activity, final String permission) {

        if (permissionCall != null) {
            permissionCall = getSharedPreferences("permissionCall", MODE_PRIVATE);
        }

        String title = null;
        String message = null;

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {


            title = "Need Call Permission...";
            message = "This app needs Call permission to call from your handset...";

            //Show Information about why you need the permission
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                    ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE_CALL);

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        } else if (permissionCall != null && permissionCall.getBoolean(permission, false)) {

            title = "Need Call Permission...";
            message = "This app needs Call permission to call from your handset...";

            //Previously Permission Request was cancelled with 'Dont Ask Again',
            // Redirect to Settings after showing Information about why you need the permission
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    sentToSettings = true;

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_CALL_PERMISSION_SETTING);
                    Toast.makeText(getBaseContext(), "Go to Permissions to Grant Call permission", Toast.LENGTH_LONG).show();


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        } else {
            //just request the permission

            ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE_CALL);

        }


        if (permissionCall != null) {
            SharedPreferences.Editor editor = permissionCall.edit();
            editor.putBoolean(permission, true);
            editor.apply();
        }


    }

    // checking if already have permission or not //
    public void readAndWritePermissionChecking(final Activity activity, final String permission) {

        if (permissionReadWriteStatus != null) {
            permissionReadWriteStatus = getSharedPreferences("permissionReadWriteStatus", MODE_PRIVATE);
        }

        String title = null;
        String message = null;

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {


            title = "Need Storage Permission...";
            message = "This app needs storage permission to read and write data from your handset...";

            //Show Information about why you need the permission
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();


                    ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE);


                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        } else if (permissionReadWriteStatus != null && permissionReadWriteStatus.getBoolean(permission, false)) {


            title = "Need Storage Permission...";
            message = "This app needs storage permission to read and write data from your handset...";

            //Previously Permission Request was cancelled with 'Dont Ask Again',
            // Redirect to Settings after showing Information about why you need the permission
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    sentToSettings = true;

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_READ_WRITE_PERMISSION_SETTING);
                    Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage permission", Toast.LENGTH_LONG).show();

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        } else {
            //just request the permission

            ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE);

        }


        if (permissionReadWriteStatus != null) {
            SharedPreferences.Editor editor = permissionReadWriteStatus.edit();
            editor.putBoolean(permission, true);
            editor.apply();
        }

    }


    // checking if already have permission or not //
    public void activityRecogPermissionChecking(final Activity activity, final String permission) {

        if (permissionactivityRecogStatus != null) {
            permissionactivityRecogStatus = getSharedPreferences("permissionactivityRecogStatus", MODE_PRIVATE);
        }

        String title = null;
        String message = null;

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {


            title = "Need Activity Recognition Permission...";
            message = "This app needs activity recognition  permission to count steps data from your handset...";

            //Show Information about why you need the permission
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();


                    ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE);


                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        } else if (permissionactivityRecogStatus != null && permissionactivityRecogStatus.getBoolean(permission, false)) {


            title = "Need Activity Recognition Permission...";
            message = "This app needs activity recognition  permission to count steps data from your handset...";


            //Previously Permission Request was cancelled with 'Dont Ask Again',
            // Redirect to Settings after showing Information about why you need the permission
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    sentToSettings = true;

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_ACTIVITY_RECOG_PERMISSION_SETTING);
                    Toast.makeText(getBaseContext(), "Go to Permissions to Grant Activity Recognition permission", Toast.LENGTH_LONG).show();

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        } else {
            //just request the permission

            ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE_ACTIVITY_RECOGNITION);

        }


        if (permissionactivityRecogStatus != null) {
            SharedPreferences.Editor editor = permissionactivityRecogStatus.edit();
            editor.putBoolean(permission, true);
            editor.apply();
        }

    }

    public void LocationPermissionChecking(final Activity activity, final String permission) {

        if (permissionLocationStatus != null) {
            permissionLocationStatus = getSharedPreferences("permissionLocationStatus", MODE_PRIVATE);
        }

        String title = null;
        String message = null;

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {


            title = "Need Location Permission...";
            message = "This app needs location permission for Maps Features";


            //Show Information about why you need the permission
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("Grant", (dialog, which) -> {
                dialog.cancel();


                ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE_LOCATION);

            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        } else if (permissionLocationStatus != null && permissionLocationStatus.getBoolean(permission, false)) {


            title = "Need Location Permission...";
            message = "This app needs location permission for Maps Features";

            //Previously Permission Request was cancelled with 'Dont Ask Again',
            // Redirect to Settings after showing Information about why you need the permission
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    sentToSettings = true;

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_lOCATION_PERMISSION_SETTING);
                    Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage permission", Toast.LENGTH_LONG).show();


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    sentToSettings = false;
                    dialog.cancel();
                }
            });

            builder.show();
        } else {
            //just request the permission
            ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE_LOCATION);


        }


        if (permissionLocationStatus != null) {
            SharedPreferences.Editor editor = permissionLocationStatus.edit();
            editor.putBoolean(permission, true);
            editor.apply();
        }


    }


    // checking if already have permission or not //
    public void audioPermissionChecking(final Activity activity, final String permission){

        if(permissionAudioStatus != null) {
            permissionAudioStatus = getSharedPreferences("permissionAudioStatus", MODE_PRIVATE);
        }

        String title = null;
        String message = null;

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)){


            title = "Need Audio Permission...";
            message = "This app needs audio permission to record audio from your handset...";

            //Show Information about why you need the permission
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();


                    ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE_AUDIO);


                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        }else if (permissionAudioStatus != null && permissionAudioStatus.getBoolean(permission,false)) {


            title = "Need Audio Permission...";
            message = "This app needs audio permission to record audio from your handset...";

            //Previously Permission Request was cancelled with 'Dont Ask Again',
            // Redirect to Settings after showing Information about why you need the permission
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    sentToSettings = true;

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_AUDIO_PERMISSION_SETTING);
                    Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage permission", Toast.LENGTH_LONG).show();


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        }else {
            //just request the permission

            Log.d(TAG, "readAndWritePermissionChecking: is running");
            ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE_AUDIO);

        }


        if (permissionAudioStatus != null) {
            SharedPreferences.Editor editor = permissionAudioStatus.edit();
            editor.putBoolean(permission, true);
            editor.apply();
        }

    }

    // Checking Map is Enabled or Not //
    public boolean isMapsEnabled() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setMessage("This requires Your GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    Intent enableGpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(enableGpsIntent, PERMISSION_REQUEST_CODE_ENABLE_GPS);
                }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        final AlertDialog alert = builder.create();
        alert.show();
    }


    // Toast For our Whole App //
    public void inFormUser(Context context, String message) {

        WeakReference<Context> weakReferenceContext = new WeakReference<>(context);
        WeakReference<String> weakReferenceString = new WeakReference<>(message);

        Toast.makeText(weakReferenceContext.get(), weakReferenceString.get(), Toast.LENGTH_LONG).show();

    }

    // SnackBar For our Whole App //
    public void showSnackBar(View view, String message) {

        WeakReference<View> weakReferenceView = new WeakReference<>(view);
        WeakReference<String> weakReferenceString = new WeakReference<>(message);

        Snackbar.make(weakReferenceView.get(), weakReferenceString.get(), Snackbar.LENGTH_LONG).show();

    }

    // checking email is correctly written or not //
    public boolean isValidEmail(CharSequence target) {

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            return true;
        } else {
            return false;
        }

    }


    // Animations enterView //
    public void enterReveal(final View myView) {

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;

        // create the animator for this view (the start radius is zero)
        Animator anim =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);

                    myView.setVisibility(View.VISIBLE);
                }
            });
            // make the view visible and start the animation


            anim.start();

        }

    }

    // Animations ExitView //
    public void exitReveal(final View myView) {


        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the initial radius for the clipping circle
        int initialRadius = myView.getWidth() / 2;

        // create the animation (the final radius is zero)
        Animator anim =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);
            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    myView.setVisibility(View.INVISIBLE);

//                supportFinishAfterTransition(); // it will finish the activity after exit animation ends //
                }
            });

            anim.start();
        }

    }


    public String format(long value) {

        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry((long) value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

//    public void gotoLocationZoom(GoogleMap mGoogleMap, double lat, double lng, float zoom) {
//        LatLng latLng = new LatLng(lat, lng);
//        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
//        mGoogleMap.animateCamera(update);
//    }



}
