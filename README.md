RxBus使用文档

版本号	修改人	时间	备注
1.0.0	王建明	2018-11-22	

1.集成步骤

(1).在工程的build.gradle中添加

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

(2)在app的build.gradle中添加

```
dependencies {
……
         implementation 'com.github.maascharge:HaylionRxBus:1.1'
}
```

2.使用方法
发送事件：在需要发送事件的地方按下列方式发送。可以自定义Tag和参数类型，通过Tag接收者可以对事件进行过滤，当接收者的Tag和发送者的Tag相同时，才可以接收到事件。

```
//1.发送tag和参数默认消息
RxBus.getInstance().post();

//2.发送自定义的Tag和默认的参数
RxBus.getInstance().post(Tags.POST_TAG);

//3.发送默认tag和自定义的参数
RxBus.getInstance().post(true);


//4.发送自定的tag和自定义的参数
RxBus.getInstance().post("\n这是自定义参数", Tags.POST_EVENT);
```

接收事件：定义需要接收的参数类型和Tag，只有当接收者和发送者的参数类型和Tag匹配时才可以接收到事件。

1.在接收类，onCreate中注册订阅关系

```
RxBus.getInstance().registerRxBus(this, true);
```

2.在onDestroy中解绑订阅关系

```
RxBus.getInstance().unregister(this);
```

3.建议在接收者类创建一个内部Tag契约类：发送者也使用该Tag契约类。1.方便以后维护，防止解耦过于严重。2，保证发送者和接收者的Tag统一，不出错。

```
public interface Tags {
    String POST_TAG = "post_tag";
    String POST_EVENT = "post_event";
}
```

4.在需要接收事件的方法上添加注解@RxBusReact(接收参数类型,Tag)

```
@RxBusReact(Clazz = Object.class)
public void showDefult(Object o) {
    tvShowInfo.setText("接收到默认Tag和默认参数的消息");
}

@RxBusReact(Clazz = Object.class, tag = Tags.POST_TAG)
public void showDiyTag(Object o) {
    tvShowInfo.setText("接收到自定义Tag和默认参数的消息");
}

@RxBusReact(Clazz = Boolean.class)
public void showDiyEvent(boolean b) {
    if (b) {
        tvShowInfo.setText("接收到默认Tag和自定义参数的消息" + b);
    }
}

@RxBusReact(Clazz = String.class, tag = Tags.POST_EVENT)
public void showDiyTagAndEvent(String s) {
    tvShowInfo.setText("接收到自定义Tag和自定义参数的消息" + s);
}

```
