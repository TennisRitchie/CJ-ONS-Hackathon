package cj.net;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.EventLogTags;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ArrayList<Integer> jsonList = new ArrayList<>(); // ArrayList 선언
    ArrayList<String> labelList = new ArrayList<>(); // ArrayList 선언
    ArrayList<Monitoring> monitorings= new ArrayList<>();
    ArrayList<Daily> dailies = new ArrayList<>();

    BarChart barChart;
    TextView minuteTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readCSV(monitorings,dailies);
        harmnessChart(monitorings);
        monitoringChart(monitorings);
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
    public void monitoringChart(ArrayList<Monitoring> monitorings){
        LineChart lineChart = findViewById(R.id.monitoringchart);
        String[] columns= {"심박수","자궁수축율","태동","혈당","혈압"};
        int[] colors= {Color.BLUE,Color.RED,Color.MAGENTA,Color.YELLOW,Color.GREEN};
        int index = 0;
        boolean[] options = new boolean[5];
        Arrays.fill(options,true);
        ArrayList<String> xAXES = new ArrayList<>();

        ArrayList<Entry>[] entry = new ArrayList[5];
        for(int i = 0; i <5;i++) entry[i] = new ArrayList<>();
        for(Monitoring monitoring : monitorings) {
            float FHRs = (float) monitoring.getFHR();
            entry[0].add(new Entry(FHRs,index));

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
        LineDataSet lineDataSet1 = new LineDataSet(entry[0], columns[0]);
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setColor(colors[0]);

        LineDataSet lineDataSet2 = new LineDataSet(entry[1], columns[1]);
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setColor(colors[1]);

        LineDataSet lineDataSet3 = new LineDataSet(entry[2], columns[2]);
        lineDataSet3.setDrawCircles(false);
        lineDataSet3.setColor(colors[2]);

        LineDataSet lineDataSet4 = new LineDataSet(entry[3], columns[3]);
        lineDataSet4.setDrawCircles(false);
        lineDataSet4.setColor(colors[3]);

        LineDataSet lineDataSet5 = new LineDataSet(entry[4], columns[4]);
        lineDataSet5.setDrawCircles(false);
        lineDataSet5.setColor(colors[4]);

        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);
        lineDataSets.add(lineDataSet3);
        lineDataSets.add(lineDataSet4);
        lineDataSets.add(lineDataSet5);

        String[] x = new String[3194];
        for(int i = 0; i < 3194; i++){
            int min = i / 60;
            int sec = i % 60;
            x[i] = "12시 "+String.valueOf(min)+"분 "+String.valueOf(sec)+"초";
        }
        lineChart.setData(new LineData(x, lineDataSets));
        lineChart.setDescription(null);
    }
    }
