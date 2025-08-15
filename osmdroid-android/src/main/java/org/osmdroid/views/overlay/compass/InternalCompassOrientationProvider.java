package org.osmdroid.views.overlay.compass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class InternalCompassOrientationProvider implements SensorEventListener, IOrientationProvider {
    private IOrientationConsumer mOrientationConsumer;
    private SensorManager mSensorManager;
    private float mAzimuth;
    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    public InternalCompassOrientationProvider(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    //
    // IOrientationProvider
    //

    /**
     * Enable orientation updates from the internal compass sensor and show the compass.
     */
    @Override
    public boolean startOrientationProvider(IOrientationConsumer orientationConsumer) {
        mOrientationConsumer = orientationConsumer;
        boolean result = false;

        final Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (sensor != null) {
            result = mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }
        return result;
    }

    @Override
    public void stopOrientationProvider() {
        mOrientationConsumer = null;
        mSensorManager.unregisterListener(this);
    }

    @Override
    public float getLastKnownOrientation() {
        return mAzimuth;
    }

    @Override
    public void destroy() {
        stopOrientationProvider();
        mOrientationConsumer = null;
        mSensorManager = null;
    }

    //
    // SensorEventListener
    //

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {
        // This is not interesting for us at the moment
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
            SensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
            mAzimuth = (float) Math.toDegrees(mOrientationAngles[0]);
            if (mOrientationConsumer != null) {
                mOrientationConsumer.onOrientationChanged(mAzimuth, this);
            }
        }
    }
}
