package pl.training.cleanarchitecturetodo.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {
    public static WallAPI createApi() {
        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        return new Retrofit.Builder()
                .baseUrl("http://wall.staging.ewejsciowki.pl/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(WallAPI.class);
    }
}
