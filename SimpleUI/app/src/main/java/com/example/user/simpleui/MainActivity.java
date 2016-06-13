package com.example.user.simpleui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CODE_DRINK_MENU_ACTIVITY = 0;
    TextView textView;
    EditText editText;
    //String selectedSex = "Male";
    //String name = "";
    CheckBox checkBox;
    RadioGroup radioGroup;
    String drinkName = "Black Tea";
    ListView listView;
    ArrayList<Order> orders = new ArrayList<>();
    Spinner storeSpinner;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //final int REQUEST_CODE_DRINK_MENU_ACTIVITY = 0;

        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.listView);
        setupListView();
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        storeSpinner = (Spinner) findViewById(R.id.spinner);
        setupSpinner();
        //radioGroup.setOnCheckedChangeListener();

        //radioGroup.setOnCheckedChangeListener((group, checkedId){
        //    RadioButton radioButton = (RadioButton)findViewById(CheckedId);
        //    drinkName = radioButton.getText().toString();
        //});
        textView = (TextView) findViewById(R.id.TestView);
        textView.setText("Hello TextView");
        editText = (EditText) findViewById(R.id.editText);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                return false;
            }
        });
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    public void goToMenu(View view){
        Intent intent = new Intent();
        intent.setClass(this, DrinkMenuActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_DRINK_MENU_ACTIVITY){
            if(resultCode==RESULT_OK){
                textView.setText(data.getStringExtra("results"));
            }
        }
    }

    void setupSpinner(){
        String[] data = getResources().getStringArray(R.array.storeInfo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        storeSpinner.setAdapter(adapter);
    }
    void setupListView(){
//        String[] data = new String[]{"123","456","789","Hello","ListView","Hi"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data);
        List<Map<String,String>> data = new ArrayList<>();
        for(int i=0;i<orders.size();i++){
            Order order = orders.get(i);
            Map<String,String> item = new HashMap<>();
            item.put("note", order.note);
            item.put("drinkName", order.drinkName);
            data.add(item);
        }
        String[] from = {"note","drinkName"};
        int[] to = {R.id.noteTextView, R.id.drinkTextView};
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.listview_order_item, from, to);
        listView.setAdapter(adapter);
    }

    public void click(View view) {
        textView.setText("Press button");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.user.simpleui/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.user.simpleui/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
