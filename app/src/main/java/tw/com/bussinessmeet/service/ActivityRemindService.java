package tw.com.bussinessmeet.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.bussinessmeet.bean.ActivityRemindBean;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.ResponseBody;

import java.util.List;

public interface ActivityRemindService {
    String baseRoute = "activityremind/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<ActivityRemindBean>>> search(@Body ActivityRemindBean activityRemindBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<ActivityRemindBean>> add(@Body ActivityRemindBean activityRemindBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<ActivityRemindBean>> update (@Body ActivityRemindBean activityRemindBean);
    @POST(baseRoute+"{activityRemindNo}/delete")
    Call<ResponseBody<Empty>> delete (@Path("activityRemindNo") Integer activityRemindNo);
}
