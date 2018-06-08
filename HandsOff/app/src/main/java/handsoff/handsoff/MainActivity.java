package handsoff.handsoff;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Locale;

//VER O PORQUE ELE ESTA DANDO QUE UMA PERMISSAO NAO SEI ACEITA!!!!
public class MainActivity extends AppCompatActivity {

    String[] PERMISSIONS = {Manifest.permission.MODIFY_PHONE_STATE, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for(String permission : PERMISSIONS){
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        permission)) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            PERMISSIONS, 1);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            PERMISSIONS, 1);
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    for (String permission : PERMISSIONS) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "No permission granted!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return;
                }

            }
        }

    }
}

