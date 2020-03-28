package com.example.barrageapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.Random;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.Danmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuTextureView;
import master.flame.danmaku.ui.widget.DanmakuView;

public class MainActivity extends AppCompatActivity {
    private boolean showDanmaku;
    private DanmakuView danmakuView;
    private DanmakuContext danmakuContext;
    private Button sendButton;
    private LinearLayout sendLayout;
    private EditText editText;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        //playVideo();
        initNetVideo();
        initDanmaku();
    }
    /**
    * 初始化控件
    * */
    private void initView(){
        videoView=findViewById(R.id.videoview);
        sendButton=findViewById(R.id.btn_send);
        sendLayout=findViewById(R.id.ly_send);
        editText=findViewById(R.id.ed_text);
        danmakuView=findViewById(R.id.danmaku);
    }
    /**
     * 播放视频**/
    private void playVideo(){
        String uri="android.resource://"+getPackageName()+"/"+R.raw.sun;
        String url = "https://flv2.bn.netease.com/videolib1/1811/26/OqJAZ893T/HD/OqJAZ893T-mobile.mp4";
        if(uri!=null){
            videoView.setVideoURI(Uri.parse(uri));
            videoView.start();
        }else {
            videoView.getBackground().setAlpha(0);//将背景设置为透明
        }
    }
    //播放网络视频
    private void initNetVideo() {
        //设置有进度条可以拖动快进
        videoView.setMediaController(new MediaController(this));
        String url = "https://flv2.bn.netease.com/videolib1/1811/26/OqJAZ893T/HD/OqJAZ893T-mobile.mp4";
        videoView.setVideoPath(url);
        videoView.start();
        videoView.requestFocus();
    }

    /**
     * 创建弹幕解析器*/
    private BaseDanmakuParser parser=new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };
    /**
     * 初始化弹幕*/
    private void initDanmaku(){
        danmakuView.setCallback(new DrawHandler.Callback() {   //-----设置回掉函数
            @Override
            public void prepared() {
                showDanmaku=true;
                danmakuView.start();//开始弹幕-----------------
                generateDanmakus();//------------生成弹幕的方法
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });
        danmakuContext=DanmakuContext.create();
        danmakuView.enableDanmakuDrawingCache(true);//-------------提升屏幕绘制效率
        danmakuView.prepare(parser,danmakuContext);//---------------进行弹幕准备
        //为danmaku设置监听事件
        danmakuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sendLayout.getVisibility()==View.GONE){
                    sendLayout.setVisibility(View.VISIBLE);//--------------显示布局
                }else{
                    sendLayout.setVisibility(View.GONE);//------------隐藏布局
                }
            }
        });
        //为Button 设置监听事件
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content=editText.getText().toString();
                addDanmaku(content,true);//--------添加一条弹幕
                editText.setText("");
            }
        });
    }
    /**
     * 添加一条弹幕
     * @param content 弹幕的具体内容
     * @param border 弹幕是否有边框
     * */
    private void addDanmaku(String content,boolean border){
        //创建弹幕对象，TYPE_SCROLL_RL表示从右向左滚动的弹幕
        BaseDanmaku danmaku=danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text=content;
        danmaku.padding=6;
        danmaku.textSize=30;
        danmaku.textColor= Color.YELLOW;
        danmaku.setTime(danmakuView.getCurrentTime());//------弹幕出现的时间+getCurrentTime当前时间。
        if(border){
            danmaku.borderColor=Color.BLUE;//--------弹幕文字边框的颜色
        }
        danmakuView.addDanmaku(danmaku);
    }
    /**
     * 随机生成弹幕
     * */
    private void generateDanmakus(){
        new Thread(new Runnable() {            //开启新线程，防止程序卡顿
            @Override
            public void run() {
                while (showDanmaku){
                    int num=new Random().nextInt(300);
                    String content=""+num;
                    addDanmaku(content,false);
                    try {
                        Thread.sleep(num);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(danmakuView!=null&&danmakuView.isPrepared()){           //----isPrepared,是否准备好
            danmakuView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(danmakuView!=null&&danmakuView.isPrepared()&&danmakuView.isPrepared()){
            danmakuView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showDanmaku=false;
        if(danmakuView!=null){
            danmakuView.release();
            danmakuView=null;
        }
    }
}
