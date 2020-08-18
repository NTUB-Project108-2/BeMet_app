package tw.com.businessmeet.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.FriendRemarkBean;
import tw.com.businessmeet.bean.ResponseBody;

import java.util.List;

public interface FriendRemarkService {
    String baseRoute = "friendremark/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<FriendRemarkBean>>> search(@Body FriendRemarkBean friendRemarkBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<FriendRemarkBean>> add(@Body FriendRemarkBean friendRemarkBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<FriendRemarkBean>> update (@Body FriendRemarkBean friendRemarkBean);
    @POST(baseRoute+"{friendRemarkNo}/delete")
    Call<ResponseBody<Empty>> delete (@Path("friendRemarkNo") Integer friendRemarkNo);
}