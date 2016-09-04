package com.openthos.launcher.openthoslauncher.activity;


import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.openthos.launcher.openthoslauncher.R;
import com.openthos.launcher.openthoslauncher.adapter.MenuAdapter;
import com.openthos.launcher.openthoslauncher.view.WindowsLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity1 extends BasicActivity  {

    private List<HashMap<String, Object>> mDatas;
    WindowsLayout root;

    public int pos = -1;
    /**
     * 双击事件判断
     **/
    // 双击事件记录最近一次点击的ID
    private int mLastClickId = -1;
    // 双击事件记录最近一次点击的时间
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        root= (WindowsLayout) findViewById(R.id.windows);
        initData();
        initDesktop();
    }

    private void initData() {
        String[] defaultName = getResources().getStringArray(R.array.default_icon_name);

        TypedArray defaultIcon=getResources().obtainTypedArray(R.array.default_icon);

        CharSequence[] defaultIsFile = getResources().getTextArray(R.array.default_is_file);
        CharSequence[] defaultIsApp = getResources().getTextArray(R.array.default_is_app);
        mDatas = new ArrayList<>();
        for (int i = 0; i < defaultName.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", defaultName[i]);
            map.put("isChecked", false);
            map.put("icon", defaultIcon.getResourceId(i,R.mipmap.ic_launcher));
            mDatas.add(map);
        }


        File dir = Environment.getExternalStorageDirectory();
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            if (files[i].isDirectory()){
                map.put("name", files[i].getName());
                map.put("path",files[i].getAbsolutePath());
                map.put("isChecked", false);
                map.put("icon", R.drawable.ic_app_file);
            }else {
                map.put("name", files[i].getName());
                map.put("path",files[i].getAbsolutePath());
                map.put("isChecked", false);
                map.put("icon", R.drawable.ic_app_text);
            }
            mDatas.add(map);
        }
    }
    RelativeLayout item;
    ImageView iv;
    TextView tv;
    CheckBox checkBox;

    private void initDesktop() {
        for (int i=0;i<mDatas.size();i++){
            View view=View.inflate(this,R.layout.item_icon,null);
            item = (RelativeLayout) view.findViewById(R.id.item);
            iv = (ImageView) view.findViewById(R.id.icon);
            tv = (TextView) view.findViewById(R.id.text);
            checkBox = (CheckBox) view.findViewById(R.id.check);

            tv.setText(mDatas.get(i).get("name").toString());
            if ((Boolean) mDatas.get(i).get("isChecked")) {
                item.setBackgroundResource(R.drawable.icon_background);
            } else if (!(Boolean) mDatas.get(i).get("isChecked")) {
                item.setBackgroundResource(R.drawable.icon_background_trans);
            }
            iv.setImageDrawable(item.getResources().getDrawable((int)mDatas.get(i).get("icon")));
            item.setTag(i);

            item.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        //双击
                        if (Math.abs(System.currentTimeMillis() - mLastClickTime) < 500 && mLastClickId == (int)v.getTag()) {
//                    PackageManager packageManager = item.getContext().getPackageManager();
//                    Intent intent  = packageManager.getLaunchIntentForPackage("com.cyanogenmod.filemanager");
//                    item.getContext().startActivity(intent);

                            LayoutInflater mLayoutInflater = ((Activity) item.getContext()).getLayoutInflater();


                            //right_pop為泡泡的布局
                            View view = mLayoutInflater.inflate(R.layout.dialog_menu, null);

                            String[] s = item.getContext().getResources().getStringArray(R.array.menu_computer);
                            MenuAdapter adapter = new MenuAdapter(item.getContext(), s);

                            PopupWindow popupWindow = new PopupWindow(view, RecyclerView.LayoutParams.WRAP_CONTENT,
                                    RecyclerView.LayoutParams.WRAP_CONTENT);


                            // 第一个参数导入泡泡的view，后面两个指定宽和高


                            //下面两句位置不能颠倒，不然无效！（经本机测试 不知道别人如何）必须设置backgroundDrawable()

                            popupWindow.setOutsideTouchable(true);
                            // 弹窗一般有两种展示方法，用showAsDropDown()和showAtLocation()两种方法实现。
                            // 以这个v为anchor（可以理解为锚，基准），在下方弹出
                            popupWindow.showAtLocation(v,0,0,0);



                        }
                        //单击
                        else {




                            //左键 单击/拖动 选择逻辑
                             if ((Boolean) mDatas.get((int)v.getTag()).get("isChecked") == false) {
                                if (pos != -1 && pos != (int)v.getTag() && (Boolean) mDatas.get(pos).get("isChecked") == true) {
                                    mDatas.get(pos).put("isChecked", !(Boolean) mDatas.get(pos).get("isChecked"));
                                }
                                mDatas.get((int)v.getTag()).put("isChecked", !(Boolean) mDatas.get((int)v.getTag()).get("isChecked"));
                                if (pos != (int)v.getTag()) {
                                    pos = (int)v.getTag();
                                } else {
                                    pos = -1;
                                }
                            }
                            mLastClickTime = System.currentTimeMillis();
                            mLastClickId = pos;

                        }
                    }

                    return true;
                }
            });
            root.addView(view);


        }
    }
}
