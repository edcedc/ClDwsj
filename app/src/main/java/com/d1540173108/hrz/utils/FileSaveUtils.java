package com.d1540173108.hrz.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.format.Formatter;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.d1540173108.hrz.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/10/22.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class FileSaveUtils {

    private static NotificationManager notificationManager;
    private static Notification notification; //下载通知进度提示
    private static NotificationCompat.Builder builder;
    private boolean flag = false; //进度框消失标示 之后发送通知

    public static void saveVideo(final Context act, final String fileUrl, final String name){
        boolean orExistsDir = FileUtils.createOrExistsDir(Constants.mainPath);
        if (orExistsDir) {
            final String videoPath = Constants.videoUrl;
            boolean orExistsDir1 = FileUtils.createOrExistsDir(videoPath);
            if (orExistsDir1){
                ToastUtils.showShort("正在下载，请稍候...");
                Observable.create(new ObservableOnSubscribe<Progress>() {
                            @Override
                            public void subscribe(@NonNull final ObservableEmitter<Progress> e) throws Exception {
                                OkGo.<File>get(fileUrl)//
                                        .execute(new FileCallback(videoPath, name + ".mp3") {
                                            @Override
                                            public void onSuccess(Response<File> response) {
                                                e.onComplete();
                                            }

                                            @Override
                                            public void onError(Response<File> response) {
                                                e.onError(response.getException());
                                            }

                                            @Override
                                            public void downloadProgress(Progress progress) {
                                                e.onNext(progress);
                                            }
                                        });
                            }
                        })
                                .doOnSubscribe(new Consumer<Disposable>() {
                                    @Override
                                    public void accept(@NonNull Disposable disposable) throws Exception {
                                        LogUtils.e("正在下载中...");
                                        initNotification(act);
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread())//
                                .subscribe(new Observer<Progress>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {
//                                        addDisposable(d);
                                    }

                                    @Override
                                    public void onNext(@NonNull Progress progress) {
                                        String downloadLength = Formatter.formatFileSize(act, progress.currentSize);
                                        String totalLength = Formatter.formatFileSize(act, progress.totalSize);
                                        LogUtils.e(downloadLength + "/" + totalLength);
                                        String speed = Formatter.formatFileSize(act, progress.speed);
//                                        tvNetSpeed.setText(String.format("%s/s", speed));
//                                        tvProgress.setText(numberFormat.format(progress.fraction));
//                                        pbProgress.setMax(10000);
//                                        pbProgress.setProgress((int) (progress.fraction * 10000));

                                        builder.setProgress(100, (int) (progress.fraction * 100), false);
                                        builder.setContentText("下载进度:" + (int) (progress.fraction * 100) + "%");
                                        notification = builder.build();
                                        notificationManager.notify(1, notification);
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        e.printStackTrace();
                                        LogUtils.e("下载出错", e.getMessage());
                                        ToastUtils.showShort("下载出错", e.getMessage());
                                    }

                                    @Override
                                    public void onComplete() {
                                        LogUtils.e("下载完成");
                                        ToastUtils.showShort("下载完成");

                                        builder.setContentTitle("下载完成")
                                                .setContentText("")
                                                .setAutoCancel(true);//设置通知被点击一次是否自动取消
                                        notification = builder.build();
                                        notificationManager.notify(1, notification);
                                    }
                                });
            }
        }
    }

    //初始化通知
    private static void initNotification(Context act) {
        notificationManager = (NotificationManager) act.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(act);
        builder.setContentTitle("正在下载...") //设置通知标题
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(act.getResources(), R.mipmap.ic_launcher)) //设置通知的大图标
                .setDefaults(Notification.DEFAULT_LIGHTS) //设置通知的提醒方式： 呼吸灯
                .setPriority(NotificationCompat.PRIORITY_MAX) //设置通知的优先级：最大
                .setAutoCancel(true)//设置通知被点击一次是否自动取消
                .setContentText("下载进度:" + "0%")
                .setProgress(100, 0, false);
        notification = builder.build();//构建通知对象
    }


    /**
     * 复制文件
     * @param source 输入文件
     * @param target 输出文件
     */
    private static void copy(File source, File target) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
