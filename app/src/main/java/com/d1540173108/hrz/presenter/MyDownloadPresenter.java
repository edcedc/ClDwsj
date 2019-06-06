package com.d1540173108.hrz.presenter;

import android.os.Handler;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.bean.DownloadBean;
import com.d1540173108.hrz.bean.HistoryBean;
import com.d1540173108.hrz.utils.Constants;
import com.d1540173108.hrz.view.impl.MyDownloadContract;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/10.
 */

public class MyDownloadPresenter extends MyDownloadContract.Presenter {
    @Override
    public void onRequest(int pageNumber) {
        LitePal.deleteAll(DownloadBean.class);
        final List<DataBean> list = new ArrayList<>();
        final List<File> files = FileUtils.listFilesInDir(Constants.videoUrl);
        if (files != null && files.size() != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (File file : files) {
                        DataBean bean = new DataBean();
                        String fileName = FileUtils.getFileName(file);
                        bean.setStoryName(fileName.substring(0, fileName.length() - 4));
                        bean.setUrlMp3(file.toString());
                        list.add(bean);
                        LogUtils.e(fileName, FileUtils.getFileSize(file), file.toString());

                        DownloadBean downloadBean = new DownloadBean();
                        downloadBean.setStoryName(fileName);
                        downloadBean.save();
                    }
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.setData(list);
                            mView.hideLoading();
                        }
                    });
                }
            }).start();
        }else {
            mView.showLoadEmpty();
        }
    }
}
