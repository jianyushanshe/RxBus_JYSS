package com.haylion.rxbuspublic.rxbuslib;

import android.annotation.SuppressLint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.SerializedSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Author:wangjianming
 * Time:2018/11/5 10:43
 * Description:RxBus
 */
public class RxBus {

    /**
     * 默认标记
     */
    public static final String DEFAULT_TAG = "default";
    private static RxBus rxBus;
    private SerializedSubject<RxBusEvent, RxBusEvent> serializedSubject;
    private SerializedSubject<RxBusEvent, RxBusEvent> eventSerializedSubject;
    private HashMap<String, CompositeSubscription> subscriptionHashMap;

    @SuppressLint("NewApi")
    private RxBus() {
        serializedSubject = new SerializedSubject<>(PublishSubject.<RxBusEvent>create());
        eventSerializedSubject = new SerializedSubject<>(ReplaySubject.<RxBusEvent>create(1));
        subscriptionHashMap = new HashMap<>();
    }

    public static RxBus getInstance() {
        if (rxBus == null) {
            rxBus = new RxBus();
        }
        return rxBus;
    }

    /**
     * 注册RxBus
     * 可以获Class对象，除继承的方法外的其他所有的方法
     */
    @SuppressLint("NewApi")
    public void register(final Object object) {
        CompositeSubscription subscriptions = new CompositeSubscription();
        for (final Method method : object.getClass().getDeclaredMethods()) {
            RxBusReact rxBusReactAnnotaion = method.getAnnotation(RxBusReact.class);
            if (rxBusReactAnnotaion != null) {
                final String tag = rxBusReactAnnotaion.tag();
                final Class clazz = rxBusReactAnnotaion.getClass();
                Scheduler observeScheduler = RxBusScheduler.getScheduler(rxBusReactAnnotaion.observeOn());
                Scheduler subscribeScheduler = RxBusScheduler.getScheduler(rxBusReactAnnotaion.subScribeOn());
                Subscription subscription = getObservable().subscribeOn(subscribeScheduler).filter(new Func1<RxBusEvent, Boolean>() {
                    @Override
                    public Boolean call(RxBusEvent rxBusEvent) {
                        return clazz.equals(rxBusEvent.getObject().getClass()) &&
                                tag.equals(rxBusEvent.getTag());
                    }
                }).observeOn(observeScheduler).subscribe(new Action1<RxBusEvent>() {
                    @Override
                    public void call(RxBusEvent rxBusEvent) {
                        try {
                            method.invoke(object, rxBusEvent.getObject());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
                subscriptions.add(subscription);
            }
        }
        subscriptionHashMap.put(object.getClass().getName(), subscriptions);
    }

    /**
     * 注册RxBus
     * 可以获取Class对象，所有public的方法，包含继承的方法
     *
     * @param object
     * @param isGetAllMethods
     */
    @SuppressLint("NewApi")
    public void register(final Object object, boolean isGetAllMethods) {
        CompositeSubscription subscriptions = new CompositeSubscription();
        for (final Method method : object.getClass().getMethods()) {
            RxBusReact rxBusReactAnnotaion = method.getAnnotation(RxBusReact.class);
            if (rxBusReactAnnotaion != null) {
                final String tag = rxBusReactAnnotaion.tag();
                final Class clazz = rxBusReactAnnotaion.clazz();
                Scheduler observeScheduler = RxBusScheduler.getScheduler(rxBusReactAnnotaion.observeOn());
                Scheduler subscribeScheduler = RxBusScheduler.getScheduler(rxBusReactAnnotaion.subScribeOn());
                Subscription subscription = getObservable().subscribeOn(subscribeScheduler).filter(new Func1<RxBusEvent, Boolean>() {
                    @Override
                    public Boolean call(RxBusEvent rxBusEvent) {
                        return clazz.equals(rxBusEvent.getObject().getClass()) && tag.equals(rxBusEvent.getTag());
                    }
                }).observeOn(observeScheduler).subscribe(new Action1<RxBusEvent>() {
                    @Override
                    public void call(RxBusEvent rxBusEvent) {
                        try {
                            method.invoke(object, rxBusEvent.getObject());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
                subscriptions.add(subscription);
            }
        }
        subscriptionHashMap.put(object.getClass().getName(), subscriptions);
    }

    /**
     * 注册RxBus
     * 传入自定义Tag
     *
     * @param object
     * @param tag
     */
    public void register(final Object object, final String tag) {
        CompositeSubscription subscriptions = new CompositeSubscription();
        for (final Method method : object.getClass().getDeclaredMethods()) {
            RxBusReact rxBusReactAnnotaion = method.getAnnotation(RxBusReact.class);
            if (rxBusReactAnnotaion != null) {
                final Class clazz = rxBusReactAnnotaion.clazz();
                Scheduler observeScheduler = RxBusScheduler.getScheduler(rxBusReactAnnotaion.observeOn());
                Scheduler subscribeScheduler = RxBusScheduler.getScheduler(rxBusReactAnnotaion.subScribeOn());
                Subscription subscription = getObservable().subscribeOn(subscribeScheduler).filter(new Func1<RxBusEvent, Boolean>() {
                    @Override
                    public Boolean call(RxBusEvent rxBusEvent) {
                        return tag.equals(rxBusEvent.getTag()) && clazz.equals(rxBusEvent.getObject().getClass());
                    }
                }).observeOn(observeScheduler).subscribe(new Action1<RxBusEvent>() {
                    @Override
                    public void call(RxBusEvent rxBusEvent) {
                        try {
                            method.invoke(object, rxBusEvent.getObject());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
                subscriptions.add(subscription);
            }

        }
        subscriptionHashMap.put(object.getClass().getName(), subscriptions);
    }

    /**
     * 反注册RxBus
     *
     * @param object
     */
    @SuppressLint("NewApi")
    public void unregister(Object object) {
        String key = object.getClass().getName();
        CompositeSubscription subscriptions = subscriptionHashMap.get(key);
        if (subscriptions != null && !subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
            subscriptionHashMap.remove(key);
        }
    }

    /**
     * 获取事件发布者
     *
     * @return：事件发布者
     */
    public Observable<RxBusEvent> getObservable() {
        return serializedSubject.asObservable().mergeWith(eventSerializedSubject.asObservable());
    }

    /**
     * 发送一个事件，并且标记Tag，只有标记的地方才可以响应
     * 需同时传入参数和Tag
     *
     * @param object
     * @param tag
     */
    public void post(Object object, String tag) {
        if (serializedSubject != null) {
            serializedSubject.onNext(new RxBusEvent(object, tag));
        }
    }

    /**
     * 发送一个事件，并且标记Tag，只有标记的地方才可以响应。
     * 只传入Tag，参数默认为Object
     *
     * @param tag
     */
    public void post(String tag) {
        if (serializedSubject != null) {
            serializedSubject.onNext(new RxBusEvent(new Object(), tag));
        }
    }

    /**
     * 发送一个事件，并且标记Tag，只有标记的地方才可以响应。
     * 只传入参数，默认Tag
     *
     * @param object
     */
    public void post(Object object) {
        if (serializedSubject != null) {
            serializedSubject.onNext(new RxBusEvent(object, DEFAULT_TAG));
        }
    }

    /**
     * 发送一个事件，Tag为默认值，参数为Object
     */
    public void post() {
        if (serializedSubject != null) {
            serializedSubject.onNext(new RxBusEvent(new Object(), DEFAULT_TAG));
        }
    }

}
