package com.jamesonli.accountview.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.jamesonli.accountview.db.DbConstants;
import com.jamesonli.accountview.db.DbManager;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AccountDataManager {

    private static AccountDataManager mDataManager;

    private DbManager dbManager;
    private Executor mAccountManagerExecutor;

    /**
     * Get an instance of AccountDataManager. Application context is recommended to prevent activity leak.
     */
    public static AccountDataManager getInstance(Context context) {
        if(mDataManager == null) {
            mDataManager = new AccountDataManager(context);
        }

        return mDataManager;
    }

    private AccountDataManager(Context context) {
        dbManager = DbManager.getInstance(context);
        mAccountManagerExecutor = Executors.newSingleThreadExecutor();
    }

    public void addBalanceEntry(final Date date, final float balance, final AccountDataListener listener) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                ContentValues value = new ContentValues();
                value.put(DbConstants.BALANCE_TABLE_DATE, date.getTime());
                value.put(DbConstants.BALANCE_TABLE_BAL, balance);

                dbManager.getWritableDatabase().insert(DbConstants.BALANCE_TABLE, null, value);

                listener.onComplete();
            }
        };

        mAccountManagerExecutor.execute(task);
    }

    public void getBalanceEntries(final AccountDataListener listener) {
        Runnable qryTask = new Runnable() {
            @Override
            public void run() {
                Cursor cursor = dbManager.getReadableDatabase().query(
                        DbConstants.BALANCE_TABLE, null, null, null, null, null,
                        DbConstants.BALANCE_TABLE_DATE + " asc");
                listener.onResult(cursor);
            }
        };

        mAccountManagerExecutor.execute(qryTask);
    }
}
