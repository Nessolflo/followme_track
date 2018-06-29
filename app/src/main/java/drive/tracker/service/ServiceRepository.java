package drive.tracker.service;

import drive.tracker.io.RetroFitHelper;
import drive.tracker.io.pojo.ResponseRegister;
import drive.tracker.service.holder.UserLocations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceRepository {

    public void sendLocation(UserLocations userLocations, CallbackSendLocation callbackSendLocation){
        RetroFitHelper.getApiServices().sendLocation("ubicacion",
                userLocations.latitud,
                userLocations.longitud,
                userLocations.idUnit).enqueue(new Callback<ResponseRegister>() {
            @Override
            public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {
                if(callbackSendLocation!=null)
                    callbackSendLocation.onSuccessful();
            }

            @Override
            public void onFailure(Call<ResponseRegister> call, Throwable t) {
                if(callbackSendLocation!=null && t.getLocalizedMessage()!=null)
                    callbackSendLocation.onFail(t.getLocalizedMessage());
            }
        });
    }

    public interface CallbackSendLocation{
        void onSuccessful();
        void onFail(String message);
    }
}
