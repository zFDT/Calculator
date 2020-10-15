package com.fdt.android.onemediaplay.ui.main.fragments;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fdt.android.onemediaplay.MainActivity;
import com.fdt.android.onemediaplay.R;
import com.fdt.android.onemediaplay.ui.main.entities.FileEntity;

import java.util.ArrayList;
import java.util.List;

import static com.fdt.android.onemediaplay.MainActivity.mediaPlayer;

/**
 * Created by Android Studio
 * @author 45169
 * Date:2020/6/9
 * Description:视频播放器，需要先到FileList点击视频后才可以播放
 */
public class VideoPlay extends Fragment implements View.OnClickListener {
    private final String TAG = "main";
    public int playId = MainActivity.playID;
    private SurfaceView surfaceView;
    private ImageButton videoPlay;
    private TextView doneTime, totalTime;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private int currentPosition = 0;
    private boolean isPlaying = false;
    private FileEntity video;
    private List<FileEntity> videoList = new ArrayList<>();
    private Handler handler = null;
    /**
     * 构建Runnable对象，在runnable中更新界面
     */
    Runnable runnableUi = new Runnable() {
        @Override
        public void run() {
            //更新界面
            int current = mediaPlayer.getCurrentPosition();
            seekBar.setProgress(current);
            toTime(mediaPlayer.getCurrentPosition());
        }

    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video_play, container, false);
        surfaceView = v.findViewById(R.id.surfaceView);
        seekBar = v.findViewById(R.id.seekBar2);
        videoPlay = v.findViewById(R.id.play_pause);
        doneTime = v.findViewById(R.id.done_time);
        totalTime = v.findViewById(R.id.total_time);
        videoList = MainActivity.VideoList;
        //创建属于主线程的handler
        handler = new Handler();
        videoPlay.setOnClickListener(this);
        if (playId != -1) {
            video = videoList.get(playId);
        }
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // 销毁SurfaceHolder的时候记录当前的播放位置并停止播放
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    currentPosition = mediaPlayer.getCurrentPosition();
                    mediaPlayer.stop();
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                }
            }

            // 当进度条停止修改的时候触发
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 取得当前进度条的刻度
                int progress = seekBar.getProgress();
                if (mediaPlayer != null) {
                    // 设置当前播放的位置
                    mediaPlayer.seekTo(progress);
                    mediaPlayer.start();
                }
            }
        });
        return v;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.play_pause) {
            if (isPlaying) {
                System.out.println("进入暂停");
                pause();
            } else {
                System.out.println("进入播放");
                play();
            }
        }
    }

    private void play() {
        try {
            playId = MainActivity.playID;
            boolean change = MainActivity.change;
            //没有点击视频文件是mediaPlay没有创建，playId=-1，将显示Toast
            if (mediaPlayer == null) {
                if (playId == -1) {
                    Toast.makeText(getActivity(), "您还未添加视频，请到FileList-moive添加视频，再回来播放", Toast.LENGTH_SHORT).show();
                } else {
                    //点击后进来播放mediaPlay没有创建为空，playId不为-1，将设置相关数据
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.reset();
                    video = videoList.get(playId);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    // 设置播放的视频源
                    mediaPlayer.setDataSource(video.getUrl());
                    // 设置显示视频的SurfaceHolder
                    mediaPlayer.setDisplay(surfaceView.getHolder());
                    mediaPlayer.prepareAsync();
                    totalTime.setText(video.getTime());
                    currentPosition = 0;
                    MainActivity.change = false;
                }
            } else if (change) {
                //如果mediaPlay不为空，且change为true，代表重新选择了视频，需要重置mediaPlay然后设置相关数据
                mediaPlayer.reset();
                video = videoList.get(playId);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                // 设置播放的视频源
                mediaPlayer.setDataSource(video.getUrl());
                // 设置显示视频的SurfaceHolder
                mediaPlayer.setDisplay(surfaceView.getHolder());
                mediaPlayer.prepareAsync();
                totalTime.setText(video.getTime());
                currentPosition = 0;
                MainActivity.change = false;
            }
            //如果mediaPlay不为空，且进度为0，就开启线程来监视进度，并刷新进度条数据
            if (mediaPlayer != null && currentPosition == 0) {
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        videoPlay.setImageResource(R.drawable.videosuspend);
                        mediaPlayer.start();
                        // 按照初始位置播放
                        mediaPlayer.seekTo(currentPosition);
                        // 设置进度条的最大进度为视频流的最大播放时长
                        seekBar.setMax(mediaPlayer.getDuration());
                        // 开始线程，更新进度条的刻度
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    isPlaying = true;
                                    while (isPlaying) {
                                        handler.post(runnableUi);
                                        sleep(1000);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                });
            } else if (mediaPlayer != null && currentPosition != 0) {
                //如果mediaPlay不为空，且进度不为0，代表只是暂停而已，因此将mediaPlay设置为播放，isPlaying设置为播放（true），改变图标
                isPlaying = true;
                videoPlay.setImageResource(R.drawable.videosuspend);
                mediaPlayer.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停，保存当前进度条进度，设置isPlaying为暂停，false，改变图标
     */
    private void pause() {
        currentPosition = mediaPlayer.getCurrentPosition();
        videoPlay.setImageResource(R.drawable.videoplay);
        isPlaying = false;
        mediaPlayer.pause();
    }

    /**
     * 在线程中使用，实时获取时间点并将其格式化为标注时间并设置在doneTime
     * @param time 线程中获取的时间节点
     */
    public void toTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        doneTime.setText(String.format("%02d:%02d:%02d", hour, minute, second));
    }

    @Override
    public void onPause() {
        super.onPause();
        pause();
        handler.removeCallbacks(runnableUi);
    }
}