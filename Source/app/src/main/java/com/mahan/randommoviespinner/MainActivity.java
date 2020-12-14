package com.mahan.randommoviespinner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private Button netflixgBtn, disneyplusBtn, appleBtn, huluBtn, amazonBtn, hboBtn;
    private Spinner genreSpinner,imdbSpinner;
    private CheckBox movieCB, showCB;


    private boolean[] chosenServices;
    private int totalServicesSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        netflixgBtn = findViewById(R.id.netflixBtn);
        disneyplusBtn = findViewById(R.id.disneyBtn);
        appleBtn = findViewById(R.id.appleBtn);
        huluBtn = findViewById(R.id.huluBtn);
        amazonBtn = findViewById(R.id.amazonBtn);
        hboBtn = findViewById(R.id.hboBtn);

        chosenServices = new boolean[] {true,false,false,false,false,false};
        totalServicesSelected = 1;

        genreSpinner = findViewById(R.id.genreSpinner);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_genres,
                R.layout.colored_spinner_layout);
        arrayAdapter.setDropDownViewResource(R.layout.colored_spinner_dropwdown_layout);
        genreSpinner.setAdapter(arrayAdapter);
        genreSpinner.setOnItemSelectedListener(this);


        movieCB = findViewById(R.id.movieCB);
        movieCB.setChecked(true);
        movieCB.setOnCheckedChangeListener(this);

        showCB = findViewById(R.id.showCB);
        showCB.setChecked(true);
        showCB.setOnCheckedChangeListener(this);

        imdbSpinner = findViewById(R.id.imdbSpinner);
        ArrayAdapter arrayAdapter2 = ArrayAdapter.createFromResource(this,
                R.array.imdbScore,
                R.layout.colored_spinner_layout);
        arrayAdapter2.setDropDownViewResource(R.layout.colored_spinner_dropwdown_layout);
        imdbSpinner.setAdapter(arrayAdapter2);
        imdbSpinner.setOnItemSelectedListener(this);





    }

    public void onServiceClick(View view){

        System.out.println(totalServicesSelected);

        switch (view.getId()){
            case R.id.netflixBtn:
                if(chosenServices[0] && totalServicesSelected > 1) {
                    netflixgBtn.setBackgroundResource(R.drawable.ic_netflix_disabled);
                    chosenServices[0] = false;
                    totalServicesSelected -= 1;
                }
                else if (!chosenServices[0]){
                    netflixgBtn.setBackgroundResource(R.drawable.ic_netflix);
                    chosenServices[0] = true;
                    totalServicesSelected += 1;
                }
                break;
            case R.id.disneyBtn:
                if(chosenServices[1] && totalServicesSelected > 1) {
                    disneyplusBtn.setBackgroundResource(R.drawable.ic_disney_plus_disable);
                    chosenServices[1] = false;
                    totalServicesSelected -= 1;
                }
                else if (!chosenServices[1]) {
                    disneyplusBtn.setBackgroundResource(R.drawable.ic_disney_plus);
                    chosenServices[1] = true;
                    totalServicesSelected += 1;
                }
                break;
            case R.id.appleBtn:
                if(chosenServices[2] && totalServicesSelected > 1) {
                    appleBtn.setBackgroundResource(R.drawable.ic_apple_tv_plus_disable);
                    chosenServices[2] = false;
                    totalServicesSelected -= 1;
                }
                else if (!chosenServices[2]) {
                    appleBtn.setBackgroundResource(R.drawable.ic_apple_tv_plus);
                    chosenServices[2] = true;
                    totalServicesSelected += 1;
                }
                break;
            case R.id.huluBtn:
                if(chosenServices[3] && totalServicesSelected > 1) {
                    huluBtn.setBackgroundResource(R.drawable.ic_hulu_disable);
                    chosenServices[3] = false;
                    totalServicesSelected -= 1;
                }
                else if (!chosenServices[3]) {
                    huluBtn.setBackgroundResource(R.drawable.ic_hulu);
                    chosenServices[3] = true;
                    totalServicesSelected += 1;
                }
                break;
            case R.id.amazonBtn:
                if(chosenServices[4] && totalServicesSelected > 1) {
                    amazonBtn.setBackgroundResource(R.drawable.ic_amazon_prime_disable);
                    chosenServices[4] = false;
                    totalServicesSelected -= 1;
                }
                else if (!chosenServices[4]) {
                    amazonBtn.setBackgroundResource(R.drawable.ic_amazon_prime);
                    chosenServices[4] = true;
                    totalServicesSelected += 1;
                }
                break;
            case R.id.hboBtn:
                if(chosenServices[5] && totalServicesSelected > 1) {
                    hboBtn.setBackgroundResource(R.drawable.ic_hbo_max_disable);
                    chosenServices[5] = false;
                    totalServicesSelected -= 1;
                }
                else if (!chosenServices[5]) {
                    hboBtn.setBackgroundResource(R.drawable.ic_hbo_max);
                    chosenServices[5] = true;
                    totalServicesSelected += 1;
                }
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked){
            if(buttonView.getId() == R.id.movieCB && !showCB.isChecked()){
                movieCB.setChecked(true);
            }
            else if(buttonView.getId() == R.id.showCB && !movieCB.isChecked()){
                showCB.setChecked(true);
            }
        }

    }
}