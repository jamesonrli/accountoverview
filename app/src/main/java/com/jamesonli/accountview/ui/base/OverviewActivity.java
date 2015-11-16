package com.jamesonli.accountview.ui.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import com.jamesonli.accountview.R;
import com.jamesonli.accountview.common.Constants;
import com.jamesonli.accountview.ui.graph.GraphFragment;
import com.jamesonli.accountview.ui.summary.OverviewSummaryFragment;

public class OverviewActivity extends Activity implements GraphFragment.GraphFragmentInteraction {

    private FloatingActionButton mAddBalanceButton;
    private GraphFragment graphFragment;
    private OverviewSummaryFragment summaryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        initView();
    }

    private void initView() {
        graphFragment = GraphFragment.newInstance(this);
        switchFullFragment(graphFragment, R.id.overview_full_container);

        mAddBalanceButton = (FloatingActionButton) findViewById(R.id.overview_fab);

        mAddBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(OverviewActivity.this, BalanceInputActivity.class), Constants.BALANCE_REQUEST);
            }
        });

        summaryFragment = OverviewSummaryFragment.newInstance();
        switchFullFragment(summaryFragment, R.id.overview_sum_container);
    }

    private void switchFullFragment(Fragment fragment, int container) {
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(container, fragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) {
            return;
        }

        switch(requestCode) {
            case Constants.BALANCE_REQUEST:
                graphFragment.notifyGraphDataChange();
                break;
        }
    }

    @Override
    public void updateSelectedValue(String date, float balance) {
        summaryFragment.setDate(date);
        summaryFragment.setBalance(balance);
    }
}
