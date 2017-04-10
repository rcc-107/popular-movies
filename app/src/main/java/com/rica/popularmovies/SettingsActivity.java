package com.rica.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.rica.popularmovies.fragments.Favorites;
import com.rica.popularmovies.fragments.MainActivityFragment;

/**
 * Created by Rica on 9/10/2016.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    public static boolean sortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar();
        getFragmentManager().beginTransaction().replace(android.R.id.content,new SettingsFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }



    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        private ListPreference lp;
        private EditTextPreference etp;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            lp = (ListPreference) findPreference((getString(R.string.pref_sort_key)));
            lp.setSummary(lp.getValue());
            etp = (EditTextPreference) findPreference(getString(R.string.pref_lang_key));
            etp.setSummary(etp.getText());
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            MainActivityFragment.rvAdapter.notifyDataSetChanged();
            Favorites.rvAdapter.notifyDataSetChanged();
            if(key.equals(getString(R.string.pref_lang_key))) {
                etp.setSummary(etp.getText());
            }else if(key.equals(getString(R.string.pref_sort_key))) {
                lp.setSummary(lp.getValue());
            }
            sortOrder = true;
        }
    }
}
