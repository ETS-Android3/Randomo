package com.mahan.randomo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Button spinBtn, netflixgBtn, disneyplusBtn, appleBtn, huluBtn, amazonBtn, hboBtn;
    private Spinner genreSpinner,imdbSpinner;
    private CheckBox movieCB, showCB;
    private LinearLayout mainLinear;


    private HashMap<String,Boolean> chosenServices;
    private boolean canSpin;
    private int totalServicesSelected;
    private HashMap<String,String> headerCodes;

    RequestQueue requestQueue;

    private LinearLayout movieView;
    private SharedPreferences sharedPreferences;
    private ArrayList<String> seenMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        netflixgBtn = findViewById(R.id.netflixBtn);
        disneyplusBtn = findViewById(R.id.disneyBtn);
        appleBtn = findViewById(R.id.appleBtn);
        huluBtn = findViewById(R.id.huluBtn);
        amazonBtn = findViewById(R.id.amazonBtn);
        hboBtn = findViewById(R.id.hboBtn);
        movieCB = findViewById(R.id.movieCB);
        showCB = findViewById(R.id.showCB);
        mainLinear = findViewById(R.id.mainLinear);
        spinBtn = findViewById(R.id.spinButton);

        canSpin = true;
        totalServicesSelected = 1;

        genreSpinner = findViewById(R.id.genreSpinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_genres,
                R.layout.colored_spinner_layout);
        arrayAdapter.setDropDownViewResource(R.layout.colored_spinner_dropwdown_layout);
        genreSpinner.setAdapter(arrayAdapter);



        movieCB.setChecked(true);
        movieCB.setOnCheckedChangeListener(this);

        showCB.setChecked(true);
        showCB.setOnCheckedChangeListener(this);

        imdbSpinner = findViewById(R.id.imdbSpinner);
        ArrayAdapter<CharSequence> arrayAdapter2 = ArrayAdapter.createFromResource(this,
                R.array.imdbScore,
                R.layout.colored_spinner_layout);
        arrayAdapter2.setDropDownViewResource(R.layout.colored_spinner_dropwdown_layout);
        imdbSpinner.setAdapter(arrayAdapter2);

        canSpin = true;

        initializeHashMaps();

        sharedPreferences = getSharedPreferences("com.mahan.freegamesnotifier",MODE_PRIVATE);

        try {
            loadSeen();
        }catch (IOException e){

        }


    }

    private void loadSeen() throws IOException {
        String seenString = sharedPreferences.getString("seenContent", ObjectSerializer.serialize(new ArrayList<String>()));
        seenMovies = (ArrayList<String>) ObjectSerializer.deserialize(seenString);

    }

    private void initializeHashMaps() {
        chosenServices = new HashMap<>();
        chosenServices.put("netflix",true);
        chosenServices.put("amazon_prime",false);
        chosenServices.put("hulu_plus",false);
        chosenServices.put("disney_plus",false);
        chosenServices.put("hbo_max",false);
        chosenServices.put("apple_tv_plus",false);

        headerCodes = new HashMap<>();
        headerCodes.put("Action & Adventure","5");
        headerCodes.put("Animation","6");
        headerCodes.put("Anime","39");
        headerCodes.put("Biography","7");
        headerCodes.put("Children","8");
        headerCodes.put("Comedy","9");
        headerCodes.put("Crime","10");
        headerCodes.put("Cult","41");
        headerCodes.put("Documentary","11");
        headerCodes.put("Drama","3");
        headerCodes.put("Family","12");
        headerCodes.put("Fantasy","13");
        headerCodes.put("Food","15");
        headerCodes.put("Game Show","16");
        headerCodes.put("History","17");
        headerCodes.put("Home & Garden","18");
        headerCodes.put("Horror","19");
        headerCodes.put("Independent","43");
        headerCodes.put("LGBTQ","37");
        headerCodes.put("Musical","22");
        headerCodes.put("Mystery","23");
        headerCodes.put("Reality","25");
        headerCodes.put("Romance","4");
        headerCodes.put("Science-Fiction","26");
        headerCodes.put("Sports","29");
        headerCodes.put("Stand-up & Talk","45");
        headerCodes.put("Thriller","32");
        headerCodes.put("Travel","33");
        headerCodes.put(">9","9");
        headerCodes.put(">8","8");
        headerCodes.put(">7","7");
        headerCodes.put(">6","6");
        headerCodes.put(">5","5");
        headerCodes.put("netflix",Integer.toString(R.drawable.ic_watch_netflix));
        headerCodes.put("amazon_prime",Integer.toString(R.drawable.ic_watch_amazon));
        headerCodes.put("hulu_plus",Integer.toString(R.drawable.ic_watch_hulu));
        headerCodes.put("disney_plus",Integer.toString(R.drawable.ic_watch_disney));
        headerCodes.put("hbo_max",Integer.toString(R.drawable.ic_watch_hbo));
        headerCodes.put("apple_tv_plus",Integer.toString(R.drawable.ic_watch_apple));

    }

    public void onSpin(final View view){
        spinBtn.setText("SPINNING...");
        if(canSpin) {
            canSpin = false;

            if (movieView != null){
                mainLinear.removeView(movieView);
            }

            StringBuilder urlBuilder = new StringBuilder("https://api.reelgood.com/v3.0/content/random?");

            final HashMap<String, String> headers = new HashMap<>();

            if (movieCB.isChecked() && showCB.isChecked()) {
                headers.put("content_kind", "both");
            } else if (!movieCB.isChecked() && showCB.isChecked()) {
                headers.put("content_kind", "show");
            } else if (movieCB.isChecked() && !showCB.isChecked()) {
                headers.put("content_kind", "movie");
            }else{
                canSpin = true;
                onSpin(null);
            }

            headers.put("free", "false");
            headers.put("nocache", "true");

            if (!genreSpinner.getSelectedItem().toString().equals("All Genres")) {
                headers.put("genre", headerCodes.get(genreSpinner.getSelectedItem().toString()));
            }

            if (!imdbSpinner.getSelectedItem().toString().equals("Any")) {
                headers.put("minimum_imdb", headerCodes.get(imdbSpinner.getSelectedItem().toString()));
            }

            headers.put("region", "us");

            StringBuilder services = new StringBuilder();
            for (HashMap.Entry<String,Boolean> service : chosenServices.entrySet()){
                if(service.getValue()){
                    services.append(service.getKey()+"%2C");
                }
            }
            String sources = services.substring(0,services.length()-3);

            headers.put("sources",sources);

            for (HashMap.Entry<String,String> header: headers.entrySet()){
                urlBuilder.append(header.getKey() + "=" + header.getValue() + "&");
            }

            String url = urlBuilder.substring(0,urlBuilder.length()-1);
            StringRequest randomRequest = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                setMovie(new JSONObject(response), headers.get("sources"));
                            } catch (JSONException e) {
                                spinBtn.setText("Nothing found...");
                                canSpin = true;
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            spinBtn.setText("Failed to spin...");
                        }
                    }
            );

            requestQueue.add(randomRequest);


        }



    }

    private void saveMovie(String title) throws IOException {
        seenMovies.add(title);
        String seenString = ObjectSerializer.serialize(seenMovies);
        sharedPreferences.edit().putString("seenContent",seenString).apply();
    }

    private void setMovie(JSONObject result, final String src) throws JSONException {
        final String id = result.getString("id");

        final String title = result.getString("title");


        if(seenMovies.contains(title)){
            onSpin(null);
            canSpin = true;
        }

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        movieView = (LinearLayout) inflater.inflate(R.layout.movie_layout,null);

        TextView nameView = movieView.findViewById(R.id.MovieTitle);
        nameView.setText(title);

        String year = (new StringBuilder(result.getString("released_on"))).substring(0,4);
        if(year.equals("null")){year="";}
        TextView yearView = movieView.findViewById(R.id.yearView);
        yearView.setText(year);

        String score = result.getString("imdb_rating");
        if(score.equals("null")){score = "?";}
        String imdbScore = "IMDB: " + score + "/10";
        TextView imdbView = movieView.findViewById(R.id.imdbView);
        imdbView.setText(imdbScore);

        String duration;
        if(result.getString("content_type").equals("m")){
            duration = minutesToHours(result.getInt("runtime"));
        }
        else {
            duration = result.getInt("season_count") + " seasons";
        }

        TextView durationView = movieView.findViewById(R.id.durationView);
        durationView.setText(duration);

        JSONArray genresJson = result.getJSONArray("genres");
        JSONArray shortGenres;
        if(genresJson.length() > 3){
            shortGenres = new JSONArray();
            shortGenres.put(genresJson.getInt(0));
            shortGenres.put(genresJson.getInt(1));
            shortGenres.put(genresJson.getInt(2));
        }
        else{
            shortGenres = genresJson;
        }

        String genres = valueToKey(shortGenres);
        TextView genreView = movieView.findViewById(R.id.movieGenreView);
        genreView.setText(genres);

        String type = "show";
        if(result.getString("content_type").equals("m")){
            type = "movie";
        }
        ImageView moviePosterView = movieView.findViewById(R.id.moviePosterView);
        String imgUrl = "https://img.reelgood.com/content/" + type + "/" + id + "/poster-780.webp";
        Glide.with(this).load(imgUrl).placeholder(R.drawable.ic_no_poster).into(moviePosterView);


        String description = limitString(result.getString("overview"),30);
        TextView descriptionView = movieView.findViewById(R.id.descriptionView);
        descriptionView.setText(description);

        final Button seenBtn = movieView.findViewById(R.id.seenBtn);
        seenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveMovie(title);
                    seenBtn.setText("Saved!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button watchButton = movieView.findViewById(R.id.watchButtonView);
        StringBuilder urlBuilder = new StringBuilder("https://api.reelgood.com/v3.0/content/" + type + "/" + id + "?");

        HashMap<String,String> headers = new HashMap<>();
        headers.put("free","false");
        headers.put("region","us");
        headers.put("sources",src);
        headers.put("interaction","true");
        for (HashMap.Entry<String,String> header: headers.entrySet()){
            urlBuilder.append(header.getKey() + "=" + header.getValue() + "&");
        }


        final String finalType = type;
        StringRequest linkRequest = new StringRequest(
                Request.Method.GET,
                urlBuilder.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            setButton(new JSONObject(response),watchButton, src, finalType);
                        } catch (JSONException e) {
                            watchButton.setText("No link :(");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        watchButton.setText("No link :(");
                    }
                }

        );

        requestQueue.add(linkRequest);

    }

    private void setButton(JSONObject details, Button btn, String src, String type) throws JSONException {

        JSONArray sources = details.getJSONArray("sources");
        String source = sources.getString(0);
        for (int i = 0; i < sources.length(); i++) {
            if(src.contains(sources.getString(i))){
                source = sources.getString(i);
                break;
            }
        }


        JSONArray availability;
        if(type.equals("movie")){
             availability = details.getJSONArray("availability");
        }
        else{
            String epID = details.getJSONObject("recommended_episode").getString("episode_id");
            availability = details.getJSONObject("episodes").getJSONObject(epID).getJSONArray("availability");
        }


        String link = "https://i.pinimg.com/originals/66/9a/78/669a787e739c53fd56e39159b2fa5c9e.gif";
        String linkBackup = "https://i.pinimg.com/originals/66/9a/78/669a787e739c53fd56e39159b2fa5c9e.gif";
        for (int i = 0; i < availability.length(); i++) {
            if(availability.getJSONObject(i).getString("source_name").equals(source)){
                linkBackup = availability.getJSONObject(i).getJSONObject("source_data").getJSONObject("links").getString("web");
                if(availability.getJSONObject(i).getJSONObject("source_data").getJSONObject("links").isNull("android")) {
                    link = linkBackup;
                }
                else{
                    link = availability.getJSONObject(i).getJSONObject("source_data").getJSONObject("links").getString("android");
                }
                break;

            }
        }
        int resource = Integer.parseInt(headerCodes.get(source));

        btn.setBackgroundResource(resource);
        btn.setText("");

        final String finalLink = link;
        final String finalLinkBackup = linkBackup;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse(finalLink);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (ActivityNotFoundException e){
                    Uri uri = Uri.parse(finalLinkBackup);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

        mainLinear.addView(movieView);
        spinBtn.setText("SPIN");
        canSpin = true;


    }

    private String valueToKey(JSONArray arr) throws JSONException {

        if(arr.length() == 0){return "";};

        String res = "";
        for (int i = 0; i < arr.length() ; i++) {
            for (HashMap.Entry<String,String> entry : headerCodes.entrySet()){
                if(entry.getValue().equals(arr.getString(i)) && entry.getKey().length() > 2){
                    res = res + entry.getKey() + ", ";
                }
            }
        }
        if(res.length() > 2){
            res = res.substring(0,res.length()-2);
        }
        return res;
    }

    private String minutesToHours(int min){
        int hours = 0;
        while (min > 60){
            min = min % 60;
            hours += 1;
        }
        return hours + "h " + min + "m";
    }

    private String limitString(String s, int n){
        String[] words = s.split(" ");
        if (words.length < n){
            return s;
        }


        StringBuilder shorterS = new StringBuilder();
        for(int i=0; i<n; i++){
            shorterS.append(words[i]).append(" ");
        }

        return shorterS.substring(0,shorterS.length()-1) + "...";
    }

    public void onServiceClick(View view){

        switch (view.getId()){
            case R.id.netflixBtn:
                if(chosenServices.get("netflix") && totalServicesSelected > 1) {
                    netflixgBtn.setBackgroundResource(R.drawable.ic_netflix_disabled);
                   chosenServices.put("netflix",false);
                    totalServicesSelected -= 1;
                }
                else if (!chosenServices.get("netflix")){
                    netflixgBtn.setBackgroundResource(R.drawable.ic_netflix);
                    chosenServices.put("netflix",true);
                    totalServicesSelected += 1;
                }
                break;
            case R.id.disneyBtn:
                if(chosenServices.get("disney_plus") && totalServicesSelected > 1) {
                    disneyplusBtn.setBackgroundResource(R.drawable.ic_disney_plus_disable);
                    chosenServices.put("disney_plus",false);
                    totalServicesSelected -= 1;
                }
                else if (!chosenServices.get("disney_plus")) {
                    disneyplusBtn.setBackgroundResource(R.drawable.ic_disney_plus);
                    chosenServices.put("disney_plus",true);
                    totalServicesSelected += 1;
                }
                break;
            case R.id.appleBtn:
                if(chosenServices.get("apple_tv_plus") && totalServicesSelected > 1) {
                    appleBtn.setBackgroundResource(R.drawable.ic_apple_tv_plus_disable);
                    chosenServices.put("apple_tv_plus",false);
                    totalServicesSelected -= 1;
                }
                else if (!chosenServices.get("apple_tv_plus")) {
                    appleBtn.setBackgroundResource(R.drawable.ic_apple_tv_plus);
                    chosenServices.put("apple_tv_plus",true);
                    totalServicesSelected += 1;
                }
                break;
            case R.id.huluBtn:
                if(chosenServices.get("hulu_plus") && totalServicesSelected > 1) {
                    huluBtn.setBackgroundResource(R.drawable.ic_hulu_disable);
                    chosenServices.put("hulu_plus",false);
                    totalServicesSelected -= 1;
                }
                else if (!chosenServices.get("hulu_plus")) {
                    huluBtn.setBackgroundResource(R.drawable.ic_hulu);
                    chosenServices.put("hulu_plus",true);
                    totalServicesSelected += 1;
                }
                break;
            case R.id.amazonBtn:
                if(chosenServices.get("amazon_prime") && totalServicesSelected > 1) {
                    amazonBtn.setBackgroundResource(R.drawable.ic_amazon_prime_disable);
                    chosenServices.put("amazon_prime",false);
                    totalServicesSelected -= 1;
                }
                else if (!chosenServices.get("amazon_prime")) {
                    amazonBtn.setBackgroundResource(R.drawable.ic_amazon_prime);
                    chosenServices.put("amazon_prime",true);
                    totalServicesSelected += 1;
                }
                break;
            case R.id.hboBtn:
                if(chosenServices.get("hbo_max") && totalServicesSelected > 1) {
                    hboBtn.setBackgroundResource(R.drawable.ic_hbo_max_disable);
                    chosenServices.put("hbo_max",false);
                    totalServicesSelected -= 1;
                }
                else if (!chosenServices.get("hbo_max")) {
                    hboBtn.setBackgroundResource(R.drawable.ic_hbo_max);
                    chosenServices.put("hbo_max",true);
                    totalServicesSelected += 1;
                }
                break;
        }

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