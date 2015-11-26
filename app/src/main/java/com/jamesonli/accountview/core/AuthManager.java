package com.jamesonli.accountview.core;

import android.content.Context;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jamesonli.accountview.common.AVUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthManager {

    private static final Logger LOG = LoggerFactory.getLogger(AuthManager.class);
    private static AuthManager mAuthManager;

    private final SharedPreferencesManager mPrefManager;
    private final Context mContext;

    /**
     * Get an instance of AccountDataManager
     */
    public static AuthManager getInstance(Context context) {
        if (mAuthManager == null) {
            mAuthManager = new AuthManager(context.getApplicationContext());
        }

        return mAuthManager;
    }

    private AuthManager(Context context) {
        mPrefManager = SharedPreferencesManager.getInstance(context);
        mContext = context;
    }

    public boolean isLoggedIn() {
        return SharedPreferencesManager.getInstance(mContext).valueExists(SharedPreferencesManager.USER_ID);
    }

    /**
     * Get device id, make login request, and store user id
     */
    public void login(AuthListener listener) {
        String deviceId = AVUtils.getDeviceId(mContext);

        JSONObject userObj = new JSONObject();
        JSONObject deviceObj = new JSONObject();
        try {
            deviceObj.put("deviceid", deviceId);
            userObj.put("user", deviceObj);

            loginRequest(userObj, listener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loginRequest(JSONObject userObj, final AuthListener listener) {
        NetworkManager.getInstance(mContext).newJsonRequest(NetworkManager.POST, NetworkManager.USERS, userObj.toString(),
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        if (response instanceof JSONObject) {
                            JSONObject responseObj = (JSONObject) response;
                            try {
                                persistUserId(responseObj.getString("id"));
                                listener.onLoginSuccess();
                            } catch (JSONException e) {
                                LOG.error("failed to parse response json: " + e.getMessage());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LOG.error("network error: " + error.getMessage());
                    }
                }
        );
    }

    private void persistUserId(String userId) {
        mPrefManager.setString(SharedPreferencesManager.USER_ID, userId);
    }

}
