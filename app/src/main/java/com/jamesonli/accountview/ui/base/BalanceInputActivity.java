package com.jamesonli.accountview.ui.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import com.jamesonli.accountview.R;
import com.jamesonli.accountview.ui.form.BalanceInputFragment;

public class BalanceInputActivity extends Activity implements BalanceInputFragment.BalanceInputFormInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_input);

        switchFullFragment(BalanceInputFragment.newInstance(this));
    }

    private void switchFullFragment(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.form_full_container, fragment).commit();
    }

    @Override
    public void onFormSubmit() {
        finish();
    }

}
