package com.gosto.kbeach.emmtek_locate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Objects;

/**
 * Created by kbeach on 23/09/2014.
 */
public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String messageReceived = "";
        if(bundle != null)
        {
            // retrieve SMS received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                messageReceived += msgs[i].getMessageBody().toString();
                messageReceived += "\n";
            }
            // get the telephone number from preferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("LocateData",Context.MODE_PRIVATE);
            String mPhoneNumber;
            mPhoneNumber = sharedPreferences.getString("phone","Default");
            String senderPhoneNumber=msgs[0].getOriginatingAddress();
            // Test toasts to verify phone numbers
            //Toast.makeText(context, "senderPhoneNumber " + senderPhoneNumber, Toast.LENGTH_SHORT).show();
            //Toast.makeText(context, "mPhoneNumber " + mPhoneNumber, Toast.LENGTH_SHORT).show();
            // Check the phone numbers are greater than 10 digits
            if (mPhoneNumber.length() > 10) {

                if (senderPhoneNumber.length() > 10) {

                    //Use only the last 10 digits to allow both +44 and 0 to register as the same phone number
                    // This will only work in the UK! Looks to see if you can remove the country code??
                    // Add in error traps as well

                    if (mPhoneNumber.substring(mPhoneNumber.length() - 10).equals(senderPhoneNumber.substring(senderPhoneNumber.length() - 10))) {

                        //display SMS message
                        //Toast.makeText(context, messageReceived, Toast.LENGTH_SHORT).show();
                        //get the HTTP address from message - strCheck validates address
                        String strCheck = context.getString(R.string.http_ref_string);
                        if(messageReceived.indexOf(strCheck) > -1) {
                            String mapAddress = messageReceived.substring(messageReceived.indexOf(strCheck));
                            //Toast.makeText(context, mapAddress, Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("mapaddress",mapAddress);
                            editor.commit();

                        }else{
                            //Toast.makeText(context, "No HTTP address", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        //Toast.makeText(context, "Different phone number", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            //get the sender phone number


        }
    }
}
