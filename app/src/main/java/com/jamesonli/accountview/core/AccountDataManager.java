package com.jamesonli.accountview.core;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jamesonli.accountview.common.AVUtils;
import com.jamesonli.accountview.provider.AVContract;
import com.jamesonli.accountview.service.AVService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AccountDataManager {

    private static final Logger LOG = LoggerFactory.getLogger(AccountDataManager.class);
    private static AccountDataManager mDataManager;

    private final Executor mAccountManagerExecutor;
    private final Context mContext;
    private final ContentResolver mContentResolver;

    /**
     * Get an instance of AccountDataManager
     */
    public static AccountDataManager getInstance(Context context) {
        if (mDataManager == null) {
            mDataManager = new AccountDataManager(context.getApplicationContext());
        }

        return mDataManager;
    }

    private AccountDataManager(Context context) {
        mAccountManagerExecutor = Executors.newSingleThreadExecutor();
        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    public void addBalanceEntry(final Date date, final float balance, final AccountDataListener listener) {
        Runnable dbTask = new Runnable() {
            @Override
            public void run() {
                ContentValues value = new ContentValues();
                value.put(AVContract.BALANCE_TABLE_DATE, date.getTime());
                value.put(AVContract.BALANCE_TABLE_BALANCE, balance);

                mContentResolver.insert(AVContract.BALANCE_DATA_URI, value);

                listener.onComplete();
            }
        };
        mAccountManagerExecutor.execute(dbTask);

        String deviceId = AVUtils.getDeviceId(mContext);

        try {
            JSONObject balanceDetails = new JSONObject();
            balanceDetails.put("deviceid", deviceId);
            balanceDetails.put("datetime", date.getTime());
            balanceDetails.put("balance", balance);
            JSONObject balanceObj = new JSONObject();
            balanceObj.put("balance", balanceDetails);

            addBalanceRequest(balanceObj);
        } catch (JSONException e) {
            LOG.error("failed to create json obj: " + e.getMessage());
        }

    }

    private void addBalanceRequest(JSONObject balanceObj) {
        NetworkManager.getInstance(mContext).newJsonRequest(NetworkManager.POST, NetworkManager.BALANCES, balanceObj.toString(),
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        LOG.info("new balance sent");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LOG.error("new balance error: " + error.getMessage());
                    }
                }
        );
    }

    public void getBalanceEntries(final AccountDataListener listener) {
        Runnable qryTask = new Runnable() {
            @Override
            public void run() {
                Cursor cursor = mContentResolver.query(
                        AVContract.BALANCE_DATA_URI, null, null, null,
                        AVContract.BALANCE_TABLE_DATE + " asc");
                listener.onResult(cursor);
            }
        };

        mAccountManagerExecutor.execute(qryTask);
    }
}
