package com.android.projeto.handsoff.service;

import android.app.Application;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;

import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class TTSApp extends Application implements TextToSpeech.OnInitListener {
    private static TextToSpeech bittar;

    @Override
    public void onCreate() {
        super.onCreate();
        bittar = new TextToSpeech(this, this);
    }

    public static void speak(String spokenText) {
        bittar.speak(spokenText, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Locale localeBR = new Locale("pt", "br");
            int result = bittar.setLanguage(localeBR);

            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED){
                //bittar.speak("Serviço iniciado com sucesso.", TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}