package com.mahan.randommoviespinner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button netflixgBtn, disneyplusBtn, appleBtn, huluBtn, amazonBtn, hboBtn;

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

    }

    public void onServiceClick(View view){

        switch (view.getId()){
            case R.id.netflixBtn:
                Toast.makeText(this, "NETFLIX PRESSED", Toast.LENGTH_SHORT).show();
                break;
            case R.id.disneyBtn:
                Toast.makeText(this, "DISNEY PRESSED", Toast.LENGTH_SHORT).show();
                break;
            case R.id.appleBtn:
                Toast.makeText(this, "APPLE PRESSED", Toast.LENGTH_SHORT).show();
                break;
            case R.id.huluBtn:
                Toast.makeText(this, "HULU PRESSED", Toast.LENGTH_SHORT).show();
                break;
            case R.id.amazonBtn:
                Toast.makeText(this, "AMAZON PRIME PRESSED", Toast.LENGTH_SHORT).show();
                break;
            case R.id.hboBtn:
                Toast.makeText(this, "HBO MAX PRESSED", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}