package com.example.mealrecommendationapp.data.network;

import android.content.Context;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:3000/";
    private static Retrofit retrofit = null;
    private static ApiService apiService = null;

    public static synchronized ApiService getService(Context context) {
        if (apiService == null) {
            Context appContext = context.getApplicationContext();
            
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        String token = SharedPreferencesHelper.getAccessToken(appContext);
                        
                        if (token != null && !token.isEmpty()) {
                            Request request = original.newBuilder()
                                    .header("Authorization", "Bearer " + token)
                                    .build();
                            return chain.proceed(request);
                        }
                        return chain.proceed(original);
                    })
                    .authenticator((route, response) -> {
                        // Prevent infinite loop if refresh request fails
                        if (response.request().url().encodedPath().contains("/auth/refresh")) {
                            return null;
                        }

                        if (response.code() == 401) {
                            String refreshToken = SharedPreferencesHelper.getRefreshToken(appContext);
                            if (refreshToken != null && !refreshToken.isEmpty()) {
                                // Call refresh token API synchronously
                                Retrofit dummyRetrofit = new Retrofit.Builder()
                                        .baseUrl(BASE_URL)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                ApiService dummyService = dummyRetrofit.create(ApiService.class);
                                
                                try {
                                    retrofit2.Response<ApiService.ApiResponse<ApiService.AuthData>> refreshResponse = 
                                            dummyService.refreshTokenSync(new ApiService.RefreshRequest(refreshToken)).execute();
                                    
                                    if (refreshResponse.isSuccessful() && refreshResponse.body() != null && refreshResponse.body().isSuccess()) {
                                        ApiService.AuthData data = refreshResponse.body().getData();
                                        SharedPreferencesHelper.saveTokens(appContext, data.getAccessToken(), data.getRefreshToken());
                                        
                                        return response.request().newBuilder()
                                                .header("Authorization", "Bearer " + data.getAccessToken())
                                                .build();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return null;
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}
