package com.d1540173108.hrz.view.impl;

import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.base.IBaseListView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by edison on 2019/1/11.
 */

public interface AnimalEncyclopediaContract {

    interface View extends IBaseListView {

        void setInto(JSONObject jsonObject);

        void setSend(JSONArray jsonArray);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest(int pageNumber);

        public abstract void onSearch(String search);

        public abstract void onInfo(String id);
    }

}
