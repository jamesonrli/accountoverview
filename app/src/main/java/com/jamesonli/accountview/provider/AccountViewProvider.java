package com.jamesonli.accountview.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by james on 11/23/15.
 */
public class AccountViewProvider extends ContentProvider {

    private DbManager mDbManager;
    private SQLiteDatabase mReadableDB;
    private SQLiteDatabase mWritableDB;

    private static final UriMatcher mUriMatcher;
    private static final int BALANCE_URI_MATCH = 1;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AVContract.PROVIDER_NAME, DbConstants.BALANCE_TABLE, BALANCE_URI_MATCH);
    }

    @Override
    public boolean onCreate() {
        mDbManager = DbManager.getInstance(getContext().getApplicationContext());

        if(mDbManager == null) {
            return false;
        }

        mWritableDB = mDbManager.getWritableDatabase();
        mReadableDB = mDbManager.getReadableDatabase();

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if(!mWritableDB.isOpen() || mUriMatcher.match(uri) != BALANCE_URI_MATCH) {
            return null;
        }

        return mReadableDB.query(DbConstants.BALANCE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    /**
     * Insert returns null if operation fails
     */
    public Uri insert(Uri uri, ContentValues values) {
        if(!mWritableDB.isOpen() || mUriMatcher.match(uri) != BALANCE_URI_MATCH) {
            return null;
        }

        long insertResult = mWritableDB.insert(DbConstants.BALANCE_TABLE, null, values);

        return insertResult == -1 ? null : uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if(!mWritableDB.isOpen() || mUriMatcher.match(uri) != BALANCE_URI_MATCH) {
            return 0;
        }

        return mWritableDB.delete(DbConstants.BALANCE_TABLE, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if(!mWritableDB.isOpen() || mUriMatcher.match(uri) != BALANCE_URI_MATCH) {
            return 0;
        }

        return mWritableDB.update(DbConstants.BALANCE_TABLE, values, selection, selectionArgs);
    }
}
