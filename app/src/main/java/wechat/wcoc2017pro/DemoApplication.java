package wechat.wcoc2017pro;

import android.app.Application;

import com.tencent.mars.app.AppLogic;
import com.tencent.mars.sample.wrapper.remote.MarsServiceProxy;
import com.tencent.mars.sample.wrapper.service.MarsServiceNative;
import com.tencent.mars.sample.wrapper.service.MarsServiceProfile;
import com.tencent.mars.sample.wrapper.service.MarsServiceProfileFactory;


public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        System.loadLibrary("stlport_shared");

        // init mars running service
        MarsServiceNative.setProfileFactory(new MarsServiceProfileFactory() {
            @Override
            public MarsServiceProfile createMarsServiceProfile() {
                return new DemoMarsServiceProfile();
            }
        });

        // init mars request proxy
        MarsServiceProxy.init(getApplicationContext(), getMainLooper(), getPackageName());
        MarsServiceProxy.inst.accountInfo = new AppLogic.AccountInfo(100000, "anonymous");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
