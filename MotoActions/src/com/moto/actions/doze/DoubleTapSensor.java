/*
 * Copyright (c) 2015 The CyanogenMod Project
 * Copyright (C) 2017-2022 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// This is a special trigger sensor it shows KEY_F1 somehow on trigger at /dev/input/event2
// writing a hal is cleanest way

package com.moto.actions.doze;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import com.moto.actions.MotoActionsSettings;
import com.moto.actions.SensorAction;
import com.moto.actions.SensorHelper;

public class DoubleTapSensor implements ScreenStateNotifier {
    private static final String TAG = "MotoActions-DoubleTapSensor";

    private final MotoActionsSettings mMotoActionsSettings;
    private final SensorHelper mSensorHelper;
    private final SensorAction mSensorAction;
    private final Sensor mDoubleTapSensor;
    private final Sensor mStowSensor;

    private boolean mEnabled;
    private boolean mIsStowed;
    private boolean mLastDoubleTap;

    public DoubleTapSensor(MotoActionsSettings MotoActionsSettings, SensorHelper sensorHelper,
                        SensorAction action) {
        mMotoActionsSettings = MotoActionsSettings;
        mSensorHelper = sensorHelper;
        mSensorAction = action;

        mDoubleTapSensor = sensorHelper.getDoubleTapSensor();
        mStowSensor = sensorHelper.getStowSensor();
    }

    @Override
    public void screenTurnedOn() {
        if (mEnabled) {
            Log.d(TAG, "Disabling");
            mSensorHelper.unregisterListener(mDoubleTapListener);
            mSensorHelper.unregisterListener(mStowListener);
            mEnabled = false;
        }
    }

    @Override
    public void screenTurnedOff() {
        if (mMotoActionsSettings.isPickUpEnabled() && !mEnabled) {
            Log.d(TAG, "Enabling");
            mSensorHelper.registerListener(mDoubleTapSensor, mDoubleTapListener);
            mSensorHelper.registerListener(mStowSensor, mStowListener);
            mEnabled = true;
        }
    }

    private final SensorEventListener mDoubleTapListener = new SensorEventListener() {
        @Override
        public synchronized void onSensorChanged(SensorEvent event) {
            boolean thisDoubleTap = (event.values[0] != 0);

            Log.d(TAG, "event: " + thisDoubleTap + " mLastDoubleTap=" + mLastDoubleTap + " mIsStowed=" +
                    mIsStowed);

            if (mLastDoubleTap && !thisDoubleTap && !mIsStowed) {
                mSensorAction.action();
            }
            mLastDoubleTap = thisDoubleTap;
        }

        @Override
        public void onAccuracyChanged(Sensor mSensor, int accuracy) {
        }
    };

    private final SensorEventListener mStowListener = new SensorEventListener() {
        @Override
        public synchronized void onSensorChanged(SensorEvent event) {
            mIsStowed = (event.values[0] != 0);
        }

        @Override
        public void onAccuracyChanged(Sensor mSensor, int accuracy) {
        }
    };
}
