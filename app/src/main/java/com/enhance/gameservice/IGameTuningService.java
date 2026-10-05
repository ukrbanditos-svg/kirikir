package com.enhance.gameservice;

import android.os.IBinder;

public interface IGameTuningService {
    int setPreferredResolution(int value);
    int setFramePerSecond(int value);
    int boostUp(int seconds);
    int getAbstractTemperature();
    int setGamePowerSaving(boolean enabled);

    abstract class Stub {
        public static IGameTuningService asInterface(IBinder binder) {
            return null;
        }
    }
}
