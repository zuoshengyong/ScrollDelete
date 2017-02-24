package com.example.scrolldelete;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.scrolldelete.ScrollListview.OnScrollItemClickListener;

public class MainActivity extends Activity{

	private ScrollListview scrollListview;
	private ArrayList<String> mData = new ArrayList<String>(){{

		for(int i=0;i<20;i++){
			add("吼吼吼"+i);
		}
	}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		scrollListview = (ScrollListview)findViewById(R.id.scrollListview);
		scrollListview.setAdapter(new MyAdapter());
//		scrollListview.setOnItemClickListener(new OnItemClickListener() {

//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//				intent.putExtra("text", "这是第"+position+"个");
//				startActivity(intent);
//				if(scrollListview.canClick()){
//					Toast.makeText(MainActivity.this, mData.get(position), Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
//		scrollListview.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//				startActivity(intent);
//				
//			}
//		});
		scrollListview.setOnScrollItemClickListener(new OnScrollItemClickListener() {
			
			@Override
			public void onScrollItemClick(int position) {
				Intent intent = new Intent(MainActivity.this, SecondActivity.class);
				Log.i("aaa", "position="+position);
				if(position > 0){
				intent.putExtra("text", mData.get(position-1));}
				startActivity(intent);
			}
		});
//		scrollListview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	}

	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(null == convertView){
				convertView = View.inflate(MainActivity.this, R.layout.scrolllistview_item, null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.tv);
			TextView delete = (TextView) convertView.findViewById(R.id.delete);
			tv.setText(mData.get(position));
			
			final int pos = position;
			delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mData.remove(pos);
					notifyDataSetChanged();
					scrollListview.turnToNormal();
				}
			});
			return convertView;
		}
		
	}

}
