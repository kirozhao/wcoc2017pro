package wechat.wcoc2017pro;

import com.google.gson.JsonObject;
import com.tencent.mars.sample.wrapper.TaskProperty;
import com.tencent.mars.sample.wrapper.remote.JsonMarsTaskWrapper;


@TaskProperty(host = DemoMarsServiceProfile.SERVER_HOST, path = "/hello")
public class HelloTask extends JsonMarsTaskWrapper {

    private OnTaskFinishCallback onTaskFinishCallback;

    public HelloTask() {
        super(new JsonObject(), new JsonObject());
    }

    public void setOnTaskFinishCallback(OnTaskFinishCallback onTaskFinishCallback) {
        this.onTaskFinishCallback = onTaskFinishCallback;
    }

    @Override
    public void onPreEncode(JsonObject request) {
        request.addProperty("user", "kiro");
        request.addProperty("says", "hello mars server!!");
    }

    @Override
    public void onPostDecode(JsonObject response) {
        if (onTaskFinishCallback != null) {
            onTaskFinishCallback.onOK(this);
        }
    }

    public String getBannerText() {
        return response.get("banner").getAsString();
    }

    interface OnTaskFinishCallback {
        void onOK(HelloTask task);
    }
}
