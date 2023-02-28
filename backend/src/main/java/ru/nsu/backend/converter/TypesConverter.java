package ru.nsu.backend.converter;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;

public class TypesConverter {

    public static String jsonToCsv(String jsonData) {

        String csv = null;

        try {
            JSONArray docs = new JSONArray(jsonData);

            csv = CDL.toString(docs);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return csv;
    }

}
