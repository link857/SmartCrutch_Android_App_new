package deltazero.smartcrutch.core;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import deltazero.smartcrutch.ui.LoginActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class LoginJsonResp {

    public int code;
    public String msg;

}

public class AccountManager {


    public String serverUrl = "http://192.168.3.18:8000/";

    static public final JsonAdapter<LoginJsonResp> loginJsonAdapter = new Moshi.Builder().build()
            .adapter(LoginJsonResp.class);

    private final OkHttpClient client = new OkHttpClient();

    private final static String TAG = "AccountManager";

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public enum LoginStatus {
        SUCCESS(0),
        USER_NOT_EXIST(1),
        PASSWORD_ERROR(2),
        NETWORK_ERROR(-1),
        UNKNOWN_ERROR(-2);

        private final int value;

        LoginStatus(int value) {
            if (value >= 0 && value <= 2)
                this.value = value;
            else
                this.value = -2;
        }

        public int getValue() {
            return value;
        }
    }


    public void login(String username, String password, LoginActivity loginActivity) {

        Log.d(TAG, "Called login func");

        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(serverUrl.concat("app/login"))
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d(TAG, "Login response: " + resp.msg);
                LoginJsonResp resp = AccountManager.loginJsonAdapter.fromJson(response.body().source());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loginActivity.loginCallback(resp.code, resp.msg);
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.w(TAG, "Login failed: network error" + e);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loginActivity.loginCallback(LoginStatus.NETWORK_ERROR, "Unexpected code ");
                    }
                });
            }

        });

    }

}