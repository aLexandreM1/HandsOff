package handsoff.handsoff;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.telecom.Call;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class InterceptCall extends BroadcastReceiver {
    private static final String TAG = null;
    String incommingNumber;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Contactables.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();


        if (null == bundle)
            return;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);


        try {
            //************** RECEBENDO MENSAGEM *************************************

            if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    String messageBody = smsMessage.getMessageBody();
                    String phone = smsMessage.getOriginatingAddress();
                    String contactName = getContactName(context, phone);

                    Log.v(TAG, messageBody);
                    Log.v(TAG, contactName);

                    if (contactName.equals(null)) {
                        Toast.makeText(context, contactName + " te mandou uma mensagem.", Toast.LENGTH_LONG).show();
                        //Toast.makeText(context, messageBody, Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context, contactName + " te mandou uma mensagem.", Toast.LENGTH_LONG).show();
                        //Toast.makeText(context, messageBody, Toast.LENGTH_LONG).show();
                    }

                }
            }

            //************************ //=// ****************************************

            //************** REJEITA A LIGACAO **************************************
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm.getCallState() == TelephonyManager.CALL_STATE_RINGING) {
                Log.v(TAG, "Get getTeleService...");
                Class c = Class.forName(tm.getClass().getName());
                Method m = c.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                com.android.internal.telephony.ITelephony telephonyService = (ITelephony) m.invoke(tm);
                Bundle b = intent.getExtras();
                incommingNumber = b.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.v(TAG, incommingNumber);

                telephonyService = (ITelephony) m.invoke(tm);
                telephonyService.silenceRinger();
                telephonyService.endCall();
                Log.v(TAG, "BYE BYE BYE");

                //**************** Mandar mensagem após o fim da ligação ********************

                String smsNumber = incommingNumber;
                String smsText = "Estou ocupado no momento, poderia ligar mais tarde por favor. Obrigado!";

                android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
                smsManager.sendTextMessage(smsNumber, null, smsText, null, null);

                //**************Pega numero de quem esta ligando*****************************

                tm.listen(new PhoneStateListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                    @Override
                    public void onCallStateChanged(int state, String incomingNumber) {
                        super.onCallStateChanged(state, incomingNumber);
                        String contactName = getContactName(context, incomingNumber);
                        if (contactName == null) {
                            Toast.makeText(context, incomingNumber + " Está te ligando.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, contactName + " Está te ligando.", Toast.LENGTH_SHORT).show();
                        }
                        //System.out.println("incomingNumber : "+incomingNumber);

                    }
                }, PhoneStateListener.LISTEN_CALL_STATE);
                //***************************************************************************
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}