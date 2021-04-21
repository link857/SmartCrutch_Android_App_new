package deltazero.smartcrutch.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import deltazero.smartcrutch.R;
import deltazero.smartcrutch.core.API;

public class MainActivity extends AppCompatActivity {

//    public static final String API_BUNDLE = "deltazero.smartcrutch.api";
//    public final API api = new API();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LoginActivity.class);
//        intent.putExtra(API_BUNDLE, api);
        startActivity(intent);
    }
}