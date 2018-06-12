package handsoff.handsoff;

import android.app.Application;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TTSApp extends Application implements TextToSpeech.OnInitListener {
    private static TextToSpeech bittar;
    public TextToSpeech bittar1;
    private String spokenText1 = "Man, I Just Wanna go flex.";

    @Override
    public void onCreate() {
        super.onCreate();
        bittar = new TextToSpeech(this, this);
    }

    public static void speak(String spokenText){
        bittar.speak(spokenText, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = bittar.setLanguage(Locale.CANADA);
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                bittar.speak("Service started successfully.", TextToSpeech.QUEUE_FLUSH,null);
            }
        } else {
              return;
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
