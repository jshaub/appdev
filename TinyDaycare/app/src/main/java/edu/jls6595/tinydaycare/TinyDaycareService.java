package edu.jls6595.tinydaycare;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class TinyDaycareService extends Service {
    private final IBinder binder = new LocalBinder();
    Pokemon currentPokemon = PokemonList.currentPokemon;
    SensorManager manager;
    Sensor stepTracker;
    SensorEventListener stepEventListener;
    ServiceEventListener listener;

    interface ServiceEventListener {
        void onServiceEvent();
    }

    public TinyDaycareService() {

    }

    // Return the instance of the binder;
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("TinyDaycareService", "Context.SENSOR_SERVICE = " + SENSOR_SERVICE);
        Log.d("TinyDaycareService", "getSystemService() = " + getSystemService(SENSOR_SERVICE));

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Log.d("homescreen", "sensor event received");

                switch (sensorEvent.sensor.getType()) {
                    case Sensor.TYPE_STEP_DETECTOR:
                        if(currentPokemon != null) {
                            currentPokemon.incrementSteps();
                            listener.onServiceEvent();
                        }
                        break;
                    default:
                        Log.wtf("TinyDaycareService", "Fatal error in SensorEventListener onSensorChanged()");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                // no operations
            }
        };

        return binder;
    }

    void setServiceEventListener(ServiceEventListener listener) {
        this.listener = listener;
    }

    public void startStepTracker() {
        stepTracker = manager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        manager.registerListener(stepEventListener, stepTracker, SensorManager.SENSOR_DELAY_GAME);
        Log.d("homescreen", "registered listener");
    }

    public void stopStepTracker() {
        manager.unregisterListener(stepEventListener);
    }

    public class LocalBinder extends Binder {
        TinyDaycareService getService() {
            Log.d("TinyDaycareService", "returning service");
            return TinyDaycareService.this;
        }
    }
}
