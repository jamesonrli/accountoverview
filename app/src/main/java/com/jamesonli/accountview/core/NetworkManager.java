package com.jamesonli.accountview.core;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by james on 11/21/15.
 */
public class NetworkManager {

    private static String INTERNAL_HOST = "10.0.1.20";
    private static String INTERNAL_PORT = "3000";
    private static String INTERNAL_URL = String.format("https://%s:%s", INTERNAL_HOST, INTERNAL_PORT);

    public static int POST = Request.Method.POST;

    private RequestQueue requestQueue;

    private static NetworkManager networkManager;

    public static synchronized NetworkManager getInstance(Context context) {
        if(networkManager == null) {
            networkManager = new NetworkManager(context);
        }

        return networkManager;
    }

    private NetworkManager(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    /**
     * All requests will run on threads managed by the RequestQueue
     */
    private void addRequest(Request request) {
        requestQueue.add(request);
    }

    public void newRequest(int method, String requestBody, Response.Listener responseListener, Response.ErrorListener errorListener) {
        JsonRequest jsonRequest = new JsonObjectRequest(method, INTERNAL_URL, requestBody, responseListener, errorListener);
        addRequest(jsonRequest);
    }

}
