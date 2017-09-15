package com.foo.umbrella.data.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.foo.umbrella.R;
import com.foo.umbrella.ui.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike0 on 9/10/2017.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.MyHolder> {

    private static final String TAG = SettingsAdapter.class.getSimpleName() + "_TAG";
    private static final String WEATHERPREF = "weatherZipUnits";

    private Context context;
    private String settings_title[];
    private String settings[];
    private static boolean isCelsius = false;

    public SettingsAdapter() {}

    public SettingsAdapter(Context context, String[] setting_title, String[] setting) {
        this.context = context;
        this.settings_title = setting_title;
        this.settings = setting;
    }

    public static boolean celsiusSelected() {
        return isCelsius;
    }

    @Override
    public SettingsAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(context).inflate(R.layout.settings_layout, null);
        SettingsAdapter.MyHolder myHolder = new SettingsAdapter.MyHolder(layout);

        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {


        holder.settings_title_txt.setText(settings_title[position]);
        holder.settings_txt.setText(settings[position]);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Clicked " + holder.settings_title_txt.getText().toString(), Toast.LENGTH_SHORT).show();
                switch (holder.settings_title_txt.getText().toString()) {
                    case "Zip":
                        Dialog dialog = new Dialog(context);
                        dialog.setTitle("Enter A Zipcode");
                        dialog.setContentView(R.layout.dialog_zipcode);
                        // Get the layout inflater
                        dialog.show();

                        EditText zipcodeTV = (EditText) dialog.findViewById(R.id.zipcode_tv);
                        Button okBtn = (Button) dialog.findViewById(R.id.btn_ok);
                        Button cancelBtn = (Button) dialog.findViewById(R.id.btn_cancel);

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.settings_txt.setText(zipcodeTV.getText().toString());

                                SharedPreferences weatherPref = PreferenceManager.getDefaultSharedPreferences(context);
                                SharedPreferences.Editor editor = weatherPref.edit();
                                editor.putString("zipcodeData", holder.settings_txt.getText().toString());
                                editor.apply();
                                Log.d(TAG, "onClick in SettingsAdapter: " + weatherPref.getString("zipcodeData", ""));

                                Toast.makeText(context, "Zipcode Changed", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        break;
                    case "Units":
                        dialog = new Dialog(context);
                        dialog.setTitle("Choose Units of Temperature");
                        dialog.setContentView(R.layout.units_radiobutton_layout);
                        List<String> stringList=new ArrayList<>();  // here is list
                        stringList.add("Fahrenheit");
                        stringList.add("Celsius");

                        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
                        RadioButton rb = null;

                        for(int i=0;i<stringList.size();i++){
                            rb = new RadioButton(context); // dynamically creating RadioButton and adding to RadioGroup.
                            rb.setText(stringList.get(i));
                            rg.addView(rb);
                        }

                        dialog.show();

                        Button okRadioBtn = (Button) dialog.findViewById(R.id.btn_radio_ok);

                        okRadioBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int selectedId = rg.getCheckedRadioButtonId();

                                SharedPreferences unitsPref = PreferenceManager.getDefaultSharedPreferences(context);
                                SharedPreferences.Editor editor = unitsPref.edit();

                                // find the radiobutton by returned id
                                RadioButton radioButton = (RadioButton) dialog.findViewById(selectedId);

                                if (radioButton.getText().toString() == "Celsius") {
                                    isCelsius = true;
                                    editor.putString("unitsData", radioButton.getText().toString());
                                    editor.putBoolean("unitsBool", true);
                                    editor.apply();
                                } else if (radioButton.getText().toString() == "Fahrenheit") {
                                    isCelsius = false;
                                    editor.putString("unitsData", radioButton.getText().toString());
                                    editor.putBoolean("unitsBool", false);
                                    editor.apply();
                                } else {
                                    holder.settings_txt.setText(unitsPref.getString("unitsData", ""));
                                }

                                holder.settings_txt.setText(radioButton.getText().toString());
                                Toast.makeText(context, "Units Changed", Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, "Units: " + radioButton.getText(), Toast.LENGTH_SHORT).show();

                                dialog.cancel();
                            }
                        });
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return settings_title.length;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView settings_title_txt;
        TextView settings_txt;

        LinearLayout linearLayout;

        public MyHolder(View itemView) {
            super(itemView);
            settings_title_txt = (TextView) itemView.findViewById(R.id.settings_title_tv);
            settings_txt = (TextView) itemView.findViewById(R.id.settings_tv);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_settings_list);
        }
    }
}
