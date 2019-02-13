package lourdes8122.radiotaller;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;


public class EnVivoFragment extends Fragment {

    private GetMainService sGetMainService;

    public interface GetMainService {
        MediaPlayerService getMainService();
    }

    private MediaPlayer player;
    private String url = "http://giss.tv:8000/santi.ogg";
    private ToggleButton btnStreaming;
    private TextView radio;
    //private Chronometer crono;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            sGetMainService = (GetMainService) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement GetMainService Interface");
        }
    }



        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_en_vivo, container, false);


        //Inicio el objeto MediaPlayer
        iniciarMediaPlayer();
        btnStreaming = rootView.findViewById(R.id.play_pause_button);
        radio = rootView.findViewById(R.id.media_player_title);
        //crono = rootView.findViewById(R.id.cronometro);


        btnStreaming.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent intent = new Intent(getContext(), MediaPlayerService.class);
                if(btnStreaming.isChecked()){
                    //getActivity().startService(new Intent(getContext(), StreamingService.class));
                    intent.setAction(MediaPlayerService.ACTION_PLAY);
                    //iniciarReproduccion();
                    //isPlay = true;
                }else{
                    //getActivity().stopService(new Intent(getContext(), StreamingService.class));
                    intent.setAction(MediaPlayerService.ACTION_PAUSE);
                    //pararReproduccion();
                    //isPlay=false;
                }
                getActivity().startService(intent);
            }
        });
        MediaPlayerService mainService = sGetMainService.getMainService();
        if(mainService!=null)
            updateToggle();

        return rootView;

    }

    private void pararReproduccion() {
            //btnStreaming.setBackgroundResource(ic_play_circle_filled);
            //crono.stop();
            radio.setText(R.string.radio_apagada);

    }

    private void iniciarReproduccion() {
        //crono.setBase(SystemClock.elapsedRealtime());
        //crono.start();
        radio.setText(R.string.radio_encendida);
        //btnStreaming.setBackgroundResource(ic_stop);

    }

    private void iniciarMediaPlayer() {
        player = new MediaPlayer();

        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.i("Buffering", ""+percent);
            }
        });

    }

    private void mostrarNotificacion(){
        Notification Notificacion = new NotificationCompat.Builder(getActivity(), "CANAL01")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_radio_black_24dp)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle())
                .setContentTitle("Estas escuchando Radio Taller")
                .setContentText("105.3 Mhz 'Nuestra Señora de Lourdes")
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(1,Notificacion);

    }



    public void updateToggle() {

        MediaPlayerService mainService = sGetMainService.getMainService();
        //Toast.makeText(getContext(),"anda",Toast.LENGTH_LONG).show();
        //The media player might have been buffering so set the buffering view to gone.

        if (mainService.isPlaying()) {
            //crono.setBase(SystemClock.elapsedRealtime());
            //crono.start();
            radio.setText(R.string.radio_encendida);

        } else {
            //crono.stop();
            radio.setText(R.string.radio_apagada);
        }
        btnStreaming.setChecked(sGetMainService.getMainService().isPlaying());
    }
}