package com.d1540173108.hrz.bean;

import org.litepal.crud.LitePalSupport;

/**
 * Created by edison on 2019/1/24.
 */

public class SaveMusicListBean extends LitePalSupport {

    private String storyName;
    private String musicTips;
    private String urlPic;
    private String urlMp3;
    private String downMp3;
    private String storyId;
    private int position;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    public String getMusicTips() {
        return musicTips;
    }

    public void setMusicTips(String musicTips) {
        this.musicTips = musicTips;
    }

    public String getUrlPic() {
        return urlPic;
    }

    public void setUrlPic(String urlPic) {
        this.urlPic = urlPic;
    }

    public String getUrlMp3() {
        return urlMp3;
    }

    public void setUrlMp3(String urlMp3) {
        this.urlMp3 = urlMp3;
    }

    public String getDownMp3() {
        return downMp3;
    }

    public void setDownMp3(String downMp3) {
        this.downMp3 = downMp3;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
