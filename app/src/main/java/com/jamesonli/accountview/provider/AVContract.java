package com.jamesonli.accountview.provider;

import android.net.Uri;

/**
 * Created by james on 11/23/15.
 */
public final class AVContract {
    public static final String PROVIDER_NAME = "com.jamesonli.accountview.provider";
    public static final String URL = "content://" + PROVIDER_NAME;
    public static final String BALANCE_DATA_URL = URL + "/" + DbConstants.BALANCE_TABLE;
    public static final Uri BALANCE_DATA_URI = Uri.parse(BALANCE_DATA_URL);

    /** Types **/
    public static final String TYPE_DIR = "vnd.android.cursor.dir";
    public static final String TYPE_BALANCE_LIST = TYPE_DIR + "/vnd.jamesonli." + DbConstants.BALANCE_TABLE;

    /** Balances **/
    public static final int BALANCE_INSERT_OP = 0xba3;
    public static final int BALANCE_DOWNLOAD_OP = 0xba4;
    public static final String BALANCE_TABLE = DbConstants.BALANCE_TABLE;
    public static final String BALANCE_TABLE_KEY = DbConstants.BALANCE_TABLE_PK;
    public static final String BALANCE_TABLE_DATE = DbConstants.BALANCE_TABLE_DATE;
    public static final String BALANCE_TABLE_BALANCE = DbConstants.BALANCE_TABLE_BAL;
}
