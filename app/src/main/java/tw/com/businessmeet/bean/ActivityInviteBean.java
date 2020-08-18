package tw.com.businessmeet.bean;

import java.util.Date;
import java.util.Map;

public class ActivityInviteBean {
    private static String[] column = new String[]{"activityInvite_no","user_no","activity_no","create_date","modify_date"};
    private Integer activityInviteNo;
    private String userId;
    private Integer activityNo;
    private String createDate;
    private String modifyDate;
    private Integer statusCode;



    public static String[] getColumn() {
        return column;
    }

    public Integer getActivityInviteNo() {
        return activityInviteNo;
    }

    public void setActivityInviteNo(Integer activityInviteNo) {
        this.activityInviteNo = activityInviteNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(Integer activityNo) {
        this.activityNo = activityNo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}