package lourdes8122.radiotaller;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EnVivoFragment.GetMainService {


    private MediaPlayerService mNowPlayingService;
    SharedPreferences myPreferences;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String IR_CONFIGURAR = "lourdes8122.radiotaller.MainActivity.IR_CONFIGURAR";
    public static final String UPDATE_PLAYER = "lourdes8122.radiotaller.MainActivity.UPDATE_PLAYER";
    public static final String BUFFERING = TAG + ".buffering_player";
    public EnVivoFragment fragmentInicio;
    public ProgramacionFragment fragmentProgramacion;
    public ConfiguracionFragment fragmentConfiguracion;
    public WebFragment fragmentWeb;
    public EnviarComentariosFragment fragmentEnviarComentario;
    public Boolean actualizarBtnToogle;

    Button modificarUsuario;

    private boolean mBound;



    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(UPDATE_PLAYER)) {
                updateMediaPlayerToggle();

            } else if (intent.getAction().equals(BUFFERING)) {
                showMediaPlayerBuffering();
            }


        }
    };


    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.MediaPlayerBinder binder = (MediaPlayerService.MediaPlayerBinder) service;
            mNowPlayingService = binder.getService();
            updateMediaPlayerToggle();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };


    private void updateMediaPlayerToggle() {
        //TODO: Arreglar exception - ProgramacionFragment cannot be cast to lourdes8122.radiotaller.EnVivoFragment
        if(getSupportFragmentManager().findFragmentById(R.id.contenedor)==fragmentInicio) {
            actualizarBtnToogle = true;
            EnVivoFragment nowPlayingFragment = (EnVivoFragment) getSupportFragmentManager().findFragmentById(R.id.contenedor);

            if (nowPlayingFragment != null) {
                nowPlayingFragment.updateToggle();
            }
        }
    }

    private void showMediaPlayerBuffering() {
        /*EnVivoFragment nowPlayingFragment = (EnVivoFragment) getSupportFragmentManager().findFragmentById(R.id.contenedor);
        if (nowPlayingFragment != null) {
            nowPlayingFragment.showBuffering(true);
        }*/
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(myPreferences.getInt("THEME", R.style.AppTheme));
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actualizarBtnToogle = false;


        fragmentProgramacion = new ProgramacionFragment();
        fragmentConfiguracion = new ConfiguracionFragment();
        fragmentWeb = new WebFragment();
        fragmentEnviarComentario = new EnviarComentariosFragment();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //navigationView.setBackgroundResource(R.drawable.banner);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            fragmentInicio = new EnVivoFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contenedor, fragmentInicio)
                    .commit();
        }

        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        switch (myPreferences.getInt("THEME", R.style.AppTheme)){
            case R.style.AppTheme:
                navigationView.inflateHeaderView(R.layout.nav_header_main).setBackgroundResource(R.drawable.banner02);
                break;
            case R.style.AppTheme2:
                navigationView.inflateHeaderView(R.layout.nav_header_main).setBackgroundResource(R.drawable.banner02);
                break;
            case R.style.AppTheme3:
                navigationView.inflateHeaderView(R.layout.nav_header_main).setBackgroundResource(R.drawable.banner01);
                break;
            case R.style.AppTheme4:
                navigationView.inflateHeaderView(R.layout.nav_header_main).setBackgroundResource(R.drawable.banner01);
                break;
        }



        createNotificationChannel();


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        WebView webView = (WebView) findViewById(R.id.web);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (webView != null && webView.canGoBack()) {
                     webView.goBack();
                }else {
                    super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.acerca_de) {
            dialogo_acerca_de mensaje = new dialogo_acerca_de();
            mensaje.show(getSupportFragmentManager(),"aceca de");
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.envivo) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contenedor, fragmentInicio)
                    .commit();

        } else if (id == R.id.programacion) {
            //Toast.makeText(MainActivity.this,"Cargando. Espere...",Toast.LENGTH_SHORT).show();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contenedor,fragmentProgramacion)
                    .commit();
        } else if (id == R.id.youtube) {
            Uri uri = Uri.parse("https://www.youtube.com/channel/UCr_tYBYP-1j4DFx1s2ksVVg");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (id == R.id.web) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contenedor, fragmentWeb)
                    .commit();
        } else if (id == R.id.comentarios) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contenedor, fragmentEnviarComentario)
                    .commit();

        } else if (id == R.id.config) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contenedor,fragmentConfiguracion)
                    .commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void registerBroadcastReceiver() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter updateIntentFilter = new IntentFilter();
        updateIntentFilter.addAction(UPDATE_PLAYER);
        updateIntentFilter.addAction(BUFFERING);
        broadcastManager.registerReceiver(broadcastReceiver, updateIntentFilter);
    }

    private void unRegisterBroadcastReceiver() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }


    private void createNotificationChannel() {
        // Crear el canal de notificaciones pero solo para API 26 io superior
        // dado que NotificationChannel es una clase nueva que no está incluida
        // en las librerías de soporte qeu brindan compatibilidad hacía atrás
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.canal_estado_nombre);
            String description = getString(R.string.canal_estado_descr);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CANAL01", name, importance);
            channel.setDescription(description);
            // Registrar el canal en el sistema
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }




    @Override
    public MediaPlayerService getMainService() {
        return mNowPlayingService;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcastReceiver();
        Intent mediaIntent = new Intent(this, MediaPlayerService.class);
        startService(mediaIntent);
        if (mNowPlayingService == null)
            bindService(mediaIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sendStopIntent();
        if (mNowPlayingService != null) {
            unbindService(mServiceConnection);
            mNowPlayingService = null;
        }
        unRegisterBroadcastReceiver();
        mBound = false;
    }

    private void sendStopIntent() {
        Intent stopIntent = new Intent(this, MediaPlayerService.class);
        stopIntent.setAction(MediaPlayerService.ACTION_CLOSE_IF_PAUSED);
        startService(stopIntent);
    }


}
