package handsoff.handsoff;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class InterceptCall extends BroadcastReceiver {
    private static final String TAG = null;
    String incommingNumber;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if(null == bundle)
            return;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);




        try {
            //************** REJEITA A LIGACAO **************************************
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Log.v(TAG, "Get getTeleService...");
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            com.android.internal.telephony.ITelephony telephonyService = (ITelephony) m.invoke(tm);
            Bundle b = intent.getExtras();
            incommingNumber = b.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.v(TAG,incommingNumber );

            telephonyService = (ITelephony) m.invoke(tm);
            telephonyService.silenceRinger();
            telephonyService.endCall();
            Log.v(TAG,"BYE BYE BYE" );
            System.out.println("REJEITANDO A LIGACAO ********************************************* ");
            //Toast.makeText(context, "NAO ATENDER", Toast.LENGTH_SHORT).show();


            //*****************************************************************************


            //**************Pega numero de quem esta ligando*****************************

            tm.listen(new PhoneStateListener(){
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    super.onCallStateChanged(state, incomingNumber);
                    Toast.makeText(context, "incomingNumber : "+incomingNumber, Toast.LENGTH_SHORT).show();
                    System.out.println("incomingNumber : "+incomingNumber);
                }
            },PhoneStateListener.LISTEN_CALL_STATE);
            //***************************************************************************

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
