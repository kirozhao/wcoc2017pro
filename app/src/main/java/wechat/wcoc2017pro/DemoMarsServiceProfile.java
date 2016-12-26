package wechat.wcoc2017pro;

import com.tencent.mars.sample.wrapper.service.MarsServiceProfile;


public class DemoMarsServiceProfile implements MarsServiceProfile {

    // public static final String SERVER_HOST = "localhost";
    public static final String SERVER_HOST = "wcoc2017pro.wemobiledev.info";

    @Override
    public short magic() {
        return 0x2333;
    }

    @Override
    public short productID() {
        return 0;
    }

    @Override
    public String longLinkHost() {
        return SERVER_HOST;
    }

    @Override
    public int[] longLinkPorts() {
        return new int[]{8001};
    }

    @Override
    public int shortLinkPort() {
        return 8000;
    }
}
