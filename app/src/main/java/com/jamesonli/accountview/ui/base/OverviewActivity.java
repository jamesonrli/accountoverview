package com.jamesonli.accountview.ui.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import com.jamesonli.accountview.R;
import com.jamesonli.accountview.common.Constants;
import com.jamesonli.accountview.core.AccountDataManager;
import com.jamesonli.accountview.core.AuthManager;
import com.jamesonli.accountview.core.SharedPreferencesManager;
import com.jamesonli.accountview.provider.AVContract;
import com.jamesonli.accountview.ui.form.LoginDialogFragment;
import com.jamesonli.accountview.ui.graph.GraphFragment;
import com.jamesonli.accountview.ui.summary.OverviewSummaryFragment;

public class OverviewActivity extends Activity implements GraphFragment.GraphFragmentInteraction {

    private FloatingActionButton mAddBalanceButton;
    private GraphFragment graphFragment;
    private OverviewSummaryFragment summaryFragment;
    private Handler uiHandler = new Handler();

    private final ContentObserver balanceDataObserver = new ContentObserver(uiHandler) {
        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);

            if(uri != null && uri.equals(AVContract.BALANCE_DATA_URI) && graphFragment != null) {
                graphFragment.notifyGraphDataChange();

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!AuthManager.getInstance(getApplicationContext()).isLoggedIn()) {
            showLoginDialog();
        }

        getContentResolver().registerContentObserver(AVContract.BALANCE_DATA_URI, true, balanceDataObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();

        getContentResolver().unregisterContentObserver(balanceDataObserver);
    }

    private void initView() {
        graphFragment = GraphFragment.newInstance(this);
        switchFullFragment(graphFragment, R.id.overview_full_container);

        mAddBalanceButton = (FloatingActionButton) findViewById(R.id.overview_fab);

        mAddBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OverviewActivity.this, BalanceInputActivity.class));
            }
        });

        summaryFragment = OverviewSummaryFragment.newInstance();
        switchFullFragment(summaryFragment, R.id.overview_sum_container);
    }

    private void switchFullFragment(Fragment fragment, int container) {
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(container, fragment).commit();
    }

    private void showLoginDialog() {
        LoginDialogFragment loginDialog = LoginDialogFragment.getInstance(this);
        loginDialog.show(getFragmentManager(), "loginDialog");
    }

    @Override
    public void updateSelectedValue(String date, float balance) {
        summaryFragment.setDate(date);
        summaryFragment.setBalance(balance);
    }

}
