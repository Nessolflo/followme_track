package drive.tracker.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitHelper {
    private static Urls urls;
    private static Gson GSON;
    public static Urls getApiServices(){
        if(urls==null){
            final HttpLoggingInterceptor interceptor
                    = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(15, TimeUnit.SECONDS)
                    .connectTimeout(15, TimeUnit.SECONDS).addInterceptor(interceptor)
                    .build();
            Retrofit adapter = new Retrofit.Builder()
                    .baseUrl(ConstantsUrl.IP)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .build();
            urls = adapter.create(Urls.class);
        }
        return urls;
    }

    private static Gson getGson() {
        if (GSON == null) {
            GSON = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .serializeNulls()
                    .create();
        }
        return GSON;
    }
}
