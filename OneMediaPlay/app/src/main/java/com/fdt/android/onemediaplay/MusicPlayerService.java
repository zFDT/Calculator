package com.fdt.android.onemediaplay;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.fdt.android.onemediaplay.ui.main.entities.FileEntity;
import com.fdt.android.onemediaplay.ui.main.fragments.FileList;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * 单独处理程序线程上的服务。
 * <p>
 * TODO:自定义类更新意图操作和额外参数。
 *
 * @author 45169
 */

public class MusicPlayerService extends Service {
    private static final int NOTIFICATION_ID = 1;
    public static MediaPlayer mediaPlayer = null;
    public static FileEntity music = null;
    /**
     * musicPosition :第几首音乐
     * currentPosition:默认进度条当前位置
     * fileEntities:音乐列表
     * music:当前播放的音乐信息
     * url:当前播放的音乐文件路径
     * mSG:传入想要的播放状态
     * NOTIFICATION_ID:如果id设置为0,会导致不能设置为前台service
     * 播放状态 noMusic没有设置音乐，wantPlay暂停，wantPause开始
     */
    private static int musicPosition;
    private int currentPosition = 0;
    private List<FileEntity> musicList;
    private String url = null;
    private String msg = null;
    private MusicBinder musicbinder = null;


    public MusicPlayerService() {
        //构造函数
        musicbinder = new MusicBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // 通过bind 返回一个IBinder对象，然后改对象调用里面的方法实现参数的传递
        return musicbinder;
    }

    public int getCurrentPosition() {
        //得到当前播放位置
        if (mediaPlayer != null) {
            currentPosition = mediaPlayer.getCurrentPosition();
        }
        return currentPosition;
    }

    public MediaPlayer getMediaPlayer() {
        //得到 mediaPlayer
        return mediaPlayer;
    }

    public int getMusicPosition() {
        //得到当前播放第几个音乐
        return musicPosition;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        musicList = MainActivity.MusicList;
        // 监听播放是否完成
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //播放完自动下一首
                playNew();
            }
        });
    }

    private void playNew() {
        //播放下一首，这里用了循环的方法，如果需要随机播放等功能可在这里实现
        musicPosition = (++musicPosition) % musicList.size();
        url = musicList.get(musicPosition).getUrl();
        palyer();
    }

    private void palyer() {
        try {
            //重置播发器状态，然后设置音乐，开启新的进程
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            music = musicList.get(musicPosition);
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        //关闭线程
        Thread.currentThread().interrupt();
        stopForeground(true);
    }

    public String toTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        return String.format(Locale.CHINA, "%02d:%02d:%02d", hour, minute, second);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            msg = intent.getStringExtra("MSG");
            String noMusic = "0";
            String wantPause = "1";
            String wantPlay = "2";
            if (noMusic.equals(msg)) {
                url = intent.getStringExtra("url");
                musicPosition = intent.getIntExtra("musicPosition", 0);
                music = musicList.get(musicPosition);
                palyer();
            } else if (wantPause.equals(msg)) {
                mediaPlayer.pause();
            } else if (wantPlay.equals(msg)) {
                mediaPlayer.start();
            }
            String name = "Current: " + url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
//            //开启前台service
            Notification notification;
            Notification.Builder builder = new Notification.Builder(this);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, FileList.class), 0);
            builder.setContentIntent(contentIntent);
            builder.setContentTitle("Enter the MusicPlayer");
            builder.setContentText(name);
            notification = builder.build();
            startForeground(NOTIFICATION_ID, notification);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 自定义的 Binder对象
     */
    public class MusicBinder extends Binder {
        public MusicPlayerService getPlayInfo() {
            return MusicPlayerService.this;
        }
    }
}

