package xyz.hxworld.codetimeline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Kshitij Nagvekar on 3/8/2016.
 */
public class SQLiteDBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "EventsDB";
    private static final String TB_Events = "events";

    private static final String KEY_ID = "id";
    private static final String KEY_Title = "title";
    private static final String KEY_descr = "description";
    private static final String KEY_url = "url";
    private static final String KEY_startTime = "startTime";
    private static final String KEY_endTime = "endTime";

    private String jURL = "http://hxworld.xyz/json/hackerrank.php";

    Context context;
    MainActivity mainActivity;

    public SQLiteDBHandler(Context context, MainActivity mainActivity) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_Events_TABLE = "CREATE TABLE " + TB_Events + "(" + KEY_Title + " TEXT, "
                + KEY_descr + " TEXT, " + KEY_url + " TEXT, " + KEY_startTime + " TEXT, "
                + KEY_endTime + " TEXT, " + KEY_ID + " INTEGER PRIMARY KEY)";
        db.execSQL(CREATE_Events_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_Events);
        onCreate(db);
    }

    public void resetTB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TB_Events);
        onCreate(db);
    }

    public void addEvents(EventModel event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_Title, event.getTitle());
        contentValues.put(KEY_descr, event.getDescription());
        contentValues.put(KEY_url, event.getUrl());
        contentValues.put(KEY_startTime, event.getStartTime());
        contentValues.put(KEY_endTime, event.getEndTime());

        db.insert(TB_Events, null, contentValues);
    }

    public void getHackerrankEvents(final int position) {
        resetTB();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, jURL, new Response.Listener<String>() {

            @Override
            public void onResponse(final String s) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("channel");
                            JSONArray jsonArray = jsonObject1.getJSONArray("item");
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject event = jsonArray.getJSONObject(i);
                                EventModel eventModel = new EventModel();
                                eventModel.setTitle(event.getString("title"));
                                eventModel.setDescription(event.getString("description"));
                                eventModel.setUrl(event.getString("url"));
                                eventModel.setStartTime(event.getString("startTime"));
                                eventModel.setEndTime(event.getString("endTime"));
                                addEvents(eventModel);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            getAllEvents(position);
                        }
                    }
                }).start();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        }) {
            protected Map<String,String> getParams() {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(stringRequest);
    }
    public void getAllEvents(int position) {
        final ArrayList<EventModel> eventModelArrayList = new ArrayList<>();
        String SQLQuery = "SELECT * FROM " + TB_Events;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQLQuery, null);

        if(cursor.moveToFirst()) {
            do {
                EventModel eventModel = new EventModel();

                eventModel.setTitle(cursor.getString(0));
                eventModel.setDescription(cursor.getString(1));
                eventModel.setUrl(cursor.getString(2));
                eventModel.setStartTime(cursor.getString(3));
                eventModel.setEndTime(cursor.getString(4));

                Date startTime = null, endTime = null;
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    startTime = simpleDateFormat.parse(eventModel.getStartTime());
                    endTime = simpleDateFormat.parse(eventModel.getEndTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                switch (position) {
                    case 0:
                        eventModelArrayList.add(eventModel);
                    case 1:
                        if(new Date().before(startTime)) {
                            eventModelArrayList.add(eventModel);
                        }
                        break;
                    case 2:
                        if(new Date().after(startTime) && new Date().before(endTime)) {
                            eventModelArrayList.add(eventModel);
                        }
                        break;
                    case 3:
                        if(new Date().after(endTime)) {
                            eventModelArrayList.add(eventModel);
                        }
                        break;
                }
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.recyclerViewAdapter = new RecyclerViewAdapter(eventModelArrayList);
                        mainActivity.recyclerView.setAdapter(mainActivity.recyclerViewAdapter);
                        mainActivity.recyclerViewAdapter.notifyDataSetChanged();
                    }
                });
            } while (cursor.moveToNext());
        }
        db.close();
    }
}
