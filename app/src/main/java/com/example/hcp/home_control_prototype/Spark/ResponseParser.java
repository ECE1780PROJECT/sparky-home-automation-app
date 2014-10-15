package com.example.hcp.home_control_prototype.Spark;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by garygraham on 2014-10-14.
 */
public class ResponseParser {

    private static final String TAG = "SparkResponseParser";

    public static String parseEntity(HttpEntity entity){
        String result = null;
        try{
            InputStream is = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine())!= null){
                sb.append(line).append("\n");
            }
            is.close();
            result = sb.toString();
            Log.i(TAG, "parseEntity() -> Received API Response -> " + result);

        }
        catch (Exception e){
            Log.e(TAG, "parseEntity() -> Could not read API response! -> " + e.toString());

        }
        return result;
    }

    public static JSONArray parseResultToJSON(String result){
        JSONArray jArray;
        try{

            jArray = new JSONArray(result);
            Log.i(TAG, "parseResultToJSON() -> " + "result was JSONArray, returning it. ");
            return jArray;

        }catch(JSONException e){

            Log.i(TAG, "parseResultToJSON() -> " + "Result wasn't an array, creating an array from single object. ");
            jArray = new JSONArray();

            try {

                JSONObject jObject = new JSONObject(result);
                jArray.put(jObject);
                return jArray;

            } catch (JSONException e1) {

                e1.printStackTrace();
                Log.e(TAG, "Error parsing the json! -> " + e1.toString());

            }

            return null;
        }
    }

    public static JSONArray parseEntityToJSON(HttpEntity entity){
        return parseResultToJSON(parseEntity(entity));
    }
}
