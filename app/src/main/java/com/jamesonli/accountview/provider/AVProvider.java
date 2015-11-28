package com.jamesonli.accountview.provider;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.jamesonli.accountview.service.BalanceService;

/**
 * AVProvider runs on the calling thread
 */
public class AVProvider extends ContentProvider {

    private DbManager mDbManager;
    private SQLiteDatabase mReadableDB;
    private SQLiteDatabase mWritableDB;
    private Context mContext;

    private static final UriMatcher mUriMatcher;
    private static final int BALANCE_URI_MATCH = 1;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AVContract.PROVIDER_NAME, DbConstants.BALANCE_TABLE, BALANCE_URI_MATCH);
    }

    @Override
    public boolean onCreate() {
        mContext = getContext().getApplicationContext();
        mDbManager = DbManager.getInstance(mContext);

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

        int matchKey = mUriMatcher.match(uri);
        switch(matchKey) {
            case BALANCE_URI_MATCH: {
                Cursor balanceCursor = mReadableDB.query(DbConstants.BALANCE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                balanceCursor.setNotificationUri(mContext.getContentResolver(), AVContract.BALANCE_DATA_URI);
                return balanceCursor;
            }
            default:
                return null;
        }
    }

    @Override
    public String getType(Uri uri) {
        int matchKey = mUriMatcher.match(uri);

        switch(matchKey) {
            case BALANCE_URI_MATCH: {
                return AVContract.TYPE_BALANCE_LIST;
            }
        }

        throw new IllegalArgumentException("Unsupported URI: " + uri);
    }

    @Override
    /**
     * Insert returns null if operation fails
     */
    public Uri insert(Uri uri, ContentValues values) {
        if(!mWritableDB.isOpen() || mUriMatcher.match(uri) != BALANCE_URI_MATCH) {
            return null;
        }

        int matchKey = mUriMatcher.match(uri);
        switch(matchKey) {
            case BALANCE_URI_MATCH: {
                long insertResult = mWritableDB.insert(DbConstants.BALANCE_TABLE, null, values);
                mContext.getContentResolver().notifyChange(AVContract.BALANCE_DATA_URI, null);

                long dateVal = values.getAsLong(AVContract.BALANCE_TABLE_DATE);
                float balVal = values.getAsFloat(AVContract.BALANCE_TABLE_BALANCE);

                Intent serviceIntent = new Intent(mContext, BalanceService.class);
                serviceIntent.addFlags(AVContract.BALANCE_INSERT_OP);
                serviceIntent.putExtra(AVContract.BALANCE_TABLE_DATE, dateVal);
                serviceIntent.putExtra(AVContract.BALANCE_TABLE_BALANCE, balVal);

                mContext.startService(serviceIntent);

                return insertResult == -1 ? null : uri;
            }
            default:
                return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        if(!mWritableDB.isOpen() || mUriMatcher.match(uri) != BALANCE_URI_MATCH) {
//            return 0;
//        }
//
//        return mWritableDB.delete(DbConstants.BALANCE_TABLE, selection, selectionArgs);

        // todo: not yet supported
        return -1;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
//        if(!mWritableDB.isOpen() || mUriMatcher.match(uri) != BALANCE_URI_MATCH) {
//            return 0;
//        }
//
//        return mWritableDB.update(DbConstants.BALANCE_TABLE, values, selection, selectionArgs);

        // todo: not yet supported
        return -1;
    }

}
