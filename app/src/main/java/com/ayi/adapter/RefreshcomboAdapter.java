package com.ayi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayi.R;
import com.ayi.entity.item_combo;
import com.ayi.home_page.Business_appointment_tc;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class RefreshcomboAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //上拉加载更多状态-默认为0
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private List<item_combo> mTitles = null;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    public Context Mcontext;
    private MyItemClickListener mItemClickListener;

    public RefreshcomboAdapter(Context context, List<item_combo> list) {
        Mcontext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mTitles = list;
    }

    /**
     * item显示类型
     *
     * @param parent
     * @param viewType
     * @return
     */
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.combo_item_view, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view, Mcontext, mItemClickListener);
            return itemViewHolder;
        }
//        else if(viewType==TYPE_FOOTER){
//            View foot_view=mInflater.inflate(R.layout.recycler_load_more_layout,parent,false);
//            //这边可以做一些属性设置，甚至事件监听绑定
//            //view.setBackgroundColor(Color.RED);
//            FootViewHolder footViewHolder=new FootViewHolder(foot_view);
//            return footViewHolder;
//        }
        return null;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            if (mTitles.get(position).getCornertitle() != null && !mTitles.get(position).getCornertitle().equals("")) {
                ((ItemViewHolder) holder).hot_text.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).hot_text.setText(mTitles.get(position).getCornertitle());
            } else {
                ((ItemViewHolder) holder).hot_text.setVisibility(View.GONE);
            }
            ((ItemViewHolder) holder).item_title.setText(mTitles.get(position).getTitle());
            ((ItemViewHolder) holder).price1.setText(mTitles.get(position).getPrice());
            ImageLoader.getInstance().displayImage(mTitles.get(position).getSimple_img(), ((ItemViewHolder) holder).item_img);
//            ((ItemViewHolder) holder).now_click.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent=new Intent(Mcontext, Business_appointment_tc.class);
//                    intent.putExtra("ccsp_id",mTitles.get(position).getCcsp_id());
////                    intent.putExtra("type_id",mTitles.get(position).getType_id());
//                    Mcontext.startActivity(intent);
//                }
//            });

        }
//        else if(holder instanceof FootViewHolder){
//            FootViewHolder footViewHolder=(FootViewHolder)holder;
//            switch (load_more_status){
//                case PULLUP_LOAD_MORE:
//                    footViewHolder.foot_view_item_tv.setText("上拉加载更多...");
//                    break;
//                case LOADING_MORE:
//                    footViewHolder.foot_view_item_tv.setText("正在加载更多数据...");
//                    break;
//            }
//        }
    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
        // 最后一个item设置为footerView
//    if (position + 1 == getItemCount()) {
//                return TYPE_FOOTER;
//            } else {
//                return TYPE_ITEM;
//            }
    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item_title;
        public ImageView item_img;
        public TextView price1;
        private Context mcontext;
        public TextView hot_text;
        private MyItemClickListener mListener;

        public ItemViewHolder(View view, Context context, MyItemClickListener onlisten) {
            super(view);
            mcontext = context;
            mListener = onlisten;
            view.setOnClickListener(this);
            item_title = (TextView) view.findViewById(R.id.item_title);
            price1 = (TextView) view.findViewById(R.id.price1);
            item_img = (ImageView) view.findViewById(R.id.item_img);
            hot_text = (TextView) view.findViewById(R.id.hot_text);
            mcontext = context;

        }


        @Override
        public void onClick(View v) {
//            System.out.println("点击了3"+getOldPosition()+","+getAdapterPosition()+","+getPosition()+","+getLayoutPosition());
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }

        }
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView foot_view_item_tv;

        public FootViewHolder(View view) {
            super(view);
//            foot_view_item_tv=(TextView)view.findViewById(R.id.foot_view_item_tv);
        }
    }

//    //添加数据
//    public void addItem(List<item_info> newDatas) {
//        //mTitles.add(position, data);
//        //notifyItemInserted(position);
//        newDatas.addAll(mTitles);
//        mTitles.removeAll(mTitles);
//        mTitles.addAll(newDatas);
//        notifyDataSetChanged();
//    }
//
//    public void addMoreItem(List<item_info> newDatas) {
//        mTitles.addAll(newDatas);
//        notifyDataSetChanged();
//    }

    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     * //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }
}
