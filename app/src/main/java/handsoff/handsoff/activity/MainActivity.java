package handsoff.handsoff.activity;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import handsoff.handsoff.R;
import handsoff.handsoff.service.PhoneCallInterceptor;
import handsoff.handsoff.service.SMSInterceptor;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {

    String[] PERMISSIONS = {Manifest.permission.MODIFY_PHONE_STATE, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS};

    PhoneCallInterceptor phoneCallInterceptor;
    SMSInterceptor smsInterceptor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneCallInterceptor = new PhoneCallInterceptor();
        smsInterceptor = new SMSInterceptor();

        List<String> neededPermissions = new ArrayList<>();

        for (String permission : PERMISSIONS)
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                neededPermissions.add(permission);

        requestPermissions(neededPermissions.toArray(new String[neededPermissions.size()]), 1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter phoneCallIntentFilter = new IntentFilter("android.intent.action.PHONE_STATE");
        IntentFilter smsIntentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(phoneCallInterceptor, phoneCallIntentFilter);
        registerReceiver(smsInterceptor, smsIntentFilter);
    }

    //onPause está sendo chamado imediatamente, removendo os receivers de ligação e SMS
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(phoneCallInterceptor);
//        unregisterReceiver(smsInterceptor);
//    }
}
