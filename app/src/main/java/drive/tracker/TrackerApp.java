package drive.tracker;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.firebase.analytics.FirebaseAnalytics;

import drive.tracker.domain.ConstantsTracker;
import drive.tracker.domain.TrackerConfiguration;
import drive.tracker.fcm.MyFirebaseMessagingService;
import drive.tracker.io.RetroFitHelper;
import drive.tracker.io.pojo.ResponseConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackerApp extends Application {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        MyFirebaseMessagingService.createNotificationChannel(this);
        TrackerConfiguration.getInstance().getConfig(this);
    }

    public FirebaseAnalytics getAnalytics() {
        return mFirebaseAnalytics;
    }
}
