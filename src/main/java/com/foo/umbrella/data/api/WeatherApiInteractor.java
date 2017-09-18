package com.foo.umbrella.data.api;

import com.foo.umbrella.data.model.ForecastCondition;
import com.foo.umbrella.data.model.WeatherData;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mike0 on 9/18/2017.
 */

public class WeatherApiInteractor {

    private WeatherService weatherServiceService;
    private OnWeatherResponseListener listener;

    public interface OnWeatherResponseListener {
        void onWeatherResponseDone(List<ForecastCondition> results);
        void onWeatherResponseError();
    }


    @Inject
    public WeatherApiInteractor(WeatherService weatherServiceService) {
        this.weatherServiceService = weatherServiceService;
    }

    public void setOnRandomResponseListener(OnWeatherResponseListener listener) {
        this.listener = listener;
    }

    public void getWeather(String zipcode) {
        weatherServiceService.forecastForZipCallable(zipcode).enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    listener.onWeatherResponseDone(response.body().getForecast());
                } else {
                    listener.onWeatherResponseError();
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                listener.onWeatherResponseError();
            }
        });
    }
}
