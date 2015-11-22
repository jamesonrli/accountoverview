package com.jamesonli.accountview.core;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by james on 11/21/15.
 */
public class NetworkManager {

    private static String INTERNAL_HOST = "10.0.1.20";
    private static String INTERNAL_PORT = "3000";
    private static String INTERNAL_URL = String.format("http://%s:%s", INTERNAL_HOST, INTERNAL_PORT);

    public static int POST = Request.Method.POST;
    public static String USERS = "users";
    public static String BALANCES = "balances";

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

    /**
     * Request with JSON
     */
    public void newJsonRequest(int method, String resource, String requestBody, Response.Listener responseListener, Response.ErrorListener errorListener) {
        JsonRequest jsonRequest = new JsonObjectRequest(
                method, String.format("%s/%s", INTERNAL_URL, resource), requestBody, responseListener, errorListener);
        addRequest(jsonRequest);
    }

    /**
     * Get Request
     */
    public void newStringRequest(String resource, String params, Response.Listener responseListener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(String.format("%s/%s/%s", INTERNAL_URL, resource, params), responseListener, errorListener);
        addRequest(stringRequest);
    }

}
