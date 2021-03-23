package com.jdcompany.jdmessenger.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InternetService {
    private static InternetApi internetApi;
    private static String BASE_URL = "http://192.168.3.111:8001";

    private InternetService(){};

    public static InternetApi getInternetApi(){
        return internetApi;
    }

    public static synchronized void startService(){
        if(internetApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            internetApi = retrofit.create(InternetApi.class);
        }
    }

}
