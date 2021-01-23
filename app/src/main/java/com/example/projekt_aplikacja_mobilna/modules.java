package com.example.projekt_aplikacja_mobilna;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class modules extends Activity {

    EditText modbusIndex, modbusValue;
    TextView textConfig, textOutput, modbusRes;
    Button modbusButtonRead, modbusButtonWrite;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_IP = "ip_key";
    public static final String KEY_PORT = "port_key";

    private String ip_key, port_key;

    public String       IP =            "192.168.001.204";  //IP of Modbus TCP server 192.168.001.204
    public String       port =          "502";              //Default Modbus TCP port 502
    public String       index =         "10";               //Holding register

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modules);

        textConfig = findViewById(R.id.textConfig);
        textOutput = findViewById(R.id.textOutput);
        modbusIndex = findViewById(R.id.modbusIndex);
        modbusValue = findViewById(R.id.modbusValue);
        modbusRes = findViewById(R.id.modbusRes);
        modbusButtonRead = findViewById(R.id.modbusButtonRead);
        modbusButtonWrite = findViewById(R.id.modbusButtonWrite);

        loadData();
        IP = ip_key;
        port = port_key;

        Intent myLocalIntent = getIntent();
        Bundle bundle = myLocalIntent.getExtras();
        String title = bundle.getString("key", "Default");

        // Set the panel title
        if(title != null && !title.isEmpty()) {
            textOutput.setText(title);
            bundle.putString("key2", "Home");
        }
        else {
            textOutput.setText("Nothing");
            bundle.putString("key2", "Empty message!");
        }
        myLocalIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK, myLocalIntent);

        // Buttons
        modbusButtonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readDataFromModbus();

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        modbusButtonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeDataToModbus();

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    public void onOptionClicked(View view) {
        TextView opt = (TextView) view;
        String data = opt.getText().toString();

        switch (data) {

            case "Home":
                finish();
                break;

            default:
        }
    }

    public void readDataFromModbus(){
        String[] paramsSW = {"","","",""};
        String indexTxt = modbusIndex.getText().toString();

        if (indexTxt.matches("")) {
            indexTxt = "10";
        }

        paramsSW[0] = IP;
        paramsSW[1] = port;
        paramsSW[2] = indexTxt;
        paramsSW[3] = "1";          //Amount of words to read

        ReadFromModbus mReadSW = new ReadFromModbus();
        try {
            String response = mReadSW.execute(paramsSW).get();
            modbusRes.setText("Register:  " + indexTxt + "\nValue:       " + response);

        } catch (ExecutionException e){
            System.out.println(e.getMessage());
            Toast.makeText(modules.this, "Read Error", Toast.LENGTH_SHORT).show();
        } catch (InterruptedException ie){
            System.out.println(ie.getMessage());
            Toast.makeText(modules.this, "Read Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void writeDataToModbus(){
        String[] paramsCW = {"","","",""};
        String indexTxt = modbusIndex.getText().toString();
        String valueTxt = modbusValue.getText().toString();

        if (indexTxt.matches("")) {
            indexTxt = "10";
        }

        if (valueTxt.matches("")) {
            valueTxt = "1";
        }

        paramsCW[0] = IP;
        paramsCW[1] = port;
        paramsCW[2] = indexTxt;
        paramsCW[3] = valueTxt;

        WriteToModbus mWriteCW = new WriteToModbus();
        mWriteCW.execute(paramsCW);
        readDataFromModbus();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        ip_key = sharedPreferences.getString(KEY_IP, "");
        port_key = sharedPreferences.getString(KEY_PORT, "");
    }
}