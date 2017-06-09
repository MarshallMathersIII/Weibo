package com.eminem.weibo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eminem.weibo.R;
import com.eminem.weibo.bean.UserItem;
import com.eminem.weibo.utils.ToastUtils;

import java.util.List;

/**
 * Created by eminem on 2017/6/8.
 */

public class UserItemAdapter extends BaseAdapter {
    private Context context;
    private List<UserItem> datas;

    public UserItemAdapter(Context context, List<UserItem> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_user, null);
            holder.v_divider = convertView.findViewById(R.id.v_divider);
            holder.ll_content = convertView.findViewById(R.id.ll_content);
            holder.iv_left = (ImageView) convertView.findViewById(R.id.iv_left);
            holder.tv_subhead = (TextView) convertView.findViewById(R.id.tv_subhead);
            holder.tv_caption = (TextView) convertView.findViewById(R.id.tv_caption);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        UserItem item = (UserItem) getItem(position);

        holder.iv_left.setImageResource(item.getLeftImg());
        holder.tv_subhead.setText(item.getSubhead());
        holder.tv_caption.setText(item.getCaption());

        holder.v_divider.setVisibility(item.isShowTopDivider() ? View.VISIBLE : View.GONE);
        holder.ll_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(context, "item click position = " + position, Toast.LENGTH_SHORT);
            }
        });


        return convertView;
    }

    public static class ViewHolder {
        public View v_divider;
        public View ll_content;
        public ImageView iv_left;
        public TextView tv_subhead;
        public TextView tv_caption;
    }
}
