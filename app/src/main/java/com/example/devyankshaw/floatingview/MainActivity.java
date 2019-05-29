package com.example.devyankshaw.floatingview;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnFloating;

    private static final int ID_DRAW_OVER_OTHER_APPS_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFloating = findViewById(R.id.btnFloating);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this)){
            /*If the android version is >= API 23/Marshmallow && if the settings of the device doesn't allow/gives permission
                to overlay the widget to others then this if blocks executes
             */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            Uri.parse("package:" + getPackageName());
            startActivityForResult(intent, ID_DRAW_OVER_OTHER_APPS_PERMISSION);
            //startActivity(intent);
        }else {
            floatTheViewOnTheScreen();
        }
    }

    private void floatTheViewOnTheScreen(){

        btnFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, FloatingViewService.class));
                finish();//When service is started then we don't need the MainActivity anymore so finsh() is used which will close it
            }
        });
    }


}
