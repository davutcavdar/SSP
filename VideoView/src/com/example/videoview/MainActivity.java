package com.example.videoview;


import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends Activity {

	private VideoView myVideoView1;
	private VideoView myVideoView2;
	private int position = 0;
	private ProgressDialog progressDialog;
	private MediaController mediaControls;
	private Button BtnKisa;
	private Button BtnUzun;


	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.video);

		if (mediaControls == null) {
			mediaControls = new MediaController(MainActivity.this);
		}


		myVideoView1 = (VideoView) findViewById(R.id.video_view);
		myVideoView2 = (VideoView) findViewById(R.id.video_view1);

		BtnKisa = (Button)findViewById(R.id.BtnKisa);
		BtnUzun = (Button)findViewById(R.id.BtnUzun);


		progressDialog = new ProgressDialog(MainActivity.this);
		progressDialog.setTitle("");
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);





		try {


			BtnKisa.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					myVideoView2.setVisibility(View.INVISIBLE);
					myVideoView1.setVisibility(View.VISIBLE);
					myVideoView1.setMediaController(mediaControls);
					myVideoView1.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.kisa));
					myVideoView1.start();

				}
			});


			BtnUzun.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					myVideoView1.setVisibility(View.INVISIBLE);
					myVideoView2.setVisibility(View.VISIBLE);
					myVideoView2.setMediaController(mediaControls);
					myVideoView2.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.uzun));
					myVideoView2.start();

				}
			});




		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}




		myVideoView1.setOnPreparedListener(new OnPreparedListener() {

			public void onPrepared(MediaPlayer mp) {
				progressDialog.dismiss();

				myVideoView1.seekTo(position);
				if (position == 0) {
					myVideoView1.start();
				} else {
					myVideoView1.pause();
				}
			}
		});

	}

}