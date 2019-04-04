package com.haylion.rxbuspublic.rxbuslib;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author:wangjianming
 * Time:2018/11/5 10:57
 * Description:RxBusScheduler:RxBus调度类
 */
public class RxBusScheduler {

    public static final String NEW_THREAD = "newThread";
    public static final String MAIN_THREAD = "main_thread";
    public static final String IO = "io";
    public static final String TEST = "test";
    public static final String COMPUTATION = "computation";
    public static final String IMMEDIATE = "immediate";
    public static final String TRAMPOLINE = "trampoline";


    private static HashMap<String, Scheduler> schedulerHashMap;

    static {
        schedulerHashMap = new HashMap<>();
        schedulerHashMap.put(NEW_THREAD, Schedulers.newThread());
        schedulerHashMap.put(MAIN_THREAD, AndroidSchedulers.mainThread());
        schedulerHashMap.put(IO, Schedulers.io());
        schedulerHashMap.put(TEST, Schedulers.test());
        schedulerHashMap.put(COMPUTATION, Schedulers.computation());
        schedulerHashMap.put(IMMEDIATE, Schedulers.immediate());
        schedulerHashMap.put(TRAMPOLINE, Schedulers.trampoline());
    }


    @StringDef({NEW_THREAD, COMPUTATION, IMMEDIATE, IO, TEST, TRAMPOLINE, MAIN_THREAD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SchedulerList {
    }

    /**
     * 获取调度器
     *
     * @param key：来自自定义注解资源SchedulerList
     * @return ：根据key获取对应的调度器
     */
    public static Scheduler getScheduler(@SchedulerList String key) {
        return schedulerHashMap.get(key);
    }


}
