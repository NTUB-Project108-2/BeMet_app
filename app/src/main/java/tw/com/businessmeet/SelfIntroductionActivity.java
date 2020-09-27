package tw.com.businessmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import tw.com.businessmeet.adapter.ProfileTimelineRecyclerViewAdapter;
import tw.com.businessmeet.background.NotificationService;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.AvatarHelper;
import tw.com.businessmeet.helper.BlueToothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.service.Impl.UserInformationServiceImpl;

public class SelfIntroductionActivity extends AppCompatActivity implements ProfileTimelineRecyclerViewAdapter .ClickListener {
    private TextView userName,userId;
    private ImageView avatar;
    private ImageButton goProfile;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH;
    private AvatarHelper avatarHelper;
    private BottomNavigationView menu;
    private BlueToothHelper blueToothHelper;
    private NotificationService notificationService = null;
    private Toolbar toolbar;
    private RecyclerView recyclerViewProfileTimeline;
    private UserInformationBean userInformationBean = new UserInformationBean();
    private UserInformationServiceImpl userInformationService = new UserInformationServiceImpl();
    private ProfileTimelineRecyclerViewAdapter profileTimelineRecyclerViewAdapter;
    private List<UserInformationBean> userInformationBeanList = new ArrayList<>();

//    private AsyncTasKHelper.OnResponseListener<String, UserInformationBean> userInfoResponseListener = new AsyncTasKHelper.OnResponseListener<String, UserInformationBean>() {
//
//
//        @Override
//        public Call<ResponseBody<UserInformationBean>> request(String... userId) {
//            return userInformationService.getById(userId[0]);
//        }
//
//        @Override
//        public void onSuccess(UserInformationBean userInformationBean) {
//            userName.append(userInformationBean.getName());
//            position.append(userInformationBean.getProfession());
//            avatar.setImageBitmap(avatarHelper.getImageResource(userInformationBean.getAvatar()));
//        }
//
//        @Override
//        public void onFail(int status,String message) {
//        }
//    };

//    private AsyncTasKHelper.OnResponseListener<UserInformationBean, List<UserInformationBean>> searchResponseListener =
//            new AsyncTasKHelper.OnResponseListener<UserInformationBean, List<UserInformationBean>>() {
//                @Override
//                public Call<ResponseBody<List<UserInformationBean>>> request(UserInformationBean... userInformationBeans) {
//
//                    return userInformationService.search(userInformationBean[0]);
//                }
//
//                @Override
//                public void onSuccess(List<UserInformationBean> userInformationBeanList) {
//                    Log.e("MatchedBean","success");
//                    for(UserInformationBean userInformationBean : userInformationBeanList) {
//                        AsyncTasKHelper.execute(getByIdResponseListener,userInformationBean.getUserId());
//                        Log.e("MatchedBean", String.valueOf(userInformationBean));
//                        //Log.e("MatchedBean", String.valueOf(matchedBean.getBlueTooth()));
//                    }
//                }
//
//                @Override
//                public void onFail(int status, String message) {
//
//                }
//            };

    private AsyncTasKHelper.OnResponseListener<String,UserInformationBean> getByIdResponseListener =
            new AsyncTasKHelper.OnResponseListener<String,UserInformationBean>() {
                @Override
                public Call<ResponseBody<UserInformationBean>> request(String... blueTooth) {

                    return userInformationService.getById(blueTooth[0]);
                }

                @Override
                public void onSuccess(UserInformationBean userInformationBean) {
                    profileTimelineRecyclerViewAdapter.dataInsert(userInformationBean);
                }

                @Override
                public void onFail(int status, String message) {

                }
            };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_introduction);
        userName = (TextView) findViewById(R.id.profile_name);
        userId = (TextView) findViewById(R.id.user_id);
        avatar = (ImageView)findViewById(R.id.edit_person_photo);
        goProfile = (ImageButton) findViewById(R.id.goProfile);
        goProfile.setOnClickListener(goProfileClick);
        menu = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        recyclerViewProfileTimeline = findViewById(R.id.profile_timeline_view);
        //this.personal = personal;
        blueToothHelper = new BlueToothHelper(this);
        avatarHelper = new AvatarHelper();
        Intent intent = new Intent(this, NotificationService.class);
        this.startService(intent);
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbarMenu
        toolbar.inflateMenu(R.menu.toolbarmenu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }

        });

        openDB();
        searchUserInformation();

        //bottomNavigationView
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //searchUserInformation();
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        Menu BVMenu = bottomNavigationView.getMenu();
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像
        AvatarHelper avatarHelper = new AvatarHelper();
        createRecyclerViewProfileTimeline();
        Log.d("seedmess", "ness");
        Cursor result = userInformationDAO.getById(blueToothHelper.getUserId());
        Log.e("result", String.valueOf(result));

        MenuItem userItem = BVMenu.findItem(R.id.menu_home);
        Bitmap myPhoto = avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
        userItem.setIcon(new BitmapDrawable(getResources(), myPhoto));

    }


    private void openDB(){
        Log.d("add","openDB");
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
    }

    public void searchUserInformation(){

        Cursor result = userInformationDAO.getById(blueToothHelper.getUserId());
        Log.d("result",String.valueOf(result.getColumnCount()));

        for(int i = 0; i<result.getColumnCount(); i++){
            Log.d("result",result.getColumnName(i));
        }


        if (result.moveToFirst()) {
            userName.append(result.getString(result.getColumnIndex("name")));
            userId.append(result.getString(result.getColumnIndex("user_id")));
            avatar.setImageBitmap(avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar"))));
        }
        result.close();



    }

    public View.OnClickListener goProfileClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeToSelfIntroductionActivityPage();
        }
    };

    public void changeToSelfIntroductionActivityPage() {
        Intent intent = new Intent();
        intent.setClass(SelfIntroductionActivity.this, SelfInformationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId",getIntent().getStringExtra("userId"));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void createRecyclerViewProfileTimeline() {
        recyclerViewProfileTimeline.setLayoutManager(new LinearLayoutManager(this));
        profileTimelineRecyclerViewAdapter = new ProfileTimelineRecyclerViewAdapter(this, this.userInformationBeanList);
        profileTimelineRecyclerViewAdapter.setClickListener(this);
        recyclerViewProfileTimeline.setAdapter(profileTimelineRecyclerViewAdapter);

    }

    public void onClick(View view, int position){
        Intent intent = new Intent();
        intent.setClass(this,EventActivity.class); //改到活動事件內容
        Bundle bundle = new Bundle();
        bundle.putString("blueToothAddress",profileTimelineRecyclerViewAdapter.getUserInformation(position).getBluetooth());
        intent.putExtras(bundle);
        startActivity(intent);

    }



    //Perform ItemSelectedListener
    BottomNavigationView.OnNavigationItemSelectedListener navListener =
            (new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.menu_home:
                    return true;
                case R.id.menu_search:
                    startActivity(new Intent(getApplicationContext()
                            ,SearchActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.menu_friends:
                    //menuItem.setIcon(R.drawable.ic_people_blue_24dp);
                    startActivity(new Intent(getApplicationContext()
                            ,FriendsActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        }


    });


}
