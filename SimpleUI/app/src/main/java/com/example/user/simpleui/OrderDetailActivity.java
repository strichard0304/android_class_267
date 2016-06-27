package com.example.user.simpleui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public class OrderDetailActivity extends AppCompatActivity {
    TextView noteTextView;
    TextView menuResultsTextView;
    TextView storeInfoTextView;
    ImageView staticMap;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        String note = intent.getStringExtra("note");
        String menuResults = intent.getStringExtra("menuResults");
        String storeInfo = intent.getStringExtra("storeInfo");

        noteTextView = (TextView)findViewById(R.id.noteTextView);
        menuResultsTextView = (TextView)findViewById(R.id.menuResultsTextView);
        storeInfoTextView = (TextView)findViewById(R.id.storeInfoTextView);
        staticMap = (ImageView)findViewById(R.id.imageView);
        if(note!=null)
            noteTextView.setText(note);
        if(storeInfo!=null)
            storeInfoTextView.setText(storeInfo);
        if(menuResults!=null){
            try{
                JSONArray jsonArray = new JSONArray(menuResults);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    String lNumber = String.valueOf(object.getInt("lNumber"));
                    String mNumber = String.valueOf(object.getInt("mNumber"));
                    text += object.getString("drinkName") + ":大杯 " + lNumber + "杯    中杯 " + mNumber + "杯\n";
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        menuResultsTextView.setText(text);

        String[] storeInfos = storeInfo.split(",");
        if(storeInfos != null && storeInfos.length>1){
            String address = storeInfos[1];
            (new GeoCodingTasks(staticMap)).execute(address);
        }
    }
    private static class GeoCodingTasks extends AsyncTask<String, Void, Bitmap>{
        WeakReference<ImageView> imageViewWeakReference;

        @Override
        protected Bitmap doInBackground(String... params) {
            String address = params[0];
            double[] latlng = Utils.getLatLngFromGoogleMapAPI(address);
            if(latlng!=null) {
                Log.d("Debug:", String.valueOf(latlng[0]));
                Log.d("Debug:", String.valueOf(latlng[1]));
            }
            //return null;
            return Utils.getStaticMap(latlng);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(imageViewWeakReference.get() != null){
                ImageView imageView = imageViewWeakReference.get();
                imageView.setImageBitmap(bitmap);
            }
        }
        public GeoCodingTasks(ImageView imageView){
            this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
        }


    }
}
