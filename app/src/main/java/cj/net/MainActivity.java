package cj.net;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ScrollView;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ArrayList<Monitoring> monitorings= new ArrayList<>();
    ArrayList<Daily> dailies = new ArrayList<>();
    boolean[] options = new boolean[5];
    float harmness = 12f;


    int color1 = Color.rgb(183,164,238); // PURPLE
    int color2 = Color.rgb(120,120,170); // PURPLE
    int color3 = Color.rgb(123,104,238); // PURPLE
    int color4 = Color.rgb(210,90,90); // PICK


    int[] LIBERTY_COLORS = {
            Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187),
            Color.rgb(75, 154, 160), Color.rgb(42, 109, 130),  Color.rgb(22, 109, 160)
    };


    int[] VORDIPLOM_COLORS = {
            Color.rgb(207, 248, 246), Color.rgb(142, 249, 183), Color.rgb(173, 139, 240), Color.rgb(255, 160, 40),
            Color.rgb(140, 234, 255), Color.rgb(255, 140, 157)
    };

    Typeface tf;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tf = Typeface.createFromAsset(getAssets(), "esamanrulight.ttf");

        if(monitorings.isEmpty()) {
            options[3] = true;
            options[4] = true;
            readCSV(monitorings, dailies);
        }

        //harmness = (float)monitorings.get(monitorings.size()-1).getHarmness();

        if (harmness >= 70) {
            showAlert();
        }

        harmnessChart(monitorings);
        monitoringChart(monitorings,options);
        setSleepBarChart(dailies); // ??????
        setWeightChart(); // ??????
        setMuscleChart(); // ?????????
        setFatChart(); // ?????????

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

    /* ???????????? */
    public void setSleepBarChart(ArrayList<Daily> dailies) {
        BarChart barChart = findViewById(R.id.sleepchart);

        barChart.setDrawGridBackground(false); // ???
        //barChart.setTouchEnabled(false); // ???????????????
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

        ArrayList sleep = new ArrayList(); // ????????????

        float total = 0;

        for (int i = 0; i < dailies.size(); i++) {
            double temp = Math.ceil(dailies.get(i).getSleep()*10)/10.0;
            float sleepData = (float) temp;

            sleep.add(new BarEntry(sleepData, i));
            total += sleepData;
        }

        // ?????? ????????????
        total /= dailies.size();

        total *= 60;
        int hour = (int)total / 60;
        int min = (int)total % 60;

        String avg = String.format("%.1f", total);
        TextView sleepAvg = findViewById(R.id.sleepAvg);

        //sleepAvg.setText(avg + "hr");
        sleepAvg.setText(hour + "?????? " + min + "???");

        ArrayList year = new ArrayList();

        for (int i = 1; i <= dailies.size(); i++) {
            year.add(i+"???");
        }

        BarDataSet bardataset = new BarDataSet(sleep, "?????? ????????????");
        barChart.animateY(3000);


        BarData data = new BarData(year, bardataset); // MPAndroidChart v3.X ?????? ?????????
        data.setValueFormatter(new MyValueFormatter()); // ????????? ????????????
        data.setValueTypeface(tf);

        sleepAvg.setTextColor(color2);

        data.setValueTextColor(color1);
        bardataset.setColor(color1);
        xAxis.setTextColor(color1); // x???
        xAxis.setGridColor(color1); // x???
        leftAxis.setTextColor(color1); // y???
        leftAxis.setGridColor(color1);
        rightAxis.setTextColor(color1); // y???
        rightAxis.setGridColor(color1); // y???

        leftAxis.setTypeface(tf);
        rightAxis.setTypeface(tf);

        xAxis.setDrawGridLines(false);

        barChart.setData(data);

    }

    /* ?????? */
    public void setWeightChart() {
        LineChart chart = findViewById(R.id.weightchart);

        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(false); // ???????????????
        chart.getLegend().setEnabled(false); // Remove label
        chart.setDescription(""); // Remove desc

        XAxis xAxis = chart.getXAxis();
        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaxValue(100f);

        ArrayList weight = new ArrayList(); // ????????????

        float total = 0;
        for (int i = 0; i < dailies.size(); i++) {
            float weightData = (float) dailies.get(i).getWeight();
            weight.add(new BarEntry(weightData, i));
            total += weightData;
        }

        // ?????? ??????
        total /= dailies.size();
        String avg = String.format("%.1f", total);
        TextView weightAvg = findViewById(R.id.weightAvg);
        weightAvg.setText(avg + "kg");

        ArrayList year = new ArrayList();

        for (int i = 1; i <= this.dailies.size(); i++) {
            year.add(i+"???");
        }

        LineDataSet bardataset = new LineDataSet(weight, " ");
        chart.animateY(3000);

        LineData data = new LineData(year, bardataset); // MPAndroidChart v3.X ?????? ??????
        data.setValueFormatter(new MyValueFormatter()); // ????????? ????????????
        data.setValueTypeface(tf);

        weightAvg.setTextColor(color2);

        data.setValueTextColor(color1);
        bardataset.setCircleColor(color1);
        bardataset.setColor(color1);
        xAxis.setTextColor(color1); // x???
        xAxis.setGridColor(color1); // x???
        xAxis.setTypeface(tf);

        leftAxis.setTextColor(color1); // y???
        leftAxis.setGridColor(color1);
        rightAxis.setTextColor(color1); // y???
        rightAxis.setGridColor(color1); // y???


        leftAxis.setTypeface(tf);
        rightAxis.setTypeface(tf);

        xAxis.setDrawGridLines(false);


        chart.setData(data);
    }

    /* ????????? */
    public void setMuscleChart() {
        LineChart chart = findViewById(R.id.musclechart);

        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(false); // ???????????????
        chart.getLegend().setEnabled(false); // Remove label
        chart.setDescription(""); // Remove desc

        XAxis xAxis = chart.getXAxis();
        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tf);
        //xAxis.setDrawGridLines(false);

        ArrayList muscle = new ArrayList(); // ????????????

        float total = 0;
        for (int i = 0; i < dailies.size(); i++) {
            float muscleData = (float) dailies.get(i).getMuscle();
            muscle.add(new BarEntry(muscleData, i));
            total += muscleData;
        }

        // ?????? ??????
        total /= dailies.size();
        String avg = String.format("%.1f", total);
        TextView muscleAvg = findViewById(R.id.muscleAvg);
        muscleAvg.setText(avg + "%");
        muscleAvg.setTextColor(color2);

        ArrayList year = new ArrayList();

        for (int i = 1; i <= this.dailies.size(); i++) {
            year.add(i+"???");
        }

        LineDataSet bardataset = new LineDataSet(muscle, " ");
        chart.animateY(3000);

        LineData data = new LineData(year, bardataset); // MPAndroidChart v3.X ?????? ??????
        data.setValueFormatter(new MyValueFormatter()); // ????????? ????????????
        data.setValueTypeface(tf);

        muscleAvg.setTextColor(color2);

        data.setValueTextColor(color1);
        bardataset.setCircleColor(color1);
        bardataset.setColor(color1);
        xAxis.setTextColor(color1); // x???
        xAxis.setGridColor(color1); // x???


        leftAxis.setTextColor(color1); // y???
        leftAxis.setGridColor(color1);
        rightAxis.setTextColor(color1); // y???
        rightAxis.setGridColor(color1); // y???

        leftAxis.setTypeface(tf);
        rightAxis.setTypeface(tf);

        xAxis.setDrawGridLines(false);


        chart.setData(data);
    }

    /* ????????? */
    public void setFatChart() {
        LineChart chart = findViewById(R.id.fatchart);

        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(false); // ???????????????
        chart.getLegend().setEnabled(false); // Remove label
        chart.setDescription(""); // Remove desc

        XAxis xAxis = chart.getXAxis();
        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //xAxis.setDrawGridLines(false);

        ArrayList fat = new ArrayList(); // ????????????


        float total = 0;
        for (int i = 0; i < dailies.size(); i++) {
            float fatData = (float) dailies.get(i).getFat();
            fat.add(new BarEntry(fatData, i));
            total += fatData;
            //System.out.println((int)dailies.get(i).getSleep());
        }

        // ?????? ????????????
        total /= dailies.size();
        String avg = String.format("%.1f", total);
        TextView fatAvg = findViewById(R.id.fatAvg);
        fatAvg.setText(avg + "%");

        ArrayList year = new ArrayList();

        for (int i = 1; i <= this.dailies.size(); i++) {
            year.add(i+"???");
        }

        LineDataSet bardataset = new LineDataSet(fat, " ");
        chart.animateY(3000);

        LineData data = new LineData(year, bardataset); // MPAndroidChart v3.X ?????? ??????

        data.setValueFormatter(new MyValueFormatter()); // ????????? ????????????
        data.setValueTypeface(tf);

        fatAvg.setTextColor(color2);

        data.setValueTextColor(color1);
        bardataset.setCircleColor(color1);
        bardataset.setColor(color1);
        xAxis.setTextColor(color1); // x???
        xAxis.setGridColor(color1); // x???

        leftAxis.setTextColor(color1); // y???
        leftAxis.setGridColor(color1);
        rightAxis.setTextColor(color1); // y???
        rightAxis.setGridColor(color1); // y???

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

            //float variable = (float)monitorings.get(monitorings.size()-1).getHarmness();

            HarmnessData.add(new Entry(harmness,0));
            HarmnessData.add(new Entry(100f - harmness,1));

            PieDataSet dataSet = new PieDataSet(HarmnessData, "Number Of Employees");
            ArrayList harmnessArr = new ArrayList();
            harmnessArr.add("");
            harmnessArr.add("");

            PieData data = new PieData(harmnessArr, dataSet); // MPAndroidChart v3.X ?????? ??????
            pieChart.setData(data);
            Legend l = pieChart.getLegend();
            l.setEnabled(false);
            // description hide
            pieChart.setDescription(null);
            int[] redColors = {color4, Color.rgb(250,200,200)};
            int[] orangeColors = {Color.rgb(255,180,0), Color.rgb(250,250,222)};
            int[] greenColors = {Color.rgb(170,235,170), Color.rgb(240,255,240)};

            if (0 <= harmness && harmness <= 30) {
                dataSet.setColors(greenColors);
                pieChart.setCenterTextColor(greenColors[0]);
            } else if (30 <= harmness && harmness <= 60) {
                dataSet.setColors(orangeColors);
                pieChart.setCenterTextColor(orangeColors[0]);
            } else if (60 <= harmness && harmness <= 100) {
                dataSet.setColors(redColors);
                pieChart.setCenterTextColor(redColors[0]);
            }

            dataSet.setDrawValues(false);
            dataSet.setValueTextColor(Color.GRAY);

            pieChart.animateXY(1000, 1000);
            pieChart.setHoleRadius(65);
            // set center Text

            Typeface tf = Typeface.createFromAsset(getAssets(), "esamanrulight.ttf");
            //pieChart.setClickable(false);
            pieChart.setCenterText(String.valueOf(Math.round(harmness)) + " %");
            pieChart.setCenterTextSize(35);
            pieChart.setCenterTextTypeface(tf);
            //pieChart.setCenterTextRadiusPercent();
    }

    // monitoring Line Charts
    public void monitoringChart(ArrayList<Monitoring> monitorings,boolean[] options) {
            LineChart lineChart = findViewById(R.id.monitoringchart);
            String[] columns = {"?????????", "???????????????", "??????", "??????", "??????"};

            CheckBox fhr = findViewById(R.id.FHR_btn);
            fhr.setTextColor(VORDIPLOM_COLORS[1]);
            CheckBox uc = findViewById(R.id.UC_btn);
            uc.setTextColor(VORDIPLOM_COLORS[2]);
            CheckBox fm = findViewById(R.id.FM_btn);
            fm.setTextColor(VORDIPLOM_COLORS[3]);
            CheckBox glu = findViewById(R.id.glucose_btn);
            glu.setTextColor(VORDIPLOM_COLORS[4]);
            CheckBox press = findViewById(R.id.pressure_btn);
            press.setTextColor(VORDIPLOM_COLORS[5]);

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
                lineDataSet1.setColor(VORDIPLOM_COLORS[1]);
                lineDataSets.add(lineDataSet1);
            }
        if(options[1]) {
            LineDataSet lineDataSet2 = new LineDataSet(entry[1], columns[1]);
            lineDataSet2.setDrawCircles(false);
            lineDataSet2.setColor(VORDIPLOM_COLORS[2]);
            lineDataSets.add(lineDataSet2);
        }
        if(options[2]) {
            LineDataSet lineDataSet3 = new LineDataSet(entry[2], columns[2]);
            lineDataSet3.setDrawCircles(false);
            lineDataSet3.setColor(VORDIPLOM_COLORS[3]);
            lineDataSets.add(lineDataSet3);
        }
        if(options[3]) {
            LineDataSet lineDataSet4 = new LineDataSet(entry[3], columns[3]);
            lineDataSet4.setDrawCircles(false);
            lineDataSet4.setColor(VORDIPLOM_COLORS[4]);
            lineDataSets.add(lineDataSet4);
        }
        if(options[4]) {
            LineDataSet lineDataSet5 = new LineDataSet(entry[4], columns[4]);
            lineDataSet5.setDrawCircles(false);
            lineDataSet5.setColor(VORDIPLOM_COLORS[5]);
            lineDataSets.add(lineDataSet5);
        }

            String[] x = new String[3194];
            for (int i = 0; i < 3194; i++) {
                int min = i / 60;
                int sec = i % 60;
                x[i] = "12??? " + String.valueOf(min) + "??? " + String.valueOf(sec) + "???";
            }

            lineChart.setData(new LineData(x, lineDataSets));
            lineChart.setDescription(null);

        XAxis xAxis = lineChart.getXAxis();

        YAxis leftAxis = lineChart.getAxisLeft();
        YAxis rightAxis = lineChart.getAxisRight();

        //data.setValueTextColor(color1);
        int grey = Color.rgb(140,140,140);
        xAxis.setTextColor(grey); // x???
        xAxis.setGridColor(grey); // x???

        leftAxis.setTextColor(grey); // y???
        leftAxis.setGridColor(grey);
        rightAxis.setTextColor(grey); // y???
        rightAxis.setGridColor(grey); // y???

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

    public void showAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String pressure = "140 mmHg";
        String glucose = "155 mg/dl";
        String msg = "????????????\n\n" +"?????? ????????? " + pressure +"??? ????????? ?????? ???????????? 30?????? ????????????.\n\n" +
                "?????? ????????? " + glucose + "??? ????????? ?????? ????????? 20?????? ??????????????????.\n\n" +
                "?????? ????????? ??????????????????.";
        builder.setMessage(msg);

        builder.setNegativeButton("??????",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        );

        final AlertDialog alertDialog = builder.create();

        //setup to change color of the button
        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color1);
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTypeface(tf);
            }
        });

        alertDialog.show();

        TextView message = alertDialog.findViewById(android.R.id.message);
        message.setTypeface(tf);
    }

    public void moveAnalysis(View view){
        Intent intent = new Intent(this,harmness > 50 ?Analysis.class : Analysis2.class);
        startActivity(intent);
        }
    public void changeMode(View view){
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.requestFocus(View.FOCUS_UP);
        scrollView.scrollTo(0,0);

        harmness = harmness < 50 ? 74 : 12;
        View v = findViewById(R.id.analysis);
        harmnessChart(monitorings);

        if (harmness >= 70) {
            showAlert();
        }
        v.invalidate();
    }
}


