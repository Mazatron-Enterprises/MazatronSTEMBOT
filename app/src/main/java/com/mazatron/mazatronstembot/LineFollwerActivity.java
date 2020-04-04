package com.mazatron.mazatronstembot;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class LineFollwerActivity extends AppCompatActivity {

    private Spinner mdropdown;
    private Button mCalibrate;
    private TextView mTextView;
    private EditText mRpmView;
    private EditText mDelayView;
    private int mRpmEntered;

    private boolean mCalbInd   = false;
    private boolean mbuttonind = false;
    private LinearLayout mlinearlayout;
    private Button mButtonSend;

    BluetoothSocket myBtSocket;

    @Override
    protected void onStop() {
        super.onStop();
        sendLineData("LINSTOP");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_follwer);

        mCalibrate    = findViewById(R.id.calibrate);
        mdropdown     = findViewById(R.id.dropdownbtn);
        mTextView     = findViewById(R.id.headtext);
        mRpmView      = findViewById(R.id.rpmView);
        mDelayView    = findViewById(R.id.delayView);
        mlinearlayout = findViewById(R.id.linearlayoutline);
        mButtonSend   = findViewById(R.id.buttonsend);

        // disable evrything
        mCalibrate.setVisibility(View.INVISIBLE);
        mTextView.setVisibility(View.INVISIBLE);
        mlinearlayout.setVisibility(View.INVISIBLE);
        mButtonSend.setVisibility(View.INVISIBLE);

        //get bluetooth instance
        MyBtSocket isBtDeviceConnected = (MyBtSocket) getApplication();
        myBtSocket = isBtDeviceConnected.getCurrentBluetoothConnection();

        //create a list of items for the spinner.
        String[] items = new String[]{"Select Type" ,"Black Line on White Background", "White Line on Black Background"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        mdropdown.setAdapter(adapter);

        mdropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        mCalbInd = false;
                        mTextView.setText("Kindly Calibrate Now");
                        mCalibrate.setText(" Calibrate Now ");
                        mCalibrate.setVisibility(View.INVISIBLE);
                        mTextView.setVisibility(View.INVISIBLE);
                        mlinearlayout.setVisibility(View.INVISIBLE);
                        mButtonSend.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        mCalbInd = false;
                        mTextView.setText("Kindly Calibrate Now");
                        mCalibrate.setText(" Calibrate Now ");
                        mCalibrate.setVisibility(View.VISIBLE);
                        mTextView.setVisibility(View.VISIBLE);
                        mlinearlayout.setVisibility(View.INVISIBLE);
                        mButtonSend.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        mCalbInd = false;
                        mTextView.setText("Kindly Calibrate Now");
                        mCalibrate.setText(" Calibrate Now ");
                        mCalibrate.setVisibility(View.VISIBLE);
                        mTextView.setVisibility(View.VISIBLE);
                        mlinearlayout.setVisibility(View.INVISIBLE);
                        mButtonSend.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        mCalbInd = false;
                        mTextView.setText("Kindly Calibrate Now");
                        mCalibrate.setText(" Calibrate Now ");
                        mTextView.setVisibility(View.INVISIBLE);
                        mCalibrate.setVisibility(View.INVISIBLE);
                        mlinearlayout.setVisibility(View.INVISIBLE);
                        mButtonSend.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            //Auto generated
            }
        });

        mCalibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mCalbInd) {
                    mCalbInd = true;
                    mCalibrate.setText("Done Calibration");
                    mTextView.setText("Click Below button after Calibration is done");

                    int dSel = mdropdown.getSelectedItemPosition();
                    if (dSel == 1){
                        sendLineData("LINB");
                    }else if (dSel == 2){
                        sendLineData("LINW");
                    }

                }else {
                    mCalibrate.setVisibility(View.INVISIBLE);
                    mTextView.setVisibility(View.INVISIBLE);
                    mlinearlayout.setVisibility(View.VISIBLE);
                    mButtonSend.setVisibility(View.VISIBLE);
                }
            }
        });

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
                    if ( (mRpmView.getText() != null) && (!mRpmView.getText().toString().equals(""))
                        && (mDelayView.getText() !=null) && (!mDelayView.getText().toString().equals("")) ){


                             //Change text to STOP
                            if (myBtSocket != null) {
                                String rpmVal = mRpmView.getText().toString();
                                String delVal = mDelayView.getText().toString();

                                String finalVal = "LINR" + rpmVal + "LIND" + delVal;
                                sendLineData(finalVal);
                                mDelayView.setEnabled(false);
                                mDelayView.setBackgroundResource(R.drawable.editdisabled);
                                mRpmView.setEnabled(false);
                                mRpmView.setBackgroundResource(R.drawable.editdisabled);

                                mButtonSend.setText(" STOP  ");
                                mbuttonind = true;
                                mButtonSend.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_close_clear_cancel, 0, 0, 0);
                            }
                    }else {
                        Toast.makeText(getApplicationContext(),"Enter Both values !",Toast.LENGTH_SHORT).show();
                    }
                }else if (mbuttonind){
                    sendLineData("LINSTOP");
                    mDelayView.setEnabled(true);
                    mDelayView.setBackgroundResource(R.drawable.edittextdesign);
                    mRpmView.setEnabled(true);
                    mRpmView.setBackgroundResource(R.drawable.edittextdesign);

                    mbuttonind = false;
                    mButtonSend.setText(" START/SEND  ");
                    mButtonSend.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_send,0,0,0);

                }
            }
        });
    }

    private  void sendLineData(String commandBle){

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