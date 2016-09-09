/**
 * ChatListviewAdapter is responsible for updating the content and presentation of 
 * the chatting history Listview in ChatActivity
 * 
 * the major function here is getView to control the display of each child view in listview
 */

package com.hanay.foundsystem.chat.chatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanay.foundsystem.activity.ChatActivity;
import com.hanay.foundsystem.bean.PrivateChatBean;
import com.hanay.foundsystem.chat.bean.ChatEntity;
import com.hanay.foundsystem.chat.chatServices.ChatService;
import com.hanay.foundsystem.chat.chatServices.ChatServiceData;
import com.hanay.foundsystem.chat.network.NetworkService;
import com.hanay.foundsystem.util.SharedPreferencesUtil;
import com.hanay.foundsystem.R;


public class ChatListviewAdapter extends BaseAdapter {
	private List<ChatEntity> mVector;
	private List<Boolean> mVectorIsSelf;
	private LayoutInflater mInflater;
	private Context mContext0;
	private PrivateChatBean toUser;
	// chuan
	private String curtime;

	// chuan

	public ChatListviewAdapter(ChatActivity par, Context context,
			List<ChatEntity> vector, List<Boolean> vectorIsSelf,
			PrivateChatBean toUser) {
		this.mVector = vector;
		mInflater = LayoutInflater.from(context);
		mContext0 = context;
		mVectorIsSelf = vectorIsSelf;
		this.toUser = toUser;
	}

	public View getView(final int position, View convertView, ViewGroup root) {
		// ImageView avatar;
		// TextView content;
//		TextView NameOfSpeaker;
		boolean isComMsg = mVectorIsSelf.get(position).booleanValue();

	

		// chuan
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
		Date curDate = new Date(System.currentTimeMillis());
		curtime = formatter.format(curDate);
		// chuan

		// if (convertView == null) {
		if (isComMsg) {
			convertView = mInflater.inflate(
					R.layout.chatting_item_msg_text_right, null);
		} else {
			convertView = mInflater.inflate(
					R.layout.chatting_item_msg_text_left, null);
		}
		final ViewHolder viewHolder =new ViewHolder();
//		viewHolder = new ViewHolder();
		viewHolder.content = (TextView) convertView
				.findViewById(R.id.tv_chatcontent);
		viewHolder.isComMsg = isComMsg;
		// chuan
		viewHolder.sendTime = (TextView) convertView
				.findViewById(R.id.tv_sendtime);
		// chuan
		viewHolder.NameOfSpeaker = (TextView) convertView
				.findViewById(R.id.tv_username);
		convertView.setTag(viewHolder);
		// }else{
		// viewHolder = (ViewHolder) convertView.getTag();
		// }
		if (mVector.get(position).getType() == 3) {
			viewHolder.content.setTextColor(mContext0.getResources().getColor(
					R.color.red));

			viewHolder.content.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					viewHolder.content.setClickable(false);
				
					String str = "{\"method\":\"sendMessage\",\"info\":{\"sendUserId\":\""
							+ SharedPreferencesUtil.getSharePreStr(mContext0,
									"userInfo", "userid")
							+ "\",\"toUserId\":\""
							+ toUser.getId()
							+ "\",\"time\":\""
							+ curtime
							+ "\",\"message\":\""
							+ mVector.get(position).getContent() + "\"}}";
					NetworkService.getInstance().sendUpload(2, str);
					ChatService.getInstance().chatActivityDisplayHistory();

				}
			});

		} else {
			viewHolder.content.setTextColor(mContext0.getResources().getColor(
					R.color.black));
		}
		viewHolder.content.setText(mVector.get(position).getContent());
		// chuan
		viewHolder.sendTime.setText(mVector.get(position).getTime());
		// chuan
		if (viewHolder.isComMsg) {
			viewHolder.NameOfSpeaker.setText("我");
		} else {
			viewHolder.NameOfSpeaker.setText(toUser.getName());

		}

		return convertView;

		// ChatEntity ent0=mVector.get(position);
		// String name=ent0.getName();
		// String time=ent0.getTime();
		// int sex=ent0.getSex();
		// String real_msg=ent0.getContent();
		//
		// if(mVectorIsSelf.get(position).booleanValue())
		// {
		// convertView =
		// mInflater.inflate(R.layout.chatting_item_msg_text_right, null);
		// content=(TextView)
		// convertView.findViewById(R.id.cb0ChatListviewMsgRight);
		// NameOfSpeaker=(TextView)
		// convertView.findViewById(R.id.cb0ChatListviewNameRight);
		// // avatar=(ImageView)
		// convertView.findViewById(R.id.cb0ChatListviewAvatarRight);
		//
		// String zhengze = "f0[0-9]{2}|f10[0-7]"; //正则表达式，用来判断消息内是否有表情
		// try {
		// SpannableString spannableString =
		// ChatEmoticonUtil.getExpressionString(mContext0, real_msg, zhengze);
		// content.setText(spannableString);
		// } catch (NumberFormatException e) {
		// e.printStackTrace();
		// } catch (SecurityException e) {
		// e.printStackTrace();
		// } catch (IllegalArgumentException e) {
		// e.printStackTrace();
		// }
		//
		// NameOfSpeaker.setText(time);
		//
		// int avatarId = ent0.getSenderAvatarid();
		// if(avatarId==0)
		// avatar.setImageResource(R.drawable.cb0_h001);
		// else
		// avatar.setImageResource(R.drawable.cb0_h003);
		// }
		// else
		// {
		// convertView = mInflater.inflate(R.layout.cb0_chat_listview_item_left,
		// null);
		//
		// content=(TextView)
		// convertView.findViewById(R.id.cb0ChatListviewMsgLeft);
		//
		// String zhengze = "f0[0-9]{2}|f10[0-7]"; //正则表达式，用来判断消息内是否有表情
		// try {
		// SpannableString spannableString =
		// ChatEmoticonUtil.getExpressionString(mContext0, real_msg, zhengze);
		// content.setText(spannableString);
		// } catch (NumberFormatException e) {
		// e.printStackTrace();
		// } catch (SecurityException e) {
		// e.printStackTrace();
		// } catch (IllegalArgumentException e) {
		// e.printStackTrace();
		// }
		//
		// NameOfSpeaker=(TextView)
		// convertView.findViewById(R.id.cb0ChatListviewNameLeft);
		// NameOfSpeaker.setText(name+" "+time);
		//
		// avatar=(ImageView)
		// convertView.findViewById(R.id.cb0ChatListviewAvatarLeft);
		// int avatarId = ent0.getSenderAvatarid();
		// if(avatarId==0)
		// avatar.setImageResource(R.drawable.cb0_h001);
		// else
		// avatar.setImageResource(R.drawable.cb0_h003);
		// }
		//
		// return convertView;
	}

	static final class ViewHolder {
		TextView content;
		TextView NameOfSpeaker;
		// chuan
		TextView sendTime;
		// chaun
		public boolean isComMsg = true;

	}

	public int getCount() {
		return mVector.size();
	}

	public Object getItem(int position) {
		return mVector.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
}