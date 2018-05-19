package handsoff.handsoff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class InterceptCall extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Toast.makeText(context, "O Viado do Fernando Lapa está te ligando!", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
