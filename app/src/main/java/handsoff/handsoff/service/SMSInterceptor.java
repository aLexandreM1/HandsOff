package handsoff.handsoff.service;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SMSInterceptor extends BroadcastReceiver {
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
        for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
            String messageBody = smsMessage.getMessageBody();
            String phone = smsMessage.getOriginatingAddress();
            String contactName = getContactName(context, phone);

            //Separando números com espaços para que o TTS fale os números individualmente
            StringBuilder auxAppender = new StringBuilder();
            for (int i = 0; i < phone.length(); i++)
                auxAppender.append(phone.charAt(i)).append(" ");

            Toast.makeText(context, "Mensagem de " + (contactName != null ? contactName : phone), Toast.LENGTH_SHORT).show();
            //TODO: resolver problema de textos com acentuação que estão sendo pulados
            TTSApp.speak("MENSAGEM DE " + (contactName != null ? contactName : auxAppender.toString()) + ". MENSAGEM." + messageBody.toUpperCase());
        }
    }
}