package tw.com.businessmeet.service.Impl;

import androidx.annotation.Nullable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.service.FriendService;

import static tw.com.businessmeet.network.RetrofitConfig.retrofit;

public class FriendServiceImpl {
    private static FriendService FriendApi = retrofit.create(FriendService.class);

    @Nullable
    public static Call<ResponseBody<List<FriendBean>>> search(FriendBean friendBean) {
        return FriendApi.search(friendBean);
    }

    @Nullable
    public static Call<ResponseBody<List<FriendBean>>> searchInviteList(FriendBean friendBean) {
        return FriendApi.searchInviteList(friendBean);
    }

    @Nullable
    public static Call<ResponseBody<FriendBean>> add(FriendBean friendBean) {
        return FriendApi.add(friendBean);
    }

    @Nullable
    public static Call<ResponseBody<FriendBean>> update(FriendBean friendBean) {
        return FriendApi.update(friendBean);
    }

    @Nullable
    public static Call<ResponseBody<Empty>> delete(Integer friendNo) {
        return FriendApi.delete(friendNo);
    }

    @Nullable
    public static Call<ResponseBody<List<FriendBean>>> searchInviteNotification() {
        return FriendApi.searchInviteNotification();
    }

    @Nullable
    public static Call<ResponseBody<FriendBean>> createInviteNotification(@Body FriendBean friendBean) {
        return FriendApi.createInviteNotification(friendBean);
    }
}
