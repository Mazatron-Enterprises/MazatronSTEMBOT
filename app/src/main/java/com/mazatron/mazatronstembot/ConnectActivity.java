package com.mazatron.mazatronstembot;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;

import android.widget.TextView;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

import java.io.IOException;

import java.util.Set;
import java.util.UUID;

public class ConnectActivity extends AppCompatActivity {

    private  ImageView mStemImage;
    private  ImageView mMazatronView;
    private  Button    mVideosTut;
    private  TextView  mClickText;
    private  Button    mConnectBut;


    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    private ProgressDialog progress;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    String address = null;
    String[] name_device = null;
    String name = null;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        mStemImage    = findViewById(R.id.imagestem);
        mMazatronView = findViewById(R.id.mazlogopump);
        mVideosTut    = findViewById(R.id.videos);
        mClickText    = findViewById(R.id.clicktext);
        mConnectBut   = findViewById(R.id.connect_blue);


        mMazatronView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentWeb = new Intent();
                intentWeb.setAction(Intent.ACTION_VIEW);
                intentWeb.addCategory(Intent.CATEGORY_BROWSABLE);
                intentWeb.setData(Uri.parse("https://www.mazatron.com/index.php?route=information/contact"));
                startActivity(intentWeb);

            }
        });

        mStemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActIntent = new Intent(getApplicationContext(),MainActivity.class);
                mainActIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                mainActIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainActIntent);
            }
        });

        //@TO-DO link youtube tutorials
        mVideosTut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mVideoIntent = new Intent(getApplicationContext(),VideoActivity.class);
                mVideoIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                mVideoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mVideoIntent);
            }
        });

        if (!isBtConnected){
            mStemImage.setVisibility(View.INVISIBLE);
            mClickText.setVisibility(View.INVISIBLE);
        }else{
            mConnectBut.setText("DISCONNECT <- STEM BOT" );
            mConnectBut.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_disconnect,0,0,0);
        }


        mConnectBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    if (myBluetooth == null) {
                        //Show a mmsg that device has no bluetooth adapter
                        msg("Bluetooth Not Supported");
                        //finish apk
                        finish();
                    } else {
                        if (myBluetooth.isEnabled()) {
                            pairedDevicesList();
                        } else {
                            //Ask to the user turn the bluetooth on
                            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(turnBTon, 1);
                        }
                    }
                }else{
                    disonnectBLE();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            pairedDevicesList();
        } else {
            msg("Scan and Pair to Bluetooth device for further working..");
        }

    }

    private void pairedDevicesList() {
        pairedDevices = myBluetooth.getBondedDevices();

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ConnectActivity.this);
        builderSingle.setIcon(android.R.drawable.ic_media_ff);
        builderSingle.setTitle("Select Device: ");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ConnectActivity.this, android.R.layout.select_dialog_singlechoice);
        if (pairedDevices.size()>0) {
            for(BluetoothDevice bt : pairedDevices) {
                if (bt.getName().contains("MAZ_STM")) {
                    arrayAdapter.add(bt.getName() + "\n" + bt.getAddress());
                }
            }
        }
        else {
            msg("No Paired Bluetooth Devices Found.");
        }

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                /*

                AlertDialog.Builder builderInner = new AlertDialog.Builder(ConnectActivity.this);
                builderInner.setMessage(strName);
                builderInner.setTitle("Your Selected Item is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                    }
                });
                builderInner.show();
                */
                address =  strName.substring(strName.length() - 17);
                name_device    =  strName.split("\n");
                name = name_device[0];
                new ConnectBT().execute(); //Call the class to connect

            }
        });
        builderSingle.show();
    }


    private class  ConnectBT extends AsyncTask<Void, Void, Void> { // UI thread
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ConnectActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) {//while the progress dialog is shown, the connection is done in background
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositive = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositive.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) { //after the doInBackground, it checks if everything went fine
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                //finish();
                progress.dismiss();
            }
            else {
                msg("Connected.");
                isBtConnected = true;
                MyBtSocket globalBtSocket = (MyBtSocket) getApplication();
                globalBtSocket.setupBluetoothConnection(btSocket);
                mStemImage.setVisibility(View.VISIBLE);
                mClickText.setVisibility(View.VISIBLE);
                doBounceAnimation(mClickText);

                mConnectBut.setText("DISCONNECT <- STEM BOT" );
                mConnectBut.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_disconnect,0,0,0);
                //selectDevice.setText("Connected To: "+name);
            }
            progress.dismiss();
        }
    }


    private void doBounceAnimation(View targetView) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(targetView, "Alpha", 0, 1);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.setDuration(700);
        anim.start();
    }


    // fast way to call Toast
    private void msg(String s) {
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
    }

    private void disonnectBLE()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}

            mConnectBut.setText("CONNECT -> STEM BOT");
            mConnectBut.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_connect,0,0,0);
            isBtConnected = false;
            mClickText.setVisibility(View.INVISIBLE);
            mStemImage.setVisibility(View.INVISIBLE);
        }
    }

}
