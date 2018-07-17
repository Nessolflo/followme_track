package drive.tracker.domain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import drive.tracker.io.RetroFitHelper;
import drive.tracker.io.pojo.ResponseConfig;
import drive.tracker.service.ServiceLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackerConfiguration {

    private static TrackerConfiguration trackerConfiguration;
    public static TrackerConfiguration getInstance(){
        if(trackerConfiguration==null){
            trackerConfiguration= new TrackerConfiguration();
        }
        return trackerConfiguration;
    }

    public void getConfig(Context context){
        final SharedPreferences sharedPreferences= context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String id = sharedPreferences.getString(ConstantsTracker.KEY_ID, "");
        if(!id.isEmpty())
        RetroFitHelper.getApiServices().getConfig("config", id).enqueue(
                new Callback<ResponseConfig>() {
                    @Override
                    public void onResponse(Call<ResponseConfig> call, Response<ResponseConfig> response) {
                        if(response.body()!=null) {
                            editor.putFloat(ConstantsTracker.KEY_METERS,
                                    Float.parseFloat(response.body().metros));
                            editor.putInt(ConstantsTracker.KEY_SECONDS,
                                    (1000* Integer.parseInt(response.body().segundos)));
                            editor.apply();
                            editor.commit();
                            context.sendBroadcast(new Intent("drive.tracker.restart_location"));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseConfig> call, Throwable t) {

                    }
                }
        );
    }
}
