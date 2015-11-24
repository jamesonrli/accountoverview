package com.jamesonli.accountview.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

class DbManager {

    private static DbManager mManager;

    private AccountDataDbHelper mDbHelper;

    static void init(Context context) {
        mManager = new DbManager(context);
    }

    static DbManager getInstance(Context context) {
        if(mManager == null) {
            init(context);
        }

        return mManager;
    }

    private DbManager(Context context) {
        mDbHelper = new AccountDataDbHelper(context);
    }

    SQLiteDatabase getReadableDatabase() {
        return mDbHelper.getReadableDatabase();
    }

    SQLiteDatabase getWritableDatabase() {
        return mDbHelper.getWritableDatabase();
    }

}
