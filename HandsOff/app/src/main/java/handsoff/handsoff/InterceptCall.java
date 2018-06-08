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
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.android.internal.telephony.ITelephony;
import java.lang.reflect.Method;

public class InterceptCall extends BroadcastReceiver {
    private static final String TAG = null;
    String incommingNumber;
    final String Desconhecido = " que não está cadastrado em contatos te mandou uma mensagem.";
    final String Conhecido =  " te mandou uma mensagem.";

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

        Intent intent1 = new Intent();
        intent1.setClass(context,TTS.class);
        context.startActivity(intent1);

/*        TextToSpeech bittar = new TextToSpeech(context, (TextToSpeech.OnInitListener)this);
        bittar.setLanguage(Locale.CANADA);
        String msg = "THIS IS A TERRIBLE TEST"; Didnt Work só pra constar. */

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
                    if (contactName != null) {
                        Toast.makeText(context, contactName + Conhecido, Toast.LENGTH_LONG).show();
//                        String N1 = "HUEHUEHUEHUE";
//                        Intent intent1 = new Intent();
//                        intent1.setClass(context,TTS.class);
//                        intent1.putExtra("N1", N1 );
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                        context.startActivity(intent1);
                        //Toast.makeText(context, messageBody, Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context, contactName + Desconhecido, Toast.LENGTH_LONG).show();
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
                        /*for(int a=0;a<1000;a++){
                            Log.v(TAG, "Delay para pegar o numero");
                        }*/
                        if (contactName == null) {
                            Toast.makeText(context, contactName + " Está te ligando.", Toast.LENGTH_SHORT).show();
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