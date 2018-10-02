package com.android.projeto.handsoff.activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.projeto.handsoff.DAO.UsuarioDAO;
import com.android.projeto.handsoff.R;
import com.android.projeto.handsoff.fragments.SettingFragment;
import com.android.projeto.handsoff.fragments.StatusFragment;
import com.android.projeto.handsoff.service.PhoneCallInterceptor;
import com.android.projeto.handsoff.service.SMSInterceptor;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {
    String[] PERMISSIONS = {Manifest.permission.MODIFY_PHONE_STATE, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS};
    PhoneCallInterceptor phoneCallInterceptor;
    SMSInterceptor smsInterceptor;

    UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //AQUI VAI O FRAGMENT, ESSE LOGIN VAI PRA OUTRA JAVA CLASS.


        phoneCallInterceptor = new PhoneCallInterceptor();
        smsInterceptor = new SMSInterceptor();

        List<String> neededPermissions = new ArrayList<>();

        for (String permission : PERMISSIONS)
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                neededPermissions.add(permission);

        requestPermissions(neededPermissions.toArray(new String[neededPermissions.size()]), 1);

        /*=========================================================================================*/

        final TabLayout tabLayout = findViewById(R.id.tabLayoutContainer);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        tabLayout.addTab(tabLayout.newTab().setText("STATUS").setIcon(R.mipmap.ic_phone_missed));
        tabLayout.addTab(tabLayout.newTab().setText("CONFIGURAÇÃO").setIcon(R.mipmap.ic_settings));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        replaceFragment(new StatusFragment());
                        break;
                    case 1:
                        replaceFragment(new SettingFragment());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(0).getIcon().clearColorFilter();
                tab.getIcon().clearColorFilter();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

            private void replaceFragment(Fragment fragment) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.FrameContainer, fragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
            }
        });

        tabLayout.getTabAt(1).select();
        tabLayout.getTabAt(0).select();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.signOut:
                usuarioDAO.onSignOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
/*    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(phoneCallInterceptor);
        unregisterReceiver(smsInterceptor);
    }*/
}
