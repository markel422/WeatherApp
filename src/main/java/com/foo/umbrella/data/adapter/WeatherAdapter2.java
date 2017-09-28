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
import com.foo.umbrella.main.MainActivity;
import com.foo.umbrella.data.model.ForecastCondition;

import org.threeten.bp.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mike0 on 9/21/2017.
 */

public class WeatherAdapter2 extends RecyclerView.Adapter<WeatherAdapter2.MyHolder> {


    private static final String TAG = WeatherAdapter2.class.getSimpleName() + "_TAG";

    private Context context;
    private List<ForecastCondition> weatherList;

    private String formedDate;
    private Date date1, date2;

    private String formatDateTime2;

    private String newDate;

    SharedPreferences weatherPref;

    private int iconId[] =
            {
                    R.drawable.weather_cloudy, R.drawable.weather_fog, R.drawable.weather_rainy, R.drawable.weather_rainy,
                    R.drawable.weather_sunny, R.drawable.weather_lightning, R.drawable.weather_lightning_rainy, R.drawable.weather_snowy_rainy,
                    R.drawable.weather_snowy, R.drawable.weather_hail, R.drawable.weather_windy_variant
            };

    public WeatherAdapter2(Context context, List<ForecastCondition> weatherDataList) {
        this.context = context;
        this.weatherList = weatherDataList;
    }

    public void updateDataSet(List<ForecastCondition> weatherList) {
        this.weatherList = weatherList;
        notifyDataSetChanged();
    }

    /*public void removeItemsCount(int count) {
        if (!weatherList.isEmpty()) {
            for (int i = 0; i < count; i++) {
                weatherList.remove(0);
                notifyItemRemoved(0);
                notifyItemRangeChanged(count, weatherList.size());
            }
        }
    }*/

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public WeatherAdapter2.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout2, null);
        WeatherAdapter2.MyHolder myHolder = new WeatherAdapter2.MyHolder(layout);

        return myHolder;
    }

    @Override
    public void onBindViewHolder(WeatherAdapter2.MyHolder holder, int position) {
        weatherPref = PreferenceManager.getDefaultSharedPreferences(context);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        String formatDateTime = weatherList.get(position).getDateTime().format(formatter);
        holder.hourly_txt.setText(formatDateTime);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yyyy");
        formedDate = dateFormat.format(currentTime);

        try {
            date1 = dateFormat.parse(formedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*Log.d(TAG, "Current Time: " +  formedDate);
        Log.d(TAG, "date1: " +  date1);*/

        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("M/dd/yyyy");
        formatDateTime2 = weatherList.get(position).getDateTime().format(formatter2);
        //Log.d(TAG, "onBindViewHolder Formed Date: " + formatDateTime2);

        try {
            date2 = dateFormat.parse(formatDateTime2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        newDate = dateFormat.format(date2);
        /*Log.d(TAG, "date2: " +  date2);
        Log.d(TAG, "newDate: " + newDate);*/

        if (date2.after(date1)) {
            //Log.d(TAG, "Adapter position: " + holder.getAdapterPosition());
        }

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

        if (weatherPref.getString("unitsData", "").equals("Celsius")) {
            holder.degrees_txt.setText(weatherList.get(position).getTempCelsius() + "\u00B0");
        } else {
            holder.degrees_txt.setText(weatherList.get(position).getTempFahrenheit() + "\u00B0");
        }
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private TextView hourly_txt;
        private ImageView weather_img;
        private TextView degrees_txt;

        public MyHolder(View itemView) {
            super(itemView);
            hourly_txt = (TextView) itemView.findViewById(R.id.hourly_tv2);
            weather_img = (ImageView) itemView.findViewById(R.id.weather_img2);
            degrees_txt = (TextView) itemView.findViewById(R.id.hourly_degrees2);
        }
    }
}
