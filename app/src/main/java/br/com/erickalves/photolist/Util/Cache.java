package br.com.erickalves.photolist.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.erickalves.photolist.Model.PhotoItem;

public class Cache {

    Context context;
    private  static String PREFS_TAG = "pref";
    private  static String PHOTOS_TAG = "photos";
    public Cache(Context context){

        this.context = context;

    }

    public static List<PhotoItem> getDataFromSharedPreferences(Context context){
        Gson gson = new Gson();
        List<PhotoItem> productFromShared = new ArrayList<>();
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE);
        String jsonPreferences = sharedPref.getString(PHOTOS_TAG, "");

        Type type = new TypeToken<List<PhotoItem>>() {}.getType();
        productFromShared = gson.fromJson(jsonPreferences, type);

        return productFromShared;
    }

    public static void addPhotoToList(Context context, PhotoItem photoList){


        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE);

        String jsonSaved = sharedPref.getString(PHOTOS_TAG, "");
        String jsonPhotoToAdd = gson.toJson(photoList);

        JSONArray jsonArrayPhotoList= new JSONArray();

        try {
            if(jsonSaved.length()!=0){
                jsonArrayPhotoList = new JSONArray(jsonSaved);
            }
            jsonArrayPhotoList.put(new JSONObject(jsonPhotoToAdd));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //SAVE NEW ARRAY
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PHOTOS_TAG, jsonArrayPhotoList.toString());
        editor.commit();
        editor.apply();



    }

}
