package com.adamzemmoura.wxforecast;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.security.auth.login.LoginException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather mCurrentWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String apiKey = getResources().getText(R.string.api_key).toString();
        String latitude = "37.8267";
        String longitude = "-122.4233";

        String forecastUrl = "https://api.darksky.net/forecast/" + apiKey + "/" + latitude + "," + longitude;
        Log.d(TAG, forecastUrl);
        Log.d(TAG, "TEST");

        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mCurrentWeather = getCurrentDetails(jsonData);
                        } else {
                            alertUserAboutError(null);
                        }
                    }
                    catch (IOException e) {
                        Log.e(TAG, "Exception caught : ", e);
                    }
                    catch (JSONException e) {
                        alertUserAboutError(AlertDialogFragment.AlertType.JSON_Parsing_Error);
                    }
                }
            });
        } else {
            alertUserAboutError(AlertDialogFragment.AlertType.No_Network_Error);
        }


    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {

        JSONObject forecast = new JSONObject(jsonData);
        JSONObject currently = forecast.getJSONObject("currently");
        String icon = currently.getString("icon");
        long time = currently.getLong("time");
        double temperature = currently.getDouble("temperature");
        double precipChance = currently.getDouble("precipProbability");
        double humidity = currently.getDouble("humidity");

        return new CurrentWeather(icon, time, temperature, humidity, precipChance);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    private void alertUserAboutError(AlertDialogFragment.AlertType type) {
        AlertDialogFragment dialog = new AlertDialogFragment();
        if (type != null) {
            dialog.setType(type);
        }
        dialog.show(getFragmentManager(), "error_dialog");
    }
}
