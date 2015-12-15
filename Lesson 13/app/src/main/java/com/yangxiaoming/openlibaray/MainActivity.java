package com.yangxiaoming.openlibaray;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {
    //JSONAdapter mJSONAdapter;
    DBAdapter dbAdapter;
    ListView mainListView;
    ArrayAdapter mArrayAdapter;
    ArrayList mNameList = new ArrayList();
    EditText searchText;
    Cursor cursor;

    LinearLayout layout;
    private static final String QUERY_URL = "http://openlibrary.org/search.json?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        searchText = new EditText(this);
        searchText.setWidth(200);

        Button searchButton = new Button(this);
        searchButton.setText("Search");
        searchButton.setOnClickListener(searchButtonListener);

        mainListView = new ListView(this);
        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mNameList);
        mainListView.setAdapter(mArrayAdapter);
        //mJSONAdapter = new JSONAdapter( this, getLayoutInflater());
        dbAdapter = new DBAdapter(this, getLayoutInflater());
        mainListView.setAdapter(dbAdapter);
        layout.addView(searchText);
        layout.addView(searchButton);
        layout.addView(mainListView);
        setContentView(layout);

    }

    View.OnClickListener searchButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doQuery(searchText.getText().toString());
        }
    };

    private void doQuery(String searchString) {
        String urlString = "";
        try {
            urlString = URLEncoder.encode(searchString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        // AsyncHttpClient.get(String url, ResponseHandlerInterface responseHandler)
        client.get(QUERY_URL + urlString, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {

                cursor = insertData2DB(jsonObject);
                dbAdapter.updateData(convertCursorToList(cursor));
                //mJSONAdapter.updateData(jsonObject.optJSONArray( "docs"));
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Log.e("Query Failure", statusCode + " " + throwable.getMessage());
            }
        });
    }

    private Cursor insertData2DB(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.optJSONArray("docs");
        Log.d("array size: ", String.valueOf(jsonArray.length()));

        SQLiteDatabase db;
        Cursor cursor = null;

        try {

            db = openOrCreateDatabase("books", MODE_PRIVATE, null);
            db.execSQL("DROP TABLE IF EXISTS " + "books");
            db.execSQL("CREATE TABLE IF NOT EXISTS books(ImageUrl VARCHAR, Title VARCHAR, Author VARCHAR);");

            String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";
            String imageURL = "";
            String title = "";
            String author = "";
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = new JSONObject(jsonArray.getString(i));

                if (object.has("cover_i")) {
                    String imageID = object.getString("cover_i");
                    imageURL = IMAGE_URL_BASE + imageID + "-S.jpg";
                    //Log.d("cover_i", imageURL);
                } else {
                    imageURL = "";
                    //Log.d("cover_i", imageURL);
                }
                if (object.has("title")) {
                    title = object.getString("title");
                    //Log.d("title", title);
                } else {
                    title = "";
                    //Log.d("title", title);
                }
                if (object.has("author_name")) {
                    //author = object.getString("author_name");
                    author = object.optJSONArray("author_name").optString(0);
                    // Log.d("author_name", author);
                } else {
                    author = "";
                    //Log.d("author_name", author);
                }
                db.execSQL("INSERT INTO books VALUES(?, ?, ?)", new String[]{imageURL, title, author});
            }

            cursor = db.rawQuery("select * from books", null);
            /*while (cursor.moveToNext()) {
                Log.d("url", cursor.getString(0));
                Log.d("title", cursor.getString(1));
                Log.d("author_name", cursor.getString(2));
            }*/
            //db.close();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cursor;
    }


    protected ArrayList<Map<String, String>> convertCursorToList(Cursor cursor) {
        ArrayList<Map<String, String>> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put("cover_i", cursor.getString(0));
            map.put("title", cursor.getString(1));
            map.put("author_name", cursor.getString(2));
            result.add(map);
        }
        return result;
    }
}