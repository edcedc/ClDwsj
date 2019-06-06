package com.d1540173108.hrz.event;

import android.content.ComponentName;
import android.os.IBinder;

/**
 * Created by edison on 2019/1/15.
 */

public class MediaServiceInEvent {

    public ComponentName name;
    public IBinder service;

    public MediaServiceInEvent(ComponentName name, IBinder service) {
        this.name = name;
        this.service = service;
    }
}
