package com.openthos.launcher.openthoslauncher.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.openthos.launcher.openthoslauncher.R;
import com.openthos.launcher.openthoslauncher.activity.MainActivity;
import com.openthos.launcher.openthoslauncher.activity.MainActivity1;
import com.openthos.launcher.openthoslauncher.entity.Type;
import com.openthos.launcher.openthoslauncher.utils.DiskUtils;
import com.openthos.launcher.openthoslauncher.utils.FileUtils;
import com.openthos.launcher.openthoslauncher.utils.OtoConsts;

import java.io.File;

/**
 * Created by xu on 2016/8/11.
 */
public class MenuDialog extends Dialog {
    private Context context;
    private Type type;
    private String path;
    private int dialogHeight;
    private static MenuDialog menuDialog;
    private static boolean existMenu;

    public MenuDialog(Context context) {
        super(context);
        this.context = context;
    }

    public MenuDialog(Context context, Type type, String path) {
        super(context);
        this.context = context;
        this.type = type;
        this.path = path;
    }

    public MenuDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected MenuDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }
    
    public static MenuDialog getInstance(Context context, Type type, String path) {
        if (menuDialog == null) {
           menuDialog = new MenuDialog(context,type,path);
        }
        return menuDialog;
    }
    
    public static void setExistMenu(boolean exist) {
        existMenu = exist;
    }
  
    public static Boolean isExistMenu() {
        return existMenu;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_menu);
        getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.color.transparent));
        android.widget.Toast.makeText(getContext(),path,android.widget.Toast.LENGTH_LONG).show();
        String[] s = {};
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        switch (type) {
            case computer:
                s = context.getResources().getStringArray(R.array.menu_computer);
                break;
            case recycle:
                s = context.getResources().getStringArray(R.array.menu_recycle);
                break;
            case directory:
                s = context.getResources().getStringArray(R.array.menu_file);
                break;
            case file:
                s = context.getResources().getStringArray(R.array.menu_file);
                break;
            case blank:
                s = context.getResources().getStringArray(R.array.menu_blank);
                break;
        }

        dialogHeight = 0;
        int mvHeight = 0;
        int mvWidth = 0;
        for (int i = 0; i < s.length; i++) {
            View mv = View.inflate(context, R.layout.item_menu, null);
            TextView tv = (TextView) mv.findViewById(R.id.text);
            tv.setText(s[i]);
            tv.setTag(s[i]);
            tv.setOnHoverListener(hoverListener);
            tv.setOnClickListener(clickListener);
            ll.addView(mv);
            if (i == 0) {
               int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
               int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
               mv.measure(width,height);
               mvWidth = mv.getMeasuredWidth();
               mvHeight = mv.getMeasuredHeight();
            }
            dialogHeight += mvHeight;
        }
    }

    public void showDialog(int x, int y) {
        //show在前面，设置高度，宽度和位置才有效
        show();
        // setContentView可以设置为一个View也可以简单地指定资源ID
        // LayoutInflater
        // li=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        // View v=li.inflate(R.layout.dialog_layout, null);
        // dialog.setContentView(v);
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置,
         * 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.dimAmount = 0.0f;
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        /*
         * lp.x与lp.y表示相对于原始位置的偏移.
         * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
         * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
         * 当参数值包含Gravity.CENTER_HORIZONTAL时
         * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
         * 当参数值包含Gravity.CENTER_VERTICAL时
         * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
         * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
         * Gravity.CENTER_VERTICAL.
         *
         * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
         * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了,
         * Gravity.LEFT, Gravity.TOP, Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
         */
        if (x > (d.getWidth() - dialogWindow.getAttributes().width))//判断显示的dialog是否超出屏幕
        {
            lp.x = d.getWidth() - dialogWindow.getAttributes().width;
        } else {
            lp.x = x; // 新位置X坐标
        }
        //if (y > (d.getHeight() - dialogWindow.getAttributes().height)) {
        //    lp.y = d.getHeight() - dialogWindow.getAttributes().height;
        if (y > (d.getHeight() - dialogHeight - OtoConsts.BAR_Y)) {
            lp.y = y - dialogHeight - OtoConsts.FIX_PADDING;
        } else {
            lp.y = y;
        }
//      lp.width = width; // 宽度
//      lp.height = height; // 高度
        lp.alpha = OtoConsts.FIX_ALPHA; // 透明度
        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(lp);
        dialogWindow.setAttributes(lp);
        /*
         * 将对话框的大小按屏幕大小的百分比设置
         */
//        WindowManager m = getWindowManager();
//        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
//        p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
//        dialogWindow.setAttributes(p);
    }

    //悬停监听
    View.OnHoverListener hoverListener = new View.OnHoverListener() {

        public boolean onHover(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_HOVER_ENTER:
                    v.setBackgroundResource(R.color.item_hover_background);
                    break;
                case MotionEvent.ACTION_HOVER_EXIT:
                    v.setBackgroundResource(R.color.transparent);
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = (String) v.getTag();
            String[] all_menu = context.getResources().getStringArray(R.array.all_menu);
            if (text.equals(all_menu[OtoConsts.INDEX_OPEN])) {
                //open
                switch (type) {
                    case computer:
                    case recycle:
                    case directory:
                        PackageManager packageManager = getContext().getPackageManager();
                        Intent openDir = packageManager.getLaunchIntentForPackage(OtoConsts.FILEMANAGER_PACKAGE);
                        openDir.putExtra("path", path);
                        getContext().startActivity(openDir);
                        break;
                    case file:
                        Intent openFile = new Intent();
                        openFile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        openFile.setAction(Intent.ACTION_VIEW);
                        String fileType = FileUtils.getMIMEType(new File(path));
                        openFile.setDataAndType(Uri.fromFile(new File(path)), fileType);
                        getContext().startActivity(openFile);
                        break;
                }
            } else if (text.equals(all_menu[OtoConsts.INDEX_ABOUT_COMPUTER])) {
                //about_computer
                PackageManager packageManager = getContext().getPackageManager();
                Intent about = packageManager.getLaunchIntentForPackage(OtoConsts.SETTINGS_PACKAGE);
                about.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(about);
            } else if (text.equals(all_menu[OtoConsts.INDEX_COMPRESS])) {
                //compress
            } else if (text.equals(all_menu[OtoConsts.INDEX_DECOMPRESSION])) {
                //decompression
            } else if (text.equals(all_menu[OtoConsts.INDEX_CROP])) {
                //crop
            } else if (text.equals(all_menu[OtoConsts.INDEX_COPY])) {
                //copy
            } else if (text.equals(all_menu[OtoConsts.INDEX_PASTE])) {
                //paste
            } else if (text.equals(all_menu[OtoConsts.INDEX_SORT])) {
                //sort
                MainActivity.mHandler.sendEmptyMessage(OtoConsts.SORT);
            } else if (text.equals(all_menu[OtoConsts.INDEX_NEW_FOLDER])) {
                //new_folder
                MainActivity.mHandler.sendEmptyMessage(OtoConsts.NEWFOLDER);
            } else if (text.equals(all_menu[OtoConsts.INDEX_DISPLAY_SETTINGS])) {
                //display_settings
                Intent display = new Intent();
                ComponentName compDisplay = new ComponentName(OtoConsts.SETTINGS_PACKAGE, OtoConsts.DISPLAY_SETTINGS);
                display.setComponent(compDisplay);
                display.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(display);
            } else if (text.equals(all_menu[OtoConsts.INDEX_CHANGE_WALLPAPER])) {
                //change_wallpaper
                Intent wallpaper=new Intent(getContext(), MainActivity1.class);
                wallpaper.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(wallpaper);
            } else if (text.equals(all_menu[OtoConsts.INDEX_DELETE])) {
                //delete
                new AlertDialog.Builder(getContext())
                        .setMessage(getContext().getResources().getString(R.string.dialog_delete_text))
                        .setPositiveButton(getContext().getResources().getString(R.string.dialog_delete_yes),
                                new android.content.DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                super.run();
                                                DiskUtils.moveFile(path, OtoConsts.RECYCLE_PATH);
                                                Message deleteFile = new Message();
                                                deleteFile.obj = path;
                                                deleteFile.what = OtoConsts.DELETE;
                                                MainActivity.mHandler.sendMessage(deleteFile);
                                            }
                                        }.start();
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton(getContext().getResources().getString(R.string.dialog_delete_no),
                                new android.content.DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).show();
            } else if (text.equals(all_menu[OtoConsts.INDEX_RENAME])) {
                //rename
                MainActivity.mHandler.sendEmptyMessage(OtoConsts.RENAME);
            } else if (text.equals(all_menu[OtoConsts.INDEX_CLEAN_RECYCLE])) {
                //clean_recycle
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        File recycle = new File(path);
                        DiskUtils.delete(recycle);
                        if (!recycle.exists()) {
                            recycle.mkdir();
                        }
                    }
                }.start();
            } else if (text.equals(all_menu[OtoConsts.INDEX_PROPERTY])) {
                //property
                Message property = new Message();
                property.obj = path;
                property.what = OtoConsts.PROPERTY;
                MainActivity.mHandler.sendMessage(property);
            }
            dismiss();
        }
    };
}
