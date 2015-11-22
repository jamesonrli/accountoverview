package com.jamesonli.accountview.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jamesonli.accountview.common.AVUtils;
import com.jamesonli.accountview.db.DbConstants;
import com.jamesonli.accountview.db.DbManager;
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

    private final DbManager mDbManager;
    private final Executor mAccountManagerExecutor;
    private final SharedPreferencesManager mPrefManager;
    private final Context mContext;

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
        mDbManager = DbManager.getInstance(context);
        mAccountManagerExecutor = Executors.newSingleThreadExecutor();
        mPrefManager = SharedPreferencesManager.getInstance(context);
        mContext = context;
    }

    public boolean isLoggedIn() {
        return SharedPreferencesManager.getInstance(mContext).valueExists(SharedPreferencesManager.USER_ID);
    }

    /**
     * Get device id, make login request, and store user id
     */
    public void login(AuthListener listener) {
        String deviceId = AVUtils.getDeviceId(mContext);

        JSONObject userObj = new JSONObject();
        JSONObject deviceObj = new JSONObject();
        try {
            deviceObj.put("deviceid", deviceId);
            userObj.put("user", deviceObj);

            loginRequest(userObj, listener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loginRequest(JSONObject userObj, final AuthListener listener) {
        NetworkManager.getInstance(mContext).newRequest(NetworkManager.POST, NetworkManager.USERS, userObj.toString(),
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        if(response instanceof JSONObject) {
                            JSONObject responseObj = (JSONObject) response;
                            try {
                                persistUserId(responseObj.getString("id"));
                                listener.onLoginSuccess();
                            } catch (JSONException e) {
                                LOG.error("failed to parse response json: " + e.getMessage());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LOG.error("network error: " + error.getMessage());
                    }
                }
        );
    }

    private void persistUserId(String userId) {
        mPrefManager.setString(SharedPreferencesManager.USER_ID, userId);
    }

    public void addBalanceEntry(final Date date, final float balance, final AccountDataListener listener) {
        Runnable dbTask = new Runnable() {
            @Override
            public void run() {
                ContentValues value = new ContentValues();
                value.put(DbConstants.BALANCE_TABLE_DATE, date.getTime());
                value.put(DbConstants.BALANCE_TABLE_BAL, balance);

                mDbManager.getWritableDatabase().insert(DbConstants.BALANCE_TABLE, null, value);

                listener.onComplete();
            }
        };

        mAccountManagerExecutor.execute(dbTask);
    }

    public void getBalanceEntries(final AccountDataListener listener) {
        Runnable qryTask = new Runnable() {
            @Override
            public void run() {
                Cursor cursor = mDbManager.getReadableDatabase().query(
                        DbConstants.BALANCE_TABLE, null, null, null, null, null,
                        DbConstants.BALANCE_TABLE_DATE + " asc");
                listener.onResult(cursor);
            }
        };

        mAccountManagerExecutor.execute(qryTask);
    }
}
