package com.jiat.usertravellanka;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class broadcastReciver extends AppCompatActivity {

    String telNr;
    EditText message;
    Button btnsendSMS;
    private final int MY_PERMISSION_REQUEST_SEND_SMS =1;

    private final String SENT="SMS_SENT";
    private final String DELIVERED="SMS_DELIVERED";

    PendingIntent sendPI,deliveredPI;
    BroadcastReceiver smsSentReciever, smsDeliveredReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_reciver);

        getSupportActionBar().setTitle("Customers Feedback");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        ImageView backimg = findViewById(R.id.imageView12);
//        Glide.with(getApplicationContext()).load(R.drawable.previous).override(39,39).into(backimg);
//        findViewById(R.id.imageView12).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(broadcastReciver.this,MainActivity.class);
//                startActivity(intent);
//            }
//        });

        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.cotactus).override(400,202).into(imageView);

        message=(EditText) findViewById(R.id.messageSMS);
        btnsendSMS=(Button) findViewById(R.id.sendSMS);

        sendPI=PendingIntent.getBroadcast(broadcastReciver.this,0,new Intent(SENT),0);
        deliveredPI=PendingIntent.getBroadcast(broadcastReciver.this,0,new Intent(DELIVERED),0);


        btnsendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String castedMessage=message.getText().toString();
                String telNr="0702269286";



                if(ContextCompat.checkSelfPermission(broadcastReciver.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(broadcastReciver.this,new String[]{Manifest.permission.SEND_SMS},MY_PERMISSION_REQUEST_SEND_SMS);
                }
                else{
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(telNr,null,castedMessage,sendPI,deliveredPI);
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause(){
        super.onPause();
        unregisterReceiver(smsSentReciever);
        unregisterReceiver(smsDeliveredReciever);
    }


    @Override
    protected void onResume(){
        super.onResume();
        smsSentReciever =new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context,"SMS Sent Successfully",Toast.LENGTH_SHORT).show();
                        message.setText("");
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context,"No Service",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context,"Null PDU",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context,"Airplane Mode / Radio Off",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        smsDeliveredReciever=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context,"SMS Delivered",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_CANCELLED:
                        Toast.makeText(context,"SMS Not Delivered",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        registerReceiver(smsSentReciever,new IntentFilter(SENT));
        registerReceiver(smsDeliveredReciever,new IntentFilter(DELIVERED));
    }
}