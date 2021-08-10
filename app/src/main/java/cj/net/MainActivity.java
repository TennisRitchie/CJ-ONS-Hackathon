package cj.net;
import android.graphics.Color;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.EventLogTags;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ArrayList<Monitoring> monitorings= new ArrayList<>();
    ArrayList<Daily> dailies = new ArrayList<>();
    boolean[] options = new boolean[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readCSV(monitorings,dailies);
        harmnessChart(monitorings);
        monitoringChart(monitorings,options);
        setSleepBarChart(); // 수면
        setWeightChart(); // 체중
        setMuscleChart(); // 근육량
        setFatChart(); // 체지방

    }

    public void readCSV(ArrayList<Monitoring> monitorings,ArrayList<Daily> dailies){
        String line;

        try { // monitoring.csv
            InputStreamReader is = new InputStreamReader(getResources().openRawResource(R.raw.monitoring));
            BufferedReader br = new BufferedReader(is);
            String[] columns = br.readLine().split(",");
            while ((line = br.readLine()) !=null){
                String[] data = line.split(",");
                monitorings.add(new Monitoring(Double.parseDouble(data[2]),Double.parseDouble(data[3]),Double.parseDouble(data[4]),Double.parseDouble(data[5]),Double.parseDouble(data[6]),Double.parseDouble(data[7])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try { // daily.csv
            InputStreamReader is = new InputStreamReader(getResources().openRawResource(R.raw.daily));
            BufferedReader br = new BufferedReader(is);
            String[] columns = br.readLine().split(",");
            while ((line = br.readLine()) !=null){
                String[] data = line.split(",");
                dailies.add(new Daily(Double.parseDouble(data[1]),Double.parseDouble(data[2]),Double.parseDouble(data[3]),Double.parseDouble(data[4])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        }

    /* 수면시 */
    public void setSleepBarChart() {
        BarChart barChart = findViewById(R.id.sleepchart);

        barChart.setDrawGridBackground(false); // 배
        barChart.setTouchEnabled(false); // 확대못하게
        barChart.getLegend().setEnabled(false); // Remove label
        barChart.setDescription(""); // Remove desc

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMaxValue(24);
        leftAxis.setAxisMinValue(0);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setAxisMaxValue(24);
        rightAxis.setAxisMinValue(0);

        ArrayList sleep = new ArrayList(); // 수면시간

        float total = 0;
        for (int i = 0; i < this.dailies.size(); i++) {
            float sleepData = (float) dailies.get(i).getSleep();
            sleep.add(new BarEntry(sleepData, i));
            total += sleepData;
            //System.out.println((int)dailies.get(i).getSleep());
        }

        total /= this.dailies.size();
        String avg = String.format("%.1f", total);
        TextView sleepAvg = findViewById(R.id.sleepAvg);
        sleepAvg.setText(avg + "hr");
        ArrayList year = new ArrayList();

        for (int i = 1; i <= this.dailies.size(); i++) {
            year.add(i+"일");
        }

        BarDataSet bardataset = new BarDataSet(sleep, "일일 수면시간");
        barChart.animateY(3000);

        BarData data = new BarData(year, bardataset); // MPAndroidChart v3.X 오류 발생
        //bardataset.setColor(ColorTemplate.getHoloBlue());
        barChart.setData(data);


    }

    /* 체중 */
    public void setWeightChart() {
        LineChart chart = findViewById(R.id.weightchart);

        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(false); // 확대못하게
        chart.getLegend().setEnabled(false); // Remove label
        chart.setDescription(""); // Remove desc

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaxValue(100f);

        ArrayList weight = new ArrayList(); // 수면시간

        for (int i = 0; i < this.dailies.size(); i++) {
            weight.add(new Entry((float)(dailies.get(i).getWeight()), i));
        }

        ArrayList year = new ArrayList();

        for (int i = 1; i <= this.dailies.size(); i++) {
            year.add(i+"일");
        }

        LineDataSet bardataset = new LineDataSet(weight, " ");
        chart.animateY(3000);

        LineData data = new LineData(year, bardataset); // MPAndroidChart v3.X 오류 발생
        data.setValueFormatter(new MyValueFormatter()); // 소수점 둘째자리

        bardataset.setColor(ColorTemplate.getHoloBlue());
        chart.setData(data);
    }

    /* 근육량 */
    public void setMuscleChart() {
        LineChart chart = findViewById(R.id.musclechart);

        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(false); // 확대못하게
        chart.getLegend().setEnabled(false); // Remove label
        chart.setDescription(""); // Remove desc

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setDrawGridLines(false);

        ArrayList muscle = new ArrayList(); // 수면시간

        for (int i = 0; i < this.dailies.size(); i++) {
            muscle.add(new Entry((float)(dailies.get(i).getMuscle()), i));
        }

        ArrayList year = new ArrayList();

        for (int i = 1; i <= this.dailies.size(); i++) {
            year.add(i+"일");
        }

        LineDataSet bardataset = new LineDataSet(muscle, " ");
        chart.animateY(3000);

        LineData data = new LineData(year, bardataset); // MPAndroidChart v3.X 오류 발생
        data.setValueFormatter(new MyValueFormatter()); // 소수점 둘째자리

        bardataset.setColor(ColorTemplate.getHoloBlue());
        chart.setData(data);
    }

    /* 체지방 */
    public void setFatChart() {
        LineChart chart = findViewById(R.id.fatchart);

        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(false); // 확대못하게
        chart.getLegend().setEnabled(false); // Remove label
        chart.setDescription(""); // Remove desc

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setDrawGridLines(false);

        ArrayList fat = new ArrayList(); // 수면시간

        for (int i = 0; i < this.dailies.size(); i++) {
            fat.add(new Entry((float)dailies.get(i).getFat(), i));
        }

        ArrayList year = new ArrayList();

        for (int i = 1; i <= this.dailies.size(); i++) {
            year.add(i+"일");
        }

        LineDataSet bardataset = new LineDataSet(fat, " ");
        chart.animateY(3000);

        LineData data = new LineData(year, bardataset); // MPAndroidChart v3.X 오류 발생

        data.setValueFormatter(new MyValueFormatter()); // 소수점 둘째자리

        bardataset.setColor(ColorTemplate.getHoloBlue());
        chart.setData(data);
    }

        // harmness Pie Chart
        public void harmnessChart(ArrayList<Monitoring> monitorings){
           PieChart pieChart = findViewById(R.id.harmnesschart);
           ArrayList HarmnessData = new ArrayList();

           float variable = (float)monitorings.get(monitorings.size()-1).getHarmness();

            HarmnessData.add(new Entry(variable,0));
            HarmnessData.add(new Entry(100f - variable,1));

           PieDataSet dataSet = new PieDataSet(HarmnessData, "Number Of Employees");
           ArrayList harmness = new ArrayList();
           harmness.add("위험 수준");
           harmness.add("");

           PieData data = new PieData(harmness, dataSet); // MPAndroidChart v3.X 오류 발생
           pieChart.setData(data);
           Legend l = pieChart.getLegend();
           l.setEnabled(false);
            // description hide
            pieChart.setDescription(null);
           dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
           pieChart.animateXY(5000, 5000);
           pieChart.setHoleRadius(75);
           // set center Text
           pieChart.setCenterText("위험도\n" + String.valueOf(Math.round(variable)) + "%\n");
           pieChart.setCenterTextSize(35);
           //pieChart.setCenterTextRadiusPercent();
    }

    // monitoring Line Charts
    public void monitoringChart(ArrayList<Monitoring> monitorings,boolean[] options) {
            LineChart lineChart = findViewById(R.id.monitoringchart);
            String[] columns = {"심박수", "자궁수축율", "태동", "혈당", "혈압"};
            int[] colors = {Color.BLUE, Color.RED, Color.MAGENTA, Color.YELLOW, Color.GREEN};
            int index = 0;
            ArrayList<String> xAXES = new ArrayList<>();

            ArrayList<Entry>[] entry = new ArrayList[5];
            for (int i = 0; i < 5; i++) entry[i] = new ArrayList<>();
            for (Monitoring monitoring : monitorings) {
                float FHRs = (float) monitoring.getFHR();
                entry[0].add(new Entry(FHRs, index));

                float UCs = (float) monitoring.getUC();
                entry[1].add(new Entry(UCs, index));

                float FMs = (float) monitoring.getFM();
                entry[2].add(new Entry(FMs, index));

                float glucoses = (float) monitoring.getGlucose();
                entry[3].add(new Entry(glucoses, index));

                float pressures = (float) monitoring.getPressure();
                entry[4].add(new Entry(pressures, index));

                index++;
            }
            ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
            if(options[0]) {
                LineDataSet lineDataSet1 = new LineDataSet(entry[0], columns[0]);
                lineDataSet1.setDrawCircles(false);
                lineDataSet1.setColor(colors[0]);
                lineDataSets.add(lineDataSet1);
            }
        if(options[1]) {
            LineDataSet lineDataSet2 = new LineDataSet(entry[1], columns[1]);
            lineDataSet2.setDrawCircles(false);
            lineDataSet2.setColor(colors[1]);
            lineDataSets.add(lineDataSet2);
        }
        if(options[2]) {
            LineDataSet lineDataSet3 = new LineDataSet(entry[2], columns[2]);
            lineDataSet3.setDrawCircles(false);
            lineDataSet3.setColor(colors[2]);
            lineDataSets.add(lineDataSet3);
        }
        if(options[3]) {
            LineDataSet lineDataSet4 = new LineDataSet(entry[3], columns[3]);
            lineDataSet4.setDrawCircles(false);
            lineDataSet4.setColor(colors[3]);
            lineDataSets.add(lineDataSet4);
        }
        if(options[4]) {
            LineDataSet lineDataSet5 = new LineDataSet(entry[4], columns[4]);
            lineDataSet5.setDrawCircles(false);
            lineDataSet5.setColor(colors[4]);
            lineDataSets.add(lineDataSet5);
        }
            String[] x = new String[3194];
            for (int i = 0; i < 3194; i++) {
                int min = i / 60;
                int sec = i % 60;
                x[i] = "12시 " + String.valueOf(min) + "분 " + String.valueOf(sec) + "초";
            }
            lineChart.setData(new LineData(x, lineDataSets));
            lineChart.setDescription(null);

    }
    public void onCheckBoxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.FHR_btn:
                options[0] = checked ? true : false;
                    break;
            case R.id.UC_btn:
                options[1] = checked ? true : false;
                    break;
            case R.id.FM_btn:
                options[2] = checked ? true : false;
                    break;
            case R.id.glucose_btn:
                options[3] = checked ? true : false;
                    break;
            case R.id.pressure_btn:
                options[4] = checked ? true : false;
                    break;
        }
        monitoringChart(monitorings,options);
    }
    }
