package cj.net;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.warkiz.widget.IndicatorSeekBar;

public class Analysis2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("분석 리포트") ;
        View root = findViewById(R.id.analysis);
        setUCSeekBar();
    }
    public void setUCSeekBar(){
        IndicatorSeekBar UC = findViewById(R.id.UC);
        UC.setDecimalScale(3);
        UC.setIndicatorTextFormat("${PROGRESS} %");
        UC.setProgress(0.005f);
        View UCView = UC.getIndicator().getContentView();
        UCView.invalidate();

        IndicatorSeekBar FHR = findViewById(R.id.FHR);
        FHR.setIndicatorTextFormat("${PROGRESS} bpm");
        View FHRView = FHR.getIndicator().getContentView();
        FHRView.invalidate();

        IndicatorSeekBar glucose = findViewById(R.id.glucose);
        glucose.setIndicatorTextFormat("${PROGRESS} md/dl");
        View glucoseView = glucose.getIndicator().getContentView();
        glucoseView.invalidate();

        IndicatorSeekBar pressure = findViewById(R.id.pressure);
        pressure.setIndicatorTextFormat("${PROGRESS} mmHg");
        View pressureView = pressure.getIndicator().getContentView();
        pressureView.invalidate();
    }
}