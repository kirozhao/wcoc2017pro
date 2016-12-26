package wechat.wcoc2017pro;

import android.app.Application;
import android.os.Environment;

import com.tencent.mars.app.AppLogic;
import com.tencent.mars.sample.wrapper.remote.MarsServiceProxy;
import com.tencent.mars.sample.wrapper.service.MarsServiceNative;
import com.tencent.mars.sample.wrapper.service.MarsServiceProfile;
import com.tencent.mars.sample.wrapper.service.MarsServiceProfileFactory;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;


public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        System.loadLibrary("stlport_shared");
        System.loadLibrary("marsxlog");

        // init xlog
        final String logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wcoc2017pro/log";
        Xlog.appenderOpen(Xlog.LEVEL_DEBUG, Xlog.AppednerModeAsync, "", logPath, "wcoc2017pro");
        Xlog.setConsoleLogOpen(true);

        Log.setLogImp(new Xlog());

        // init mars running service
        MarsServiceNative.setProfileFactory(new MarsServiceProfileFactory() {
            @Override
            public MarsServiceProfile createMarsServiceProfile() {
                return new DemoMarsServiceProfile();
            }
        });

        // init mars request proxy
        MarsServiceProxy.init(getApplicationContext(), getMainLooper(), getPackageName());
        MarsServiceProxy.inst.accountInfo = new AppLogic.AccountInfo(100000, "user");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        Log.appenderClose();
    }
}
