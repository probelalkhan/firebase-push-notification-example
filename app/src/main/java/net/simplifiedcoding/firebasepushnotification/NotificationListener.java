package net.simplifiedcoding.firebasepushnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by Belal on 3/18/2016.
 */
//Class extending service as it is a service that will run in background
public class NotificationListener extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //When the service is started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Opening sharedpreferences
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);

        //Getting the firebase id from sharedpreferences
        String id = sharedPreferences.getString(Constants.UNIQUE_ID, null);

        //Creating a firebase object
        Firebase firebase = new Firebase(Constants.FIREBASE_APP + id);

        //Adding a valueevent listener to firebase
        //this will help us to  track the value changes on firebase
        firebase.addValueEventListener(new ValueEventListener() {

            //This method is called whenever we change the value in firebase
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Getting the value from firebase
                //We stored none as a initial value
                String msg = snapshot.child("msg").getValue().toString();

                //So if the value is none we will not create any notification
                if (msg.equals("none"))
                    return;

                //If the value is anything other than none that means a notification has arrived
                //calling the method to show notification
                //String msg is containing the msg that has to be shown with the notification
                showNotification(msg);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });

        return START_STICKY;
    }


    private void showNotification(String msg){
        //Creating a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.simplifiedcoding.net"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("Firebase Push Notification");
        builder.setContentText(msg);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}
