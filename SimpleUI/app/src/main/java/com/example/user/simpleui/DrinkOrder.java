package com.example.user.simpleui;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2016/6/16.
 */
public class DrinkOrder {
    String drinkName;
    String ice = "正常";
    String sugar = "正常";
    int lNumber = 0;
    int mNumber = 0;
    int lPrice;
    int mPrice;
    String note;

    public JSONObject getJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("drinkName", drinkName);
            jsonObject.put("ice", ice);
            jsonObject.put("sugar", sugar);
            jsonObject.put("lNumber", lNumber);
            jsonObject.put("mNumber", mNumber);
            jsonObject.put("lPrice", lPrice);
            jsonObject.put("mPrice", mPrice);
            jsonObject.put("note", note);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonObject;
    }
    public static DrinkOrder newInstanceWithJsonObject(String data){
        try{
            JSONObject jsonObject = new JSONObject();
            DrinkOrder drinkOrder = new DrinkOrder();
            drinkOrder.ice = jsonObject.getString("ice");
            drinkOrder.drinkName = jsonObject.getString("drinkName");
            drinkOrder.sugar = jsonObject.getString("sugar");
            drinkOrder.note = jsonObject.getString("note");
            drinkOrder.lPrice = jsonObject.getInt("lPrice");
            drinkOrder.mPrice = jsonObject.getInt("mPrice");
            drinkOrder.lNumber = jsonObject.getInt("lNumber");
            drinkOrder.mNumber = jsonObject.getInt("mNumber");
            return drinkOrder;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
