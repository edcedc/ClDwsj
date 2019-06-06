package com.d1540173108.hrz.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.SeekBar;
import com.blankj.utilcode.util.StringUtils;
import com.d1540173108.hrz.MainActivity;
import com.d1540173108.hrz.event.AppointMusicInEvent;
import com.d1540173108.hrz.event.MediaSuccessInEvent;
import com.d1540173108.hrz.event.PhoneListenInEvent;
import com.d1540173108.hrz.event.PlayMusicEndInEvent;
import com.d1540173108.hrz.service.MediaService;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.adapter.AnimalDescAdapter;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.databinding.FAnimalDescBinding;
import com.d1540173108.hrz.presenter.AnimalDescPresenter;
import com.d1540173108.hrz.utils.GlideLoadingUtils;
import com.d1540173108.hrz.view.impl.AnimalDescContract;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/11.
 * 动物详情
 */

public class AnimalDescFrg extends BaseFragment<AnimalDescPresenter, FAnimalDescBinding> implements AnimalDescContract.View, View.OnClickListener, OnBannerListener {

    private String id;
    private String link;
    private List<DataBean> listBannerBean;
    private JSONObject data;
    private String mp3Url;
    private String path;

    public static AnimalDescFrg newInstance() {
        Bundle args = new Bundle();
        AnimalDescFrg fragment = new AnimalDescFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private List<DataBean> listBean = new ArrayList<>();
    private AnimalDescAdapter adapter;
    private int mLayoutWonderfulStoryHeight = 0;  //动画执行的padding高度
    private int mLayoutEncyclopedicKnowledgeHeight = 0;  //动画执行的padding高度
    private int mLayoutWonderfulStoryHeight2 = 0;  //动画执行的padding高度
    private int mLayoutEncyclopedicKnowledgeHeight2 = 0;  //动画执行的padding高度
    private boolean isOpen = false; //是否开启状态
    private boolean isOpen2 = false; //是否开启状态

    //进度条下面的当前进度文字，将毫秒化为m:ss格式
    private SimpleDateFormat time = new SimpleDateFormat("m:ss");

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        id = bundle.getString("id");
        path = bundle.getString("path");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_animal_desc;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setTitle(getString(R.string.details), R.mipmap.y22, R.color.white, R.color.blue_6759AE);
    }

