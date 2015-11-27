package com.jamesonli.accountview.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.jamesonli.accountview.provider.AVContract;

/**
 * Created by james on 11/26/15.
 */
public class BalanceService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int intentFlags = intent.getFlags();

        if((intentFlags & AVContract.BALANCE_INSERT_OP) == AVContract.BALANCE_INSERT_OP) {
            long dateVal = intent.getLongExtra(AVContract.BALANCE_TABLE_DATE, 0);
            float balVal = intent.getFloatExtra(AVContract.BALANCE_TABLE_BALANCE, 0);
            BalanceServiceHelper.addBalanceEntry(getApplicationContext(), dateVal, balVal, this, startId); // calls stopSelf
        } else if((intentFlags & AVContract.BALANCE_DOWNLOAD_OP) == AVContract.BALANCE_DOWNLOAD_OP) {
            // todo: download balances for logged in user
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
