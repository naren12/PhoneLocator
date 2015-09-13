package example.phonecalls;



import java.io.IOException;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashScreen extends Activity implements AnimationListener {
	
	ImageView imgPoster;
	Animation animBounce,animFadein;
	TextView txtMessage;
	LinearLayout layout;
	RelativeLayout rlayout;
	private MediaPlayer mediaplayer=new MediaPlayer();
	
	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
	    getWindow().addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    getWindow().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		setContentView(R.layout.activity_splash_screen);
		
		imgPoster = (ImageView) findViewById(R.id.imageView1);
		txtMessage = (TextView) findViewById(R.id.textView1);
		layout= (LinearLayout) findViewById(R.id.linearlayout);
		rlayout=(RelativeLayout) findViewById(R.id.relativelayout);
	
		
		animBounce = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
		animFadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
		

		animBounce.setAnimationListener(this);
		animFadein.setAnimationListener(this);
		
		imgPoster.setVisibility(View.VISIBLE);
		imgPoster.startAnimation(animBounce);
		
		txtMessage.setVisibility(View.VISIBLE);
		txtMessage.startAnimation(animFadein);
		
		layout.setVisibility(View.VISIBLE);
		layout.startAnimation(animFadein);
		
		Integer colorFrom = getResources().getColor(R.color.blue);
		Integer colorTo = getResources().getColor(R.color.blue);
		ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
		colorAnimation.addUpdateListener(new AnimatorUpdateListener() {

		    @Override
		    public void onAnimationUpdate(ValueAnimator animator) {
		    	//layout.setBackgroundColor((Integer)animator.getAnimatedValue());
		    }

		});
		colorAnimation.start();
		
		
	
		rlayout.setVisibility(View.VISIBLE);
		rlayout.startAnimation(animFadein);
		Integer rcolorFrom = getResources().getColor(R.color.red);
		Integer rcolorTo = getResources().getColor(R.color.red);
		
		ValueAnimator colorAnimationMain=ValueAnimator.ofObject(new ArgbEvaluator(), rcolorTo,rcolorFrom);
		colorAnimationMain.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				rlayout.setBackgroundColor((Integer)animation.getAnimatedValue());
			}
		});
		colorAnimationMain.start();
		
		
		mediaplayer = MediaPlayer.create(this, R.raw.sound);
		try {
			mediaplayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaplayer.start();
		
		
		
	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	@Override
	public void onBackPressed() {
	    	
		
	}
	

}
