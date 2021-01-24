package com.example.projekt_aplikacja_mobilna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView textConfig, textModules, textModbus;
    CheckBox led1, led2;
    Button sendBtn, modbusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textConfig = findViewById(R.id.textConfig);
        textModules = findViewById(R.id.textModules);
        led1 = findViewById(R.id.checkBox1);
        led2 = findViewById(R.id.checkBox2);
        sendBtn = findViewById(R.id.sendButton);
    }

    public void onOptionClicked(View view) {

        TextView opt = (TextView) view;
        String data = opt.getText().toString();

        switch (data) {

            case "Configuration":
                Intent intentConfig = new Intent(MainActivity.this, configuration.class);
                Bundle bundleConfig = new Bundle();
                bundleConfig.putString("key", data);
                intentConfig.putExtras(bundleConfig);
                startActivityForResult(intentConfig,101);
                break;

            case "Modules":
                Intent intentModules = new Intent(MainActivity.this, modules.class);
                Bundle bundleModules = new Bundle();
                bundleModules.putString("key", data);
                intentModules.putExtras(bundleModules);
                startActivityForResult(intentModules,101);
                break;

            default:
        }
    }

    public void onSend(View view) {
        WebView myWebView = (WebView) findViewById(R.id.webview);
        String L1, L2;

        L1 = led1.isChecked() ? "1" : "0";
        L2 = led2.isChecked() ? "2" : "0";

        String url = "http://192.168.1.204/leds.cgi?led="+L1+"&led="+L2;
        myWebView.loadUrl(url);

        if (led1.isChecked()) led1.setText("Led 1 [ON]");
        else led1.setText("Led 1 [OFF]");
        if (led2.isChecked()) led2.setText("Led 2 [ON]");
        else led2.setText("Led 2 [OFF]");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if ((requestCode == 101 ) && (resultCode == Activity.RESULT_OK)){
                Bundle myResultBundle = data.getExtras();
                String myResult = myResultBundle.getString("key2");
                Toast.makeText(MainActivity.this, myResult, Toast.LENGTH_SHORT).show();
//                textOutput.setText(myResult);
            }
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}