package com.android.projeto.handsoff.activity;

import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.projeto.handsoff.DAO.UsuarioDAO;
import com.android.projeto.handsoff.R;
import com.android.projeto.handsoff.fragments.SettingFragment;
import com.android.projeto.handsoff.fragments.StatusFragment;
import com.android.projeto.handsoff.service.PhoneCallInterceptor;
import com.android.projeto.handsoff.service.SMSInterceptor;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {
    PhoneCallInterceptor phoneCallInterceptor;
    SMSInterceptor smsInterceptor;

    UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //AQUI VAI O FRAGMENT, ESSE LOGIN VAI PRA OUTRA JAVA CLASS.


        phoneCallInterceptor = new PhoneCallInterceptor();
        smsInterceptor = new SMSInterceptor();

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
