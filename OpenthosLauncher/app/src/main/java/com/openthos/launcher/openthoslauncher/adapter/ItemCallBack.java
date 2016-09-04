package com.openthos.launcher.openthoslauncher.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by xu on 2016/8/8.
 */
public class ItemCallBack extends ItemTouchHelper.Callback{
    private RecycleCallBack mCallBack;


    public ItemCallBack(RecycleCallBack callBack) {
        this.mCallBack = callBack;
    }
//    void fix() {
//        Field field = null;
//        try {
//            field = GestureDetector.class.getDeclaredField("LONGPRESS_TIMEOUT");
//        } catch (NoSuchFieldException e) {
//            Log.i("ww", e.getStackTrace()+"");
//
//        }
//        //将字段的访问权限设为true：即去除private修饰符的影响
//        field.setAccessible(true);
//        /*去除final修饰符的影响，将字段设为可修改的*/
//        Field modifiersField = null;
//        try {
//            modifiersField = Field.class.getDeclaredField("modifiers");
//            modifiersField.setAccessible(true);
//            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//        //把字段值设为10
//        try {
//            field.set(null, 10);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        mCallBack.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {}

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int interpolateOutOfBoundsScroll(RecyclerView recyclerView, int viewSize, int viewSizeOutOfBounds, int totalSize, long msSinceStartScroll) {
        return 0;
}

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (viewHolder instanceof HolderCallBack){
            HolderCallBack holder = (HolderCallBack) viewHolder;
            holder.onSelect();
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof HolderCallBack){
            HolderCallBack holder = (HolderCallBack) viewHolder;
            holder.onClear();
        }
    }
}
