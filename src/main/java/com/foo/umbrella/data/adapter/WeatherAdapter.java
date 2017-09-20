package com.foo.umbrella.data.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foo.umbrella.R;
import com.foo.umbrella.data.model.CurrentObservation;
import com.foo.umbrella.data.model.ForecastCondition;
import com.foo.umbrella.WeatherMain.MainActivity;
import com.squareup.picasso.Picasso;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by mike0 on 9/10/2017.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyHolder> {

    private static final String TAG = MainActivity.class.getSimpleName() + "_TAG";

    private Context context;
    private List<ForecastCondition> weatherList;
    private CurrentObservation observationData;
    private String currentObservation;

    private String formatDateTime2;

    private boolean checkCelsius = false;
    SharedPreferences weatherPref;

    private int iconId[] =
            {
                    R.drawable.weather_cloudy, R.drawable.weather_fog, R.drawable.weather_rainy, R.drawable.weather_rainy,
                    R.drawable.weather_sunny, R.drawable.weather_lightning, R.drawable.weather_lightning_rainy, R.drawable.weather_snowy_rainy,
                    R.drawable.weather_snowy, R.drawable.weather_hail, R.drawable.weather_windy_variant
            };

    public WeatherAdapter(Context context, List<ForecastCondition> weatherDataList) {
        this.context = context;
        this.weatherList = weatherDataList;
        this.observationData = observationData;
    }

    public WeatherAdapter(List<ForecastCondition> weatherDataList, String currentObservation) {
        this.weatherList = weatherDataList;
        this.currentObservation = currentObservation;
    }

    public void updateDataSet(List<ForecastCondition> resultList, String currentObservation) {
        this.weatherList = resultList;
        this.currentObservation = currentObservation;
        notifyDataSetChanged();
    }

    public String getDateTime() {
        return formatDateTime2;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout, null);
        MyHolder myHolder = new MyHolder(layout);

        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        String formatDateTime = weatherList.get(position).getDateTime().format(formatter);
        holder.hourly_txt.setText(formatDateTime);

        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("M/dd/yyyy");
        formatDateTime2 = weatherList.get(position).getDateTime().format(formatter2);
        Log.d(TAG, "onBindViewHolder Formed Date: " + formatDateTime2);

        switch (weatherList.get(position).getIcon()) {
            case "clear":
                holder.weather_img.setImageResource(R.drawable.weather_sunny);
                break;
            case "mostlycloudy":
                holder.weather_img.setImageResource(R.drawable.weather_cloudy);
                break;
            case "cloudy":
                holder.weather_img.setImageResource(R.drawable.weather_cloudy);
                break;
            case "partlycloudy":
                holder.weather_img.setImageResource(R.drawable.weather_fog);
                break;
            case "chancerain":
                holder.weather_img.setImageResource(R.drawable.weather_rainy);
                break;
            case "rain":
                holder.weather_img.setImageResource(R.drawable.weather_rainy);
                break;
        }
        checkCelsius = SettingsAdapter.celsiusSelected();
        weatherPref = PreferenceManager.getDefaultSharedPreferences(context);

        if (checkCelsius == true || weatherPref.getString("unitsData", "") == "Celsius") {
            holder.degrees_txt.setText(weatherList.get(position).getTempCelsius() + "\u00B0");
        } else {
            holder.degrees_txt.setText(weatherList.get(position).getTempFahrenheit() + "\u00B0");
        }
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private TextView hourly_txt;
        private ImageView weather_img;
        private TextView degrees_txt;

        public MyHolder(View itemView) {
            super(itemView);
            hourly_txt = (TextView) itemView.findViewById(R.id.hourly_tv);
            weather_img = (ImageView) itemView.findViewById(R.id.weather_img);
            degrees_txt = (TextView) itemView.findViewById(R.id.hourly_degrees);
        }
    }
}
