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
    private static final int MOVE_LIMITATION = 100;// �����ƶ������ؾ���  
    private float mLastMotionX; // ��ָ������Ļ�����һ��x����  
    private int mCurScreen;  
  
    private Scroller mScroller; // �����ؼ�  
   
  
    private void init(Context context) {  
        mScroller = new Scroller(context);  
        mCurScreen = 0;// Ĭ��������ʾ��һ��VIEW  
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
                // snapToDestination(); // ����ָ��ҳ  
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
     * ���ݻ����ľ����ж��ƶ����ڼ�����ͼ 
     */  
    public void snapToDestination() {  
        final int screenWidth = getWidth();  
        final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;  
        snapToScreen(destScreen);  
    }  
  
    /** 
     * �������ƶ�����ͼ 
     *  
     * @param whichScreen 
     *            ��ͼ�±� 
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
     * �������������¼��ģ�ÿ�������¼������ȵ������������Layout���onInterceptTouchEventĬ�Ϸ���ֵ��false, 
     * ����touch�¼��ᴫ�ݵ�childview�ؼ� ���������false�ӿؼ�������Ӧ�������˿ؼ�����Ӧ��������Ҫ�������ӿؼ�����Ӧ�� 
     * ��ViewGroup���ܷ���ֵ��ʲô����ִ��onTouchEvent 
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

