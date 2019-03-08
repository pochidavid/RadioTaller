package lourdes8122.radiotaller;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;


import java.io.IOException;
import java.nio.channels.Channel;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import xdroid.toaster.Toaster;


/**
 * Servicio que controla el reproductor multimedia y la notificación que lo representa.
 */
public class MediaPlayerService extends Service implements AudioManager.OnAudioFocusChangeListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener {
    private static final String TAG = MediaPlayerService.class.getSimpleName();

    private final IBinder mMediaPlayerBinder = new MediaPlayerBinder();
    public static final String ACTION_PLAY = "lourdes8122.radiotaller.PLAY";
    public static final String ACTION_PAUSE = "lourdes8122.radiotaller.PAUSE";
    private static final String ACTION_CLOSE = "lourdes8122.radiotaller.APP_CLOSE";
    public static final String ACTION_CLOSE_IF_PAUSED = "lourdes8122.radiotaller.APP_CLOSE_IF_PAUSED";
    private static final int NOTIFICATION_ID = 4223;
    private MediaPlayer mMediaPlayer = null;
    private AudioManager mAudioManager = null;

    private MiCountDownTimer crono;

    //The URL Streaming
    private static final String mStreamUrl = "http://giss.tv:8000/radiotallerfm.mp3";

    //Wifi Lock  para asegurar que el wifi no se duerma mientras estamos en stearming .
    private WifiManager.WifiLock mWifiLock;

    enum State {
        Retrieving,
        Stopped,  //Media player is stopped and not prepared to play
        Preparing, // Media player is preparing to play
        Playeng,  // MediaPlayer playback is active.

        Paused // Audio Playback is paused
    }

    private State mState = State.Stopped;

    enum AudioFocus {
        NoFocusNoDuck,
        NoFocusCanDuck,
        Focused
    }

