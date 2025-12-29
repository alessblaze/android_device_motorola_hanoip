/*
 * Copyright (c) 2015 The CyanogenMod Project
 * Copyright (C) 2017-2022 The LineageOS Project
 * Copyright (C) 2025 Aless Microsystems
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
 
// The kernel logic and the sensor hal plus this tap code is compatible with
// all touch ics which have similar characteristics. not just ilitek.
// all it needs to be added correct code at correct place.
// kernel irqs and sysfs works the same mostly accross all the touch ics
// depends on where you notifying the sysfs and how much you want to
// not use vendor hals which in future may need constant patching.
// we can notify via drm/fb path which we doing currently.
// we can also notify via panel notifications.
// all we need is to find proper path of code where it notifies any vendor
// hal motorola, samsung, xiaomi, huawei doesn't matter.
// example links to read to understand
// https://github.com/LineageOS/android_kernel_motorola_sm8550-modules/blob/f3f462029acff3a64f67f4c2068ce3d97522a73f/motorola/drivers/input/touchscreen/focaltech_v2_mmi/focaltech_gesture.c#L276
// https://github.com/LineageOS/android_kernel_xiaomi_sm8250/blob/e6a50a8beb81fe11a21096b609959e376ebaa1c8/drivers/input/touchscreen/goodix_driver_gt9886/goodix_ts_gesture.c#L345

package com.moto.actions.doze;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.util.Log;

import com.moto.actions.MotoActionsSettings;
import com.moto.actions.SensorAction;
import com.moto.actions.SensorHelper;

public class TapSensor implements ScreenStateNotifier {
    private static final String TAG = "MotoActions-TapSensor";

    private final MotoActionsSettings mMotoActionsSettings;
    private final SensorHelper mSensorHelper;
    private final SensorAction mSensorAction;
    private final Sensor mTapSensor;
    private final Sensor mStowSensor;

    private boolean mEnabled;
    private boolean mIsStowed;



    public TapSensor(MotoActionsSettings MotoActionsSettings, SensorHelper sensorHelper,
                        SensorAction action) {
        mMotoActionsSettings = MotoActionsSettings;
        mSensorHelper = sensorHelper;
        mSensorAction = action;

        mTapSensor = sensorHelper.getTapSensor();
        mStowSensor = sensorHelper.getStowSensor();
    }

    @Override
    public void screenTurnedOn() {
    if (mEnabled) {
        Log.d(TAG, "Disabling");
        mSensorHelper.cancelTriggerSensor(mTapSensor, mTapTriggerListener);
        mSensorHelper.unregisterListener(mStowListener);
        mEnabled = false;
     }
    }

    @Override
    public void screenTurnedOff() {
    if (mMotoActionsSettings.isTapEnabled() && !mEnabled) {
        Log.d(TAG, "Enabling");
        mSensorHelper.requestTriggerSensor(mTapSensor, mTapTriggerListener);
        mSensorHelper.registerListener(mStowSensor, mStowListener);
        mEnabled = true;
     }
    }


    private final TriggerEventListener mTapTriggerListener = new TriggerEventListener() {
    @Override
    public synchronized void onTrigger(TriggerEvent event) {
    Log.d(TAG, "tap: ts=" + event.timestamp
            + " values=" + java.util.Arrays.toString(event.values)
            + " stowed=" + mIsStowed);

    if (!mIsStowed) mSensorAction.action();

    // Re-arm: trigger sensors are one-shot and get canceled automatically after firing.
    if (mEnabled) mSensorHelper.requestTriggerSensor(mTapSensor, this);
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
