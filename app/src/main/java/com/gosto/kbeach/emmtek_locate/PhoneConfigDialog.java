package com.gosto.kbeach.emmtek_locate;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by kbeach on 21/09/2014.
 */
public class PhoneConfigDialog extends DialogFragment {
    Button ok,cancel;
    EditText mETPhoneNumber;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phone_config,null);
         getDialog().setTitle("EmmTek Unit Phone Number");
         ok = (Button) view.findViewById(R.id.phone_config_OK);
         cancel = (Button) view.findViewById(R.id.phone_config_Cancel);
         mETPhoneNumber = (EditText) view.findViewById(R.id.phone_config_EditText);

        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LocateData", Context.MODE_PRIVATE);
        String mPhoneNumber;
        mPhoneNumber = sharedPreferences.getString("phone","Default");

        // Set the dialog edittext to the existing phone number in the preferences
        mETPhoneNumber.setText(mPhoneNumber);

        //Set onclick listeners

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("phone",mETPhoneNumber.getText().toString());
                editor.commit();
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }


}