    private AudioFocus mAudioFocus = AudioFocus.NoFocusNoDuck;


    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                mAudioFocus = AudioFocus.Focused;
                // resume playback
                if (mState == State.Playeng) {
                    startMediaPlayer();
                    mMediaPlayer.setVolume(1.0f, 1.0f);
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                mAudioFocus = AudioFocus.NoFocusNoDuck;
                // Lost focus for an unbounded amount of time: stop playback and release media player
                stopMediaPlayer();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                mAudioFocus = AudioFocus.NoFocusNoDuck;
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                processPauseRequest();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                mAudioFocus = AudioFocus.NoFocusCanDuck;
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mMediaPlayer.isPlaying()) mMediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }



    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        startMediaPlayer();
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }


    private void setupAudioManager() {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        }
    }

    private void setupWifiLock() {
        if (mWifiLock == null) {
            mWifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                    .createWifiLock(WifiManager.WIFI_MODE_FULL, "mediaplayerlock");
        }
    }

    private void setupMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnInfoListener(this);
            mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            crono = new MiCountDownTimer(10000,1000);
            crono.start();

            try {
                mMediaPlayer.setDataSource(this, Uri.parse(mStreamUrl));
            } catch (IOException e) {
                //Toaster.toast("Error en la conexión. Posible servidor caido");
                e.printStackTrace();
                stopSelf();
            }
        }
    }

    /**
     * El servicio de transmisión de radio se ejecuta en modo forground para evitar que el sistema operativo Android lo mate.
     * El OnStartCommand se llama cada vez que hay una llamada para iniciar el servicio y el servicio es
     * ya iniciado. Al pasar un intento a onStartCommand podemos reproducir y pausar la música.
     */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = null;
        if (intent != null) {
            action = intent.getAction();
        }
        if (action != null) {
            switch (action) {
                case ACTION_PLAY:
                    processPlayRequest();
                    break;
                case ACTION_PAUSE:
                    processPauseRequest();
                    break;
                case ACTION_CLOSE_IF_PAUSED:
                    closeIfPaused();
                    break;
                case ACTION_CLOSE:
                    close();
                    break;
            }
        }
        return START_STICKY; // no reiniciar el servicio si se cancela.
    }

    // si el reproductor multimedia está pausado o detenido y este método se ha activado, detenga el servicio.
    private void closeIfPaused() {
        if (mState == State.Paused || mState == State.Stopped) {
            close();
        }
    }

    private void close() {
        removeNotification();
        stopSelf();
    }

    private void initMediaPlayer() {
        setupMediaPlayer();
        requestResources();
    }

    /**
     * Compruebe si el reproductor multimedia se ha inicializado y si tenemos enfoque de audio.
     * Sin enfoque de audio no iniciamos el reproductor multimedia.
     * Cambiar estado y empezar a preparar async.
     */

    private void configAndPrepareMediaPlayer() {
        initMediaPlayer();
        mState = State.Preparing;
        buildNotification(true);
        mMediaPlayer.prepareAsync();

    }

    /**
     * El reproductor de medios está preparado para verificar que no estamos en los estados de parada o pausa
     * antes de iniciar el reproductor multimedia
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mState != State.Paused && mState != State.Stopped) {
            startMediaPlayer();
        }
    }

    /*
        Compruebe si el reproductor multimedia está disponible e inícielo.
     */

    private void startMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            sendUpdatePlayerIntent();
            mState = State.Playeng;
            buildNotification(false);

        }
    }

    private void sendUpdatePlayerIntent() {
        Log.d(TAG, "updatePlayerIntent");
        Intent updatePlayerIntent = new Intent(MainActivity.UPDATE_PLAYER);
        LocalBroadcastManager.getInstance(this).sendBroadcast(updatePlayerIntent);
        crono.cancel();

    }


    private void requestResources() {
        setupAudioManager();
        setupWifiLock();
        mWifiLock.acquire();

        tryToGetAudioFocus();
    }

    private void tryToGetAudioFocus() {
        if (mAudioFocus != AudioFocus.Focused && AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN))
            mAudioFocus = AudioFocus.Focused;

    }


    private void stopMediaPlayer() {

        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mState = State.Stopped;

        relaxResources();
        giveUpAudioFocus();
    }


    private void processPlayRequest() {
        if (mState == State.Stopped) {
            Toast.makeText(this, "Conectando con el servidor. Por favor, espere...", Toast.LENGTH_LONG).show();
            sendBufferingIntent();
            configAndPrepareMediaPlayer();
        } else if (mState == State.Paused) {
            requestResources();
            startMediaPlayer();
        }
    }


    private void sendBufferingIntent() {
        Intent bufferingPlayerIntent = new Intent(MainActivity.BUFFERING);
        LocalBroadcastManager.getInstance(this).sendBroadcast(bufferingPlayerIntent);
    }

    private void processPauseRequest() {

        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            sendUpdatePlayerIntent();
            mState = State.Paused;
            relaxResources();
            buildNotification(false);
        }
    }

    /**
     * No hay notificación de estilo de medios para sistemas operativos debajo de la API 21. Así que este método se basa
     * una notificación de compatibilidad simple que tiene un botón de reproducción o pausa dependiendo de si el reproductor está
     * pausado o en play. Si foreGroundOrUpdate, el servicio debe ir al primer plano. más
     * Solo actualiza la notificación.
     */


    private void buildNotification(boolean startForeground) {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        Bitmap largeIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_logoradio);
        Notification.MediaStyle style = new Notification.MediaStyle();


        intent.setAction(ACTION_CLOSE);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, "CANAL01" )
                    .setContentTitle("105.3 MHz 'Ntra Sra de Lourdes'").setContentText("Streaming En Vivo")
                    .setSmallIcon(R.drawable.logo).setOngoing(true)
                    .setLargeIcon(largeIcon)
                    .setBadgeIconType(R.drawable.logoradio)
                    .setContentIntent(getMainContentIntent())
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setDeleteIntent(pendingIntent)
                    .setStyle(style);
        }else{
            builder = new Notification.Builder(this)
                    .setContentTitle("105.3 MHz 'Ntra Sra de Lourdes'").setContentText("Streaming En Vivo")
                    .setSmallIcon(R.drawable.ic_radio_black_24dp).setOngoing(true)
                    .setLargeIcon(largeIcon)
                    .setContentIntent(getMainContentIntent())
                    .setDeleteIntent(pendingIntent)
                    .setStyle(style);
        }
        if (mState == State.Paused || mState == State.Stopped) {
            builder.addAction(generateAction(android.R.drawable.ic_media_play, "Play", ACTION_PLAY));
        } else {
            builder.addAction(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
        }
        builder.addAction(generateAction(android.R.drawable.ic_menu_close_clear_cancel, "Close", ACTION_CLOSE));



        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        if (startForeground)
            startForeground(NOTIFICATION_ID, builder.build());
        else
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    private PendingIntent getMainContentIntent() {
        Intent resultIntent = new Intent(this, MainActivity.class);
        return PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


    }


    private Notification.Action generateAction(int icon, String title, String intentAction) {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(intentAction);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        return new Notification.Action.Builder(icon, title, pendingIntent).build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMediaPlayerBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onDestroy() {
        stopMediaPlayer();
    }


    private void relaxResources() {


        if (mWifiLock != null && mWifiLock.isHeld()) {
            mWifiLock.release();
        }



        stopForeground(true);

    }

    private void removeNotification() {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

        notificationManagerCompat.cancel(NOTIFICATION_ID);
    }

    private void giveUpAudioFocus() {
        if ((mAudioFocus == AudioFocus.Focused || mAudioFocus == AudioFocus.NoFocusCanDuck) &&
                AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAudioManager.abandonAudioFocus(this)) {
            mAudioFocus = AudioFocus.NoFocusNoDuck;
        }
    }

    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    public class MediaPlayerBinder extends Binder {

        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    public class MiCountDownTimer extends CountDownTimer {

        public MiCountDownTimer(long starTime, long interval) {
            super(starTime, interval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub

            Toaster.toast("Error en la conexión. Posible servidor caido");
            sendUpdatePlayerIntent();
            stopMediaPlayer();
            close();

        }
    }


}
