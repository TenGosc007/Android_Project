package com.example.projekt_aplikacja_mobilna;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class configuration extends Activity {

    EditText configIp, configPort;
    TextView textConfig, textOutput;
    Button configButton;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_IP = "ip_key";
    public static final String KEY_PORT = "port_key";

    private String ip_key, port_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);

        textConfig = findViewById(R.id.textConfig);
        textOutput = findViewById(R.id.textOutput);
        configIp = findViewById(R.id.configIp);
        configPort = findViewById(R.id.configPort);
        configButton = findViewById(R.id.configButton);

        navPanel();

        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        loadData();
        updateViews();
    }

    public void navPanel() {
        Intent myLocalIntent = getIntent();
        Bundle bundle = myLocalIntent.getExtras();
        String title = bundle.getString("key", "Default");

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

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_IP, configIp.getText().toString());
        editor.putString(KEY_PORT, configPort.getText().toString());

        editor.apply();

        Toast.makeText(configuration.this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        ip_key = sharedPreferences.getString(KEY_IP, "");
        port_key = sharedPreferences.getString(KEY_PORT, "");
    }

    public void updateViews() {
        configIp.setText(ip_key);
        configPort.setText(port_key);
    }
}