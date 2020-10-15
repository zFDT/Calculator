package com.fdt.android.onemediaplay.ui.main.fragments;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fdt.android.onemediaplay.MainActivity;
import com.fdt.android.onemediaplay.MusicPlayerService;
import com.fdt.android.onemediaplay.R;
import com.fdt.android.onemediaplay.ui.main.entities.FileEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.fdt.android.onemediaplay.MainActivity.intent;
import static com.fdt.android.onemediaplay.MainActivity.mediaPlayer;
import static com.fdt.android.onemediaplay.MainActivity.musicPlayerService;

/**
 * Created by Android Studio
 * @author 45169
 * Date:2020/6/9
 * Description:显示文件列表的fragment，通过按钮控制分别显示的是视频文件还是音频文件
 *  在音频文件中设置一简单播放器，可实时播放。（此播放器与MusicPlay中的播放器共享）
 *  在视频文件中需要点击完文件后到VideoPlay进行播放
 *  目前删除按钮功能未完全实现，无法从物理上根本删除文件
 */
@SuppressWarnings("ALL")
public class FileList extends Fragment implements View.OnClickListener {
    /**
     * topbar 内的按钮，用来区分是music还是moive列表
     * @musicButton 点击后切换到音乐列表
     * @videoButton 点击后切换到视频列表
     * @topBar      顶部的BAR控件
     * @musicBar    底部的BAR控件
     * @bartype     判断此时处于那一个列表
     */
    private Button musicButton;
    private Button videoButton;
    private LinearLayout topBar, musicBar;
    private boolean barType = false;

    /**
     * 和回收视图有关
     * @recyclerView    在xml中的RecyclerView
     * @fileAdapter     自定义类，展示music列表用
     * @musicList       存储音频文件列表
     * @videoList       存储视频文件列表
     */
    private RecyclerView recyclerView;
    private FileAdapter fileAdapter;
    private List<FileEntity> musicList = new ArrayList<>();
    private List<FileEntity> videoList = new ArrayList<>();

    /**
     * 和音乐的服务有关的
     * @isPlay                  当前是否有播放
     * @isServiceRunning        退出应用再进入时（点击app图标或者在通知栏点击service）使用，判断服务是否在启动
     * @mLastY                  标记上下滑动时上次滑动位置,滑动隐藏上下标题栏
     * @musicPosition           当前播放列表里哪首音乐
     * @conn                    自定义服务连接
     */
    private boolean isPlay = false;
    private boolean isServiceRunning = false;
    private float mLastY = -1;
    public static int musicPosition = -1;
    private ServiceConnection conn;

    /**
     * 和系统有关
     * @isExit      返回键判断
     */
    private boolean isExit = false;

