package cn.com.xscaler.insthelper;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import static android.R.attr.host;
import static android.R.attr.port;

public class NetworkTask extends AsyncTask<Void, byte[], Boolean> {

    String dstAddress;
    int dstPort;
    String sendcmd;
    String sendmsg;
    Socket nsocket; //Network Socket
    InputStream nis; //Network Input Stream
    OutputStream nos; //Network Output Stream
    TextView textStatus;
    TextView textStatus2;

    NetworkTask(String msendcmd, String msendmsg, TextView mtextStatus, TextView mtextStatus2) {
        dstAddress = "221.6.33.186";
        dstPort = 33445;
        sendcmd = msendcmd;
        sendmsg = msendmsg;
        textStatus = mtextStatus;
        textStatus2 = mtextStatus2;
    }


    @Override
    protected void onPreExecute() {
        Log.i("AsyncTask", "onPreExecute");
    }

    @Override
    protected Boolean doInBackground(Void... params) { //This runs on a different thread
        boolean result = false;
        try {
            Log.i("AsyncTask", "doInBackground: Creating socket");
            SocketAddress sockaddr = new InetSocketAddress(dstAddress, dstPort);
            nsocket = new Socket();
            nsocket.connect(sockaddr, 5000); //10 second connection timeout
            if (nsocket.isConnected()) {

                nis = nsocket.getInputStream();
                nos = nsocket.getOutputStream();
                Log.i("AsyncTask", "doInBackground: Socket created, streams assigned");
                Log.i("AsyncTask", "doInBackground: Waiting for inital data...");
                SendDataToNetwork(sendcmd+" "+sendmsg);

                byte[] buffer = new byte[4096];
                int read = nis.read(buffer, 0, 4096); //This is blocking
                while(read != -1){
                    byte[] tempdata = new byte[read];
                    System.arraycopy(buffer, 0, tempdata, 0, read);
                    publishProgress(tempdata);
                    Log.i("AsyncTask", "doInBackground: Got some data");
                    read = nis.read(buffer, 0, 4096); //This is blocking
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("AsyncTask", "doInBackground: IOException");
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("AsyncTask", "doInBackground: Exception");
            result = true;
        } finally {
            try {
                nis.close();
                nos.close();
                nsocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("AsyncTask", "doInBackground: Finished");
        }
        return result;
    }

    public void SendDataToNetwork(String cmd) { //You run this from the main thread.
        try {
            if (nsocket.isConnected()) {
                Log.i("AsyncTask", "SendDataToNetwork: Writing received message to socket");
                nos.write(cmd.getBytes());
            } else {
                Log.i("AsyncTask", "SendDataToNetwork: Cannot send message. Socket is closed");
            }
        } catch (Exception e) {
            Log.i("AsyncTask", "SendDataToNetwork: Message send failed. Caught an exception");
        }
    }

    @Override
    protected void onProgressUpdate(byte[]... values) {
        if (values.length > 0) {
            Log.i("AsyncTask", "onProgressUpdate: " + values[0].length + " bytes received.");
            if (textStatus2 == null) {
                textStatus.setText(new String(values[0]));
            }
            else {
                String s = new String(values[0]);
                String[] splited = s.split(" ");
                textStatus2.setText(splited[0]);
                textStatus.setText(s);
            }
        }
    }
    @Override
    protected void onCancelled() {
        Log.i("AsyncTask", "Cancelled.");
        //btnStart.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Log.i("AsyncTask", "onPostExecute: Completed with an Error.");
            textStatus.setText("Error: Failed to connect.");
        } else {
            Log.i("AsyncTask", "onPostExecute: Completed.");
            if (textStatus.getText().toString().equals("Success")) {
                if (sendcmd.equals("login") || sendcmd.equals("register")) {
                    int space = sendmsg.indexOf(' ');
                    ActivityLogin.loginname = sendmsg.substring(0, space);
                }
                //main_activity.doButtonLoginClicked();
            }
        }
        //btnStart.setVisibility(View.VISIBLE);
    }
}
