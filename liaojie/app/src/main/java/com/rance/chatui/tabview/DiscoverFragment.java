package com.rance.chatui.tabview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rance.chatui.R;
/**
 * Created by yx on 16/4/3.
 */
public class DiscoverFragment extends BaseFragment implements ITabClickListener{
    @Override
    public void fetchData() {
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discvover_layout, container, false);
        return view;
    }

    @Override
    public void onMenuItemClick() {

    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }
}
