package com.example.katherine.foodapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;

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

    boolean checksVisible = false;

    ArrayList<PlaceholderFragment> allFragments = new ArrayList<PlaceholderFragment>();

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

    private static class JSONAdapter extends BaseAdapter implements ListAdapter {
        private final ArrayList<String> arrayList;
        LayoutInflater inflater;
        Context c;
        private JSONAdapter (Context c, ArrayList<String> arrayList) {
            assert arrayList != null;

            this.arrayList = arrayList;
            this.c = c;
        }

        @Override public int getCount() {
            if(null==arrayList)
                return 0;
            else
                return arrayList.size();
        }

        @Override public String getItem(int position) {
            if(null==arrayList) return null;
            else
                return arrayList.get(position);
        }

        @Override public long getItemId(int position) {
            return position;
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {

            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (inflater != null &&  convertView == null)
                convertView = inflater.inflate(R.layout.row, parent, false);

            TextView text =(TextView)convertView.findViewById(R.id.nameTxt);
            text.setTextSize(20);

            final String name = arrayList.get(position);
            text.setText(name);

            return convertView;
        }
    }

    public void createDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(FoodListActivity.this);
        dialog.setCancelable(true);
        dialog.setTitle("Are you sure you want to delete these foods?");
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Delete function
                startActivity(reset_intent);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Action for cancel
                    }
                });
        final AlertDialog alert = dialog.create();
        alert.show();
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

        FloatingActionButton confirmDeleteButton = (FloatingActionButton) findViewById(R.id.confirmDeleteButton);
        confirmDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

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
            //File delete = new File(getExternalFilesDir(null), "foods.json");
            //delete.delete();
            //startActivity(reset_intent);
            checksVisible = !checksVisible;
            for(int i = 0; i < allFragments.size(); i++){
                allFragments.get(i).setCheckVisibility(checksVisible);
            }

            if(checksVisible){
                item.setIcon(R.drawable.ic_clear_24dp);
                findViewById(R.id.confirmDeleteButton).setVisibility(View.VISIBLE);
            }
            else{
                item.setIcon(R.drawable.ic_delete_24dp);
                findViewById(R.id.confirmDeleteButton).setVisibility(View.GONE);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    @SuppressLint("ValidFragment")
    public static class PlaceholderFragment extends Fragment {

        public String getStringFromFile(String filePath) throws Exception {
            File fl = new File(filePath);
            FileInputStream fin = new FileInputStream(fl);
            String ret = convertStreamToString(fin);
            //Make sure you close all streams.
            fin.close();
            return ret;
        }

        public String convertStreamToString(InputStream is) throws Exception {
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

        public ArrayList<String> parseJSON(int section){
            ArrayList<String> foodNames = null;
            String type = " ";
            JSONArray arrayMain;
            if(section == 1){type = "dine";}
            else if(section == 2){type = "take";}
            else if(section == 3){type = "cook";}
            else if(section == 4){type = "dessert";}
            try {
                JSONObject jsonMain = new JSONObject(loadJSON());
                arrayMain = jsonMain.getJSONArray(type);
                foodNames = new ArrayList<String>();
                if(arrayMain == null){
                    return null;
                }
                for(int i = 0; i < arrayMain.length(); i++){
                    if(!(arrayMain.getString(i).equals(""))){
                        foodNames.add(arrayMain.getString(i));
                    }
                }
            } catch (JSONException e) {
                return null;
            }
            return foodNames;
        }

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        ListView listView;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int index){
            PlaceholderFragment currentFragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, index);
            currentFragment.setArguments(args);
            return currentFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_food_list, container, false);
            JSONAdapter jsonAdapter;
            listView = (ListView) rootView.findViewById(R.id.section_label);
            jsonAdapter = new JSONAdapter(getActivity(), parseJSON(getArguments().getInt(ARG_SECTION_NUMBER)));
            listView.setAdapter(jsonAdapter);
            return rootView;
        }

        public void setCheckVisibility(boolean isVisible){
            int length = listView.getCount();
            if(isVisible) {
                for (int i = 0; i < length; i++) {
                    View row = listView.getChildAt(i);
                    row.findViewById(R.id.chk).setVisibility(View.VISIBLE);
                }
            }
            else{
                for(int j = 0; j < length; j++){
                    View row = listView.getChildAt(j);
                    row.findViewById(R.id.chk).setSelected(false);
                    row.findViewById(R.id.chk).setVisibility(View.GONE);
                }
            }
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
            PlaceholderFragment currentFragment = PlaceholderFragment.newInstance(position + 1);
            allFragments.add(currentFragment);
            return currentFragment;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }

}
