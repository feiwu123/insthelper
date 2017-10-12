package cn.com.xscaler.insthelper;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.speech.RecognizerIntent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.R.id.message;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static cn.com.xscaler.insthelper.R.id.editText;

public class FragMenuOffer extends Fragment implements View.OnClickListener  {

    private static MainActivity main_activity;
    private TextView joboffer;
    private View mContent;
    private TextView outmsg;
    private NetworkTask networktask = null;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    public static Fragment newInstance(MainActivity mactivity) {
        Fragment frag = new FragMenuOffer();
        main_activity = mactivity;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag_menu_offer, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize views
        mContent = view.findViewById(R.id.msg_content);
        mContent.setBackgroundColor(getResources().getColor(R.color.color_offer));

        outmsg = (TextView)view.findViewById(R.id.OutMsg);

        joboffer = (TextView)view.findViewById(R.id.editText);
        if (joboffer != null) {
            joboffer.setImeOptions(EditorInfo.IME_ACTION_DONE);
            joboffer.setRawInputType(InputType.TYPE_CLASS_TEXT);
        }


        Button a = (Button) view.findViewById(R.id.button);
        a.setOnClickListener(this);

        Button c = (Button) view.findViewById(R.id.photo);
        c.setOnClickListener(this);

        Button d = (Button) view.findViewById(R.id.voice);
        d.setOnClickListener(this);

        Button m=(Button) view.findViewById(R.id.map);
        m.setOnClickListener(this);
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (networktask != null) {
            networktask.cancel(true); //In case the task is currently running
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                uploadJob();
                break;
            case R.id.photo:
                Intent myIntent = new Intent(getActivity(), ActivityPhoto.class);
                getActivity().startActivity(myIntent);
                break;
            case R.id.voice:
                askSpeechInput();
                break;
            case R.id.map:
                Intent Intent =new Intent(getActivity(),ActivityLBS.class);
                getActivity().startActivity(Intent);
                break;
        }
    }

    private void uploadJob() {
        if (ActivityLogin.loginname.equals("anon")) {
            Intent myIntent = new Intent(getActivity(), ActivityLogin.class);
            getActivity().startActivity(myIntent);
            return;
        }

        /*double longitudeGPS, latitudeGPS;

        longitudeGPS = 0.0;
        latitudeGPS = 0.0;
        LocationManager locationManager =
                (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if ((locationManager != null) &&
                (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                 locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                ){
            try {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                longitudeGPS = location.getLongitude();
                latitudeGPS = location.getLatitude();
            } catch (SecurityException e) { }
        }*/

        String inmsg =  ActivityLogin.loginname + " "+ joboffer.getText().toString();
        NetworkTask networktask = new NetworkTask("OfferJob", inmsg, outmsg, null); //New instance of NetworkTask
        networktask.execute();
    }

    // Showing google speech input dialog

    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hi speak something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    // Receiving speech input

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    joboffer.setText(result.get(0));
                }
                break;
            }

        }
    }

}
