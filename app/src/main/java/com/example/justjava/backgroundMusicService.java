package com.example.justjava;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

public class backgroundMusicService extends Service {
    MediaPlayer player ;

    @Nullable
    @Override
//    onBind -> binds services with activity
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("check","onStart") ;
        player=MediaPlayer.create(this,R.raw.song) ;
        player.start();
        player.setLooping(true);
//        To pause the song after playing for 10 seconds
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onDestroy();
            }
        }, 10000);

//        Service is not restarted. Used for services which are periodically triggered anyway.
//        The service is only restarted if the runtime has pending startService() calls
//        since the service termination.
        return START_NOT_STICKY ;
    }

    @Override
    public void onDestroy() {
        Log.d("check","onDestroy") ;
        player.stop();
        super.onDestroy();
    }
}