    /**
     * music_bottom内的组件
     * @previous    上一首
     * @playPause   播放/暂停
     * @next        下一首
     * @seekBar     进度条
     * @music_name  此时播放音乐名
     * @done_time   此时播放音乐时间点
     * @total_time  此时播放音乐的总时长
     * @Handler     更新进度条和时间用
     * @seekBarHandler 自定义进程
     */
    private ImageButton previous;
    private ImageButton playPause;
    private ImageButton next;
    public static SeekBar seekBar;
    public static TextView music_name;
    private static TextView done_time;
    private static TextView total_time;
    public static Handler handler = null;

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
        View v = inflater.inflate(R.layout.fragment_file_list, container, false);
        //初始化各种控件
        recyclerView = v.findViewById(R.id.recycler);
        musicButton = v.findViewById(R.id.MusicButton);
        videoButton = v.findViewById(R.id.VideoButton);
        previous = v.findViewById(R.id.previous);
        playPause = v.findViewById(R.id.play_pause);
        next = v.findViewById(R.id.next);
        seekBar = v.findViewById(R.id.seekBar);
        done_time = v.findViewById(R.id.done_time);
        total_time = v.findViewById(R.id.total_time);
        music_name = v.findViewById(R.id.musice_name);
        //初始化数据
        handler = new Handler();
        musicList = MainActivity.MusicList;
        videoList = MainActivity.VideoList;
        topBar = v.findViewById(R.id.music_top);
        musicBar = v.findViewById(R.id.music_bottom);
        //设置回收视图
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fileAdapter = new FileAdapter(musicList);
        recyclerView.setAdapter(fileAdapter);
        //退出后再次进去程序时，进度条保持持续更新
        if (MusicPlayerService.mediaPlayer != null) {
            //更新页面布局以及变量相关
            reinit();
        }
        //设置进度条
        setBar();
        //设置上下滚动时
        setUpAndDown();
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        playPause.setOnClickListener(this);
        musicButton.setOnClickListener(this);
        videoButton.setOnClickListener(this);
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
                System.out.println("没有服务对象");
                musicPlayerService = null;
            }
        };
        return v;
    }

    /**
     * 设置上下滚动时，显示Top或者Music，或者都不显示的BAR
     */
    private void setUpAndDown() {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                if (barType) {
                    topBar.setVisibility(View.VISIBLE);
                    musicBar.setVisibility(View.GONE);
                    return false;
                }
                if (mLastY == -1) {
                    mLastY = event.getRawY();
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        //判断上滑还是下滑
                        if (event.getRawY() > mLastY) {
                            //下滑显示bottom，隐藏top
                            topBar.setVisibility(View.GONE);
                            musicBar.setVisibility(View.VISIBLE);
                        } else if (event.getRawY() < mLastY) {
                            //上滑，显示top，隐藏bottom
                            topBar.setVisibility(View.VISIBLE);
                            musicBar.setVisibility(View.GONE);
                        } else {
                            // deltaY = 0.0 时
                            topBar.setVisibility(View.VISIBLE);
                            musicBar.setVisibility(View.VISIBLE);
                            mLastY = event.getRawY();
                            return false;//返回false即可响应click事件
                        }
                        mLastY = event.getRawY();
                        break;
                    default:
                        // reset
                        mLastY = -1;
                        topBar.setVisibility(View.VISIBLE);
                        musicBar.setVisibility(View.VISIBLE);
                        break;
                }
                return false;
            }
        });
    }

    /**
     *初始化musicBar
     */
    private void setBar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (musicPosition == -1) {
                    showInfo("还未选择播放音乐");
                }
                if (fromUser) {
                    //这里有个问题，如果播放时用户拖进度条还好说，但是如果是暂停时，拖完会自动播放，所以还需要把图标设置一下
                    playPause.setImageResource(R.drawable.musicstop);
                    MusicPlayerService.mediaPlayer.seekTo(progress);// 当进度条的值改变时，音乐播放器从新的位置开始播放
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
    }

    /**
     * 实现player的三种方式
     */
    private void player(String info) {
        //放入播放转态
        intent.putExtra("MSG", info);
        isPlay = true;
        playPause.setImageResource(R.drawable.musicstop);
        getActivity().startService(intent);
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
        isServiceRunning = true;
        playPause.setImageResource(R.drawable.musicstop);
        getActivity().startService(intent);
        getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);

    }

    private void player() {
        player(musicPosition);
    }

    /**
     * 显示前台Toast
     * @param info 要显示的内容
     */
    private void showInfo(String info) {
        Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
    }

    /**
     * 当点击停止时的操作
     */
    private void pause() {
        intent.putExtra("MSG", "1");
        isPlay = false;
        playPause.setImageResource(R.drawable.musicplay);
        getActivity().startService(intent);
    }

    /**
     * 双击返回时的操作
     * @param info
     */
    private void exit(String info) {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            getActivity().finish();
        }
    }

    private void reinit() {
        //设置进度条最大值
        isServiceRunning = true;
        //如果是正在播放
        if (MusicPlayerService.mediaPlayer.isPlaying()) {
            isPlay = true;
            playPause.setImageResource(R.drawable.musicplay);
        }
        //重新绑定service
        getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.MusicButton:
                //如果点击MusicButton切换为含有musicList的材料视图，并改变bartype的转态，供setUpAndDown()实现
                fileAdapter = new FileAdapter(musicList);
                recyclerView.setAdapter(fileAdapter);
                barType = false;
                break;
            case R.id.VideoButton:
                //如果点击MusicButton切换为含有videoList的材料视图，并改变bartype的转态，供setUpAndDown()实现
                fileAdapter = new FileAdapter(videoList);
                recyclerView.setAdapter(fileAdapter);
                musicBar.setVisibility(View.GONE);
                barType = true;
                break;
            case R.id.previous:
                //点击上一首会进行判断，如果到头就从尾开始
                if (musicPosition > 0) {
                    musicPosition -= 1;
                } else {
                    musicPosition = musicList.size() - 1;
                }
                player();
                break;
            case R.id.next:
                //点击下一首会进行判断，如果到尾了就从头开始，这里的temp没有实际含义，只为编码规范，不出现魔法值
                int temp = 2;
                if (musicPosition < musicList.size() - temp) {
                    musicPosition += 1;
                } else {
                    musicPosition = 0;
                }
                player();
                break;
            case R.id.play_pause:
                //直接点击播放将从头开始播放
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

    private static class FileHolder extends RecyclerView.ViewHolder {
        public FileEntity fileEntity;
        ImageButton deleteButton;
        private TextView name;
        private TextView url;
        private TextView time;
        private TextView size;
        private TextView createDate;
        private LinearLayout linearLayout;

        public FileHolder(LayoutInflater inflater, ViewGroup viewGroup) {
            super(inflater.inflate(R.layout.one_file_card, viewGroup, false));
            name = itemView.findViewById(R.id.Name);
            url = itemView.findViewById(R.id.Url);
            time = itemView.findViewById(R.id.Time);
            size = itemView.findViewById(R.id.Size);
            createDate = itemView.findViewById(R.id.CreateDate);
            deleteButton = itemView.findViewById(R.id.DeleteButton);
            linearLayout = itemView.findViewById(R.id.LinearLayout);
        }

        void setFile(FileEntity fileEntity) {
            this.fileEntity = fileEntity;
            name.setText(fileEntity.getName());
            url.setText(fileEntity.getUrl());
            time.setText(fileEntity.getTime());
            size.setText(fileEntity.getSize());
            createDate.setText(fileEntity.getCreateDate());
        }
    }

    private class FileAdapter extends RecyclerView.Adapter<FileHolder> {
        private List<FileEntity> fileEntityList;

        private FileAdapter(List<FileEntity> fileEntityList) {
            this.fileEntityList = fileEntityList;
        }

        @NonNull
        @Override
        public FileHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new FileHolder(inflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull FileHolder holder, int i) {
            final int tempI = i;
            holder.setFile(fileEntityList.get(i));
            //当点击删除的按钮时，实现
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FileEntity fileEntity = fileEntityList.get(tempI);
                    File file = new File(fileEntity.getUrl());
                    //判断文件的类型是音乐还是视频，分别调用不同的方法进行删除
                    if (fileEntity.getType() == 0) {
                        try {
                            getContext().getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, fileEntity.getUrl(), null);
                            getContext().getContentResolver().delete(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, fileEntity.getUrl(), null);
                            file.delete();
                        } catch (Exception e) {
                            System.out.println("删除失败");
                        }
                        MainActivity.MusicList.remove(tempI);
                        fileEntityList.remove(tempI);
                        notifyItemRemoved(tempI);
                    } else {
                        try {
                            getContext().getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, fileEntity.getUrl(), null);
                            getContext().getContentResolver().delete(MediaStore.Video.Media.INTERNAL_CONTENT_URI, fileEntity.getUrl(), null);
                            file.delete();
                        } catch (Exception e) {
                            System.out.println("删除失败");
                        }
                        MainActivity.VideoList.remove(tempI);
                        fileEntityList.remove(tempI);
                        notifyItemRemoved(tempI);
                    }
                }
            });
            //当点击文件时
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FileEntity fileEntity = fileEntityList.get(tempI);
                    //判断文件类型，如果是音乐直接当前文件播放，如果是视频修改MainActivity对应参数给VideoPlay使用
                    if (fileEntity.getType() == 0) {
                        player(tempI);
                    } else {
                        MainActivity.playID = tempI;
                        MainActivity.change = true;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return fileEntityList.size();
        }

    }
}