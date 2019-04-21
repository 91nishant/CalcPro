package com.nian.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import java.util.ArrayList;

public class CustomLogger {
    // Tag for the App
    private static String APP_TAG = "NianApp";

    // Tag for the local components
    private static String LOCAL_TAG = "LocalTag";

    private static boolean VERBOSE = true;
    private static boolean DEBUG = true;
    private static final boolean INFO = true;
    private static final boolean WARNING = true;
    private static final boolean ERROR = true;

    private static ArrayList<String> disabledClasses = new ArrayList<String>();

    /**
     * Method to set the tag for App
     *
     * @param aContext Context of application needs to be passed
     */
    public static void setAppTag(Context aContext) {
        APP_TAG = getApplicationName(aContext);
    }

    /**
     * Method to set the tag for App
     *
     * @param aName Name of application needs to be passed
     *
     */
    public static void setAppTag(String aName) {
        APP_TAG = aName;
    }

    /**
     * Method to set the tag for local components
     *
     * @param aActivity Local activity object needs to be passed
     *
     */
    @Deprecated
    public static void setLocalTag(Activity aActivity) {
        LOCAL_TAG = aActivity.getLocalClassName();
    }

    /**
     * Method to set the tag for local components
     *
     * @param aName Local module/component/class name needs to be passed
     *
     */
    @Deprecated
    public static void setLocalTag(String aName) {
        LOCAL_TAG = aName;
    }

    /**
     * Method to set the tag for local components
     *
     * @param aName Local class instance needs to be passed
     *
     */
    @Deprecated
    public static void setLocalTag(Class aName) {
        LOCAL_TAG = aName.getSimpleName();
        // TAG = APP_TAG + " : " + LOCAL_TAG;
    }

    public static boolean setVerbose(boolean enable) {
        if (enable) VERBOSE = DEBUG = true;
        else VERBOSE = false;
        return true;
    }

    public static boolean setDebug(boolean enable) {
        DEBUG = enable;
        return true;
    }

    public static boolean disableClass(String aClassName) {
        return disabledClasses.add(aClassName);
    }

    public static boolean enableClass(String aClassName) {
        return disabledClasses.remove(aClassName);
    }

    /**
     * Method to be used by the apps to print verbose logs from their App
     *
     * @param aMessage Message to be passed which can be a set of comma separated values
     *                 formed by a combination of integers, strings or other values
     *
     */
    public static void printVerbose(Object... aMessage) {
        if (VERBOSE && (null != aMessage[0])?checkEnabled(aMessage[0].toString()):true) {
            Log.v(APP_TAG, combineMessage(aMessage));
        }
    }

    /**
     * Method to be used by the apps to print debug logs from their App
     *
     * @param aMessage Message to be passed which can be a set of comma separated values
     *                 formed by a combination of integers, strings or other values
     *
     */
    public static void printDebug(Object... aMessage) {
        if (DEBUG && (null != aMessage[0])?checkEnabled(aMessage[0].toString()):true) {
            Log.d(APP_TAG, combineMessage(aMessage));
        }
    }

    /**
     * Method to be used by the apps to print info logs from the TTS Test App
     *
     * @param aMessage Message to be passed which can be a set of comma separated values
     *                 formed by a combination of integers, strings or other values
     *
     */
    public static void printInfo(Object... aMessage) {
        if (INFO) Log.i(APP_TAG, combineMessage(aMessage));
    }

    /**
     * Method to be used by the apps to print warning logs from the TTS Test App
     *
     * @param aMessage Message to be passed which can be a set of comma separated values
     *                 formed by a combination of integers, strings or other values
     *
     */
    public static void printWarning(Object... aMessage) {
        if (WARNING) Log.w(APP_TAG, combineMessage(aMessage));
    }

    /**
     * Method to be used by the apps to print error logs from the TTS Test App
     *
     * @param aMessage Message to be passed which can be a set of comma separated values
     *                 formed by a combination of integers, strings or other values
     *
     */
    public static void printError(Object... aMessage) {
        if (ERROR) Log.e(APP_TAG, combineMessage(aMessage));
    }

    /**
     * Method to be used to get a single String by combining all individual messages
     * within an object array.
     * Visibility: private
     *
     * @param aMessage An object array formed by a combination of different values
     * @return String
     *
     */
    private static String combineMessage(Object[] aMessage) {
        String lLogMessage = "";
        for (int i=aMessage.length, j=0; i>0 && null != aMessage[j]; i--, j++) lLogMessage = lLogMessage + " " + aMessage[j].toString();
        return lLogMessage;
    }

    private static boolean checkEnabled(String aClassName) {
        return !disabledClasses.contains(aClassName);
    }

    private static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }
}