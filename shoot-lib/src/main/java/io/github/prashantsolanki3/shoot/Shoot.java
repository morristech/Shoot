package io.github.prashantsolanki3.shoot;

import android.content.Context;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

import io.github.prashantsolanki3.shoot.listener.OnShootListener;

import static io.github.prashantsolanki3.shoot.preference.PM.getIteration;
import static io.github.prashantsolanki3.shoot.preference.PM.getRun;
import static io.github.prashantsolanki3.shoot.preference.PM.setExecutionTime;
import static io.github.prashantsolanki3.shoot.preference.PM.setIteration;
import static io.github.prashantsolanki3.shoot.preference.PM.setRun;

/**
 * Package io.github.prashantsolanki3.shoot
 * <p>
 * Created by Prashant on 1/13/2016.
 * <p>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public class Shoot {

    public static Context context;

    public static synchronized void with(Context context){
        Shoot.context = context;
    }

    public static void isInit(){
        if(context==null)
            throw new RuntimeException("Shoot must be initialized before usage.");
    }

    public static synchronized void once(String TAG, OnShootListener onShootListener) {
        once(APP_INSTALL,TAG,onShootListener);
    }
 

    public static synchronized void once(@Scope int scope, String TAG, OnShootListener onShootListener) {
        isInit();

        if(getRun(scope, TAG)){
            onShootListener.onExecute(1);
            setRun(scope,TAG, true);
            setExecutionTime(scope, TAG, new Date().getTime());
        }
    }


    public static synchronized void repeatAfter(int iterations, String Tag, OnShootListener onShootListener) {
        repeatAfter(APP_INSTALL,iterations,Tag,onShootListener);
    }


    public static synchronized void repeatAfter(@Scope int scope, int iterations, String TAG, OnShootListener onShootListener) {
        isInit();
        int iterationNo = getIteration(scope,TAG);

        if(iterationNo%iterations==0){
            onShootListener.onExecute(iterationNo+1);
            setRun(scope,TAG,true);
            setExecutionTime(scope, TAG, new Date().getTime());
        }

        setIteration(scope, TAG, iterationNo+1);
    }


    public static synchronized boolean isShot(String TAG) {
        return isShot(APP_INSTALL,TAG);
    }

 
    public static synchronized boolean isShot(@Scope int Scope, String TAG) {
        isInit();
        return false;
    }


    //TODO: shoot at a specific time. See Android-Job.
/*    public static synchronized void at(String TAG, long time, OnShootListener onShootListener) {
        isInit();
    }

 
    public static synchronized void at(String TAG, int time, OnShootListener onShootListener) {
        isInit();
    }*/


    public static final int APP_INSTALL = 0;
    public static final int APP_VERSION = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({APP_INSTALL, APP_VERSION})
    public  @interface Scope{

    }

}