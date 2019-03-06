package entity;

/**
 * lacevent消息实体
 */
public class LacEvent {

    //事件编码
    private String eventId;
    //事件名称
    private String eventName;
    //事件描述
    //private String eventDesc;
    //区域Id
    private String areaId;
    //卡标示
    private String IMSI;
    //手机号码
    private String MSISDN;
    //进入时间&离开时间&统计节点时间
    private String eventTime;
    //进入类型&离开类型&统计类型
    private String subType;
    //驻留时长
    private String stayDuration;
    //NewLAC
    private String currentLac;
    //NewCI
    private String currentCi;
    //NewRAT
    private String currentRat;
    //OldLAC
    private String preLac;
    //OldCI
    private String prevCi;
    //OldRAT
    private String preRAT;


    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getIMSI() {
        return IMSI;
    }

    public void setIMSI(String IMSI) {
        this.IMSI = IMSI;
    }

    public String getMSISDN() {
        return MSISDN;
    }

    public void setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getStayDuration() {
        return stayDuration;
    }

    public void setStayDuration(String stayDuration) {
        this.stayDuration = stayDuration;
    }

    public String getCurrentLac() {
        return currentLac;
    }

    public void setCurrentLac(String currentLac) {
        this.currentLac = currentLac;
    }

    public String getCurrentCi() {
        return currentCi;
    }

    public void setCurrentCi(String currentCi) {
        this.currentCi = currentCi;
    }

    public String getCurrentRat() {
        return currentRat;
    }

    public void setCurrentRat(String currentRat) {
        this.currentRat = currentRat;
    }

    public String getPreLac() {
        return preLac;
    }

    public void setPreLac(String preLac) {
        this.preLac = preLac;
    }

    public String getPrevCi() {
        return prevCi;
    }

    public void setPrevCi(String prevCi) {
        this.prevCi = prevCi;
    }

    public String getPreRAT() {
        return preRAT;
    }

    public void setPreRAT(String preRAT) {
        this.preRAT = preRAT;
    }

}

