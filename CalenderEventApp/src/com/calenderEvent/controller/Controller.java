package com.calenderEvent.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Controller extends Activity {

	private Context calledContext;

	/* Static variables */
	private static int activityState;
	private AlertDialog.Builder alertbox;

	public Controller(Context calledContext) {
		this.calledContext = calledContext;

	}

	public void initialRequirements() {
		Controller.setUpforListView();
	}

	private static void setUpforListView() {

	}

	public static void setActivityState(int state) {
		activityState = state;
	}

	public static int getActivityState() {

		return activityState;
	}

	public void layoutSwitcher(int targetId, int layoutId, boolean isRemoveView) {
		try {
			ViewGroup parent = (ViewGroup) ((Activity) calledContext)
					.findViewById(targetId);
			View viewSwitcher = LayoutInflater.from(calledContext).inflate(
					layoutId, null);
			if (isRemoveView)
				parent.removeViewAt(0);

			parent.addView(viewSwitcher);
		} catch (Exception e) {
			setLog("Error", e.toString(), States.SEVERITY_ERROR);
		}
	}

	public void setTextViewText(int targetId, String text) {

		try {
			TextView tv = (TextView) ((Activity) calledContext)
					.findViewById(targetId);
			tv.setText(text);
		} catch (Exception e) {
			setLog("ERROR @ TEXTView CREATION", e.toString(),
					States.SEVERITY_ERROR);

		}
	}

	public void setLog(String logMessage, String displayMessage, int severity) {

		switch (severity) {
		case States.SEVERITY_DEBUG:
		default:
			Log.d(logMessage, displayMessage);
			break;
		case States.SEVERITY_WARN:
			Log.w(logMessage, displayMessage);
			break;
		case States.SEVERITY_VERBOSE:
			Log.v(logMessage, displayMessage);
			break;
		case States.SEVERITY_INFO:
			Log.i(logMessage, displayMessage);
			break;
		case States.SEVERITY_ERROR:
			Log.e(logMessage, displayMessage);
			break;
		case States.SEVERITY_WTF:
			Log.wtf(logMessage, displayMessage);
			break;
		}

	}

	public void setImageView(View currentView, int targetImageId) {
		ImageView imageView = (ImageView) currentView;
		imageView.setImageResource(targetImageId);

	}

	public void setImageView(int targetImageId, int ImageResId) {
		ImageView imageView = (ImageView) ((Activity) calledContext)
				.findViewById(targetImageId);
		imageView.setImageResource(ImageResId);

	}

	public void resetMenuimage(int choice) {
		ImageView im;

		/*
		 * int imageViewids[] = { R.id.warmupIcon, R.id.flexStretchIcon, }; int
		 * imageResources[] = { R.drawable.warmupicon, R.drawable.stretchicon,
		 * };
		 * 
		 * for (int i = 0; i < imageViewids.length; i++) { im = (ImageView)
		 * ((Activity) calledContext) .findViewById(imageViewids[i]);
		 * im.setImageResource(imageResources[i]); }
		 * 
		 * switch (choice) { case 1: im = (ImageView) ((Activity) calledContext)
		 * .findViewById(imageViewids[choice - 1]);
		 * im.setImageResource(R.drawable.warmupiconhighlight); break;
		 * 
		 * case 2: im = (ImageView) ((Activity) calledContext)
		 * .findViewById(imageViewids[choice - 1]);
		 * im.setImageResource(R.drawable.stretchiconhighlight); break;
		 * 
		 * }
		 */
	}

	public void hapticFeedback(int miliseconds) {
		Vibrator vib = (Vibrator) calledContext
				.getSystemService(VIBRATOR_SERVICE);
		vib.vibrate(miliseconds);
	}

	public void inflateView(int targetId, int layoutId) {
		ViewGroup parent = (ViewGroup) ((Activity) calledContext)
				.findViewById(targetId);
		View viewSwitcher = LayoutInflater.from(calledContext).inflate(
				layoutId, null);
		parent.addView(viewSwitcher);

	}

	public void call_alert_donothing(String title, String message,
			String buttondisplay) {
		alertbox = new AlertDialog.Builder(calledContext);
		alertbox.setTitle(title);
		alertbox.setCancelable(true);
		alertbox.setMessage(message);

		alertbox.setPositiveButton(buttondisplay,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						hapticFeedback(60);
					}
				});
		alertbox.show();
	}

	public void call_alert_customLayout(String title, View v,
			String buttondisplay) {
		alertbox = new AlertDialog.Builder(calledContext);
		alertbox.setTitle(title);
		alertbox.setCancelable(true);
		alertbox.setView(v);

		alertbox.setPositiveButton(buttondisplay,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						hapticFeedback(60);
					}
				});
		alertbox.show();
	}
}
