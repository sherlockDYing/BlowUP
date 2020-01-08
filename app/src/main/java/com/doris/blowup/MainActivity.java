package com.doris.blowup;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

//
// Created by dingying on 2020/1/8.
//
public class MainActivity extends Activity implements View.OnClickListener{
    private Button windmillBtn;
    private Button dandelionBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
        requestPower();

    }

    private void initLayout(){
        windmillBtn = findViewById(R.id.windmill_btn);
        dandelionBtn = findViewById(R.id.dandelion_btn);
        windmillBtn.setOnClickListener(this);
        dandelionBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.windmill_btn :
                Intent intent = new Intent(MainActivity.this,WindmillActivity.class);
                startActivity(intent);
                break;
            case R.id.dandelion_btn :
                Intent intent1 = new Intent(MainActivity.this,DandelionActivity.class);
                startActivity(intent1);
                break;
            default:
                 break;
        }
    }

    public void requestPower() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限.它在用户选择"不再询问"的情况下返回false
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO,}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
