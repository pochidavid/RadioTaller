package lourdes8122.radiotaller.id;

import android.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

    private static final String BASE_URL = "https://e8bb8611-616f-4699-a7c7-b22ffce0d91e.mock.pstmn.io";
    //private static final String BASE_URL = "http://10.0.2.2:4000/";
    private static final String DB_NAME = "radio-taller-DB";

    Application application;

    public DataModule(Application application){
        this.application = application;
    }

    @Provides
    public AppRepository provideAppRepository(ProgramacionService programacionService,
                                              ProgramacionDao programacionDao,
                                              SubscripcionesDao subscripcionesDao,
                                              Executor executor
            //, UsuarioDao usuarioDao
    ){

        return new AppRepository(programacionService,programacionDao,subscripcionesDao,executor
               // , usuarioDao
        );
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
        return Executors.newCachedThreadPool();
    }

    //@Provides
    //public UsuarioDao provideUsuarionDao(AppDatabase appDatabase){
    //    return appDatabase.usuarioDao();
    //}
}
