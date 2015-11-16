package com.jamesonli.accountview.ui.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.jamesonli.accountview.R;
import com.jamesonli.accountview.ui.form.BalanceInputFragment;

public class BalanceInputActivity extends AppCompatActivity implements BalanceInputFragment.BalanceInputFormInteractionListener {

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
        setResult(RESULT_OK);
        finish();
    }

}
