package dev.glatorre.sailaway;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SailawayService {
    private final String USERNAME = "Insert your key, you can find it @ https://sailaway.world/myaccount.pl";
    private final String API_KEY = "Insert your key, you can find it @ https://sailaway.world/myaccount.pl";
    private final String API_URL = "http://srv.sailaway.world/cgi-bin/sailaway/APIBoatInfo.pl?usrnr="+ USERNAME + "&key=" + API_KEY;
    private final int POLLING_PERIOD = 10 * 60 * 1000; // 10 minutes
    private SailawayHandler handler;

    public SailawayService(Context context) {
        handler = (SailawayHandler) context;
        final RequestQueue queue = Volley.newRequestQueue(context);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, API_URL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Boat[] boats = new Boat[response.length()];
                            for(int i=0; i<response.length(); i++){
                                JSONObject boat = response.getJSONObject(i);
                                boats[i] = new Boat(boat.getString("boatname"),
                                        boat.getDouble("latitude"),
                                        boat.getDouble("longitude"),
                                        boat.getDouble("spd"),
                                        boat.getDouble("cog"));
                            }
                            handler.onBoatsReceived(boats);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Sailaway", error.toString());
                    }
                });

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        queue.add(jsonArrayRequest);
                        Thread.sleep(POLLING_PERIOD);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }
}
