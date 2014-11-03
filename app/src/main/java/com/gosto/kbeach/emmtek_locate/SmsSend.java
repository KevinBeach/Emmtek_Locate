package com.gosto.kbeach.emmtek_locate;

import android.telephony.SmsManager;

/**
 * Created by kbeach on 23/09/2014.
 */
public class SmsSend {

    private String phoneNumber;
    private String text;

    //constuctor for SmSSend
    public SmsSend(String phone, String body) {
        phoneNumber = phone;
        text = body;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean Send (){
        try {
            // Get the default instance of the SmsManager
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber,
                    null,
                    text,
                    null,
                    null);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
