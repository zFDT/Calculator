package com.fdt.android.onemediaplay.ui.main.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fdt.android.onemediaplay.MusicPlayerService;
import com.fdt.android.onemediaplay.R;
import com.fdt.android.onemediaplay.ui.main.entities.FileEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fdt.android.onemediaplay.MainActivity.MusicList;
import static com.fdt.android.onemediaplay.MainActivity.intent;
import static com.fdt.android.onemediaplay.MainActivity.mediaPlayer;
import static com.fdt.android.onemediaplay.MainActivity.musicPlayerService;


/**
 * Created by Android Studio
 * @author 45169
 * Date:2020/6/9
 * Description:音乐播放器，和FileLIST中的播放器共享
 */
public class MusicPlay extends Fragment implements View.OnClickListener {
    /**
     * 和播放音乐的服务有关的
     * @isPlay                  当前是否有播放
     * @isServiceRunning        退出应用再进入时（点击app图标或者在通知栏点击service）使用，判断服务是否在启动
     * @musicPosition           当前播放列表里哪首音乐
     * @conn                    自定义服务连接
     * @musicList               音乐文件列表
     */
    private boolean isPlay = false;
    private boolean isServiceRunning = false;
    private static int musicPosition = -1;
    private ServiceConnection conn;
    private List<FileEntity> musicList = new ArrayList<>();
    private ImageButton playPause;
    public static SeekBar seekBar;
    private static TextView music_name;
    private static TextView done_time;
    private static TextView total_time;
    private static Handler handler = null;
    static Runnable seekBarHandler = new Runnable() {
        @Override
        public void run() {
            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setProgress(musicPlayerService.getCurrentPosition());
            done_time.setText(musicPlayerService.toTime(musicPlayerService.getCurrentPosition()));
            handler.postDelayed(seekBarHandler, 1000);

        }
    };
    /**
     * 1s更新一次进度条
     */
    Runnable seekBarThread = new Runnable() {
        @Override
        public void run() {
            while (musicPlayerService != null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                seekBar.setProgress(musicPlayerService.getCurrentPosition());
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_music_play, container, false);
        ImageButton previous = v.findViewById(R.id.previous);
        playPause = v.findViewById(R.id.play_pause);
        ImageButton next = v.findViewById(R.id.next);
        seekBar = v.findViewById(R.id.seekBar);
        done_time = v.findViewById(R.id.done_time);
        total_time = v.findViewById(R.id.total_time);
        music_name = v.findViewById(R.id.musice_name);
        handler = new Handler();
        musicList = MusicList;
        //退出后再次进去程序时，进度条保持持续更新
        if (MusicPlayerService.mediaPlayer != null) {
            reinit();//更新页面布局以及变量相关
        }
        //设置进度条
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (musicPosition == -1) {
                    showInfo("还未选择播放音乐");
                } else {
                    if (fromUser) {
                        //这里有个问题，如果播放时用户拖进度条还好说，但是如果是暂停时，拖完会自动播放，所以还需要把图标设置一下
                        playPause.setImageResource(R.drawable.musicplay);
                        MusicPlayerService.mediaPlayer.seekTo(progress);// 当进度条的值改变时，音乐播放器从新的位置开始播放
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
            }
        });
        conn = new ServiceConnection() {
            /** 获取服务对象时的操作 */
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //获得服务的线程号
                musicPlayerService = ((MusicPlayerService.MusicBinder) service).getPlayInfo();
                //获得播放器
                mediaPlayer = musicPlayerService.getMediaPlayer();
                //获得播放音乐的id
                musicPosition = musicPlayerService.getMusicPosition();
                //设置进度条最大值
                seekBar.setMax(mediaPlayer.getDuration());
                handler.post(seekBarHandler);
            }

            /** 无法获取到服务对象时的操作 */
            @Override
            public void onServiceDisconnected(ComponentName name) {
                musicPlayerService = null;
            }
        };
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        playPause.setOnClickListener(this);
        return v;
    }

    /**
     * 实现player的三种方式
     */
    private void player(String info) {
        //放入播放转态
        intent.putExtra("MSG", info);
        isPlay = true;
        playPause.setImageResource(R.drawable.musicstop);
        Objects.requireNonNull(getActivity()).startService(intent);
    }

    private void player(int i) {
        musicPosition = i;
        FileEntity playmusic = musicList.get(i);
        music_name.setText(playmusic.getName());
        //把位置传回去，方便再启动时调用
        intent.putExtra("musicPosition", i);
        intent.putExtra("url", playmusic.getUrl());
        intent.putExtra("MSG", "0");
        total_time.setText(playmusic.getTime());
        //播放改变播放状态与图标
        isPlay = true;
        playPause.setImageResource(R.drawable.musicstop);
        Objects.requireNonNull(getActivity()).startService(intent);
        getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private void player() {
        player(musicPosition);
    }

    private void showInfo(String info) {
        Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
    }

    private void pause() {
        intent.putExtra("MSG", "1");
        isPlay = false;
        playPause.setImageResource(R.drawable.musicplay);
        Objects.requireNonNull(getActivity()).startService(intent);
    }

    private void reinit() {
        isServiceRunning = true;
        //如果是正在播放
        if (MusicPlayerService.mediaPlayer.isPlaying()) {
            isPlay = true;
            music_name.setText(MusicPlayerService.music.getName());
            total_time.setText(MusicPlayerService.music.getTime());
            playPause.setImageResource(R.drawable.musicplay);
        }
        //重新绑定service
        Objects.requireNonNull(getActivity()).bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous:
                if (musicPosition > 0) {
                    musicPosition -= 1;
                } else {
                    musicPosition = MusicList.size() - 1;
                }
                player();
                break;
            case R.id.next:
                int temp = 2;
                if (musicPosition < MusicList.size() - temp) {
                    musicPosition += 1;
                } else {
                    musicPosition = 0;
                }
                player();
                break;
            case R.id.play_pause:
                //服务启动着，这里点击播放暂停按钮时只需要当前音乐暂停或者播放就好
                if (isServiceRunning) {
                    if (isPlay) {
                        pause();
                    } else {
                        //暂停--->继续播放
                        player("2");
                    }
                } else {
                    if (isPlay) {
                        pause();
                    } else {
                        //服务没有启动，如果点击了播放默认从0开始播放
                        musicPosition = 0;
                        player();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(seekBarHandler);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
