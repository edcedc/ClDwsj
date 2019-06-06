package com.d1540173108.hrz.view;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.adapter.MeAdapter;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.User;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.databinding.BRecyclerBinding;
import com.d1540173108.hrz.databinding.FMyInfoBinding;
import com.d1540173108.hrz.presenter.MyInfoPresenter;
import com.d1540173108.hrz.utils.PickerUtils;
import com.d1540173108.hrz.utils.cache.ShareSessionIdCache;
import com.d1540173108.hrz.view.bottomFrg.SexBottomFrg;
import com.d1540173108.hrz.view.impl.MyInfoContract;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/10.
 *  我的信息
 */

public class MyInfoFrg extends BaseFragment<MyInfoPresenter, FMyInfoBinding> implements MyInfoContract.View{

    private MeAdapter meAdapter;
    private List<DataBean> listBean = new ArrayList<>();
    private int mPosition = -1;
    private SexBottomFrg sexBottomFrg;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_my_info;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.my_info));
        mB.refreshLayout.setPureScrollModeOn();
        showLoadDataing();


        if (meAdapter == null){
            meAdapter = new MeAdapter(act, listBean, true);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.setAdapter(meAdapter);
        meAdapter.notifyDataSetChanged();

        meAdapter.setClick(new MeAdapter.OnClick() {
            @Override
            public void onClick(final int position, String text) {
                mPosition = position;
                switch (position){
                    case 0:
                        UIHelper.startUpdateNicknameFrg(MyInfoFrg.this);
                        break;
                    case 1:
                        sexBottomFrg.show(getChildFragmentManager(), "dialog");
                        break;
                    case 2:
                        PickerUtils.setPickerData(act, new PickerUtils.OnDatePickListener() {
                            @Override
                            public void onDatePicked(String year, String month, String day) {
                                listBean.get(position).setContent(year + "-" + month + "-" + day);
                                meAdapter.notifyItemChanged(mPosition);
                                mPresenter.updateBirthDate(year + "-" + month + "-" + day);
                            }
                        });
                        break;
                }
            }
        });
        hideLoading();

        if (sexBottomFrg == null){
            sexBottomFrg = new SexBottomFrg();
        }
        sexBottomFrg.setSexListener(new SexBottomFrg.onSexListener() {
            @Override
            public void sex(int sex) {
                mPresenter.updateSex(sex);
                listBean.get(mPosition).setContent(sex == 1 ? "女" : "男");
                meAdapter.notifyItemChanged(mPosition);
            }
        });
        mB.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.getInstance().setLogin(false);
                User.getInstance().setUserObj(null);
                User.getInstance().setUserInfoObj(null);
                ShareSessionIdCache.getInstance(act).remove();
                UIHelper.startLoginAct();
                ActivityUtils.finishAllActivities();
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        listBean.clear();

        JSONObject userInfoObj = User.getInstance().getUserInfoObj();
        String[] str = {act.getString(R.string.nick), act.getString(R.string.sex), act.getString(R.string.birthday)};
        for (int i = 0; i < str.length; i++) {
            DataBean bean = new DataBean();
            switch (i){
                case 0:
                    if (userInfoObj != null){
                        bean.setContent(userInfoObj.optString("userNickName"));
                    }else {
                        bean.setContent("未设置");
                    }
                    break;
                case 1:
                    if (userInfoObj != null){
                        bean.setContent(userInfoObj.optString("sex"));
                    }else {
                        bean.setContent("未设置");
                    }
                    break;
                case 2:
                    if (userInfoObj != null){
                        String birthDate = userInfoObj.optString("birthDate");
                        if (!StringUtils.isEmpty(birthDate) || !birthDate.equals("null")){
                            String[] split = birthDate.split(" ");
                            bean.setContent(split[0]);
                        }
                    }else {
                        bean.setContent("未设置");
                    }
                    break;
            }
            bean.setName(str[i]);
            listBean.add(bean);
        }
        meAdapter.notifyDataSetChanged();
    }
}
