package lourdes8122.radiotaller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Rating;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by paulruiz on 10/28/14.
 */
public class MediaPlayerService extends Service {

    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";

    public static final String ACTION_STOP = "action_stop";

    private MediaPlayer mMediaPlayer;
    private MediaSessionManager mManager;
    private MediaSession mSession;
    private MediaController mController;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleIntent( Intent intent ) {
        if( intent == null || intent.getAction() == null )
            return;

        String action = intent.getAction();

        if( action.equalsIgnoreCase( ACTION_PLAY ) ) {
            mController.getTransportControls().play();
            Toast.makeText(this, "Conectando, por favor espere...", Toast.LENGTH_LONG).show();
            try {
                mMediaPlayer.setDataSource("http://giss.tv:8000/radiolast.mp3");
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mMediaPlayer.start();
                    }
                });

                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Streaming caido. Intente más tarde.", Toast.LENGTH_LONG).show();
            }

        } else if( action.equalsIgnoreCase( ACTION_PAUSE ) ) {
            mController.getTransportControls().stop();
            mMediaPlayer.stop();
            mMediaPlayer.release();

        } else if( action.equalsIgnoreCase( ACTION_STOP ) ) {
            mController.getTransportControls().stop();
            mMediaPlayer.stop();
            mMediaPlayer.release();

        }
    }

    private Notification.Action generateAction( int icon, String title, String intentAction ) {
        Intent intent = new Intent( getApplicationContext(), MediaPlayerService.class );
        intent.setAction( intentAction );
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        return new Notification.Action.Builder( icon, title, pendingIntent ).build();
    }

    private void buildNotification( Notification.Action action ) {
        Notification.MediaStyle style = new Notification.MediaStyle();
        Bitmap largeIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_logoradio);


        Intent intent = new Intent( getApplicationContext(), MediaPlayerService.class );
        intent.setAction( ACTION_STOP );
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        Notification.Builder builder = new Notification.Builder( this )
                .setSmallIcon(R.drawable.ic_radio_black_24dp)
                .setLargeIcon(largeIcon)
                .setContentTitle( "Estas escuchando Radio Taller 105.3 Mhz" )
                .setContentText( "Nuestra Señora de Lourdes" )
                .setDeleteIntent( pendingIntent )
                .setStyle(style);

        //builder.addAction( generateAction( android.R.drawable.ic_media_previous, "Previous", ACTION_PREVIOUS ) );
        //builder.addAction( generateAction( android.R.drawable.ic_media_rew, "Rewind", ACTION_REWIND ) );
        builder.addAction( action );
        //builder.addAction( generateAction( android.R.drawable.ic_media_ff, "Fast Foward", ACTION_FAST_FORWARD ) );
        //builder.addAction( generateAction( android.R.drawable.ic_media_next, "Next", ACTION_NEXT ) );
        style.setShowActionsInCompactView(0);

        NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificationManager.notify( 1, builder.build() );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if( mManager == null ) {
            initMediaSessions();
        }

        handleIntent( intent );
        return super.onStartCommand(intent, flags, startId);
    }

    private void initMediaSessions() {
        mMediaPlayer = new MediaPlayer();
        mManager = ( MediaSessionManager ) getSystemService ( Context . MEDIA_SESSION_SERVICE );
        mSession = new MediaSession(getApplicationContext(), "simple player session");
        mController =new MediaController(getApplicationContext(), mSession.getSessionToken());

        mSession.setCallback(new MediaSession.Callback(){
                                 @Override
                                 public void onPlay() {
                                     super.onPlay();
                                     Log.e( "MediaPlayerService", "onPlay");
                                     buildNotification( generateAction( android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE ) );

                                 }

                                 @Override
                                 public void onPause() {
                                     super.onPause();
                                     Log.e( "MediaPlayerService", "onPause");
                                     buildNotification(generateAction(android.R.drawable.ic_media_play, "Play", ACTION_PLAY));

                                 }


                                 @Override
                                 public void onStop() {
                                     super.onStop();
                                     Log.e( "MediaPlayerService", "onStop");
                                     //Stop media player here
                                     NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                     notificationManager.cancel( 1 );
                                     Intent intent = new Intent( getApplicationContext(), MediaPlayerService.class );
                                     stopService( intent );
                                 }

                             }
        );
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mSession.release();
        return super.onUnbind(intent);
    }
}
