package com.example.user.simpleui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DrinkMenuActivity extends AppCompatActivity implements DrinkOrderDialog.OnFragmentInteractionListener {
    ListView drinkListView;
    TextView priceTextView;

    ArrayList<Drink> drinks = new ArrayList<>();
    ArrayList<Drink> drinkOrders = new ArrayList<>();
    //SET DATA
    String[] names={"冬瓜紅茶","玫瑰鹽奶蓋紅茶","珍珠紅茶拿鐵","紅茶拿鐵"};
    int[] mPrices = {25,35,45,35};
    int[] lPrices = {35,45,55,45};
    int[] imageId= {R.drawable.drink1,R.drawable.drink2,R.drawable.drink3,R.drawable.drink4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);
        setData();
        //get UI Componet
        drinkListView = (ListView)findViewById(R.id.drinkTextView);
        priceTextView = (TextView)findViewById(R.id.priceTextView);
        setupListView();
        drinkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //DrinkAdapter drinkAdapter = (DrinkAdapter) parent.getAdapter();
                //Drink drink = (Drink) drinkAdapter.getItem(position);
                //drinkOrders.add(drink);
                //updateTotalPrice();
                Drink drink = (Drink)parent.getAdapter().getItem(position);
                ShowDetailDrinkMenu(drink);
            }
        });
    }
    private void ShowDetailDrinkMenu(Drink drink){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        //以json傳參數, 所以先宣告 drinkOrder
        DrinkOrder drinkOrder = new DrinkOrder();
        drinkOrder.mPrice = drink.mPrice;
        drinkOrder.lPrice = drink.lPrice;
        drinkOrder.drinkName = drink.name;
        DrinkOrderDialog orderDialog = DrinkOrderDialog.newInstance(drinkOrder);

        //ft.replace(R.id.root, orderDialog);
        //ft.addToBackStack(null);
        //ft.commit(); // mark 這三行, 改成以下
        orderDialog.show(ft,"DrinkOrderDialog");
    }
    private void updateTotalPrice(){
        int total = 0;
        for(Drink drink: drinkOrders){
            total += drink.mPrice;
        }
        priceTextView.setText(String.valueOf(total));
    }
    public void setupListView(){
        DrinkAdapter adapter = new DrinkAdapter(this, drinks);
        drinkListView.setAdapter(adapter);
    }
    public void done(View view){ // back to last layout just by intent
        Intent intent = new Intent();
        //convert to json to goback last layout
        JSONArray array = new JSONArray();
        for(Drink drink: drinkOrders){
            JSONObject object = drink.getData();
            array.put(object);
        }
        intent.putExtra("results", array.toString());
        setResult(RESULT_OK, intent);
        finish();
    }
    private void setData(){
        for(int i=0;i<4;i++){
            Drink drink = new Drink();
            drink.name = names[i];
            drink.mPrice = mPrices[i];
            drink.lPrice = lPrices[i];
            drink.imageId = imageId[i];
            drinks.add(drink);
        }
    }
}
