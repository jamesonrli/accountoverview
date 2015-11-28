package com.jamesonli.accountview.service;

import android.app.Service;
import android.content.Context;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jamesonli.accountview.common.AVUtils;
import com.jamesonli.accountview.core.NetworkManager;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by james on 11/26/15.
 */
class BalanceServiceHelper {
    private static final Logger LOG = LoggerFactory.getLogger(BalanceServiceHelper.class);

    static void addBalanceEntry(Context context, long date, float balance, final Service service, final int startId) {
        String deviceId = AVUtils.getDeviceId(context);

        JSONObject balanceObj = ServerUtils.buildBalanceObject(deviceId, date, balance);

        // network calls work the volley provided thread pool
        NetworkManager.getInstance(context).newJsonRequest(NetworkManager.POST, NetworkManager.BALANCES, balanceObj.toString(),
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        LOG.info("new balance sent");
                        service.stopSelf(startId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LOG.error("new balance error: " + error.getMessage());
                        service.stopSelf(startId);
                    }
                }
        );
    }

    static void getBalanceEntries() {

    }
}
