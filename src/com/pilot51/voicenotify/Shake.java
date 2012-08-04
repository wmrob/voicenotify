package com.pilot51.voicenotify;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Shake implements SensorEventListener {
	private SensorManager manager;
	private Sensor sensor;
	private OnShakeListener listener;
	private static final int SHAKE_THRESHOLD = 800;
	private float lastX, lastY, lastZ;
	private long lastUpdate;

	public Shake(Context c) {
		manager = (SensorManager)c.getSystemService(Context.SENSOR_SERVICE);
		sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}
	
	protected void enable() {
		manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
	}
	
	protected void disable() {
		manager.unregisterListener(this);
	}

	public void setOnShakeListener(OnShakeListener listener) {
		this.listener = listener;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.equals(sensor)) {
			long timeCur = System.currentTimeMillis(),
				timeDiff = timeCur - lastUpdate;
			if (timeCur - lastUpdate > 100) {
				float x = event.values[SensorManager.DATA_X],
					y = event.values[SensorManager.DATA_Y],
					z = event.values[SensorManager.DATA_Z],
					speed = Math.abs(x + y + z - lastX - lastY - lastZ) / timeDiff * 10000;
				if (speed > SHAKE_THRESHOLD && listener != null)
					listener.onShake();
				lastX = x;
				lastY = y;
				lastZ = z;
				lastUpdate = timeCur;
			}
		}
	}

	public interface OnShakeListener {
		public void onShake();
	}
}