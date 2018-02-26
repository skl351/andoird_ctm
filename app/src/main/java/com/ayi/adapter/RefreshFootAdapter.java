package com.ayi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayi.R;
import com.ayi.entity.item_info;
import com.socks.library.KLog;

import java.util.List;


public class RefreshFootAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    //上拉加载更多
    public static final int  PULLUP_LOAD_MORE=0;
    //正在加载中
    public static final int  LOADING_MORE=1;
    //上拉加载更多状态-默认为0
    private int load_more_status=0;
    private LayoutInflater mInflater;
    private List<item_info> mTitles=null;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    public Context Mcontext;
    private MyItemClickListener mItemClickListener;
    public RefreshFootAdapter(Context context, List<item_info> list){
        Mcontext=context;
        this.mInflater= LayoutInflater.from(context);
        this.mTitles=list;
    }
    /**
     * item显示类型
     * @param parent
     * @param viewType
     * @return
     */
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if(viewType==TYPE_ITEM){
            View view=mInflater.inflate(R.layout.item_info_recycler_view,parent,false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            ItemViewHolder itemViewHolder=new ItemViewHolder(view,Mcontext,mItemClickListener);

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
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }
    public interface MyItemClickListener {
         void onItemClick(View view, int postion);
    }
    /**
     * 数据的绑定显示
     * @param holder
     * @param position
     */
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        KLog.e("运行了信息");
        if(holder instanceof ItemViewHolder) {
            ((ItemViewHolder)holder).spot_info.setVisibility(View.VISIBLE);
            ((ItemViewHolder)holder).item_tv.setTextColor(Mcontext.getResources().getColor(R.color.black));
            ((ItemViewHolder)holder).item_time.setTextColor(Mcontext.getResources().getColor(R.color.default_textcolor));
            ((ItemViewHolder)holder).item_tv.setText(mTitles.get(position).getTitle());
            ((ItemViewHolder)holder).item_time.setText(mTitles.get(position).getTimestamp());
            if (mTitles.get(position).isFlag_review()){
                ((ItemViewHolder)holder).spot_info.setVisibility(View.INVISIBLE);
                ((ItemViewHolder)holder).item_tv.setTextColor(Mcontext.getResources().getColor(R.color.gray));
                ((ItemViewHolder)holder).item_time.setTextColor(Mcontext.getResources().getColor(R.color.gray));
            }
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
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView item_tv;
        public TextView item_time;
        public ImageView spot_info;
        private Context mcontext;
        private MyItemClickListener mListener;
        public ItemViewHolder(View view, Context context, MyItemClickListener onlisten){
            super(view);
            mcontext=context;
            mListener=onlisten;
            view.setOnClickListener(this);
            item_tv = (TextView) view.findViewById(R.id.title_info);
            item_time = (TextView) view.findViewById(R.id.time_info);
            spot_info= (ImageView) view.findViewById(R.id.spot_info);
        }


        @Override
        public void onClick(View v) {
//            System.out.println("点击了3"+getOldPosition()+","+getAdapterPosition()+","+getPosition()+","+getLayoutPosition());
            if(mListener != null){
                mListener.onItemClick(v,getPosition());
            }

        }
    }
    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends  RecyclerView.ViewHolder{
        private TextView foot_view_item_tv;
        public FootViewHolder(View view) {
            super(view);
//            foot_view_item_tv=(TextView)view.findViewById(R.id.foot_view_item_tv);
        }
    }

    //添加数据
    public void addItem(List<item_info> newDatas) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        newDatas.addAll(mTitles);
        mTitles.removeAll(mTitles);
        mTitles.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<item_info> newDatas) {
        mTitles.addAll(newDatas);
        notifyDataSetChanged();
    }

    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     *  //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     * @param status
     */
    public void changeMoreStatus(int status){
        load_more_status=status;
        notifyDataSetChanged();
    }
}
