package com.auction.view;
 

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Scroller;

public class ImageViewPager extends ViewPager {

	public ImageViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public ImageViewPager(Context context, AttributeSet attrs) {
		
		super(context,attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}
	private static final String TAG = "dzt_pager";  
    private static final int MOVE_LIMITATION = 100;// 触发移动的像素距离  
    private float mLastMotionX; // 手指触碰屏幕的最后一次x坐标  
    private int mCurScreen;  
  
    private Scroller mScroller; // 滑动控件  
   
  
    private void init(Context context) {  
        mScroller = new Scroller(context);  
        mCurScreen = 0;// 默认设置显示第一个VIEW  
    }  
  
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        // TODO Auto-generated method stub  
  
        final int action = event.getAction();  
        final float x = event.getX();  
        switch (action) {  
        case MotionEvent.ACTION_DOWN:  
            mLastMotionX = x;  
            break;  
        case MotionEvent.ACTION_MOVE:  
            break;  
        case MotionEvent.ACTION_UP:  
            if (Math.abs(x - mLastMotionX) < MOVE_LIMITATION) {  
                // snapToDestination(); // 跳到指定页  
                snapToScreen(getCurrentItem());  
                return true;  
            }  
            break;  
        default:  
            break;  
        }  
        return super.onTouchEvent(event);  
    }  
  
    @Override  
    public void computeScroll() {  
        // TODO Auto-generated method stub  
        super.computeScroll();  
  
        if (mScroller.computeScrollOffset()) {  
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());  
            invalidate();  
        }  
  
    }  
  
    /** 
     * 根据滑动的距离判断移动到第几个视图 
     */  
    public void snapToDestination() {  
        final int screenWidth = getWidth();  
        final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;  
        snapToScreen(destScreen);  
    }  
  
    /** 
     * 滚动到制定的视图 
     *  
     * @param whichScreen 
     *            视图下标 
     */  
    public void snapToScreen(int whichScreen) {  
        // whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() -  
        // 1));  
        if (getScrollX() != (whichScreen * getWidth())) {  
  
            final int delta = whichScreen * getWidth() - getScrollX();  
            
            mScroller.startScroll(getScrollX(), 0, delta, 0,  
                    Math.abs(delta) * 2);  
            mCurScreen = whichScreen;  
            invalidate();  
        }  
    }  
  
    /** 
     * 用于拦截手势事件的，每个手势事件都会先调用这个方法。Layout里的onInterceptTouchEvent默认返回值是false, 
     * 这样touch事件会传递到childview控件 ，如果返回false子控件可以响应，否则了控件不响应，这里主要是拦截子控件的响应， 
     * 对ViewGroup不管返回值是什么都会执行onTouchEvent 
     */  
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent arg0) {  
        // TODO Auto-generated method stub  
        final int action = arg0.getAction();  
        final float x = arg0.getX();  
        switch (action) {  
        case MotionEvent.ACTION_DOWN:  
            mLastMotionX = x;  
            break;  
        case MotionEvent.ACTION_MOVE:  
            break;  
        case MotionEvent.ACTION_UP:  
            break;  
        default:  
            break;  
        }  
        return super.onInterceptTouchEvent(arg0);  
    }  
}  

