package wechat.wcoc2017pro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.mars.sample.wrapper.remote.MarsServiceProxy;
import com.tencent.mars.sample.wrapper.remote.PushMessage;
import com.tencent.mars.sample.wrapper.remote.PushMessageHandler;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    public static final int UPDATE_COUNT_CMD_ID = 100;
    private TextView visitTextView;
    private TextView bannerTextView;
    private Button startStopButton;
    private boolean isPause = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        visitTextView = (TextView) findViewById(R.id.text_visit);
        bannerTextView = (TextView) findViewById(R.id.text_banner);
        startStopButton = (Button) findViewById(R.id.btn_start_stop);
        startStopButton.setText(isPause ? "START": "PAUSE");
        startStopButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPause = !isPause;
                startStopButton.setText(isPause ? "START": "PAUSE");
            }
        });

        final HelloTask task = new HelloTask();
        bannerTextView.setClickable(true);
        bannerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MarsServiceProxy.send(task);
            }
        });

        task.setOnTaskFinishCallback(new HelloTask.OnTaskFinishCallback() {
            @Override
            public void onOK(final HelloTask task) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bannerTextView.setText(task.getBannerText());
                    }
                });
            }
        });

        MarsServiceProxy.setOnPushMessageListener(UPDATE_COUNT_CMD_ID, new PushMessageHandler() {
            @Override
            public void process(final PushMessage pushMessage) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (isPause) {
                                return;
                            }
                            final String visitCount = new String(pushMessage.buffer, "utf-8");
                            visitTextView.setText(visitCount);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // MarsServiceProxy.setForegroundMode(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // MarsServiceProxy.setForegroundMode(false);
    }

    @Override
    protected void onDestroy() {
        MarsServiceProxy.setOnPushMessageListener(UPDATE_COUNT_CMD_ID, null);
        super.onDestroy();
    }

}
