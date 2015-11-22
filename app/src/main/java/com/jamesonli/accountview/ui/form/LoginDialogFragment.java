package com.jamesonli.accountview.ui.form;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.jamesonli.accountview.R;
import com.jamesonli.accountview.core.AccountDataManager;
import com.jamesonli.accountview.core.AuthListener;

/**
 * Created by james on 11/21/15.
 */
public class LoginDialogFragment extends DialogFragment {

    public static LoginDialogFragment getInstance(Context context) {
        LoginDialogFragment dialog = new LoginDialogFragment();
        return dialog;
    }

    public LoginDialogFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button loginButton = (Button) view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnLoginHandler(this));
    }

    private static class OnLoginHandler implements View.OnClickListener {
        private LoginDialogFragment mFrag;

        public OnLoginHandler(LoginDialogFragment frag) {
            mFrag = frag;
        }
        @Override
        public void onClick(View view) {
            AccountDataManager.getInstance(view.getContext()).login(new AuthListener() {
                @Override
                public void onLoginSuccess() {
                    mFrag.dismiss();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if(dialog == null) { return; }

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
