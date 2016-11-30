package com.eminem.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.eminem.weibo.activity.MainActivity;

/**
 * Created by Eminem on 2016/11/30.
 *
 */

public class BaseFragment extends Fragment {
    protected MainActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    protected void intent2Activity(Class<? extends Activity> tarActivity){
        Intent intent = new Intent(activity, tarActivity);
        startActivity(intent);
    }

}
