package com.example.workoutic.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAARClt9co:APA91bGspnpAWiuQRaFUOBUxPOfQYRUSB59OY5M_dYapLMrszQP6HEmrFsa70FX0dMeJEOHJ_GAoi6SprX_pjI59J5VDXs7oGDzazowstJKbeefkN1_k--Qgy04ki8CFqONDxIUpkdFD"
            }
    )

    @POST("fcm/send")
    Call<WKResponse> sendNotification(@Body Sender body);
}
