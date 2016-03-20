package net.simplifiedcoding.firebasepushnotification;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Belal on 3/20/2016.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //Initializing firebase
        Firebase.setAndroidContext(getApplicationContext());
    }
}
