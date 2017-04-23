package com.gn.djay.viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by DJAY on 2017/4/16.
 */

public class MyFragment extends Fragment {

    public final static String KEY_CONTENT = "key_content";
    private String strContent = "Default Str";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(null != bundle)
            strContent = bundle.getString(KEY_CONTENT);

        TextView tv = new TextView(getActivity());
        tv.setTextSize(30);
        tv.setText(strContent);
        tv.setGravity(Gravity.CENTER);

        return tv;
    }

    public static MyFragment generateFragment(String str){
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CONTENT,str);
        MyFragment fragment = new MyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
