package com.jamesonli.accountview.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbManager {

    private static DbManager mManager;

    private AccountDataDbHelper mDbHelper;

    public static DbManager getInstance(Context context) {
        if(mManager == null) {
            mManager = new DbManager(context);
        }

        return mManager;
    }

    private DbManager(Context context) {
        mDbHelper = new AccountDataDbHelper(context);
    }

    public SQLiteDatabase getReadableDatabase() {
        return mDbHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        return mDbHelper.getWritableDatabase();
    }

}
