package maheshwari.devashish.moviesapp.retrofit;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientRetrofit {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static Retrofit retrofit  = null ;

    public static Retrofit getRetrofit() {

        if(retrofit == null) {
             retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                     .client(new OkHttpClient.Builder()
                             .addNetworkInterceptor(new StethoInterceptor())
                             .build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit;
    }
}