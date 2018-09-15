package handsoff.handsoff.service;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.android.internal.telephony.ITelephony;
import java.lang.reflect.Method;

public class InterceptCall extends BroadcastReceiver {
    private static final String TAG = "";
    String incommingNumber;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getContactName(Context context, String phoneNumber) {

        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Contactables.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = phoneNumber;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }


    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle == null)
            return;
        try {
            //*********************** RECEBENDO MENSAGEM ******************************

            if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    String messageBody = smsMessage.getMessageBody();
                    String phone = smsMessage.getOriginatingAddress();
                    String contactName = getContactName(context, phone);
                    if (contactName != null) {
                        Toast.makeText(context, contactName + " te enviou uma mensagem", Toast.LENGTH_LONG).show();
                        TTSApp.speak(contactName + " te enviou uma mensagem" + messageBody);
                    }else{
                        Toast.makeText(context, contactName + " te enviou uma mensagem", Toast.LENGTH_LONG).show();
                        TTSApp.speak(contactName + "te enviou uma mensagem:" + messageBody);
                    }

                }
            }

            //************************ //=// ****************************************

            //********************** REJEITA A LIGACAO ******************************

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm.getCallState() == TelephonyManager.CALL_STATE_RINGING) {
                Log.v(TAG, "Get getTeleService...");
                Class c = Class.forName(tm.getClass().getName());
                Method m = c.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                com.android.internal.telephony.ITelephony telephonyService = (ITelephony) m.invoke(tm);
                Bundle b = intent.getExtras();
                incommingNumber = b.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

                telephonyService = (ITelephony) m.invoke(tm);
                telephonyService.silenceRinger();
                telephonyService.endCall();

                //**************** Mandar mensagem após o fim da ligação ********************

                String smsNumber = incommingNumber;
                String smsText = "Estou ocupado no momento, poderia ligar mais tarde por favor. Obrigado!";

                android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
                smsManager.sendTextMessage(smsNumber, null, smsText, null, null);

                //***************** Pega numero de quem esta ligando *************************

                tm.listen(new PhoneStateListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                    @Override
                    public void onCallStateChanged(int state, String incomingNumber) {
                        super.onCallStateChanged(state, incomingNumber);
                        String contactName = getContactName(context, incomingNumber);
                        if (contactName != null) {
                            Toast.makeText(context, contactName + " Está te ligando.", Toast.LENGTH_SHORT).show();
                            TTSApp.speak(contactName + "está te ligando.");
                        } else {
                            Toast.makeText(context, contactName + "Está te ligando.", Toast.LENGTH_SHORT).show();
                            TTSApp.speak(contactName + "está te ligando.");
                        }

                    }
                }, PhoneStateListener.LISTEN_CALL_STATE);

                //**********************************//=//************************************
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}