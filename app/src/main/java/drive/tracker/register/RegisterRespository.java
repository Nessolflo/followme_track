package drive.tracker.register;

import drive.tracker.io.RetroFitHelper;
import drive.tracker.io.pojo.ResponseRegister;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterRespository {

    public void sendParams(String FCM, String placa, CallbackRegister callbackRegister ){
        RetroFitHelper.getApiServices().getRegister("registrar",placa, FCM)
                .enqueue(new Callback<ResponseRegister>() {
            @Override
            public void onResponse(Call<ResponseRegister> call,
                                   Response<ResponseRegister> response) {
                if(callbackRegister!=null && response.body()!=null )
                    callbackRegister.onResponse(response.body());
                else if(callbackRegister!=null)
                    callbackRegister.onFail();
            }

            @Override
            public void onFailure(Call<ResponseRegister> call, Throwable t) {
                if(callbackRegister!=null)
                    callbackRegister.onFail();
            }
        });
    }


    public interface CallbackRegister{
        void onResponse(ResponseRegister responseRegister);
        void onFail();
    }
}
