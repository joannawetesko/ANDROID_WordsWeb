package pl.com.wordsweb.config;

import android.app.Application;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.crashlytics.android.Crashlytics;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.com.wordsweb.api.RestApi;
import pl.com.wordsweb.api.TokenAuthenticator;
import pl.com.wordsweb.svg.SvgDecoder;
import pl.com.wordsweb.svg.SvgDrawableTranscoder;
import pl.com.wordsweb.svg.SvgSoftwareLayerSetter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static pl.com.wordsweb.config.Constants.AUTH_URL;
import static pl.com.wordsweb.config.Constants.BASE_URL;

/**
 * Created by wewe on 06.10.16.
 */

public class AppSettings extends Application {


    public static RestApi.UserApi userApi;
    public static RestApi.PhraseApi phraseApi;
    public static RestApi.LearnApi learnApi;
    public static RestApi.OpinionApi opinionApi;
    public static EventBus bus;
    public static GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;
    private static Retrofit clientRetrofit ;
    private static Retrofit clientAuth;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        bus = EventBus.getDefault();
        requestBuilder = Glide.with(this)
                .using(Glide.buildStreamModelLoader(Uri.class, this), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        httpClient.retryOnConnectionFailure(true);
        TokenAuthenticator tokenAuthenticator = new TokenAuthenticator(getApplicationContext());
        httpClient.authenticator(tokenAuthenticator);


        clientRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        clientAuth = new Retrofit.Builder()
                .baseUrl(AUTH_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        userApi = clientAuth.create(RestApi.UserApi.class);
        phraseApi = clientRetrofit.create(RestApi.PhraseApi.class);
        learnApi = clientRetrofit.create(RestApi.LearnApi.class);
        opinionApi = clientRetrofit.create(RestApi.OpinionApi.class);

    }

}
