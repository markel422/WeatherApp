package com.foo.umbrella.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.foo.umbrella.R;
import com.foo.umbrella.data.adapter.SettingsAdapter;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName() + "_TAG";
    private static final String WEATHERPREF = "weatherZipUnits";

    SharedPreferences weatherPref;

    SettingsAdapter settingsAdapter;

    RecyclerView settingsRecyclerView;

    TextView settings_title_txt;
    TextView settings_txt;

    private String settingsTitleList[] = {"Zip", "Units"};

    private String settingsList[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        weatherPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        settingsAdapter = new SettingsAdapter();

        settingsList = new String[]{weatherPref.getString("zipcodeData", ""), weatherPref.getString("unitsData", "")};

        settingsRecyclerView = (RecyclerView) findViewById(R.id.settings_recycler_list);
        settingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        settingsRecyclerView.addItemDecoration(new DividerItemDecoration(SettingsActivity.this, DividerItemDecoration.VERTICAL));
        SettingsAdapter settingsAdapter = new SettingsAdapter(SettingsActivity.this, settingsTitleList, settingsList);
        settingsRecyclerView.setAdapter(settingsAdapter);

        settings_title_txt = (TextView) findViewById(R.id.settings_title_tv);
        settings_txt = (TextView) findViewById(R.id.settings_tv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
