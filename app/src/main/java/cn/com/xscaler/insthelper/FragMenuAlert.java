package cn.com.xscaler.insthelper;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Fragment class for each nav menu item
 */
public class FragMenuAlert extends Fragment implements View.OnClickListener {

    private static MainActivity main_activity;
    private View mContent;
    private NetworkTask networktask;

    public static Fragment newInstance(MainActivity mactivity) {
        Fragment frag = new FragMenuAlert();
        main_activity = mactivity;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag_menu_alert, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize views
        mContent = view.findViewById(R.id.alert_content);
        mContent.setBackgroundColor(getResources().getColor(R.color.color_alert));

        TextView name1 = (TextView) view.findViewById(R.id.name1);
        TextView msg1 = (TextView) view.findViewById(R.id.msg1);
        networktask = new NetworkTask("GetJob", "1", msg1, name1); //New instance of NetworkTask
        networktask.execute();
        Button bid1 = (Button) view.findViewById(R.id.bid1);
        bid1.setOnClickListener(this);

        TextView name2 = (TextView) view.findViewById(R.id.name2);
        TextView msg2 = (TextView) view.findViewById(R.id.msg2);
        networktask = new NetworkTask("GetJob", "2", msg2, name2); //New instance of NetworkTask
        networktask.execute();
        Button bid2 = (Button) view.findViewById(R.id.bid2);
        bid2.setOnClickListener(this);

        TextView name3 = (TextView) view.findViewById(R.id.name3);
        TextView msg3 = (TextView) view.findViewById(R.id.msg3);
        networktask = new NetworkTask("GetJob", "3", msg3, name3); //New instance of NetworkTask
        networktask.execute();
        Button bid3 = (Button) view.findViewById(R.id.bid3);
        bid3.setOnClickListener(this);

        TextView name4 = (TextView) view.findViewById(R.id.name4);
        TextView msg4 = (TextView) view.findViewById(R.id.msg4);
        networktask = new NetworkTask("GetJob", "4", msg4, name4); //New instance of NetworkTask
        networktask.execute();
        Button bid4 = (Button) view.findViewById(R.id.bid4);
        bid4.setOnClickListener(this);


        TextView name5 = (TextView) view.findViewById(R.id.name5);
        TextView msg5 = (TextView) view.findViewById(R.id.msg5);
        networktask = new NetworkTask("GetJob", "5", msg5, name5); //New instance of NetworkTask
        networktask.execute();
        Button bid5 = (Button) view.findViewById(R.id.bid5);
        bid5.setOnClickListener(this);


        TextView name6 = (TextView) view.findViewById(R.id.name6);
        TextView msg6 = (TextView) view.findViewById(R.id.msg6);
        networktask = new NetworkTask("GetJob", "6", msg6, name6); //New instance of NetworkTask
        networktask.execute();
        Button bid6 = (Button) view.findViewById(R.id.bid6);
        bid6.setOnClickListener(this);

    }

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
            case R.id.bid1:
            case R.id.bid2:
            case R.id.bid3:
            case R.id.bid4:
            case R.id.bid5:
            case R.id.bid6:
                if (ActivityLogin.loginname.equals("anon")) {
                    Intent myIntent = new Intent(getActivity(), ActivityLogin.class);
                    getActivity().startActivity(myIntent);
                } else {
                    main_activity.PostAlert("Offer", "Bid this offer");
                }
                break;
            default:
                break;
        }
    }
}
