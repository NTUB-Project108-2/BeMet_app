package tw.com.businessmeet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import tw.com.businessmeet.bean.ActivityInviteBean;
import tw.com.businessmeet.bean.ActivityLabelBean;
import tw.com.businessmeet.bean.TimelineBean;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTaskHelper;
import tw.com.businessmeet.helper.DeviceHelper;
import tw.com.businessmeet.service.Impl.TimelineServiceImpl;

public class EventActivity extends AppCompatActivity {
    private Activity activity = this;
    private Toolbar toolbar;
    private TextView event, eventDate, eventTime, eventLocation, eventParticipant, addEventMemo;
    private ChipGroup eventTag;
    private Boolean meet = true;
    private String friendId;
    private TimelineBean activityBean;
    private ImageView tagIcon, participantIcon;
    private UserInformationDAO userInformationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event);

        Integer timelineNo = Integer.parseInt(getIntent().getStringExtra("timelineNo"));

        event = findViewById(R.id.event);
        eventDate = findViewById(R.id.event_date);
        eventTime = findViewById(R.id.event_time);
        eventLocation = findViewById(R.id.event_location);
        eventParticipant = findViewById(R.id.event_participant);
        eventTag = findViewById(R.id.event_label);
        addEventMemo = findViewById(R.id.add_event_memo);
        participantIcon = findViewById(R.id.participant_icon);
        tagIcon = findViewById(R.id.tag_icon);
        AsyncTaskHelper.execute(() -> TimelineServiceImpl.getById(timelineNo), timelineBean -> {
            event.setText(timelineBean.getTitle());
            eventLocation.setText(timelineBean.getPlace());
            addEventMemo.setText(timelineBean.getRemark());
            if (timelineBean.getTimelinePropertiesNo() == 1) {
                meet = false;
                activityBean = timelineBean;
                eventDate.setText(timelineBean.getStartDate());
                eventTime.setText(timelineBean.getEndDate());
                ActivityLabelBean activityLabelBean = timelineBean.getActivityLabelBean();
                String[] contentArray = activityLabelBean.getContent().split(",");
                for (String chipString : contentArray) {

                    Chip chip = new Chip(activity);
                    ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(activity, null, 0, R.style.Widget_MaterialComponents_Chip_Action);
                    chip.setChipDrawable(chipDrawable);
                    chip.setText(chipString);

                    eventTag.addView(chip);
                }
//                eventTag.setText(timelineBean.getActivityLabelBeanList().get(0).getContent());

                List<ActivityInviteBean> activityInviteBeanList = timelineBean.getActivityInviteBeanList();
//                participantAvatar.setImageBitmap(avatarHelper.getImageResource(timelineBean.getActivityInviteBeanList().get(0).getAvatar()));
                String inviteName = "";
                for (int i = 0; i < activityInviteBeanList.size(); i++) {
                    activityInviteBeanList.get(i).setAvatar("");
                    if (i == 0) {
                        inviteName = activityInviteBeanList.get(i).getUserName();
                        String userName = DeviceHelper.getUserName(this);
                        if (!inviteName.equals(userName)) {
                            View delete = toolbar.findViewById(R.id.menu_toolbar_delete);
                            View search = toolbar.findViewById(R.id.menu_toolbar_search);
                            delete.setVisibility(View.GONE);
                            search.setVisibility(View.GONE);
                        }
                    } else {
                        inviteName += "," + activityInviteBeanList.get(i).getUserName();
                    }
                }
                activityBean.setActivityInviteBeanList(activityInviteBeanList);
                eventParticipant.setText(inviteName);
            } else {
                friendId = getIntent().getStringExtra("friendId");
                tagIcon.setVisibility(View.GONE);
                eventTag.setVisibility(View.GONE);
                eventTime.setVisibility(View.GONE);
                participantIcon.setVisibility(View.GONE);
                eventParticipant.setVisibility(View.GONE);
                eventDate.setText(timelineBean.getCreateDateStr());
            }
        });

        toolbar = (Toolbar) findViewById(R.id.event_toolbar);
        toolbar.inflateMenu(R.menu.event_toolbarmenu);
        toolbar.setNavigationIcon(R.drawable.ic_cancel_16dp);  //back
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do back
                Intent intent = new Intent(activity, SelfIntroductionActivity.class);
                startActivity(intent);
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_toolbar_delete:
                        AlertDialog dialog = new AlertDialog.Builder(EventActivity.this)
                                .setTitle("刪除確認")
                                .setMessage("確定要刪除嗎?一但刪除便無法復原。")
                                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AsyncTaskHelper.execute(() -> TimelineServiceImpl.delete(timelineNo));
                                        Intent intent = new Intent();
                                        if (meet) {
                                            intent.setClass(EventActivity.this, FriendsTimelineActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("friendId", friendId);
                                            intent.putExtras(bundle);
                                        } else {
                                            intent.setClass(EventActivity.this, SelfIntroductionActivity.class);
                                        }
                                        startActivity(intent);
                                    }
                                }).setNegativeButton("取消", null).create();
                        dialog.show();
                        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.GRAY);


                        break;
                    case R.id.menu_toolbar_search:
                        Intent intent = new Intent();
                        intent.setClass(EventActivity.this, EditEventActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("title", event.getText().toString());
                        bundle.putString("place", eventLocation.getText().toString());
                        bundle.putString("addEventMemo", addEventMemo.getText().toString());
                        bundle.putString("timelineNo", timelineNo.toString());
                        if (meet) {
                            bundle.putString("action", "meet");
                            bundle.putString("friendId", friendId);
                            bundle.putString("eventDate", eventDate.getText().toString());
                        } else {
                            bundle.putString("action", "activity");
                            bundle.putParcelable("activityBean", activityBean);

                        }
                        intent.putExtras(bundle);
                        startActivity(intent);


                }
                return false;
            }

        });


    }

}
