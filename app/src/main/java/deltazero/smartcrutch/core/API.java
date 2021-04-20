package deltazero.smartcrutch.core;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import deltazero.smartcrutch.ui.LoginActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class API {

    public String serverUrl = "http://192.168.31.114:8000/";
    private final OkHttpClient client = new OkHttpClient();
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    // Login

    private static JSONObject loginReq = new JSONObject();
    loginReq.put("KEY1", "VALUE1");
    loginReq.put("KEY2", "VALUE2");

    private static class LoginResp {
        public int code;
        public String msg;
    }

    private static final JsonAdapter<LoginResp> loginRespAdapter = new Moshi.Builder().build()
            .adapter(LoginResp.class);

    public void login(String username, String password, LoginActivity loginActivity) {

        Log.d("login", "Called login func");

        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(this.serverUrl.concat("app/login"))
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                LoginResp resp = API.loginRespAdapter.fromJson(response.body().source());
                Log.d("login", "Login response: " + resp.msg);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loginActivity.loginCallback(resp.code, resp.msg);
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.w("login", "Login failed: network error" + e);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loginActivity.loginCallback(-1, "Unexpected code".concat(e.toString()));
                    }
                });
            }

        });

    }


    // Register

}
