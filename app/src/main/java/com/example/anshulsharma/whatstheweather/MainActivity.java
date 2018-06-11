package com.example.anshulsharma.whatstheweather;

import android.content.Context;
import android.hardware.input.InputManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String>name=new ArrayList<>();
    ArrayList<String>weather=new ArrayList<>();
    EditText cityName;
    Button checkWeather;
    TextView displayWeather;

    public class DownloadTask extends AsyncTask<String,Void ,String>{


        @Override
        protected String doInBackground(String... urls) {

            String result="";
            URL url;
            HttpURLConnection urlConnection=null;
            try {
                url = new URL(urls[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while (data!=-1){
                 char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String nameInfo=jsonObject.getString("list");
                String weatherInfo;
                JSONArray nameArr=new JSONArray(nameInfo);
                for(int i=0;i<nameArr.length();i++){
                    JSONObject jsonPart=nameArr.getJSONObject(i);
                    name.add(jsonPart.getString("name"));
                    weatherInfo=jsonPart.getString("weather");
                    JSONArray weatherArr=new JSONArray(weatherInfo);
                    for(int j=0;j<weatherArr.length();j++){
                        JSONObject weatherPart=weatherArr.getJSONObject(j);
                        weather.add(weatherPart.getString("main"));
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    public void check(View view){

        String city=cityName.getText().toString();
        int a=0;
        for(int i=0;i<name.size();i++){
            if(name.get(i).equals(city))
                displayWeather.setText(weather.get(i));
            else
                a++;
        }
        if(a==name.size())
            displayWeather.setText("Sorry");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task=new DownloadTask();
        task.execute("http://samples.openweathermap.org/data/2.5/box/city?bbox=12,32,15,37,10&appid=b6907d289e10d714a6e88b30761fae22");

        cityName=findViewById(R.id.city);
        checkWeather=findViewById(R.id.weather);
        displayWeather=findViewById(R.id.displayWeather);
        InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(),0);
    }

}
