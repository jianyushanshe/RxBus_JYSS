package com.haylion.rxbuspublic.rxbuslib;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author:wangjianming
 * Time:2018/11/5 10:31
 * Description:RxBusReactAnnotaion:自定义注解类，通过注解让方法响应RxBus事件
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RxBusReact {
    /**
     * 事件实体类对象，@NonNull注解为非空对象
     *
     * @return
     */
    Class clazz();

    /**
     * 事件的标记
     *
     * @return
     */
    String tag() default RxBus.DEFAULT_TAG;

    /**
     * 事件发布所在的线程
     *
     * @return
     */

    @RxBusScheduler.SchedulerList
    String subScribeOn() default RxBusScheduler.IMMEDIATE;

    /**
     * 事件响应所在的线程
     *
     * @return
     */

    @RxBusScheduler.SchedulerList
    String observeOn() default RxBusScheduler.IMMEDIATE;
}
