package com.haylion.rxbuspublic.rxbuspublic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.haylion.rxbuspublic.rxbuslib.RxBus;
import com.haylion.rxbuspublic.rxbuslib.RxBusReact;

/**
 * Author:wangjianming
 * Time:2018/11/5
 * Description:RxBus
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btPostDefultTagAndEvent;
    Button btPostDiyTag;
    Button btPostDiyEvent;
    Button btPostDiyTagAndEvent;
    TextView tvShowInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注册RxBus激活订阅关系
        RxBus.getInstance().register(this, true);
        initView();
    }

    private void initView() {
        btPostDefultTagAndEvent = (Button) findViewById(R.id.bt_post_defult_tag);
        btPostDiyTag = (Button) findViewById(R.id.bt_post_tag);
        btPostDiyEvent = (Button) findViewById(R.id.bt_post_event);
        btPostDiyTagAndEvent = (Button) findViewById(R.id.bt_post_tag_event);
        tvShowInfo = (TextView) findViewById(R.id.tv_show_get_info);
        btPostDefultTagAndEvent.setOnClickListener(this);
        btPostDiyTag.setOnClickListener(this);
        btPostDiyEvent.setOnClickListener(this);
        btPostDiyTagAndEvent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_post_defult_tag:
                //发送tag和参数默认消息
                RxBus.getInstance().post();
                break;
            case R.id.bt_post_tag:
                //发送自定义的Tag和默认的参数
                RxBus.getInstance().post(Tags.POST_TAG);
                break;
            case R.id.bt_post_event:
                //发送默认tag和自定义的参数
                RxBus.getInstance().post(true);
                break;
            case R.id.bt_post_tag_event:
                //发送自定的tag和自定义的参数
                RxBus.getInstance().post("\n这是自定义参数", Tags.POST_EVENT);
                break;
        }
    }

    public interface Tags {
        String POST_TAG = "post_tag";
        String POST_EVENT = "post_event";
    }

    @RxBusReact(clazz = Object.class)
    public void showDefult(Object o) {
        tvShowInfo.setText("接收到默认Tag和默认参数的消息");
    }

    @RxBusReact(clazz = Object.class, tag = Tags.POST_TAG)
    public void showDiyTag(Object o) {
        tvShowInfo.setText("接收到自定义Tag和默认参数的消息");
    }

    @RxBusReact(clazz = Boolean.class)
    public void showDiyEvent(boolean b) {
        if (b) {
            tvShowInfo.setText("接收到默认Tag和自定义参数的消息" + b);
        }
    }

    @RxBusReact(clazz = String.class, tag = Tags.POST_EVENT)
    public void showDiyTagAndEvent(String s) {
        tvShowInfo.setText("接收到自定义Tag和自定义参数的消息" + s);
    }

    @Override
    protected void onDestroy() {
        //解绑RxBus订阅关系
        RxBus.getInstance().unregister(this);
        super.onDestroy();
    }
}
