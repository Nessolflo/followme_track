package drive.tracker.register;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import drive.tracker.R;
import drive.tracker.io.pojo.ResponseRegister;

import static drive.tracker.domain.ConstantsTracker.TAG;

public class RegisterModel extends ViewModel {

    private RegisterRespository respository;
    private Application application;
    private MutableLiveData<ResponseRegister> responseLiveData=
            new MutableLiveData<>();

    public void init(Application application){
        respository = new RegisterRespository();
        this.application = application;
    }

    public void sendData(String placa, String FCM){
        respository.sendParams(FCM, placa, new RegisterRespository.CallbackRegister() {
            @Override
            public void onResponse(ResponseRegister responseRegister) {
                responseLiveData.postValue(responseRegister);
            }

            @Override
            public void onFail() {
                final ResponseRegister responseRegister= new ResponseRegister();
                responseRegister.id = 0;
                responseRegister.message= application.getString(R.string.error_get_data);
                responseRegister.result=false;
                responseLiveData.postValue(responseRegister);
            }
        });
    }

    public LiveData<ResponseRegister> getResponseData(){
        return responseLiveData;
    }



}
