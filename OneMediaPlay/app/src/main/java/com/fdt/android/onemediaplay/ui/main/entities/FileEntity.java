package com.fdt.android.onemediaplay.ui.main.entities;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Android Studio
 * @author 45169
 * Date:2020/6/9
 * Description:File的实体，存储音频文件和视频文件都用他
 */
public class FileEntity {
    /**
     * @name    文件的名字
     * @url     文件的路径
     * @time    文件的时长
     * @size    文件的大小
     * @createDate  文件的创建日期
     * @image   文件对应的图片（还未开发，后续版本增加）
     * @type    文件的类型（是视频还是音频）
     */
    private String name;
    private String url;
    private String time;
    private String size;
    private String createDate;
    private String image;
    private int type;

    public FileEntity() {
    }

    public FileEntity(String name, String url, String time, String size, String createDate, String image) {
        this.name = name;
        this.url = url;
        this.time = time;
        this.size = size;
        this.createDate = createDate;
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    /**
     * 将获得的毫秒转换为标准的 时：分：秒 格式
     * @param time 数据库获得的时间，单位为毫秒
     */
    public void setTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        this.time = String.format(Locale.CHINA, "%02d:%02d:%02d", hour, minute, second);
    }

    public String getSize() {
        return size;
    }

    /**
     * 将获得的KB按大小分为：GB，MB，KB单位的标准格式
     * @param size 数据库获得的大小，单位为kb
     */
    public void setSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            this.size = String.format(Locale.CHINA, "%.1fGB", (float) size / gb);
        } else if (size >= mb) {
            float temp = (float) size / mb;
            this.size = String.format(temp > 100 ? "%.0f MB" : "%.1f MB", temp);
        } else {
            float temp = (float) size / kb;
            this.size = String.format(Locale.CHINA, "%.0f KB", temp);
        }
    }

    public String getCreateDate() {
        return createDate;
    }

    /**
     * 将获得的时间点转化为标准的 年-月-日 格式
     * @param createDate 数据库获得的时间，单位为毫秒
     */
    public void setCreateDate(long createDate) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        this.createDate = sdf.format(new Date(Long.parseLong(createDate + "000")));

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", time='" + time + '\'' +
                ", size='" + size + '\'' +
                ", createDate='" + createDate + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
