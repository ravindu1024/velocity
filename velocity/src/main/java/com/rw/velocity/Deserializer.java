package com.rw.velocity;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * SimpleHttp
 * <p/>
 * Created by ravindu on 12/09/16.
 */
@SuppressWarnings("WeakerAccess")
public class Deserializer
{
    public <T> T deserialize(String jsonData, Class<T> cls)
    {
        return new Gson().fromJson(jsonData, cls);
    }

    public <T> T deserialize(String jsonData, String elementToExtract, Class<T> cls)
    {
        JSONObject j = null;
        try
        {
            JSONObject jobj = new JSONObject(jsonData);
            j = jobj.getJSONObject(elementToExtract);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        assert j != null;

        return deserialize(j.toString(), cls);
    }

    public <T> T[] deserializeArray(String jsonData, String elementToExtract, Class<T[]> cls)
    {
        JSONArray j = null;
        try
        {
            JSONObject jobj = new JSONObject(jsonData);
            j = jobj.getJSONArray(elementToExtract);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        assert j != null;

        return deserialize(j.toString(), cls);
    }


    public <T> ArrayList<T> deserializeArrayList(String jsonData, Class<T[]> cls)
    {
        T[] array = new Gson().fromJson(jsonData, cls);

        return new ArrayList<>(Arrays.asList(array));
    }

    public <T> ArrayList<T> deserializeArrayList(String jsonData, String elementToExtract, Class<T[]> cls)
    {
        JSONArray j = null;
        try
        {
            JSONObject jobj = new JSONObject(jsonData);
            j = jobj.getJSONArray(elementToExtract);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        assert j != null;

        return deserializeArrayList(j.toString(), cls);
    }
}
