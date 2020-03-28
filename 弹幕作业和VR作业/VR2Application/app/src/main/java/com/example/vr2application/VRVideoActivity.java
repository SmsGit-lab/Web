package com.example.vr2application;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

public class VRVideoActivity extends AppCompatActivity {
    private VrVideoView videoView;
    private AsyncTask<Void,Void,Void>task;
    private SeekBar seekBar;
    private TextView seekbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrvideo);
        init();
    }
    private void init(){
        seekBar=findViewById(R.id.seekbar);
        seekbarText=findViewById(R.id.seekbar_text);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    long duration=videoView.getDuration();
                    long newPosition=(long)(i*0.01f*duration);
                    videoView.seekTo(newPosition);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        videoView=findViewById(R.id.vr_video);
        videoView.setTag(true);
        videoView.setEventListener(new VrVideoEventListener(){
            @Override
            public void onClick() {
                super.onClick();
                boolean isPlay=(boolean)videoView.getTag();
                if(isPlay){
                    isPlay=false;
                    videoView.pauseVideo();     //暂停播放

                }else{
                    isPlay=true;
                    videoView.playVideo();      //继续播放18】
                }
                videoView.setTag(isPlay);
            }
            //视图画面切换到下一帧时被调用

            @Override
            public void onNewFrame() {
                super.onNewFrame();
                seekBar.setMax(100);
                long duration=videoView.getDuration();      //获取视频时长，单位毫秒
                long cerrentPosition=videoView.getCurrentPosition();        //获取当前位置
                int percent=(int)(cerrentPosition*100f/duration+0.5f);
                seekBar.setProgress(percent);
                seekbarText.setText(percent+"% ");
            }

            @Override
            public void onLoadSuccess() {
                super.onLoadSuccess();
                Toast.makeText(VRVideoActivity.this,"开始播放",Toast.LENGTH_SHORT).show();
                seekBar.setMax(100);
                seekBar.setProgress(10);
                seekbarText.setText("0% ");
            }

            @Override
            public void onLoadError(String errorMessage) {
                super.onLoadError(errorMessage);
                Toast.makeText(VRVideoActivity.this,"播放出错",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompletion() {
                super.onCompletion();
                seekBar.setMax(100);
                seekBar.setProgress(100);
                seekbarText.setText("100% ");
            }
        });
        //创建异步任务
        task=new AsyncTask<Void, Void, Void>() {
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... params) {
                String fileName="congo.mp4";
                VrVideoView.Options option=new VrVideoView.Options();
                //非流媒体
                option.inputFormat=VrVideoView.Options.FORMAT_DEFAULT;
                //设置3D效果
                option.inputType=VrVideoView.Options.TYPE_STEREO_OVER_UNDER;
                try {
                    //从资源目录加载视频
                    videoView.loadVideoFromAsset(fileName,option);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(VRVideoActivity.this,"加载成功开始播放",Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }
    //细节一，切换屏幕变黑

    @Override
    protected void onPostResume() {
        super.onPostResume();
        videoView.resumeRendering();        //重新渲染
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pauseRendering();     //停止渲染
    }
    //细节二，AsyncTask处理

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.shutdown();       //停止
        if (task!=null){
            task.cancel(true);
            task=null;
        }
    }
}
