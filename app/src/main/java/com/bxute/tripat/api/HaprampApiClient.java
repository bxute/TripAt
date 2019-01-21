/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HaprampApiClient {
  private static Retrofit retrofit = null;


  public static Retrofit getClient() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient
     .Builder()
     .connectTimeout(30, TimeUnit.SECONDS)
     .readTimeout(30, TimeUnit.SECONDS)
     .addInterceptor(logging)
     .build();

    retrofit = new Retrofit.Builder()
     .baseUrl(URLS.BASE_URL)
     .addConverterFactory(ScalarsConverterFactory.create())
     .addConverterFactory(GsonConverterFactory.create())
     .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
     .client(client)
     .build();
    return retrofit;
  }
}
