package com.jamesonli.accountview.core;

import android.database.Cursor;

public interface AccountDataListener {
    void onResult(Cursor cursor);
    void onComplete();
}
