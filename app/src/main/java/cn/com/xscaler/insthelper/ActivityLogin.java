package cn.com.xscaler.insthelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static cn.com.xscaler.insthelper.MainActivity.LOGIN_MESSAGE;
import static cn.com.xscaler.insthelper.MainActivity.PASSWORD_MESSAGE;

public class ActivityLogin extends Activity {
    private NetworkTask networktask = null;
    TextView textStatus;
    private MainActivity main_activity;
    public static String loginname = "anon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textStatus = (TextView)findViewById(R.id.chat_sdk_version_name);
        //Intent intent = getIntent();

    }

    /*public void doButtonLoginClicked() {
        Intent intent = new Intent(this, ActivityHome.class);
        EditText loginText = (EditText) findViewById(R.id.chat_sdk_et_mail);
        String loginmessage = loginText.getText().toString();
        intent.putExtra(LOGIN_MESSAGE, loginmessage);
        EditText passwordText = (EditText) findViewById(R.id.chat_sdk_et_password);
        String passwordmessage = loginText.getText().toString();
        intent.putExtra(PASSWORD_MESSAGE, passwordmessage);
        startActivity(intent);
    }*/

    public void check_server(String cmd) {
        EditText loginText = (EditText) findViewById(R.id.chat_sdk_et_mail);
        String loginmessage = loginText.getText().toString();
        EditText passwordText = (EditText) findViewById(R.id.chat_sdk_et_password);
        String passwordmessage = passwordText.getText().toString();

        String svrcmd = loginmessage + " " + passwordmessage;
        //New instance of NetworkTask
        networktask = new NetworkTask(cmd, svrcmd, textStatus, null);
        networktask.execute();

    }

    public void ButtonLoginClicked(View view) {
        check_server("login");
    }
    public void ButtonRegisterClicked(View view) {
        check_server("register");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networktask != null) {
            networktask.cancel(true); //In case the task is currently running
        }
    }

}
