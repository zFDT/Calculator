package com.fdt.android.onemediaplay;

import android.Manifest;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.fdt.android.onemediaplay.ui.main.SectionsPagerAdapter;
import com.fdt.android.onemediaplay.ui.main.entities.FileEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Android Studio
 * @author 45169
 * Date:2020/6/9
 * Description:在这里获取音乐和视频的文件，开启服务，共享给所有的fragment
 */
public class MainActivity extends AppCompatActivity {
    /**
     * 要申请的权限放在PERMISSIONS_REQUIRED，同时获取它的数量，方便后面申请
     */
    private static final String[] PERMISSIONS_REQUIRED = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int PERMISSIONS_COUNT = PERMISSIONS_REQUIRED.length;
    /**
     * 创建两个列表作为APP存放文件的地方
     */
    public static List<FileEntity> MusicList, VideoList;

    /**
     * 将意图在Activity中创建，以便共享
     * playID是播放的歌曲ID，在这里也是进行共享作用，change是是否有进行改变
     */
    public static Intent intent = null;
    public static int playID = -1;
    public static boolean change = false;
    public static MusicPlayerService musicPlayerService = null;
    public static MediaPlayer mediaPlayer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //打开时检查，假设全部已经给权限了，进行检查，如果有一个没给权限那么全部重新申请一次
        for (int i = 0; i < PERMISSIONS_COUNT; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, PERMISSIONS_REQUIRED[i]) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_REQUIRED, 1);
                break;
            }
        }
        //初始化文件列表
        MusicList = new ArrayList<>();
        VideoList = new ArrayList<>();
        //获得内外部文件
        // EXTERNAL_CONTENT_URI是内部文件路径
        // INTERNAL_CONTENT_URL是外部文件路径
        MusicList.addAll(getFile(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI));
        MusicList.addAll(getFile(MediaStore.Audio.Media.INTERNAL_CONTENT_URI));
        VideoList.addAll(getFile(MediaStore.Video.Media.EXTERNAL_CONTENT_URI));
        VideoList.addAll(getFile(MediaStore.Video.Media.INTERNAL_CONTENT_URI));
        //初始化意图
        intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("musicPlayer");
        intent.setPackage(getPackageName());

        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setIcon(R.drawable.music);
        tabs.getTabAt(1).setIcon(R.drawable.video);
        tabs.getTabAt(2).setIcon(R.drawable.filelist);

    }


    /**
     * 在申请权限中实现的方法类，已复写
     * requestCode=1时表示需要进行申请
     * permission内存放的是是否有权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            //循环一一检测是否给了权限，如果有一个没给权限，那么我们将换个姿势再来一次
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝权限将无法使用程序，请全部选择同意", Toast.LENGTH_SHORT).show();
                    onResume();
                    break;
                }
            }
        }
    }

    /**
     * 获得对应文件列表
     * @param uri 文件在数据库中的位置，有类型（音频，视频），位置（内部，外部）的区分
     * @return 对应的文件列表
     */
    private List<FileEntity> getFile(Uri uri) {
        List<FileEntity> fileEntities = new ArrayList<>();
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        //获取的到数据时，判断条件为：第一条有数据，对此需要先判断cursor是否存在（如果查询语句错误则不存在），预防空指针问题
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String fileName = null, fileUrl = null;
                int fileTime = 0, type = -1;
                long fileSize = 0, fileDate = 0;
                //按类别来获得内容
                if ("audio/mp3".equals(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)))) {
                    fileName = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    fileUrl = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                    fileTime = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                    fileSize = cursor.getLong(cursor.getColumnIndexOrThrow("_size"));
                    fileDate = cursor.getLong(cursor.getColumnIndexOrThrow("date_added"));
                    type = 0;
                } else if ("video/mp4".equals(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)))) {
                    fileName = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    fileUrl = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                    fileTime = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                    fileSize = cursor.getLong(cursor.getColumnIndexOrThrow("_size"));
                    fileDate = cursor.getLong(cursor.getColumnIndexOrThrow("date_added"));
                    type = 1;
                }
                //keep解释：当
                // type不为-1时（文件为.mp3音乐时type=0，为.mp4视频时type=1，其余都为0）
                // 且
                // 文件时间大于60秒或大小大于100kb时（二者满足一个就行）
                //才成立（二者需同时满足）
                boolean keep = type != -1 && (fileTime >= 60 * 1000 || fileSize >= 100 * 1024);
                //文件小于60秒不存或者小于100k不存
                if (keep) {
                    FileEntity fileEntity = new FileEntity();
                    fileEntity.setName(fileName);
                    fileEntity.setUrl(fileUrl);
                    fileEntity.setTime(fileTime);
                    fileEntity.setSize(fileSize);
                    fileEntity.setCreateDate(fileDate);
                    fileEntity.setType(type);
                    fileEntities.add(fileEntity);
                }
                cursor.moveToNext();
            }
        }
        assert cursor != null;
        cursor.close();
        return fileEntities;
    }

}