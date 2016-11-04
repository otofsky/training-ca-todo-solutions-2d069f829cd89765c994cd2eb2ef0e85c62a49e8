package pl.training.cleanarchitecturetodo.api;

import java.util.List;

import pl.training.cleanarchitecturetodo.api.model.NewMessage;
import pl.training.cleanarchitecturetodo.api.model.RemoteMessageData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WallAPI {
    @GET("/wall/posts/")
    Call<List<RemoteMessageData>> getAllMessages();

    @POST("/wall/posts/")
    Call<RemoteMessageData> sendMessage(@Body NewMessage message);
}
