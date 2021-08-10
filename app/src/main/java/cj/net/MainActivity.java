package cj.net;

import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ArrayList<Monitoring> monitorings= new ArrayList<>();
    ArrayList<Daily> dailies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String path = System.getProperty("user.dir");
        System.out.println("Working Directory = " + path);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readCSV(monitorings,dailies);

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



}
