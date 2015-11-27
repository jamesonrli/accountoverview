package com.jamesonli.accountview.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by james on 11/26/15.
 */
public class ServerUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ServerUtils.class);

    public static JSONObject buildBalanceObject(String deviceId, long date, float balance) {
        try {
            JSONObject balanceDetails = new JSONObject();
            balanceDetails.put("deviceid", deviceId);
            balanceDetails.put("datetime", date);
            balanceDetails.put("balance", balance);

            JSONObject balanceObj = new JSONObject();
            balanceObj.put("balance", balanceDetails);

            return balanceObj;
        } catch (JSONException e) {
            LOG.error("failed to create json obj: " + e.getMessage());
        }

        return null;
    }
}