    @Override
    protected void initView(View view) {
        MainActivity.mMyBinder.pauseMusic();
        MediaService.singlePlayType = 0;
        mB.ivMore.setOnClickListener(this);
        mB.ivJust.setOnClickListener(this);
        mB.ivWonderfulStory.setOnClickListener(this);
//        mB.ivPlay.setOnClickListener(this);
        mB.ivPlayImg.setOnClickListener(this);
        mB.ivPlayImg.setEnabled(false);
        mB.ivEncyclopedicKnowledge.setOnClickListener(this);
        if (adapter == null) {
            adapter = new AnimalDescAdapter(act, listBean);
        }
        mB.listView.setAdapter(adapter);
        showLoadDataing();
        mB.refreshLayout.setPureScrollModeOn();
        mB.banner.updateBannerStyle(BannerConfig.NOT_INDICATOR);
        mB.banner.setOnBannerListener(this);
        mB.banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                DataBean bean = listBannerBean.get(position);
                if (bean.getContent().equals("vedio")) {
                    mB.ivPlay.setVisibility(View.VISIBLE);
                } else {
                    mB.ivPlay.setVisibility(View.GONE);
                }
                mB.tvBannerSize.setText((position += 1) + "/" + listBannerBean.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPresenter.onRequest(id);
        mB.musicSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //这里很重要，如果不判断是否来自用户操作进度条，会不断执行下面语句块里面的逻辑，然后就会卡顿卡顿
                if (fromUser) {
                    MainActivity.mMyBinder.seekToPositon(seekBar.getProgress());
//                    mMediaService.mMediaPlayer.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new PlayMusicEndInEvent());
        MainActivity.mMyBinder.pauseMusic();
        MediaService.singlePlayType = -1;
        EventBus.getDefault().post(new PhoneListenInEvent(true));
        mHandler.removeCallbacks(mRunnable);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainMediaInEvent(MediaSuccessInEvent event) {
        mB.ivPlayImg.setEnabled(true);
        mB.musicSeekbar.setMax(MainActivity.mMyBinder.getProgress());
        setMusicData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainAppointMusicInEvent(AppointMusicInEvent event) {
        mB.musicSeekbar.setMax(MainActivity.mMyBinder.getProgress());
        mB.ivPlayImg.setBackgroundResource(R.mipmap.y45);
        setMusicData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainPlayMusicEndInEvent(PlayMusicEndInEvent event) {
        mB.ivPlayImg.setBackgroundResource(R.mipmap.y44);
        MainActivity.mMyBinder.playLocal2(mp3Url);
    }

    private void setMusicData() {
        mB.tvEndTime.setText(time.format(MainActivity.mMyBinder.getProgress()));
    }

    /**
     * 更新ui的runnable
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (MainActivity.mMyBinder.isPlaying()) {
                mB.musicSeekbar.setProgress(MainActivity.mMyBinder.getPlayPosition());
                mB.tvStartTime.setText(time.format(MainActivity.mMyBinder.getPlayPosition()));
            }
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void setOnRightClickListener() {
        super.setOnRightClickListener();
        UIHelper.startShareFrg(this, data, path);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_encyclopedic_knowledge:
                boolean look = adapter.isLook();
                if (!look) {
                    adapter.setLook(true);
                } else {
                    adapter.setLook(false);
                }
                adapter.notifyDataSetChanged();
//                setValueAnimator2(mLayoutEncyclopedicKnowledgeHeight2, mB.listView, mB.ivEncyclopedicKnowledge);
                break;
            case R.id.iv_wonderful_story:
                setValueAnimator(mLayoutWonderfulStoryHeight, mB.tvWonderfulStory, mB.ivWonderfulStory);
                break;
            case R.id.iv_more:
                UIHelper.startAnimalEncyclopediaAct();
                break;
            case R.id.iv_just:
                UIHelper.startHtmlAct("亲子游", link);
                break;
            case R.id.iv_play_img:
                mHandler.post(mRunnable);
                if (MainActivity.mMyBinder.isPlaying()) {
                    MainActivity.mMyBinder.pauseMusic();
                    mB.ivPlayImg.setBackgroundResource(R.mipmap.y44);
                } else {
                    MainActivity.mMyBinder.playMusic();
                    mB.ivPlayImg.setBackgroundResource(R.mipmap.y45);
                }
                break;
        }
    }

    private void setValueAnimator(final int height, final View layout, final View imageView) {
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        if (!isOpen) {
            //隐藏当前控件
            params.height = 400;
            imageView.setVisibility(View.VISIBLE);
        } else {
            params.height = height;
        }
        layout.setLayoutParams(params);
        isOpen = !isOpen;
    }

    private void setValueAnimator2(final int height, final View layout, final View imageView) {
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        if (!isOpen2) {
            //隐藏当前控件
            params.height = 400;
            imageView.setVisibility(View.VISIBLE);
        } else {
            params.height = height;
        }
        layout.setLayoutParams(params);
        isOpen2 = !isOpen2;
    }


    @Override
    public void setData(Object object) {
        data = (JSONObject) object;
        JSONArray media = data.optJSONArray("media");
        if (media != null && media.length() != 0) {
            for (int i = 0; i < media.length(); i++) {
                JSONObject object1 = media.optJSONObject(i);
                if (object1.optString("type").equals("sound")) {//判断是否有音频
                    mp3Url = object1.optString("url");
                    MainActivity.mMyBinder.playLocal2(mp3Url);
                    mB.lyMusic.setVisibility(View.VISIBLE);
                    break;
                } else {
                    mB.lyMusic.setVisibility(View.GONE);
                }
            }

            listBannerBean = new ArrayList<>();
            for (int i = 0; i < media.length(); i++) {
                JSONObject image = media.optJSONObject(i);
                if (image.optString("type").equals("img")) {
                    DataBean bean = new DataBean();
                    bean.setImage(image.optString("url"));
                    bean.setContent(image.optString("type"));
                    listBannerBean.add(bean);
                }
            }
            for (int i = 0; i < media.length(); i++) {
                JSONObject image = media.optJSONObject(i);
                if (image.optString("type").equals("vedio")) {
                    DataBean bean = new DataBean();
                    if (listBannerBean.size() != 0) {
                        bean.setImage(listBannerBean.get(0).getImage());
                    }
                    bean.setPath(image.optString("url"));
                    bean.setContent(image.optString("type"));
                    listBannerBean.add(0, bean);
                    break;
                }
            }

            //默认是CIRCLE_INDICATOR
            mB.banner.setImages(listBannerBean)
                    .setImageLoader(new ImageLoader() {
                        @Override
                        public void displayImage(Context context, Object path, ImageView imageView) {
                            DataBean bean = (DataBean) path;
                            GlideLoadingUtils.load(act, bean.getImage(), imageView);
                        }
                    })
                    .start();
        }
        mB.tvName.setText(data.optString("name"));
        mB.tvWonderful.setText(data.optString("name") + "在长隆");
        mB.tvNick.setText(data.optString("enName"));
        mB.tvWonderfulStory.setText(data.optString("story"));
        mB.tvWonderfulStory.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mB.tvWonderfulStory.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mLayoutWonderfulStoryHeight = mB.tvWonderfulStory.getMeasuredHeight();

                if (mLayoutWonderfulStoryHeight > 400) {
                    ViewGroup.LayoutParams params = mB.tvWonderfulStory.getLayoutParams();
                    params.height = 400;
                    mB.tvWonderfulStory.setLayoutParams(params);
                    setValueAnimator(mLayoutWonderfulStoryHeight, mB.tvWonderfulStory, mB.ivWonderfulStory);
                }
            }
        });
        String intro = data.optString("intro");
        if (!StringUtils.isEmpty(intro)) {
            DataBean bean = new DataBean();
            bean.setName("简介");
            bean.setContent(intro);
            listBean.add(bean);
        }
        String feature = data.optString("feature");
        if (!StringUtils.isEmpty(feature)) {
            DataBean bean = new DataBean();
            bean.setName("特征");
            bean.setContent(feature);
            listBean.add(bean);
        }
        String family = data.optString("family");
        if (!StringUtils.isEmpty(family)) {
            DataBean bean = new DataBean();
            bean.setName("科");
            bean.setContent(family);
            listBean.add(bean);
        }
        String distribution = data.optString("distribution");
        if (!StringUtils.isEmpty(distribution)) {
            DataBean bean = new DataBean();
            bean.setName("分布");
            bean.setContent(distribution);
            listBean.add(bean);
        }
        String habits = data.optString("habits");
        if (!StringUtils.isEmpty(habits)) {
            DataBean bean = new DataBean();
            bean.setName("习性");
            bean.setContent(habits);
            listBean.add(bean);
        }
        String food = data.optString("food");
        if (!StringUtils.isEmpty(food)) {
            DataBean bean = new DataBean();
            bean.setName("食性");
            bean.setContent(food);
            listBean.add(bean);
        }
        String behavior = data.optString("behavior");
        if (!StringUtils.isEmpty(behavior)) {
            DataBean bean = new DataBean();
            bean.setName("行为");
            bean.setContent(behavior);
            listBean.add(bean);
        }
        String sexy = data.optString("sexy");
        if (!StringUtils.isEmpty(sexy)) {
            DataBean bean = new DataBean();
            bean.setName("繁殖");
            bean.setContent(sexy);
            listBean.add(bean);
        }
        String protection = data.optString("protection");
        if (!StringUtils.isEmpty(protection)) {
            DataBean bean = new DataBean();
            bean.setName("现状与文化");
            bean.setContent(protection);
            listBean.add(bean);
        }
        String culture = data.optString("culture");
        if (!StringUtils.isEmpty(culture)) {
            DataBean bean = new DataBean();
            bean.setName("文化");
            bean.setContent(culture);
            listBean.add(bean);
        }
        String others = data.optString("others");
        if (!StringUtils.isEmpty(others)) {
            DataBean bean = new DataBean();
            bean.setName("其它");
            bean.setContent(others);
            listBean.add(bean);
        }
        String gang = data.optString("gang");
        if (!StringUtils.isEmpty(gang)) {
            DataBean bean = new DataBean();
            bean.setName("纲");
            bean.setContent(gang);
            listBean.add(bean);
        }
        String list1 = data.optString("list");
        if (!StringUtils.isEmpty(list1)) {
            DataBean bean = new DataBean();
            bean.setName("目");
            bean.setContent(list1);
            listBean.add(bean);
        }
        String kind = data.optString("kind");
        if (!StringUtils.isEmpty(kind)) {
            DataBean bean = new DataBean();
            bean.setName("属");
            bean.setContent(kind);
            listBean.add(bean);
        }
        String iucn = data.optString("iucn");
        if (!StringUtils.isEmpty(iucn)) {
            DataBean bean = new DataBean();
            bean.setName("IUCN红色名录等级(2017)");
            bean.setContent(iucn);
            listBean.add(bean);
        }
        String cites = data.optString("cites");
        if (!StringUtils.isEmpty(iucn)) {
            DataBean bean = new DataBean();
            bean.setName("CITES附录(2017)");
            bean.setContent(cites);
            listBean.add(bean);
        }
        String level = data.optString("level");
        if (!StringUtils.isEmpty(level)) {
            DataBean bean = new DataBean();
            bean.setName("国内保护等级");
            bean.setContent(level);
            listBean.add(bean);
        }
        adapter.notifyDataSetChanged();
        JSONArray advertisement = data.optJSONArray("advertisement");
        if (advertisement != null && advertisement.length() != 0) {
            JSONObject object1 = advertisement.optJSONObject(0);
            GlideLoadingUtils.load(act, object1.optString("advPic"), mB.ivJust);
            link = object1.optString("link");
        }
    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        mB.banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        mB.banner.stopAutoPlay();
    }

    @Override
    public void OnBannerClick(int position) {
        DataBean bean = listBannerBean.get(position);
        if (bean.getContent().equals("vedio")) {
            UIHelper.startVideoAct(bean.getPath());
        }
    }
}
