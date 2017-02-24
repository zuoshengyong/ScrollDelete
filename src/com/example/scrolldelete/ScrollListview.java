package com.example.scrolldelete;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ScrollListview extends ListView{

	private int mScreenWidth;//屏幕宽度
	private int mDownX;//按下点的x值
	private int mDownY;//按下点的y值
	private int mDeleteBtnWidth;//删除按钮的宽度
	private boolean isDeleteShown;//删除按钮是否正在显示
	private ViewGroup mPointChild;//当前处理的item
	private LinearLayout.LayoutParams mLayoutParams;//当前处理的item的LayoutParams

	public ScrollListview(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ScrollListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		//获取屏幕宽度
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
	}

	public boolean onTouchEvent(MotionEvent ev){
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i("aaa", "ACTION_DOWN");
			performActionDown(ev);
			if(a){
				a = false;
				return false;}
			else
				break;
		case MotionEvent.ACTION_MOVE:
			Log.i("aaa", "ACTION_MOVE");
			return performActionMove(ev);
		case MotionEvent.ACTION_UP:
			Log.i("aaa", "ACTION_UP");
			performActionUp(ev); 
			break;
		}
		return super.onTouchEvent(ev);
	}
	boolean a = false;
	//处理action_down
	private void performActionDown(MotionEvent ev) {
		if(isDeleteShown){
			turnToNormal();
			a = true;
			return;
		}
		mDownX = (int) ev.getX();
		mDownY = (int) ev.getY();
		Log.i("aaa", "mDownX="+mDownX);
		Log.i("aaa", "mDownY="+mDownY);
		Log.i("aaa", "pointToPosition(mDownX, mDownY)="+pointToPosition(mDownX, mDownY));
		//获取当前点的item
		if(pointToPosition(mDownX, mDownY) == -1){
			return;
		}
		mPointChild = (ViewGroup) getChildAt(pointToPosition(mDownX, mDownY) - getFirstVisiblePosition());
		mDeleteBtnWidth = mPointChild.getChildAt(1).getLayoutParams().width;
		mLayoutParams = (LinearLayout.LayoutParams) mPointChild.getChildAt(0).getLayoutParams();
		smoothScroll(mLayoutParams, mScreenWidth);
//		mLayoutParams.width = mScreenWidth;
//		mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
	}

	private boolean performActionMove(MotionEvent ev){
		int nowX = (int) ev.getX();
		int nowY = (int) ev.getY();
		if(Math.abs(nowX - mDownX) > Math.abs(nowY - mDownY)){
			if(nowX < mDownX){
				int scroll = (nowX - mDownX)/2;
				if(-scroll >= mDeleteBtnWidth){
					scroll = -mDeleteBtnWidth;
				}
				mLayoutParams.leftMargin = scroll;
				mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
			}
			return true;
		}else{
			turnToNormal();
		}
		return super.onTouchEvent(ev);
	}
	int position = 0;
	private void performActionUp(MotionEvent ev) {
		//判断是上下滑动还是左右滑动
		int nowX = (int) ev.getX();
		int nowY = (int) ev.getY();
		Log.i("aaa", "nowX="+nowX);
		Log.i("aaa", "nowY="+nowY);
		int scrollDis = Math.abs(nowX - mDownX) - Math.abs(nowY - mDownY);
		if(Math.abs(nowX - mDownX)<8 && Math.abs(nowY - mDownY)<8){
			position = pointToPosition(mDownX, mDownY) + 1;
			Log.i("aaa", "position="+position);
			onScrollItemClickListener.onScrollItemClick(position);
			Toast.makeText(getContext(), "position="+position, Toast.LENGTH_SHORT).show();
			Log.i("aaa", "pointToPosition(mDownX, mDownY)="+pointToPosition(mDownX, mDownY));
		}
		if(scrollDis >= 0){
			// 偏移量大于button的一半，则显示button,否则恢复默认
			if(-mLayoutParams.leftMargin >= mDeleteBtnWidth/2){
				mLayoutParams.leftMargin = -mDeleteBtnWidth;
				isDeleteShown = true;
			}else if(-mLayoutParams.leftMargin > 8 && -mLayoutParams.leftMargin < mDeleteBtnWidth/2){
				turnToNormal();
			}
			mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
		}else{
			turnToNormal();
		} 
			
	}

	public void turnToNormal() {
		mLayoutParams.leftMargin = 0;
		mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
		isDeleteShown = false;
	}

	private void smoothScroll(LinearLayout.LayoutParams mLayoutParams,int end){
		mLayoutParams.width = end;
		mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
	}
	//是否可点击
	public boolean canClick(){
		return !isDeleteShown;
	}

	public interface OnScrollItemClickListener{

		void onScrollItemClick(int position);
	}
	private OnScrollItemClickListener onScrollItemClickListener;
	public void setOnScrollItemClickListener(OnScrollItemClickListener onScrollItemClickListener){
		this.onScrollItemClickListener = onScrollItemClickListener;
	}
}
