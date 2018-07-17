package drive.tracker.io;


import drive.tracker.io.pojo.ResponseConfig;
import drive.tracker.io.pojo.ResponseRegister;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Urls {

    @POST(ConstantsUrl.ENDPOINT_FUN)
    Call<ResponseRegister> getRegister(@Query("accion") String action,
                                       @Query("placa") String placa,
                                       @Query("fcm") String fcm);

    @POST(ConstantsUrl.ENDPOINT_FUN)
    Call<ResponseRegister> sendLocation(@Query("accion") String action,
                                        @Query("latitud") String latitud,
                                        @Query("longitud") String longitud,
                                        @Query("idunidad") String idunidad);
    @POST(ConstantsUrl.ENDPOINT_FUN)
    Call<ResponseConfig> getConfig(@Query("accion")String accion,
                                   @Query("idunidad") String idunidad);
}
