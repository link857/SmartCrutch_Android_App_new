package deltazero.smartcrutch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import deltazero.smartcrutch.R;
import deltazero.smartcrutch.core.API;

public class MainActivity extends AppCompatActivity {

    private API api;
    private String uuid;

    final String LOGTAG = "MainActivity";

    private TextView tvUserInfo, tvStatus, tvStatusInfo;
    private MaterialButton btViewMap;
    private MaterialCardView cvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get uuid & init api

        uuid = getSharedPreferences("deltazero.smartcrutch.prefs", MODE_PRIVATE)
                .getString("uuid", null);

        if (uuid == null) {

            // Launch login activity
            Log.i(LOGTAG, "No uuid cache found, start login activity");

            Intent intent = new Intent(this, LoginActivity.class);
            // intent.putExtra(API_BUNDLE, api);
            startActivity(intent);

            uuid = getSharedPreferences("deltazero.smartcrutch.prefs", MODE_PRIVATE)
                    .getString("uuid", null);

        } else {
            Log.i(LOGTAG, String.format("uuid cache found, skip login: uuid=%s", uuid));
        }

        api = new API(uuid);


        // Init UI

        cvStatus = findViewById(R.id.main_card_status);
        tvStatus = findViewById(R.id.main_status_text_view);
        tvStatusInfo = findViewById(R.id.main_status_info_text_view);
        tvUserInfo = findViewById(R.id.main_user_info);
        btViewMap = findViewById(R.id.main_view_map_button);

        tvUserInfo.setText(String.format(getString(R.string.user_info_text_view), uuid));
        tvStatus.setText(getString(R.string.status_loading));
        tvStatusInfo.setText(getString(R.string.status_info_loading));
        cvStatus.setCardBackgroundColor(getColor(R.color.LightSlateGray));

    }


    public void updateStatus(int code, String msg, String status) {
        switch (code) {
            case 0:
                switch (status) {
                    case "ok":
                        tvStatus.setText(getString(R.string.status_ok));
                        tvStatusInfo.setText(getString(R.string.status_info_ok));
                        cvStatus.setCardBackgroundColor(getColor(R.color.BlueViolet));
                        break;

                    case "emergency":
                        tvStatus.setText(getString(R.string.status_emergency));
                        tvStatusInfo.setText(getString(R.string.status_info_emergency));
                        cvStatus.setCardBackgroundColor(getColor(R.color.OrangeRed));
                        break;

                    case "offline":
                        tvStatus.setText(getString(R.string.status_offline));
                        tvStatusInfo.setText(getString(R.string.status_info_offline));
                        cvStatus.setCardBackgroundColor(getColor(R.color.LightSlateGray));
                        break;
                }
                break;

            case 1:
                // invalid uuid
                break;

            case -1:
                tvStatus.setText(getString(R.string.status_network_err));
                tvStatusInfo.setText(msg);
                cvStatus.setCardBackgroundColor(getColor(R.color.LightSlateGray));
                break;

        }

    }

}