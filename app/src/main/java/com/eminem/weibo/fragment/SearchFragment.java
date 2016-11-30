package com.eminem.weibo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eminem.weibo.BaseFragment;
import com.eminem.weibo.R;

/**
 * Created by Eminem on 2016/11/30.
 */

public class SearchFragment extends BaseFragment {
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= View.inflate(activity,R.layout.frag_search,null);
        return view;
    }
}
