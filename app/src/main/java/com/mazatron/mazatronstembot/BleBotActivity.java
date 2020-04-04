package com.mazatron.mazatronstembot;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class BleBotActivity extends AppCompatActivity {

    private EditText mRpmView;
    private int mRpmEntered;
    private LinearLayout mJoyStickView;

    private boolean mbuttonind = false;
    private Button mButtonSend;
    BluetoothSocket myBtSocket;

    @Override
    protected void onStop() {
        super.onStop();
        sendBleData("BLESTOP");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_bot);

        mRpmView      = findViewById(R.id.rpmView);
        mButtonSend   = findViewById(R.id.buttonsend);
        mJoyStickView = findViewById(R.id.joystick);

        //get bluetooth instance
        MyBtSocket isBtDeviceConnected = (MyBtSocket) getApplication();
        myBtSocket = isBtDeviceConnected.getCurrentBluetoothConnection();

        //disable
        mJoyStickView.setVisibility(View.INVISIBLE);

        mRpmView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        if (mRpmView.getText() != null && !(mRpmView.getText().toString().equals(""))){
                            mRpmEntered = Integer.parseInt(mRpmView.getText().toString());
                            if (mRpmEntered > 255){
                                mRpmView.setText("255");
                                Toast.makeText(getApplicationContext(),"RPM max value is 255",Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mbuttonind){
                    if ( (mRpmView.getText() != null) && (!mRpmView.getText().toString().equals("")) ){

                        if (myBtSocket != null) {

                            String rpmVal = mRpmView.getText().toString();
                            String finalVal = "BLER" + rpmVal;
                            sendBleData(finalVal);
                            mRpmView.setEnabled(false);
                            mRpmView.setBackgroundResource(R.drawable.editdisabled);

                            //Change text to STOP
                            mButtonSend.setText(" STOP  ");
                            mbuttonind = true;
                            mButtonSend.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_close_clear_cancel, 0, 0, 0);
                            mJoyStickView.setVisibility(View.VISIBLE);
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Enter value !",Toast.LENGTH_SHORT).show();
                    }
                }else if (mbuttonind){
                    sendBleData("BLESTOP");
                    mRpmView.setEnabled(true);
                    mRpmView.setBackgroundResource(R.drawable.edittextdesign);

                    mbuttonind = false;
                    mButtonSend.setText(" START/SEND  ");
                    mButtonSend.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_send,0,0,0);
                    mJoyStickView.setVisibility(View.INVISIBLE);
                }
            }
        });


    }

    public void botBackward(View view) {
        sendBleData("BLEB");
    }

    public void botRight(View view) {
        sendBleData("BLER");
    }

    public void botForward(View view) {
        sendBleData("BLEF");
    }

    public void botLeft(View view){
        sendBleData("BLEL");
    }


    private  void sendBleData(String commandBle){

        if (myBtSocket!=null) {
            try {
                myBtSocket.getOutputStream().write(commandBle.getBytes());
            }
            catch (IOException e) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
