package com.example.hellocarts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LayoutInflater mInflater;
    private View view1;
    private View view2;
    private View view3;
    private List<String>mTitleList=new ArrayList<>();       //页卡标题集合
    private List<View>mViewList=new ArrayList<>();      //页卡视图合集
    //线性图
    private int[] temperture={25,27,26,25,26,27,24};        //图表的数据点
    //x轴的标注
    private String[] lineData={"周一","周二","周三","周四","周五","周六","周日"};
    private List<PointValue>pointValues=new ArrayList<PointValue>();
    private List<AxisValue>axisValues=new ArrayList<AxisValue>();
    private LineChartView lineChartView;

    //饼状图
    private PieChartView pieChartView;
    private PieChartData pieChartData;
    private List<SliceValue>sliceValues=new ArrayList<SliceValue>();
    private int[] pieData={8,24,35,23,10};  //数据
    private int[] color={Color.parseColor("#356fb3"),Color.parseColor("#b53633"),Color.parseColor("#86aa3d"),Color.parseColor("#6a4b90"),Color.parseColor("#2e9cba")};
    private String[] stateChar={"高等教育","职业教育","语言培育","K12教育","其他"};

    //柱状图
    private String[] year=new String[]{"2013","2014","2015","2016","2017"};
    private ColumnChartView columnChartView;
    private ColumnChartData columnData;
    private int[] columnY={500,1000,1500,2000,2500,3000};
    /**
     * 初始化柱状图
     * */
    private void initColumnChart(){
        List<AxisValue>axisValues=new ArrayList<AxisValue>();       //储存X轴标注
        List<AxisValue>axisYValues=new ArrayList<AxisValue>();       //储存Y轴标注
        List<Column> columns=new ArrayList<Column>();
        List<SubcolumnValue>subcolumnValues;        //储存
        for (int k=0;k<columnY.length;k++){
            axisValues.add(new AxisValue(k).setValue(columnY[k]));
        }
        for (int i=0;i<year.length;i++){
            subcolumnValues=new ArrayList<SubcolumnValue>();
            for (int j=0;j<1;++j){
                switch (i+1){
                    case 1:
                        subcolumnValues.add(new SubcolumnValue(924, ChartUtils.COLOR_BLUE));
                        break;
                    case 2:
                        subcolumnValues.add(new SubcolumnValue(1220, ChartUtils.COLOR_GREEN));
                        break;
                    case 3:
                        subcolumnValues.add(new SubcolumnValue(1610, ChartUtils.COLOR_RED));
                        break;
                    case 4:
                        subcolumnValues.add(new SubcolumnValue(2125, ChartUtils.COLOR_ORANGE));
                        break;
                    case 5:
                        subcolumnValues.add(new SubcolumnValue(2805, ChartUtils.COLOR_VIOLET));
                        break;
                }
            }
            //点击图显示数据
            axisValues.add(new AxisValue(i).setLabel(year[i]));
            columns.add(new Column(subcolumnValues).setHasLabelsOnlyForSelected(true));
        }
        //X轴
        Axis axisX=new Axis(axisValues);
        axisX.setHasLines(false);
        axisX.setTextColor(Color.BLACK);
        //Y轴
        Axis axisY=new Axis(axisValues);
        axisY.setHasLines(true);
        axisY.setTextColor(Color.BLACK);
        axisY.setMaxLabelChars(5);

        columnData=new ColumnChartData(columns);
        columnData.setAxisXBottom(axisX);
        columnData.setAxisYLeft(axisY);
        columnChartView.setColumnChartData(columnData);
        columnChartView.setValueSelectionEnabled(true);
        columnChartView.setZoomType(ZoomType.HORIZONTAL);
    }
    /**
     * 设置饼状图中的数据
     * */
    private void setPieChartData(){
        for (int i=0;i<pieData.length;i++){
            SliceValue sliceValue=new SliceValue((float)pieData[i],color[i]);
            sliceValues.add(sliceValue);        //添加到集合中
        }
    }
    /**
     * 初始化饼状图
     * */
    private void initPieChart(){
        pieChartData=new PieChartData();
        pieChartData.setHasLabels(true);        //显示标签
        pieChartData.setHasLabelsOnlyForSelected(false);        //不用点击显示占的比例
        pieChartData.setHasLabelsOutside(false);        //数据显示在图的外层
        pieChartData.setValues(sliceValues);        //填充数据
        pieChartData.setCenterCircleColor(Color.WHITE);     //设置环形中间的颜色
        pieChartData.setCenterCircleScale(0.5f);    //设置中心圆占饼状图的比例
        pieChartData.setHasCenterCircle(true);      //是否显示中心圆
        pieChartData.setCenterText1("数据");      //中心圆默认显示的文字
        pieChartData.setCenterText1FontSize(10);
        pieChartView.setPieChartData(pieChartData);     //为饼图设置数据
        pieChartView.setValueSelectionEnabled(true);        //选择饼图的块会变大
        pieChartView.setAlpha(0.9f);        //设置透明度
        pieChartView.setCircleFillRatio(1f);        //设置饼图大小，占整个View的比例

        //柱状图
        columnChartView=(ColumnChartView)view3.findViewById(R.id.cv_chart);
        initColumnChart();
    }
    /**
     * 数据所占百分比
     * */
    private String calPercent(int i){
        String result="";
        int sum=0;
        for (int j=0;j<pieData.length;j++){
            sum+=pieData[j];
        }
        result=String.format("% .2f",(float)pieData[i]*100/sum)+"% ";
        return result;
    }
    /**
     * 饼状图的事件监听器
     * */
    PieChartOnValueSelectListener selectListener=new PieChartOnValueSelectListener() {
        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            //选择对应图形后，在中间部分显示对应信息
            pieChartData.setCenterText1(stateChar[arcIndex]);       //中心圆的第一文本
            pieChartData.setCenterText2(value.getValue()+"("+calPercent(arcIndex)+")");
        }

        @Override
        public void onValueDeselected() {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void init(){
        mViewPager=findViewById(R.id.vp_view);
        mTabLayout=findViewById(R.id.tabs);
        mInflater=LayoutInflater.from(this);
        view1=mInflater.inflate(R.layout.layout_line_chart,null);
        view2=mInflater.inflate(R.layout.layout_pie_chart,null);
        view3=mInflater.inflate(R.layout.layout_column_chart,null);
        //添加页卡视图
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
        //添加页卡标题
        mTitleList.add("现状图");
        mTitleList.add("饼状图");
        mTitleList.add("柱状图");
        //设置Tab模式，当前为系统默认模式
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        //添加选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));
        ViewPagerAdapter mAdapter=new ViewPagerAdapter(mViewList,mTitleList);
        mViewPager.setAdapter(mAdapter);        //给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);      //给TabLayout设置关联
        mTabLayout.setTabsFromPagerAdapter(mAdapter);       //给Tabs设置适配器
        //线形图
        lineChartView=(LineChartView) view1.findViewById(R.id.lv_chart);
        setAxisXLables();
        setAxisPoints();
        initLineChart();
    }
    /**
     * 设置X轴的标注
     * **/
    private void setAxisXLables(){
        for (int i=0;i<lineData.length;i++){
            axisValues.add(new AxisValue(i).setLabel(lineData[i]));
        }
    }
    /**
     * 设置线形图中的每个数据点
     * */
    private void setAxisPoints(){
        for(int i=0;i<temperture.length;i++){
            pointValues.add(new PointValue(i,temperture[i]));
        }
    }
    /**
     * 初始化线形图
     * */
    private void initLineChart(){
        //设置线的颜色形状等
        Line line=new Line();
        line.setColor(Color.parseColor("#33b5e5"));
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(false);
        line.setHasLabels(true);
        line.setHasLines(true);
        line.setHasPoints(true);
        line.setValues(pointValues);
        List<Line>lines=new ArrayList<Line>();
        lines.add(line);
        LineChartData data=new LineChartData();
        data.setLines(lines);
        //x轴
        Axis axisX=new Axis();
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.BLACK);
        axisX.setMaxLabelChars(5);
        axisX.setValues(axisValues);
        data.setAxisXBottom(axisX);
        axisX.setHasLines(true);
        //Y轴
        Axis axisY=new Axis();
        data.setAxisYLeft(axisY);
        axisY.setTextColor(Color.BLACK);
        axisY.setMaxLabelChars(5);
        //
        lineChartView.setInteractive(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.setMaxZoom((float) 2);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.setLineChartData(data);
        lineChartView.setVisibility(View.VISIBLE);

        //饼状图
        pieChartView=(PieChartView)view2.findViewById(R.id.pv_chart);
        pieChartView.setOnValueTouchListener(selectListener);       //设置事件监听事件
        setPieChartData();
        initPieChart();

    }
}
