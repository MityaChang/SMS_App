package sg.edu.rp.c346.id19034275.smsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button button, btnSendMsg;
    ;
    private BroadcastReceiver br;
    EditText etTo, etContent;
    String[] setNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        etTo = findViewById(R.id.editText);
        etContent = findViewById(R.id.editText2);
        br = new MessageReceiver();
        btnSendMsg = findViewById(R.id.buttonSendMsg);
        if (etTo.getText().toString().contains(",")) {
            setNo = etTo.getText().toString().split(",");
        }

        checkPermission();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br, filter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setNo.length == 0) {
                    if (!etTo.getText().toString().trim().isEmpty()) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(etTo.getText().toString().trim(), null, etContent.getText().toString(), null, null);
                        Toast.makeText(MainActivity.this, "Message Sent", Toast.LENGTH_LONG).show();
                        etTo.setText("");
                        etContent.setText("");
                    } else {
                        Toast.makeText(MainActivity.this, "The address have not been added", Toast.LENGTH_LONG).show();
                    }
                } else {
                    for (String i : setNo) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(i, null, etContent.getText().toString(), null, null);
                        Toast.makeText(MainActivity.this, "Message Sent", Toast.LENGTH_LONG).show();
                        etTo.setText("");
                        etContent.setText("");
                    }
                }
            }
//                String toReceiver = etTo.getText().toString();
//                String message = etContent.getText().toString();
//
//                SmsManager smsManager = SmsManager.getDefault();
//                smsManager.sendTextMessage(toReceiver, null, message, null, null);
//                Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
//                etContent.setText("");
//            }
        });


        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setNo.length == 0) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setType("vnd.android-dir/mms-sms");
                    intent.putExtra("address", etTo.getText().toString().trim());
                    intent.putExtra("sms_body", etContent.getText().toString());
                    startActivity(intent);
                } else {
                    for (String i : setNo) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setType("vnd.android-dir/mms-sms");
                        intent.putExtra("address", i);
                        intent.putExtra("sms_body", etContent.getText().toString());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(br);
    }
}

