package lourdes8122.radiotaller;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import static lourdes8122.radiotaller.R.drawable.ic_play_circle_filled;


public class EnVivoFragment extends Fragment {

    public Chronometer crono;

    private MediaPlayer player;
    private String url = "http://giss.tv:8000/agenda.mp3";
    private Button btnStreaming;
    protected boolean isPlay = false;
    private TextView radio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_en_vivo, container, false);


        //Inicio el objeto MediaPlayer
        iniciarMediaPlayer();
        btnStreaming = rootView.findViewById(R.id.btn_reproductor);
        radio = rootView.findViewById(R.id.textView5);
        crono = rootView.findViewById(R.id.cronometro);

        btnStreaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlay = !isPlay;

                if(isPlay){
                    iniciarReproduccion();
                }else{

                    pararReproduccion();
                }
            }
        });

        return rootView;

    }

    private void pararReproduccion() {
        if(player.isPlaying()){
            player.stop();
            player.release();
            iniciarMediaPlayer();
            btnStreaming.setBackgroundResource(ic_play_circle_filled);
            crono.stop();
            radio.setText(R.string.radio_apagada);
        }

    }

    private void iniciarReproduccion() {

        try{
            Toast.makeText(getContext(), "Conectando, por favor espere...", Toast.LENGTH_LONG).show();

            player.reset();
            player.setDataSource(url);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);

            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    player.start();
                    btnStreaming.setBackgroundResource(R.drawable.ic_stop);
                    crono.setBase(SystemClock.elapsedRealtime());
                    crono.start();
                    radio.setText(R.string.radio_encendida);
                }
            });
            player.prepareAsync();

        } catch (IllegalArgumentException | SecurityException
                | IllegalStateException | IOException e){
            Toast.makeText(getContext(),"Streaming caido. Intente más tarde.", Toast.LENGTH_LONG).show();
            btnStreaming.setBackgroundResource(ic_play_circle_filled);
        }
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

}
