package com.example.forstudy.sub;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

// Should enroll service at Manifest file
public class ServiceExample extends Service {

	public ServiceExample() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
	}
}