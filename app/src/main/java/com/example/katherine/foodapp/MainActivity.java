package com.example.katherine.foodapp;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Random;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {

    android.content.res.Resources res;
    int type;
    String e, t, c, d, typeString;
    Intent food_list_intent;

    public void createDialog(){
        final EditText editText = new EditText(MainActivity.this);
        editText.setHint("Food Name");
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setCancelable(true);
        dialog.setTitle("Add Food Option").setSingleChoiceItems(R.array.types, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                type = i+1;
            }
        });
        dialog.setView(editText);
        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(type == 1){typeString = "dine";}
                else if(type == 2){typeString = "take";}
                else if(type == 3){typeString = "cook";}
                else if(type == 4){typeString = "dessert";}
                String addFood = editText.getText().toString();
                parseJSON(typeString, addFood);
                Toast toast = Toast.makeText(MainActivity.this, addFood + " Added", Toast.LENGTH_SHORT);
                toast.show();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

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

    public void parseJSON(String type, String food){
        BufferedWriter bufferedWriter = null;
        JSONObject mainObj;
        JSONArray mainArr;
        File outFile = new File(getExternalFilesDir(null), "foods.json");
        try {
            mainObj = new JSONObject(loadJSON());
            mainArr = mainObj.getJSONArray(type);
            FileWriter fileWriter = new FileWriter(outFile);
            bufferedWriter = new BufferedWriter(fileWriter);
            //create object to put in array
            mainArr.put(food);
            bufferedWriter.write(mainObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File delete = new File(getExternalFilesDir(null), "foods.json");
        //delete.delete();

        File JSONfile = new File(getExternalFilesDir(null), "foods.json");
        if(!JSONfile.exists()) {
            copyAssets();
        }

        food_list_intent = new Intent (this, FoodListActivity.class);
        res = getResources();
        e = "dine";
        t = "take";
        c = "cook";
        d = "dessert";

        Button eat_out = (Button)findViewById(R.id.button1);
        Button take_out = (Button)findViewById(R.id.button2);
        Button cook_b = (Button)findViewById(R.id.button3);
        Button dessert_b = (Button)findViewById(R.id.button4);
        FloatingActionButton add_food = (FloatingActionButton)findViewById(R.id.addFoodButton);

        eat_out.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){showResult(e);}
        });

        take_out.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){showResult(t);}
        });

        cook_b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){showResult(c);}
        });

        dessert_b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){showResult(d);}
        });

        add_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_list:
                startActivity(food_list_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showResult(String opt){
        String output;
        try {
            JSONObject jsonMain = new JSONObject(loadJSON());
            JSONArray arrayMain = jsonMain.getJSONArray(opt);
            output = arrayMain.getString(new Random().nextInt(arrayMain.length()));
        } catch (JSONException e) {
            output = "Error";
        }
        TextView view = (TextView) findViewById(R.id.output);
        view.setText(output);
        view.setVisibility(View.VISIBLE);
    }
}
