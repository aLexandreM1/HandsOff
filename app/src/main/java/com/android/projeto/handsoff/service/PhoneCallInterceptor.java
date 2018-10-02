package com.android.projeto.handsoff.service;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PhoneCallInterceptor extends BroadcastReceiver {
    private String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Contactables.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        if (cursor.moveToFirst()) {
            phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            cursor.close();
            return phoneNumber;
        } else {
            cursor.close();
            return null;
        }
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            Method m = Class.forName(tm.getClass().getName()).getDeclaredMethod("getITelephony");
            m.setAccessible(true);

            ITelephony telephonyService = (ITelephony) m.invoke(tm);
            telephonyService.silenceRinger();
            telephonyService.endCall();

            Bundle b = intent.getExtras();
            String incomingNumber = b.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            String contactName = getContactName(context, incomingNumber);

            StringBuilder auxAppender = new StringBuilder();
            for (int i = 0; i < incomingNumber.length(); i++)
                auxAppender.append(incomingNumber.charAt(i)).append(" ");

            Toast.makeText(context, "Ligação de " + (contactName != null ? contactName : incomingNumber) + " rejeitada.", Toast.LENGTH_SHORT).show();
            TTSApp.speak("Ligação de " + (contactName != null ? contactName : auxAppender.toString()) + " rejeitada.");

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(incomingNumber, null, "Estou ocupado. Por gentileza, retorne mais tarde. Grato.", null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}