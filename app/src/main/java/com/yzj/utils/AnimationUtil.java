package com.yzj.utils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimationUtil {
    
    public static void startTranslate(View v,float x,float dx,float y,float dy,long duration,AnimationListener listener){
		TranslateAnimation translateAnimation = new TranslateAnimation(
			Animation.RELATIVE_TO_SELF,x,
			Animation.RELATIVE_TO_SELF,dx,
			Animation.RELATIVE_TO_SELF,y,
			Animation.RELATIVE_TO_SELF,dy
        );
        translateAnimation.setDuration(duration);
        translateAnimation.setFillAfter(true);
		if(listener!=null)translateAnimation.setAnimationListener(listener);
		v.startAnimation(translateAnimation);
	}
    
	
	public abstract static class AnimationListener implements Animation.AnimationListener {

		@Override
		public void onAnimationStart(Animation anim) {
		}

		@Override
		public abstract void onAnimationEnd(Animation anim)
		
		@Override
		public void onAnimationRepeat(Animation anim) {
		}



		
	}
}
