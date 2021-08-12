package cj.net;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ArrayList<Monitoring> monitorings= new ArrayList<>();
    ArrayList<Daily> dailies = new ArrayList<>();
    boolean[] options = new boolean[5];

    int color1 = Color.rgb(183,164,238); // PURPLE
    int color2 = Color.rgb(120,120,170); // PURPLE
    int color3 = Color.rgb(123,104,238); // PURPLE
    int color4 = Color.rgb(210,90,90); // PICK


    int[] LIBERTY_COLORS = {
            Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187),
            Color.rgb(75, 154, 160), Color.rgb(42, 109, 130),  Color.rgb(22, 109, 160)
    };

    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tf = Typeface.createFromAsset(getAssets(), "esamanrulight.ttf");

        if(monitorings.isEmpty())
            readCSV(monitorings,dailies);

        harmnessChart(monitorings);
        monitoringChart(monitorings,options);
        setSleepBarChart(dailies); // 수면
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
            br.close();
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
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        }

    /* 수면시간 */
    public void setSleepBarChart(ArrayList<Daily> dailies) {
        BarChart barChart = findViewById(R.id.sleepchart);

        barChart.setDrawGridBackground(false); // 배
        //barChart.setTouchEnabled(false); // 확대못하게
        barChart.getLegend().setEnabled(false); // Remove label
        barChart.setDescription(""); // Remove desc

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tf);

        YAxis leftAxis = barChart.getAxisLeft();
        //leftAxis.setAxisMaxValue(24);
        //leftAxis.setAxisMinValue(0);

        YAxis rightAxis = barChart.getAxisRight();
        //rightAxis.setAxisMaxValue(24);
        //rightAxis.setAxisMinValue(0);

        ArrayList sleep = new ArrayList(); // 수면시간

        float total = 0;

        for (int i = 0; i < dailies.size(); i++) {
            double temp = Math.ceil(dailies.get(i).getSleep()*10)/10.0;
            float sleepData = (float) temp;

            sleep.add(new BarEntry(sleepData, i));
            total += sleepData;
        }

        // 평균 수면시간
        total /= dailies.size();

        total *= 60;
        int hour = (int)total / 60;
        int min = (int)total % 60;

        String avg = String.format("%.1f", total);
        TextView sleepAvg = findViewById(R.id.sleepAvg);

        //sleepAvg.setText(avg + "hr");
        sleepAvg.setText(hour + "시간 " + min + "분");

        ArrayList year = new ArrayList();

        for (int i = 1; i <= dailies.size(); i++) {
            year.add(i+"일");
        }

        BarDataSet bardataset = new BarDataSet(sleep, "일일 수면시간");
        barChart.animateY(3000);


        BarData data = new BarData(year, bardataset); // MPAndroidChart v3.X 오류 발생정
        data.setValueFormatter(new MyValueFormatter()); // 소수점 첫째자리
        data.setValueTypeface(tf);

        sleepAvg.setTextColor(color2);

        data.setValueTextColor(color1);
        bardataset.setColor(color1);
        xAxis.setTextColor(color1); // x축
        xAxis.setGridColor(color1); // x축
        leftAxis.setTextColor(color1); // y축
        leftAxis.setGridColor(color1);
        rightAxis.setTextColor(color1); // y축
        rightAxis.setGridColor(color1); // y축

        leftAxis.setTypeface(tf);
        rightAxis.setTypeface(tf);

        xAxis.setDrawGridLines(false);

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
        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaxValue(100f);

        ArrayList weight = new ArrayList(); // 수면시간

        float total = 0;
        for (int i = 0; i < dailies.size(); i++) {
            float weightData = (float) dailies.get(i).getWeight();
            weight.add(new BarEntry(weightData, i));
            total += weightData;
        }

        // 평균 체중
        total /= dailies.size();
        String avg = String.format("%.1f", total);
        TextView weightAvg = findViewById(R.id.weightAvg);
        weightAvg.setText(avg + "kg");

        ArrayList year = new ArrayList();

        for (int i = 1; i <= this.dailies.size(); i++) {
            year.add(i+"일");
        }

        LineDataSet bardataset = new LineDataSet(weight, " ");
        chart.animateY(3000);

        LineData data = new LineData(year, bardataset); // MPAndroidChart v3.X 오류 발생
        data.setValueFormatter(new MyValueFormatter()); // 소수점 둘째자리
        data.setValueTypeface(tf);

        weightAvg.setTextColor(color2);

        data.setValueTextColor(color1);
        bardataset.setCircleColor(color1);
        bardataset.setColor(color1);
        xAxis.setTextColor(color1); // x축
        xAxis.setGridColor(color1); // x축
        xAxis.setTypeface(tf);

        leftAxis.setTextColor(color1); // y축
        leftAxis.setGridColor(color1);
        rightAxis.setTextColor(color1); // y축
        rightAxis.setGridColor(color1); // y축


        leftAxis.setTypeface(tf);
        rightAxis.setTypeface(tf);

        xAxis.setDrawGridLines(false);


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
        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tf);
        //xAxis.setDrawGridLines(false);

        ArrayList muscle = new ArrayList(); // 수면시간

        float total = 0;
        for (int i = 0; i < dailies.size(); i++) {
            float muscleData = (float) dailies.get(i).getMuscle();
            muscle.add(new BarEntry(muscleData, i));
            total += muscleData;
        }

        // 평균 근육
        total /= dailies.size();
        String avg = String.format("%.1f", total);
        TextView muscleAvg = findViewById(R.id.muscleAvg);
        muscleAvg.setText(avg + "%");
        muscleAvg.setTextColor(color2);

        ArrayList year = new ArrayList();

        for (int i = 1; i <= this.dailies.size(); i++) {
            year.add(i+"일");
        }

        LineDataSet bardataset = new LineDataSet(muscle, " ");
        chart.animateY(3000);

        LineData data = new LineData(year, bardataset); // MPAndroidChart v3.X 오류 발생
        data.setValueFormatter(new MyValueFormatter()); // 소수점 둘째자리
        data.setValueTypeface(tf);

        muscleAvg.setTextColor(color2);

        data.setValueTextColor(color1);
        bardataset.setCircleColor(color1);
        bardataset.setColor(color1);
        xAxis.setTextColor(color1); // x축
        xAxis.setGridColor(color1); // x축


        leftAxis.setTextColor(color1); // y축
        leftAxis.setGridColor(color1);
        rightAxis.setTextColor(color1); // y축
        rightAxis.setGridColor(color1); // y축

        leftAxis.setTypeface(tf);
        rightAxis.setTypeface(tf);

        xAxis.setDrawGridLines(false);


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
        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //xAxis.setDrawGridLines(false);

        ArrayList fat = new ArrayList(); // 수면시간


        float total = 0;
        for (int i = 0; i < dailies.size(); i++) {
            float fatData = (float) dailies.get(i).getFat();
            fat.add(new BarEntry(fatData, i));
            total += fatData;
            //System.out.println((int)dailies.get(i).getSleep());
        }

        // 평균 체지방률
        total /= dailies.size();
        String avg = String.format("%.1f", total);
        TextView fatAvg = findViewById(R.id.fatAvg);
        fatAvg.setText(avg + "%");

        ArrayList year = new ArrayList();

        for (int i = 1; i <= this.dailies.size(); i++) {
            year.add(i+"일");
        }

        LineDataSet bardataset = new LineDataSet(fat, " ");
        chart.animateY(3000);

        LineData data = new LineData(year, bardataset); // MPAndroidChart v3.X 오류 발생

        data.setValueFormatter(new MyValueFormatter()); // 소수점 둘째자리
        data.setValueTypeface(tf);

        fatAvg.setTextColor(color2);

        data.setValueTextColor(color1);
        bardataset.setCircleColor(color1);
        bardataset.setColor(color1);
        xAxis.setTextColor(color1); // x축
        xAxis.setGridColor(color1); // x축

        leftAxis.setTextColor(color1); // y축
        leftAxis.setGridColor(color1);
        rightAxis.setTextColor(color1); // y축
        rightAxis.setGridColor(color1); // y축

        leftAxis.setTypeface(tf);
        rightAxis.setTypeface(tf);
        xAxis.setTypeface(tf);

        xAxis.setDrawGridLines(false);


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
            harmness.add("");
            harmness.add("");

            PieData data = new PieData(harmness, dataSet); // MPAndroidChart v3.X 오류 발생
            pieChart.setData(data);
            Legend l = pieChart.getLegend();
            l.setEnabled(false);
            // description hide
            pieChart.setDescription(null);
            int[] redColors = {color4, Color.rgb(250,200,200)};
            int[] orangeColors = {Color.rgb(255,180,0), Color.rgb(250,250,222)};
            int[] greenColors = {Color.rgb(170,235,170), Color.rgb(240,255,240)};

            if (0 <= variable && variable <= 30) {
                dataSet.setColors(greenColors);
                pieChart.setCenterTextColor(greenColors[0]);
            } else if (30 <= variable && variable <= 60) {
                dataSet.setColors(orangeColors);
                pieChart.setCenterTextColor(orangeColors[0]);
            } else if (60 <= variable && variable <= 100) {
                dataSet.setColors(redColors);
                pieChart.setCenterTextColor(redColors[0]);
            }

            dataSet.setDrawValues(false);
            dataSet.setValueTextColor(Color.GRAY);

            pieChart.animateXY(5000, 5000);
            pieChart.setHoleRadius(65);
            // set center Text

            Typeface tf = Typeface.createFromAsset(getAssets(), "esamanrulight.ttf");

            pieChart.setCenterText(String.valueOf(Math.round(variable)) + "%");
            pieChart.setCenterTextSize(35);
            pieChart.setCenterTextTypeface(tf);
            //pieChart.setCenterTextRadiusPercent();
    }

    // monitoring Line Charts
    public void monitoringChart(ArrayList<Monitoring> monitorings,boolean[] options) {
            LineChart lineChart = findViewById(R.id.monitoringchart);
            String[] columns = {"심박수", "자궁수축율", "태동", "혈당", "혈압"};

            CheckBox fhr = findViewById(R.id.FHR_btn);
            fhr.setTextColor(LIBERTY_COLORS[1]);
            CheckBox uc = findViewById(R.id.UC_btn);
            uc.setTextColor(LIBERTY_COLORS[2]);
            CheckBox fm = findViewById(R.id.FM_btn);
            fm.setTextColor(LIBERTY_COLORS[3]);
            CheckBox glu = findViewById(R.id.glucose_btn);
            glu.setTextColor(LIBERTY_COLORS[4]);
            CheckBox press = findViewById(R.id.pressure_btn);
            press.setTextColor(LIBERTY_COLORS[5]);

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
                lineDataSet1.setColor(LIBERTY_COLORS[1]);
                lineDataSets.add(lineDataSet1);
            }
        if(options[1]) {
            LineDataSet lineDataSet2 = new LineDataSet(entry[1], columns[1]);
            lineDataSet2.setDrawCircles(false);
            lineDataSet2.setColor(LIBERTY_COLORS[2]);
            lineDataSets.add(lineDataSet2);
        }
        if(options[2]) {
            LineDataSet lineDataSet3 = new LineDataSet(entry[2], columns[2]);
            lineDataSet3.setDrawCircles(false);
            lineDataSet3.setColor(LIBERTY_COLORS[3]);
            lineDataSets.add(lineDataSet3);
        }
        if(options[3]) {
            LineDataSet lineDataSet4 = new LineDataSet(entry[3], columns[3]);
            lineDataSet4.setDrawCircles(false);
            lineDataSet4.setColor(LIBERTY_COLORS[4]);
            lineDataSets.add(lineDataSet4);
        }
        if(options[4]) {
            LineDataSet lineDataSet5 = new LineDataSet(entry[4], columns[4]);
            lineDataSet5.setDrawCircles(false);
            lineDataSet5.setColor(LIBERTY_COLORS[5]);
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

        XAxis xAxis = lineChart.getXAxis();

        YAxis leftAxis = lineChart.getAxisLeft();
        YAxis rightAxis = lineChart.getAxisRight();

        //data.setValueTextColor(color1);
        xAxis.setTextColor(LIBERTY_COLORS[5]); // x축
        xAxis.setGridColor(LIBERTY_COLORS[5]); // x축

        leftAxis.setTextColor(LIBERTY_COLORS[5]); // y축
        leftAxis.setGridColor(LIBERTY_COLORS[5]);
        rightAxis.setTextColor(LIBERTY_COLORS[5]); // y축
        rightAxis.setGridColor(LIBERTY_COLORS[5]); // y축

        xAxis.setDrawGridLines(false);

        xAxis.setTypeface(tf);
        leftAxis.setTypeface(tf);
        rightAxis.setTypeface(tf);

        lineChart.getLegend().setTypeface(tf);

        lineChart.getXAxis().setDrawGridLines(false);

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
        LineChart lineChart = findViewById(R.id.monitoringchart);
        lineChart.invalidate();
    }
    public void moveAnalysis(View view){
        Intent intent = new Intent(this,Analysis.class);
        startActivity(intent);
        }
    }
