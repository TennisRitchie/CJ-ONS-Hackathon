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
        setContentView(R.layout.activity_analysis2);
        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("분석 리포트") ;
        setUCSeekBar();
    }
    public void setUCSeekBar(){
        IndicatorSeekBar UC = findViewById(R.id.lUC);
        UC.setDecimalScale(3);
        UC.setIndicatorTextFormat("${PROGRESS} %");
        UC.setProgress(0.005f);
        View UCView = UC.getIndicator().getContentView();
        UCView.invalidate();

        IndicatorSeekBar FHR = findViewById(R.id.lFHR);
        FHR.setIndicatorTextFormat("${PROGRESS} bpm");
        View FHRView = FHR.getIndicator().getContentView();
        FHRView.invalidate();

        IndicatorSeekBar glucose = findViewById(R.id.lglucose);
        glucose.setIndicatorTextFormat("${PROGRESS} md/dl");
        View glucoseView = glucose.getIndicator().getContentView();
        glucoseView.invalidate();

        IndicatorSeekBar pressure = findViewById(R.id.lpressure);
        pressure.setIndicatorTextFormat("${PROGRESS} mmHg");
        View pressureView = pressure.getIndicator().getContentView();
        pressureView.invalidate();
    }
}