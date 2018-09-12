package com.example.katherine.foodapp;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FoodListActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    Intent reset_intent;

    static File JSONFile;

    private void copyAssets() {
        AssetManager assetManager = getApplicationContext().getAssets();
        String[] files = null;
        try {files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);}
        if (files != null) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open("foods.json");
                File outFile = new File(getExternalFilesDir(null), "foods.json");
                out = new FileOutputStream(outFile, true);
                copyFile(in, out);
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + "foods.json", e);
            } finally {
                if (in != null) {
                    try {in.close();} catch (IOException e) {}
                }
                if (out != null) {
                    try {out.close();} catch (IOException e) {}
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public static String getStringFromFile(String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    public String loadJSON(){
        String json = null;
        File JSONfile = new File(getExternalFilesDir(null), "foods.json");
        try {
            json = getStringFromFile(JSONfile.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        reset_intent = new Intent (this, FoodListActivity.class);

        File JSONfile = new File(getExternalFilesDir(null), "foods.json");
        if(!JSONfile.exists()) {
            copyAssets();
        }

        JSONFile = new File(getExternalFilesDir(null), "foods.json");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            File delete = new File(getExternalFilesDir(null), "foods.json");
            delete.delete();
            startActivity(reset_intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private void copyFile(InputStream in, OutputStream out) throws IOException {
            byte[] buffer = new byte[1024];
            int read;
            while((read = in.read(buffer)) != -1){
                out.write(buffer, 0, read);
            }
        }

        public static String getStringFromFile(String filePath) throws Exception {
            File fl = new File(filePath);
            FileInputStream fin = new FileInputStream(fl);
            String ret = convertStreamToString(fin);
            //Make sure you close all streams.
            fin.close();
            return ret;
        }

        public static String convertStreamToString(InputStream is) throws Exception {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }

        public String loadJSON(){
            String json = null;
            try {
                json = getStringFromFile(JSONFile.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        public String parseJSON(int section){
            String ret = " ";
            String type = " ";
            if(section == 1){type = "dine";}
            else if(section == 2){type = "take";}
            else if(section == 3){type = "cook";}
            else if(section == 4){type = "dessert";}
            try {
                JSONObject jsonMain = new JSONObject(loadJSON());
                JSONArray arrayMain = jsonMain.getJSONArray(type);
                if(arrayMain == null){
                    return "No food saved";
                }
                for(int i = 0; i < arrayMain.length(); i++){
                    if(!(arrayMain.getString(i).equals(""))){
                        ret += arrayMain.getString(i) + " \n ";
                    }
                }
            } catch (JSONException e) {
                return "Error";
            }
            return ret;
        }

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_food_list, container, false);
            final TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(parseJSON(getArguments().getInt(ARG_SECTION_NUMBER)));
            textView.setTextSize(20);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }
}
