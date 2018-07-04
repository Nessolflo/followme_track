package drive.tracker;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;

import drive.tracker.fcm.MyFirebaseMessagingService;

public class TrackerApp extends Application {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        MyFirebaseMessagingService.createNotificationChannel(this);
    }

    public FirebaseAnalytics getAnalytics(){
        return mFirebaseAnalytics;
    }
}
