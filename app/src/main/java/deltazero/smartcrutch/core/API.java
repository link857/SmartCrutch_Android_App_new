package deltazero.smartcrutch.core;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import deltazero.smartcrutch.ui.LoginActivity;
import deltazero.smartcrutch.ui.MainActivity;
import deltazero.smartcrutch.ui.MapActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class API {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    public String serverUrl = "http://39.103.138.199:5283/";
    public String uuid;

    public API(String uuid) {
        this.uuid = uuid;
    }


    /* Login

    Description
        获取拐杖uuid，App登录时调用

    Request
        username: 用户名，不可为空http://192.168.3.18:8000/
        password: 密码，不可为空

    Response
        code: 返回值:
        0: 成功
        1: 用户名不存在
        2: 密码错误
        msg: 返回值信息
        uuid: 拐杖uuid

     */

    private static class LoginResp {
        public int code;
        public String msg;
        public String uuid;
    }

    private static final JsonAdapter<LoginResp> loginRespAdapter = new Moshi.Builder().build()
            .adapter(LoginResp.class);

    public void login(String username, String password, LoginActivity uiActivity) {

        Log.d("login", "Called login func");


        JSONObject jsonResq = new JSONObject();
        try {
            jsonResq.put("username", username);
            jsonResq.put("password", password);
        } catch (JSONException e) {
            mHandler.post(() -> uiActivity.loginCallback(-3, e.toString(), null));
            return;
        }

        RequestBody formBody = RequestBody.create(jsonResq.toString(), JSON);

        Request request = new Request.Builder()
                .url(this.serverUrl.concat("app/login"))
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                LoginResp resp = API.loginRespAdapter.fromJson(response.body().source());
                Log.d("login", "Login response: " + resp.msg);

                mHandler.post(() -> uiActivity.loginCallback(resp.code, resp.msg, resp.uuid));
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.w("login", "Login failed: network error" + e);
                mHandler.post(() -> uiActivity.loginCallback(-1, e.toString(), null));
            }

        });

    }


    /* Get Status

    Description
        获取拐杖状态信息

    Request
        uuid: 拐杖uuid

    Response
        code: 返回值:
            0: 成功
            1: 无效的uuid
        msg: 返回值信息
        status: 拐杖状态码:
            'ok': 正常
            'emergency': 摔倒
            'error': 错误
            'offline': 离线

     */

    private static class GetStatusResp {
        public int code;
        public String msg;
        public String status;
    }

    private static final JsonAdapter<GetStatusResp> getStatusRespAdapter = new Moshi.Builder().build()
            .adapter(GetStatusResp.class);

    public void get_status(MainActivity uiActivity) {

        Request request = new Request.Builder()
                .url(this.serverUrl.concat(String.format("app/get_status/%s", this.uuid)))
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                GetStatusResp resp = API.getStatusRespAdapter.fromJson(response.body().source());
                Log.d("get_status", "Get status response: " + resp.msg);

                mHandler.post(() -> uiActivity.updateStatus(resp.code, resp.msg, resp.status));
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.w("get_status", "Get status failed: network error" + e);
                mHandler.post(() -> uiActivity.updateStatus(-1, e.toString(), null));
            }

        });

    }



    /* Get Loc
    Description
        获取拐杖状态信息

    Request
        uuid: 拐杖uuid

    Response
        code: 返回值:
            0: 成功
            1: 无效的uuid
        msg: 返回值信息
        loc: 可选项，拐杖位置信息
            latitude: 纬度
            longitude: 经度

     */

    private static class GetLocResp {
        public int code;
        public String msg;
        public Loc loc;
    }

    private static class Loc {
        public float latitude;
        public float longitude;
    }

    private static final JsonAdapter<GetLocResp> getLocRespAdapter = new Moshi.Builder().build()
            .adapter(GetLocResp.class);

    public void getLoc(MapActivity uiActivity) {

        Request request = new Request.Builder()
                .url(this.serverUrl.concat(String.format("app/get_loc/%s", this.uuid)))
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                GetLocResp resp = API.getLocRespAdapter.fromJson(response.body().source());
                Log.d("get_status", "Get loc response: " + resp.msg);

                if (resp.loc != null)
                    mHandler.post(() -> uiActivity.updateLoc(resp.code, resp.msg, resp.loc.latitude, resp.loc.longitude));
                else
                    mHandler.post(() -> uiActivity.updateLoc(-1, "No loc info", 0, 0));
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.w("get_status", "Get loc failed: network error" + e);
                mHandler.post(() -> uiActivity.updateLoc(-2, e.toString(), 0, 0));
            }

        });

    }

}
