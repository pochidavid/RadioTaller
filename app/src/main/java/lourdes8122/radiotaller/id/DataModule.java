package lourdes8122.radiotaller.id;

import android.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import lourdes8122.radiotaller.repository.AppDatabase;
import lourdes8122.radiotaller.repository.AppRepository;
import lourdes8122.radiotaller.repository.ProgramacionDao;
import lourdes8122.radiotaller.repository.ProgramacionService;
import lourdes8122.radiotaller.repository.SubscripcionesDao;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DataModule {

    private static final String BASE_URL = "https://49f1f420-26be-4d7b-898f-7ea54d1f1419.mock.pstmn.io";
    private static final String DB_NAME = "radio-taller-DB";

    Application application;

    public DataModule(Application application){
        this.application = application;
    }

    @Provides
    public AppRepository provideAppRepository(ProgramacionService programacionService,
                                              ProgramacionDao programacionDao,
                                              SubscripcionesDao subscripcionesDao,
                                              Executor executor){

        return new AppRepository(programacionService,programacionDao,subscripcionesDao,executor);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                //converts Retrofit response into Observable
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    public OkHttpClient provideHttpClient() {
        return new OkHttpClient().newBuilder().build();
    }

    @Provides
    public ProgramacionService provideProgramacionService(Retrofit retrofit){
        return retrofit.create(ProgramacionService.class);
    }

    @Provides
    public ProgramacionDao provideProgramacionDao(AppDatabase appDatabase){
        return appDatabase.programacionDao();
    }

    @Provides
    public SubscripcionesDao provideSubscripcionesDao(AppDatabase appDatabase){
        return appDatabase.subscripcionesDao();
    }

    @Provides
    public AppDatabase provideAppDatabase(){
        return Room.databaseBuilder(application.getApplicationContext(), AppDatabase.class,DB_NAME)
                .enableMultiInstanceInvalidation()
                .build();
    }

    @Provides
    public Executor provideExecutor(){
        return Executors.newSingleThreadExecutor();
    }
}
