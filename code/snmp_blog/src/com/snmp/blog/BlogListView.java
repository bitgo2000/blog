package com.snmp.blog;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.snmp.utils.LogUtils;

public class BlogListView extends ListView {
	protected BlogTweetListAdapter mTweetAdapter;
    protected BlogNostrListAdapter mNostrAdapter;
	private Activity mActivity;

	public BlogListView(Context context) {
		super(context);
	}

	public BlogListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BlogListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	public void init(Activity activity) {
	    mActivity = activity;
	    if (BlogListActivity.TWEET) {
	        mTweetAdapter = new BlogTweetListAdapter(null);
	        setAdapter(mTweetAdapter);
	    } else {
	        mNostrAdapter = new BlogNostrListAdapter(null);
	        setAdapter(mNostrAdapter);
	    }
		setOnItemClickListener(mItemClickListener);
	}

	private AdapterView.OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> av, View view, int position,
				long id) {
			BlogListView.this.onItemClick(view, id);
		}
	};

	protected void onItemClick(View view, long id) {
		int realPosition = (int) id;
		String data;
		if (BlogListActivity.TWEET) {
		    data = ((BlogTweetItemData) mTweetAdapter.getItem(realPosition)).mContent;
		} else {
            data = ((NostrItemData) mNostrAdapter.getItem(realPosition)).mContent;
		}
		try {
//			Intent intent = new Intent();
//			intent.setClassName(SnmpApplication.getInstance().getPackageName(),
//					BlogDetailActivity.class.getName());
//
//			Bundle bundle = new Bundle();
//			bundle.putString("id", item.mId);
//			intent.putExtras(bundle);
//			intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
//
//			SnmpApplication.getInstance().startActivity(intent);
		    SelectDialog.showDialog(mActivity, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@ExportedProperty(category = "drawing")
	public boolean hasOverlappingRendering() {
		return false;
	}

	public class BlogTweetListAdapter extends BaseAdapter {
		private ArrayList<BlogTweetItemData> mDataList = new ArrayList<BlogTweetItemData>();

		public BlogTweetListAdapter(ArrayList<BlogTweetItemData> appData) {
			mDataList = appData;
		}

		public void refresh(ArrayList<BlogTweetItemData> data) {
			mDataList = data;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			if (mDataList != null) {
				return mDataList.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int index) {
			if (mDataList != null) {
				return mDataList.get(index);
			} else {
				return null;
			}
		}

		@Override
		public long getItemId(int index) {
			return index; 
		}

		@Override
		public View getView(int position, View covertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (covertView == null) {
				covertView = LayoutInflater.from(getContext()).inflate(
						R.layout.blog_list_item, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.mIcon = (ImageView) covertView
						.findViewById(R.id.list_item_avatar);
				viewHolder.mContent = (TextView) covertView
						.findViewById(R.id.list_item_content);
                viewHolder.mTimeStamp = (TextView) covertView
                        .findViewById(R.id.list_item_title_timestamp);
				covertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) covertView.getTag();
			}

			String itemTxt = mDataList.get(position).mContent;
			viewHolder.mContent.setText(itemTxt);
			if (mDataList.get(position).mCrypto) {
			    viewHolder.mIcon.setBackgroundResource(R.drawable.blog_crypto);
			} else {
                viewHolder.mIcon.setBackgroundResource(R.drawable.blog_icon);
			}
            String timeStamp = mDataList.get(position).mTimeStamp;
            viewHolder.mTimeStamp.setText(timeStamp);
			return covertView;
		}

		public ArrayList<BlogTweetItemData> getDataList() {
			return mDataList;
		}
	}

    public class BlogNostrListAdapter extends BaseAdapter {
        private ArrayList<NostrItemData> mDataList = new ArrayList<NostrItemData>();

        public BlogNostrListAdapter(ArrayList<NostrItemData> appData) {
            mDataList = appData;
        }

        public void refresh(ArrayList<NostrItemData> dataList) {
            mDataList = dataList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mDataList != null) {
                return mDataList.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int index) {
            if (mDataList != null) {
                return mDataList.get(index);
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int index) {
            return index; 
        }

        @Override
        public View getView(int position, View covertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (covertView == null) {
                covertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.blog_list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mIcon = (ImageView) covertView
                        .findViewById(R.id.list_item_avatar);
                viewHolder.mContent = (TextView) covertView
                        .findViewById(R.id.list_item_content);
                covertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) covertView.getTag();
            }

            String itemTxt = mDataList.get(position).mContent;
            viewHolder.mContent.setText(itemTxt);
            return covertView;
        }

        public ArrayList<NostrItemData> getDataList() {
            return mDataList;
        }
    }

    private class ViewHolder {
		ImageView mIcon;
		TextView mContent;
        TextView mTimeStamp;
	}
}
