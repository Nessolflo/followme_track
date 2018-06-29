package drive.tracker;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;

public class TrackerApp extends Application {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    public FirebaseAnalytics getAnalytics(){
        return mFirebaseAnalytics;
    }
}
