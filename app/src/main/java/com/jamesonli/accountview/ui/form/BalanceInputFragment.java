package com.jamesonli.accountview.ui.form;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.jamesonli.accountview.R;
import com.jamesonli.accountview.core.AccountDataManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BalanceInputFragment extends Fragment {

    private Button mSubmitButton;
    private EditText mBalanceInput;
    private EditText mDateInput;

    private BalanceInputFormInteractionListener mListener;

    public static BalanceInputFragment newInstance(BalanceInputFormInteractionListener listener) {
        BalanceInputFragment fragment = new BalanceInputFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public BalanceInputFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_balance_input_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBalanceInput = (EditText) view.findViewById(R.id.balance_input);
        mDateInput = (EditText) view.findViewById(R.id.date_input);
        mSubmitButton = (Button) view.findViewById(R.id.submit_button);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitBalance();
            }
        });
    }

    private void submitBalance() {
        AccountDataManager dataManager = AccountDataManager.getInstance(getActivity().getApplicationContext());

        Date date = null;
        float balanceVal = 0;

        try {
            balanceVal = Float.parseFloat(mBalanceInput.getText().toString());
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

            date = dateFormat.parse(mDateInput.getText().toString());
        } catch (ParseException|NumberFormatException e) {
            Toast.makeText(getActivity(), "invalid input", Toast.LENGTH_SHORT).show();
            return;
        }

        dataManager.addBalanceEntry(date, balanceVal);
        mListener.onFormSubmit();
    }

    private void setListener(BalanceInputFormInteractionListener listener) {
        mListener = listener;
    }

    public interface BalanceInputFormInteractionListener {
        void onFormSubmit();
    }
}
