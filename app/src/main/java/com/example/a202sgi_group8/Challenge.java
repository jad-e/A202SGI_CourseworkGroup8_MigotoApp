package com.example.a202sgi_group8;

public class Challenge {

    private String mChallengeId;

    private int status;

    private String finalizedDate;

    private String alarmTime;

    public Challenge() {

    }

    public Challenge(String mChallengeId, int status, String finalizedDate, String alarmTime) {

        this.mChallengeId = mChallengeId;
        this.status = status;
        this.finalizedDate = finalizedDate;
        this.alarmTime = alarmTime;

    }

    public String getChallengeId() {
        return mChallengeId;
    }

    public void setChallengeId(String challengeId) {
        mChallengeId = challengeId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFinalizedDate() {
        return finalizedDate;
    }

    public void setFinalizedDate(String finalizedDate) {
        this.finalizedDate = finalizedDate;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }
}
