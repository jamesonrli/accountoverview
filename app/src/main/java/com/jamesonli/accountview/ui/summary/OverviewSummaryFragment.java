package com.jamesonli.accountview.ui.summary;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.jamesonli.accountview.R;
import org.w3c.dom.Text;

public class OverviewSummaryFragment extends Fragment {

    private TextView dateView;
    private TextView balanceView;

    public static OverviewSummaryFragment newInstance() {
        OverviewSummaryFragment fragment = new OverviewSummaryFragment();
        return fragment;
    }

    public OverviewSummaryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview_summary, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateView = (TextView) view.findViewById(R.id.summary_date);
        balanceView = (TextView) view.findViewById(R.id.summary_balance_amount);
    }

    public void setDate(String date) {
        dateView.setText(date);
    }

    public void setBalance(float val) {
        balanceView.setText(String.format("$%.2f", val));
    }

}
