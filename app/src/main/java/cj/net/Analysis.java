package cj.net;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

public class Analysis extends AppCompatActivity {
    float harmness = 0f;
    int color4 = Color.rgb(210,90,90); // PICK
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        Intent intent = getIntent();
        harmness = intent.getFloatExtra("harmness",0f);
        harmnessChart();
    }

    // harmness Pie Chart
    public void harmnessChart(){
        PieChart pieChart = findViewById(R.id.harmnesschart);
        ArrayList HarmnessData = new ArrayList();

        HarmnessData.add(new Entry(harmness,0));
        HarmnessData.add(new Entry(100f - harmness,1));

        PieDataSet dataSet = new PieDataSet(HarmnessData, "Number Of Employees");
        ArrayList harmnessLabel = new ArrayList();
        harmnessLabel.add("");
        harmnessLabel.add("");

        PieData data = new PieData(harmnessLabel, dataSet); // MPAndroidChart v3.X 오류 발생
        pieChart.setData(data);
        Legend l = pieChart.getLegend();
        l.setEnabled(false);
        // description hide
        pieChart.setDescription(null);
        int[] colors = {color4, Color.rgb(250,200,200)};
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        dataSet.setValueTextColor(Color.GRAY);

        pieChart.animateXY(5000, 5000);
        pieChart.setHoleRadius(65);
        // set center Text

        Typeface tf = Typeface.createFromAsset(getAssets(), "esamanrulight.ttf");

        pieChart.setCenterText(String.valueOf(Math.round(harmness)) + "%");
        pieChart.setCenterTextSize(35);
        pieChart.setCenterTextTypeface(tf);
        pieChart.setCenterTextColor(Color.rgb(210,90,90));
        //pieChart.setCenterTextRadiusPercent();
    }
    public void goBackActivity(){
        super.onBackPressed();  //토스트 메시지
        Intent intent = new Intent(this, MainActivity.class); //지금 액티비티에서 다른 액티비티로 이동하는 인텐트 설정
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    //인텐트 플래그 설정
        startActivity(intent);  //인텐트 이동
        finish();   //현재 액티비티 종료
    }
}