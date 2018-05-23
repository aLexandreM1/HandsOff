package handsoff.handsoff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Old_MainActivity extends AppCompatActivity implements View.OnClickListener {

    //button objects
    private Button buttonStart;
    private Button buttonStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting buttons from xml
        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);

        //attaching onclicklistener to buttons
        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonStart) {
            //starting service
            startService(new Intent(this, HandsOffService.class));
        } else if (view == buttonStop) {
            //stopping service
            stopService(new Intent(this, HandsOffService.class));
        }
    }
}
