#Desktop设计与实现

###整体思想
写一个布局，背景透明覆盖到Launcher3的主界面，然后将Launcher3中不要的功能移除/隐藏。

##代码相关部分
类似Windows，ubuntu桌面，从左向右排列，从上向下排列，布局使用的是Android5.0新增加的RecyclerView。
```
   private RecyclerView mRecyclerView;
   int heightNum = OtoConsts.MAX_LINE;
   mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
   mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(heightNum,
   StaggeredGridLayoutManager.HORIZONTAL));
```

桌面每个图标的数据是以HashMap对象存放的，主要有如下数据，分别表示图标名，路径，是否被选中，是否是空白区域标识，图标，类型：
```
   public List<HashMap<String, Object>> mDatas = new ArrayList<>();
   HashMap<String, Object> map = new HashMap<>();
   map.put("name", file.getName());
   map.put("path", file.getAbsolutePath());
   map.put("isChecked", false);
   map.put("null", false);
   map.put("icon", R.drawable.ic_app_file);
   map.put("type", Type.directory);
   mDatas.set(i, map);
```

拖动方法回调(ItemTouchHelper已封装好执行，这里只需要进行内存中数据的变换即可)
```
   public void onMove(int from, int to) {
        if ((Boolean) mDatas.get(from).get("null") != true) {
            if (to > 0 && from > 0) {
                synchronized (this) {
                    if (from > to) {
                        int count = from - to;
                        for (int i = 0; i < count; i++) {
                            Collections.swap(mDatas, from - i, from - i - 1);
                        }
                    }
                    if (from < to) {
                        int count = to - from;
                        for (int i = 0; i < count; i++) {
                            Collections.swap(mDatas, from + i, from + i + 1);
                        }
                    }
                    mAdapter.setData(mDatas);
                    mAdapter.notifyItemMoved(from, to);
                    mAdapter.pos = to;
                }
            }
        }
   }
```

右键菜单的弹出，根据点击的位置，传入不同的类型，来生成不同的菜单
```
   MenuDialog dialog = new MenuDialog(item.getContext(), 
                                 (Type) data.get(getAdapterPosition()).get("type"), 
                                 (String) data.get(getAdapterPosition()).get("path"));
   dialog.showDialog((int) event.getRawX(), (int) event.getRawY());
```
```
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
        for (int i = 0; i < s.length; i++) {
            View mv = View.inflate(context, R.layout.item_menu, null);
            TextView tv = (TextView) mv.findViewById(R.id.text);
            tv.setText(s[i]);
            tv.setTag(s[i]);
            tv.setOnHoverListener(hoverListener);
            tv.setOnClickListener(clickListener);
            ll.addView(mv);
        }
```

右键菜单增加两个监听，分别是onHoverd，onClick分别用来标识鼠标悬停的界面变化和点击后执行的代码
```
   View.OnClickListener clickListener = new View.OnClickListener() {}
   View.OnHoverListener hoverListener = new View.OnHoverListener() {}
```
