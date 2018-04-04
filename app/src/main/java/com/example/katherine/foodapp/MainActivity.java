package com.example.katherine.foodapp;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    android.content.res.Resources res;
    String[] eatOut;
    String[] takeOut;
    String[] cook;
    String[] dessert;
    int e, t, c, d;

    /*public void createDialog(String date, String list){
        AlertDialog.Builder dialog = new AlertDialog.Builder(SymptomLogActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle(date);
        dialog.setMessage("Symptoms: \n"+list);
        dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Action for "Delete".
            }
        })
                .setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        res = getResources();
        e = 1;
        t = 2;
        c = 3;
        d = 4;
        eatOut = res.getStringArray(R.array.eat_out_list);
        takeOut = res.getStringArray(R.array.take_out_list);
        cook = res.getStringArray(R.array.cook_list);
        dessert = res.getStringArray(R.array.dessert_list);

        Button eat_out = (Button)findViewById(R.id.button1);
        Button take_out = (Button)findViewById(R.id.button2);
        Button cook_b = (Button)findViewById(R.id.button3);
        Button dessert_b = (Button)findViewById(R.id.button4);

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
    }

    private void showResult(int i){
        String output;
        if(i == 1) {
            output = eatOut[new Random().nextInt(eatOut.length)];
        }
        else if(i == 2){
            output = takeOut[new Random().nextInt(takeOut.length)];
        }
        else if(i == 3){
            output = cook[new Random().nextInt(cook.length)];
        }
        else{
            output = dessert[new Random().nextInt(dessert.length)];
        }
        TextView view = (TextView) findViewById(R.id.output);
        view.setText(output);
        view.setVisibility(View.VISIBLE);
    }
}
