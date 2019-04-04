package com.haylion.rxbuspublic.rxbuslib;


/**
 * Author:wangjianming
 * Time:2018/11/5 10:23
 * Description:RxBusEvent,事件实体类
 */
public class RxBusEvent {
    /**
     * 事件的标记
     */
    private String tag;
    /**
     * 事件的主体
     */
    private Object object;

    /**
     * 事件的构造方法
     *
     * @param tag
     * @param object
     */
    public RxBusEvent(Object object, String tag) {
        this.tag = tag;
        this.object = object;
    }

    /**
     * 获取事件标记
     */
    public String getTag() {
        return tag;
    }

    /**
     * 设置事件标记
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * 获取主体
     */
    public Object getObject() {
        return object;
    }

    /**
     * 设置主体
     */
    public void setObject(Object object) {
        this.object = object;
    }
}
