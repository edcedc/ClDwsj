package com.d1540173108.hrz.view;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.ToastUtils;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.databinding.FLyricBinding;
import com.d1540173108.hrz.event.LrcViewInEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by edison on 2019/1/15.
 */

public class PlayMusicLyricFrg extends BaseFragment<BasePresenter, FLyricBinding> {

    String sss = "[ti:成都]\n" +
            "[ar:赵雷]\n" +
            "[al:无法长大]\n" +
            "[by:0]\n" +
            "[offset:0]\n" +
            "[00:01.34]成都\n" +
            "[00:02.09]\n" +
            "[00:03.96]作词：赵雷\n" +
            "[00:03.96]作曲：赵雷\n" +
            "[00:05.99]编曲：赵雷,喜子\n" +
            "[00:09.04]演唱：赵雷\n" +
            "[00:12.90]\n" +
            "[00:17.65]让我掉下眼泪的 \n" +
            "[00:21.57]不止昨夜的酒\n" +
            "[00:25.82]让我依依不舍的 \n" +
            "[00:29.62]不止你的温柔\n" +
            "[00:33.78]余路还要走多久 \n" +
            "[00:37.64]你攥着我的手\n" +
            "[00:41.63]让我感到为难的 \n" +
            "[00:45.47]是挣扎的自由\n" +
            "[00:49.20]\n" +
            "[00:51.90]分别总是在九月 \n" +
            "[00:55.38]回忆是思念的愁\n" +
            "[00:59.46]深秋嫩绿的垂柳 \n" +
            "[01:03.32]亲吻着我额头\n" +
            "[01:07.31]在那座阴雨的小城里 \n" +
            "[01:11.44]我从未忘记你\n" +
            "[01:15.56]成都 带不走的 只有你\n" +
            "[01:21.76]\n" +
            "[01:22.81]和我在成都的街头走一走 \n" +
            "[01:31.25]直到所有的灯都熄灭了也不停留\n" +
            "[01:38.88]你会挽着我的衣袖 \n" +
            "[01:42.67]我会把手揣进裤兜\n" +
            "[01:46.56]走到玉林路的尽头 \n" +
            "[01:50.45]坐在小酒馆的门口\n" +
            "[01:55.65]\n" +
            "[02:30.35]分别总是在九月 \n" +
            "[02:34.31]回忆是思念的愁\n" +
            "[02:38.17]深秋嫩绿的垂柳 \n" +
            "[02:42.48]亲吻着我额头\n" +
            "[02:46.66]在那座阴雨的小城里 \n" +
            "[02:50.34]我从未忘记你\n" +
            "[02:53.78]成都 带不走的 只有你\n" +
            "[03:00.95]\n" +
            "[03:02.38]和我在成都的街头走一走 \n" +
            "[03:10.13]直到所有的灯都熄灭了也不停留\n" +
            "[03:18.32]你会挽着我的衣袖 \n" +
            "[03:21.99]我会把手揣进裤兜\n" +
            "[03:25.99]走到玉林路的尽头 \n" +
            "[03:29.79]坐在小酒馆的门口\n" +
            "[03:36.36]\n" +
            "[03:38.40]和我在成都的街头走一走 \n" +
            "[03:46.45]直到所有的灯都熄灭了也不停留\n" +
            "[03:54.27]和我在成都的街头走一走 \n" +
            "[04:02.30]直到所有的灯都熄灭了也不停留\n" +
            "[04:10.29]你会挽着我的衣袖 \n" +
            "[04:13.57]我会把手揣进裤兜\n" +
            "[04:17.56]走到玉林路的尽头 \n" +
            "[04:21.77]坐在(走过)小酒馆的门口\n" +
            "[04:27.72]\n" +
            "[04:36.02]和我在成都的街头走一走 \n" +
            "[04:43.67]直到所有的灯都熄灭了也不停留\n" +
            "[04:51.93]\n";

    private String musicTips;

    public void setMusicTips(String musicTips) {
//        mB.tvLyric.setText(musicTips);
//        mB.webView.loadDataWithBaseURL(null, musicTips, "text/html", "utf-8", null);
        mB.webView.loadUrl("http://chimelong.powert.top/h5/#/auth?id=" + musicTips);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {
        musicTips = bundle.getString("musicTips");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_lyric;
    }

    @Override
    protected void initView(View view) {
        setSwipeBackEnable(false);

//        mB.tvLyric.setText(musicTips);
//        mB.lrcView.updateTime(0);
//        mB.lrcView.loadLrc(getLrcText("chengdu.lrc"));
//        mB.lrcView.loadLrc(musicTips);
        EventBus.getDefault().register(this);
//        mB.lrcView.setOnPlayClickListener(new LrcView.OnPlayClickListener() {
//            @Override
//            public boolean onPlayClick(long time) {
//                return true;
//            }
//        });
        mB.webView.loadUrl("http://chimelong.powert.top/h5/#/auth?id=" + musicTips);
        mB.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView var1, int var2, String var3, String var4) {
                mB.progressBar.setVisibility(View.GONE);
                ToastUtils.showShort("网页加载失败");
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //这个是一定要加上那个的,配合scrollView和WebView的height=wrap_content属性使用
                int w = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                //重新测量
                mB.webView.measure(w, h);
            }
        });
        //进度条
        mB.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mB.progressBar.setVisibility(View.GONE);
                    return;
                }
                mB.progressBar.setVisibility(View.VISIBLE);
                mB.progressBar.setProgress(newProgress);
            }
        });

    }



    @Subscribe
    public void onMainLrcViewInEvent(LrcViewInEvent event){
//        mB.lrcView.updateTime(event.progress);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private String getLrcText(String fileName) {
        String lrcText = null;
        try {
            InputStream is = act.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            lrcText = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lrcText;
    }

}
