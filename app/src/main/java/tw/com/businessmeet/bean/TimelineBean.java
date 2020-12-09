package tw.com.businessmeet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimelineBean implements Parcelable {
    private static String[] column = new String[]{"timeline_no", "matchmaker_id", "friend_id",
            "place", "title", "remark", "timeline_properties_no", "color", "create_date", "modify_date"};
    private Integer timelineNo;
    private String matchmakerId;
    private String friendId;
    private String place;
    private String title;
    private String remark;
    private Integer timelinePropertiesNo;
    private String color;
    private String activityDate;
    private String createDateStr;
    private String modifyDateStr;
    @SerializedName("startDateStr")
    private String startDate;
    @SerializedName("endDateStr")
    private String endDate;
    private ActivityLabelBean activityLabelBean;
    private List<ActivityInviteBean> activityInviteBeanList;
    private ActivityDateBean activityDateBean;
    private Integer statusCode;

    public TimelineBean() {
    }


    protected TimelineBean(Parcel in) {
        if (in.readByte() == 0) {
            timelineNo = null;
        } else {
            timelineNo = in.readInt();
        }
        matchmakerId = in.readString();
        friendId = in.readString();
        place = in.readString();
        title = in.readString();
        remark = in.readString();
        if (in.readByte() == 0) {
            timelinePropertiesNo = null;
        } else {
            timelinePropertiesNo = in.readInt();
        }
        color = in.readString();
        activityDate = in.readString();
        createDateStr = in.readString();
        modifyDateStr = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        activityLabelBean = in.readParcelable(ActivityLabelBean.class.getClassLoader());
        activityInviteBeanList = in.createTypedArrayList(ActivityInviteBean.CREATOR);
        if (in.readByte() == 0) {
            statusCode = null;
        } else {
            statusCode = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (timelineNo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(timelineNo);
        }
        dest.writeString(matchmakerId);
        dest.writeString(friendId);
        dest.writeString(place);
        dest.writeString(title);
        dest.writeString(remark);
        if (timelinePropertiesNo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(timelinePropertiesNo);
        }
        dest.writeString(color);
        dest.writeString(activityDate);
        dest.writeString(createDateStr);
        dest.writeString(modifyDateStr);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeParcelable(activityLabelBean, flags);
        dest.writeTypedList(activityInviteBeanList);
        if (statusCode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(statusCode);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TimelineBean> CREATOR = new Creator<TimelineBean>() {
        @Override
        public TimelineBean createFromParcel(Parcel in) {
            return new TimelineBean(in);
        }

        @Override
        public TimelineBean[] newArray(int size) {
            return new TimelineBean[size];
        }
    };

    public static String[] getColumn() {
        return column;
    }

    public Integer getTimelineNo() {
        return timelineNo;
    }

    public void setTimelineNo(Integer timelineNo) {
        this.timelineNo = timelineNo;
    }

    public String getMatchmakerId() {
        return matchmakerId;
    }

    public void setMatchmakerId(String matchmakerId) {
        this.matchmakerId = matchmakerId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getTimelinePropertiesNo() {
        return timelinePropertiesNo;
    }

    public void setTimelinePropertiesNo(Integer timelinePropertiesNo) {
        this.timelinePropertiesNo = timelinePropertiesNo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public String getModifyDateStr() {
        return modifyDateStr;
    }

    public void setModifyDateStr(String modifyDateStr) {
        this.modifyDateStr = modifyDateStr;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    public ActivityLabelBean getActivityLabelBean() {
        return activityLabelBean;
    }

    public void setActivityLabelBean(ActivityLabelBean activityLabelBean) {
        this.activityLabelBean = activityLabelBean;
    }

    public List<ActivityInviteBean> getActivityInviteBeanList() {
        return activityInviteBeanList;
    }

    public void setActivityInviteBeanList(List<ActivityInviteBean> activityInviteBeanList) {
        this.activityInviteBeanList = activityInviteBeanList;
    }

    public ActivityDateBean getActivityDateBean() {
        return activityDateBean;
    }

    public void setActivityDateBean(ActivityDateBean activityDateBean) {
        this.activityDateBean = activityDateBean;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
