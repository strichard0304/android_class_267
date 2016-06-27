package com.example.user.simpleui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
    //ArrayList<Order> orders = new ArrayList<>(); // 6/23
    List<Order> orders = new ArrayList<>();
    Spinner storeSpinner;
    String menuResult;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    SharedPreferences sharedPreferences; //6/20
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //final int REQUEST_CODE_DRINK_MENU_ACTIVITY = 0;
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        final FindCallback<Order> callback = new FindCallback<Order>() {
            @Override
            public void done(List<Order> objects, ParseException e) {
                if(e==null) { // check not exception and go on
                    orders = objects;
                    setupListView();
                }
            }
        }
        if(info!=null && info.isConnected()){
           Order.getOrdersFromRemote(new FindCallback<Order>() {
               @Override
               public void done(List<Order> objects, ParseException e) {
                   if(e!=null){
                       Toast.makeText(MainActivity.this, "Sync Failed", Toast.LENGTH_LONG).
                       Order.getQuery().fromLocalDatastore().findInBackground(callback);
                   }else{
                       callback.done(objects,e);
                   }
               }
           });
        }else {
            Order.getQuery().fromLocalDatastore().findInBackground(callback);
        }

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("TestObject");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    for(ParseObject object: objects){
                        Toast.makeText(MainActivity.this, object.getString("foo"),Toast)
                    }
                    //for(int i=0;i<objects.size();i++){
                    //    Log.i("debug",objects.get(i).toString());
                    //}
                }
            }

            @Override
            public void done(Object o, Throwable throwable) {

            }
        });



        Utils.writeFile(this, "history", orders);

        textView.setText(note);
        menuResult = "";
        editText.setText("");

        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.listView);

        sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setupOrdersData(); // 6/20  before setupListView
        setupListView();
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        storeSpinner = (Spinner) findViewById(R.id.spinner);
        setupSpinner();

        //radioGroup.setOnCheckedChangeListener();

        //radioGroup.setOnCheckedChangeListener((group, checkedId){
        //    RadioButton radioButton = (RadioButton)findViewById(CheckedId);
        //    drinkName = radioButton.getText().toString();
        //});
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("Hello TextView");
        editText = (EditText) findViewById(R.id.editText);
        editText.setText(sharedPreferences.getString("editText", "")); //下次開啟, 可以讀入
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String text = editText.getText().toString();
                editor.putString("editText",text);
                editor.apply();
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
        textView.setText(sharedPreferences.getString("textView",""));
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editor.putString("textView", s.toString());
                editor.apply();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = (Order)parent.getAdapter().getItem(position);
                goToDetailOrder(order);
            }
        });

    }
    private void goToDetailOrder(){
        Intent intent = new Intent();
        intent.setClass(this, OrderDetailActivity.class);
        intent.putExtra("note", order.getNote());
        intent.putExtra("menuResults", order.getMenuResults());
        intent.putExtra("storeInfo", order.getStoreInfo());
        startActivity(intent);
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
        //setupOrdersData();
//        String[] data = new String[]{"123","456","789","Hello","ListView","Hi"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data);
        List<Map<String,String>> data = new ArrayList<>();
        for(int i=0;i<orders.size();i++){
            Order order = orders.get(i);
            Map<String,String> item = new HashMap<>();
            //item.put("note", order.note); // 6/23
            item.put("note", order.getNote());
            //item.put("drinkName", order.drinkName);  // 6/23
            item.put("drinkName", order.getMenuResults());
            data.add(item);
        }
        String[] from = {"note","drinkName"};
        int[] to = {R.id.storeInfoTextView, R.id.noteTextView};
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.listview_order_item, from, to);
        listView.setAdapter(adapter);
    }
    private void setupOrdersData(){
//        String content = Utils.readFile(this, "history");
//        String[] datas = content.split("\n");
//        for(int i=0;i<datas.length;i++){
//            Order order = Order.newInstanceWithData(datas[i]);
//            if(order!=null){
//                orders.add(order);
//            }
//        }  // 6/23
        Order.getQuery().findInBackground(new FindCallback<Order>() {
            @Override
            public void done(List<Order> objects, ParseException e) {
                orders = objects;
                setupListView();
            }
        });

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
