package com.example.moneyonmobileapplication;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;


public class SignatureView extends View {
    
    private static final float 	MINP = 0.25f;
    private static final float 	MAXP = 0.75f;
    public Paint       		mPaint;
    private Canvas      		mCanvas;
    private Path        		mPath;
    private Paint       		mBitmapPaint;
    public  Bitmap    	mBitmap;
    public boolean 		movedone = false;
    Display display; 
    int width;
    int height;
    boolean drawSignaturText =false;
    public boolean signatureDrawn = false; 
    Intent reintent = new Intent();
    public SignatureView(Context c, AttributeSet attrs) 
    {
        super(c, attrs);
       display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
       width = display.getWidth();
       height = display.getHeight();
        
        
       
    /* display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
       // width = display.getWidth();
        Point size = new Point();
        overrideGetSize(display, size);
        
                     
        if (  Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 13 ) {
         
         
         
        	 display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
                 int width = display.getWidth();
                 int height = display.getHeight();
         
         
         
               } else {
         
         
            	   display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
                      Point size1 = new Point();
                      display.getSize(size1);
                      int width = size.x;
                      int height = size.y;
         
         
         
               }*/
        System.out.println("The width and height of the screen " + width + "  " + height);
        setFocusable(true);
        setFocusableInTouchMode(true);
        
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        //mPaint.setColor(0xFFFF0000);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);
        
        mPath 	= new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
      	
    }

   /*private void overrideGetSize(Display display, Point size) {
		// TODO Auto-generated method stub
    	try {
            // test for new method to trigger exception
            Class pointClass = Class.forName("android.graphics.Point");
            Method newGetSize = display.class.getMethod("getSize", new Class[]{ pointClass });
            
            // no exception, so new method is available, just use it
            newGetSize.invoke(outSize);
          } catch(NoSuchMethodException ex) {
            // new method is not available, use the old ones
            outSize.x = display.getWidth();
            outSize.y = display.getHeight();
          }
		
	}*/

	@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) 
    {
        super.onSizeChanged(w, h, oldw, oldh);
      	System.out.println("Onsize changed widht " + w + " h "  + h + " ow " + oldw + " oldh " +oldh);
      	
    	mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
      	mCanvas = new Canvas(mBitmap);
      	mCanvas.drawColor(Color.WHITE);
      	
        //if(!drawSignaturText)
        //{
        	Paint paint = new Paint();
        	paint.setTextSize(10);
 			// smooth's out the edges of what is being drawn
        	paint.setAntiAlias(true);
        	drawSignaturText = true;
        	Rect bounds = new Rect();
            paint.getTextBounds("Signature", 0, 9, bounds);
        	System.out.println("The size of the mCanvas is  " +  mCanvas.getWidth() + " " + mCanvas.getHeight() + " " +  bounds.width() + " " + bounds.height());
            
        	int y = mCanvas.getHeight() - (bounds.height() *2 );
        	System.out.println("The y position is " + y);
        	mCanvas.drawRect(3, mCanvas.getHeight() - (bounds.height() * 2), mCanvas.getWidth() -3, mCanvas.getHeight() - ((bounds.height() *2)+1), paint);
        	//mCanvas.drawRect(2,30, 300, 31, paint);
            mCanvas.drawText("Signature", mCanvas.getWidth()-(bounds.width()+10), mCanvas.getHeight() - (bounds.height()), paint);
          
        //}

  
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	//System.out.println("the ondrae canvas widht " + getWidth() + " height " + getHeight());
    	//canvas.drawColor(0xFFAAAAAA);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
        
  
    }
    
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
    
    private void touch_start(float x, float y) {
    	signatureDrawn = true;
        mPath.reset();
        mPath.moveTo(x, y); 
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    
    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	this.getParent().requestDisallowInterceptTouchEvent(true);
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                movedone = true;
                break;
            case MotionEvent.ACTION_UP:
            	
                touch_up();
                invalidate();
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return true;
    }
    
    public void clear()
    {
     	mCanvas.drawColor(Color.WHITE);
      	
        //if(!drawSignaturText)
        //{
            
         	Paint paint = new Paint();
        	paint.setTextSize(10);
 			// smooth's out the edges of what is being drawn
        	paint.setAntiAlias(true);
        	drawSignaturText = true;
        	Rect bounds = new Rect();
            paint.getTextBounds("Signature", 0, 9, bounds);
        	System.out.println("The size of the mCanvas is  " +  mCanvas.getWidth() + " " + mCanvas.getHeight() + " " +  bounds.width() + " " + bounds.height());
            
        	int y = mCanvas.getHeight() - (bounds.height() *2 );
        	System.out.println("The y position is " + y);
        	mCanvas.drawRect(3, mCanvas.getHeight() - (bounds.height() * 2), mCanvas.getWidth() -3, mCanvas.getHeight() - ((bounds.height() *2)+1), paint);
        	//mCanvas.drawRect(2,30, 300, 31, paint);
            mCanvas.drawText("Signature", mCanvas.getWidth()-(bounds.width()+10), mCanvas.getHeight() - (bounds.height()), paint);
            invalidate();
            
            
            
            
            
    }
   
	public void onUserLeaveHint() {
		this.clear();
	}

	
}