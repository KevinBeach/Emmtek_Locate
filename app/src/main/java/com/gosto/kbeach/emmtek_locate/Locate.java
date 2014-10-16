package com.gosto.kbeach.emmtek_locate;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;


public class Locate extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LocateFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.locate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id==R.id.action_phonenumber){
            //ADD IN
            // Handlers to change the phone number preference

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            final PhoneConfigDialog phoneConfigDialog = new PhoneConfigDialog();
            phoneConfigDialog.show(fragmentManager,"phoneconfig");

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class LocateFragment extends Fragment {
        //Main fragment - in google maps branch this will hold the map
        private Button mLocate;
        private WebView mMapWebView;
        private SharedPreferences sharedPreferences;
        public LocateFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_locate, container, false);
            mLocate = (Button) rootView.findViewById(R.id.buttonLocate);
            mMapWebView = (WebView) rootView.findViewById(R.id.webViewLocate);
            sharedPreferences = getActivity().getSharedPreferences("LocateData", Context.MODE_PRIVATE);
            final String mURL = sharedPreferences.getString("mapaddress","https://www.google.co.uk/maps/@54.8736587,-4.8013245,6z?hl=en");
            //Add in new client with override URL Loading to prevent browser selection
            mMapWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });
            //Set JavaScript to be enabled for google maps.
            WebSettings webSettings = mMapWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mMapWebView.loadUrl(mURL);

            //add in the shared preferences listener to update the map on changes to shared preference
            // this is a bit clunky - change to using the broadcast listener SMS inside the fragment
            // enabled after locate is pressed and then pass data as per fragments.

            SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {

                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                    //Toast.makeText(getActivity(), "preference changed", Toast.LENGTH_SHORT).show();
                    if (s.equals("mapaddress")){
                        //Toast.makeText(getActivity(), "mapaddress changed", Toast.LENGTH_SHORT).show();
                        final String mURL = sharedPreferences.getString("mapaddress","https://www.google.co.uk/maps/@54.8736587,-4.8013245,6z?hl=en");
                        mMapWebView.loadUrl(mURL);
                    }
                }
            };

            sharedPreferences.registerOnSharedPreferenceChangeListener(listener);


            mLocate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),"button clicked",Toast.LENGTH_LONG).show();
                    String mPhoneNumber;
                    String mSmsText = new String("LOCATE");
                    mPhoneNumber = sharedPreferences.getString("phone","Default");
                    if (mPhoneNumber.equals("Default")){
                        Toast.makeText(getActivity(),"Set EmmTek Phone Number",Toast.LENGTH_LONG).show();

                    }
                    else{
                        SmsSend smsSend = new SmsSend(mPhoneNumber,mSmsText);
                        if (smsSend.Send()){
                            Toast.makeText(getActivity(), "Your sms has successfully sent!",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getActivity(), "Your sms has failed...",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
            return rootView;
        }
    }
}
