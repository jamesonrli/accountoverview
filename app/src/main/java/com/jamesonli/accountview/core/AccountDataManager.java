package com.jamesonli.accountview.core;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jamesonli.accountview.common.AVUtils;
import com.jamesonli.accountview.provider.AVContract;
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

    public void addBalanceEntry(final Date date, final float balance) {
        Runnable dbTask = new Runnable() {
            @Override
            public void run() {
                ContentValues value = new ContentValues();
                value.put(AVContract.BALANCE_TABLE_DATE, date.getTime());
                value.put(AVContract.BALANCE_TABLE_BALANCE, balance);

                mContentResolver.insert(AVContract.BALANCE_DATA_URI, value);
            }
        };
        mAccountManagerExecutor.execute(dbTask);
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
