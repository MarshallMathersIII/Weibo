package com.eminem.weibo.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eminem.weibo.R;
import com.eminem.weibo.bean.Comment;
import com.eminem.weibo.bean.PicUrls;
import com.eminem.weibo.bean.Status;
import com.eminem.weibo.bean.User;
import com.eminem.weibo.utils.DateUtils;
import com.eminem.weibo.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.eminem.weibo.R.id.tv_message_details;

/**
 * Created by eminem on 2017/8/9.
 * 消息页adapter
 */

public class MessageDetailsAdapter extends BaseAdapter {
    private Context context;
    private List<Comment> datas;
    private String quoteBmiddlePic;

    public MessageDetailsAdapter(Context context, List<Comment> datas) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_message, null);
            holder.ll_message_item = (LinearLayout) convertView.findViewById(R.id.ll_message_item);

            holder.ll_card_content = (LinearLayout) convertView.findViewById(R.id.ll_card_content);
            holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            holder.rl_content = (RelativeLayout) convertView.findViewById(R.id.rl_content);
            holder.tv_head_name = (TextView) convertView.findViewById(R.id.tv_head_name);
            holder.tv_head_desc = (TextView) convertView.findViewById(R.id.tv_head_desc);
//            holder.iv_more = (ImageView) convertView.findViewById(R.id.iv_more);
//            holder.iv_reply = (ImageView) convertView.findViewById(R.id.iv_reply);

            holder.tv_message = (TextView) convertView.findViewById(R.id.tv_message);

            holder.ll_message = (LinearLayout) convertView.findViewById(R.id.ll_message);
            holder.iv_message = (ImageView) convertView.findViewById(R.id.iv_message);
            holder.tv_message_name = (TextView) convertView.findViewById(R.id.tv_message_name);
            holder.tv_message_details = (TextView) convertView.findViewById(tv_message_details);

            holder.ll_message_retweeted = (LinearLayout) convertView.findViewById(R.id.ll_message_retweeted);
            holder.tv_message_retweeted = (TextView) convertView.findViewById(R.id.tv_message_retweeted);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Comment comment = (Comment) getItem(position);
        User user = comment.getUser();
        Status status = comment.getStatus();

        final String commentUserName = user.getName();//回复者Name
        final String commentCreatedAt = comment.getCreated_at();//评论创建时间.
        String commentSource = comment.getSource();//评论来源.
        String commentAvatar = user.getAvatar_hd();//用户头像.

        String commentText = comment.getText();//评论内容.


        User quoteUser = status.getUser();
        String quoteName = quoteUser.getName();//引用name
        String quoteText = status.getText();//引用正文
        //引用图片，如有正文图片，则引入
        ArrayList<PicUrls> pic_urls = status.getPic_urls();
        if (!(pic_urls == null)) {
            PicUrls quotePicUrls = pic_urls.get(0);
            quoteBmiddlePic = quotePicUrls.getBmiddle_pic();
        }
        //如无正文图片，则引入用户头像
        final String quoteAvatar = quoteUser.getAvatar_hd();//user头像

        Glide.with(context).load(commentAvatar).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.drawable.head_pistion).into(holder.iv_head);
        holder.tv_head_name.setText(commentUserName);
        holder.tv_head_desc.setText(DateUtils.getShortTime(commentCreatedAt) + " 来自 " + Html.fromHtml(commentSource));
        holder.tv_message.setText(StringUtils.getWeiboContent(context, holder.tv_message, commentText));


        if (pic_urls == null) {
            Glide.with(context).load(quoteAvatar).placeholder(R.drawable.head_pistion).into(holder.iv_message);
        } else {
            Glide.with(context).load(quoteBmiddlePic).placeholder(R.drawable.head_pistion).into(holder.iv_message);
        }
        holder.tv_message_name.setText("@" + quoteName);
        holder.tv_message_details.setText(quoteText);

        return convertView;
    }

    private static class ViewHolder {
        LinearLayout ll_message_item;

        LinearLayout ll_card_content;
        ImageView iv_head;
        RelativeLayout rl_content;
        TextView tv_head_name;
        TextView tv_head_desc;
//        ImageView iv_more;
//        ImageView iv_reply;

        TextView tv_message;

        LinearLayout ll_message;
        ImageView iv_message;
        TextView tv_message_name;
        TextView tv_message_details;

        LinearLayout ll_message_retweeted;
        TextView tv_message_retweeted;


    }
}
