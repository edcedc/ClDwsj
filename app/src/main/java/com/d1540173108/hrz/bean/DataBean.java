package com.d1540173108.hrz.bean;


import android.graphics.Bitmap;

import com.d1540173108.hrz.utils.GlideLoadingUtils;

import java.io.Serializable;

/**
 * Created by yc on 2017/8/17.
 */

public class DataBean implements Serializable {

    private String name;
    private int img;
    private boolean isSelect = false;
    private int position;
    private String title;
    private String content;
    private String image;
    private int type;
    private String id;
    private String path;
    private String orgName;
    private String createTime;
    private String storyName;
    private String urlPic;
    private String musicTips;
    private String urlMp3;
    private String storyId;
    private String timeMusic;
    private String downMp3;
    private String time;
    private double probability;
    private Bitmap bitmpPath;
    private String spreadImg;
    private String knowledgeImg;
    private int useMethod;
    private String knowledgeTitle;
    private String knowledgeUrl;
    private String bannerImg;
    private String bannerTitle;

    public String getBannerTitle() {
        return bannerTitle;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public String getKnowledgeUrl() {
        return knowledgeUrl;
    }

    public String getKnowledgeTitle() {
        return knowledgeTitle;
    }

    public int getUseMethod() {
        return useMethod;
    }

    public String getKnowledgeImg() {
        return knowledgeImg;
    }

    public String getSpreadImg() {
        return spreadImg;
    }

    public Bitmap getBitmpPath() {
        return bitmpPath;
    }

    public void setBitmpPath(Bitmap bitmpPath) {
        this.bitmpPath = bitmpPath;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    public void setUrlPic(String urlPic) {
        this.urlPic = urlPic;
    }

    public void setMusicTips(String musicTips) {
        this.musicTips = musicTips;
    }

    public void setUrlMp3(String urlMp3) {
        this.urlMp3 = urlMp3;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public void setTimeMusic(String timeMusic) {
        this.timeMusic = timeMusic;
    }

    public void setDownMp3(String downMp3) {
        this.downMp3 = downMp3;
    }

    public String getDownMp3() {
        return downMp3;
    }

    public String getTimeMusic() {
        return timeMusic;
    }

    public String getStoryId() {
        return storyId;
    }

    public String getUrlMp3() {
        return urlMp3;
    }

    public String getMusicTips() {
        return musicTips;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getUrlPic() {
        return urlPic;
    }

    public String getStoryName() {
        return storyName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}