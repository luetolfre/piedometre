package net.ictcampus.piedometre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class StatsActivity extends AppCompatActivity {

    private static final String TRAININGS = "trainings";


    protected String[] mMonths = new String[] {"Jan", "Feb", "Mar", "Apr", "May", "June"};
    private LineData data;
    private CombinedChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // initialize combined chart for a BarChart and a LineChart combination
        mChart = (CombinedChart) findViewById(R.id.lineChart);
        mChart.setHighlightFullBarEnabled(true);
        mChart.setDescription(null);

        // methods that calibrate the mChart so that the Axis, Lines and Columns are correctly shown up

        // draw bars behind lines
        mChart.setDrawOrder(new CombinedChart.DrawOrder[] {CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE});

        // Legend object gets
        /*Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        */

        // Axis on both sides of the Graph
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);


        CombinedData data = new CombinedData();

        data.setData(generateLineData());
        data.setData(generateBarData());

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        // xAxis.setValueFormatter(new XAxisValueFormatter());
        mChart.setData(data);
        mChart.getLegend().setCustom( new LegendEntry[0]);

        mChart.invalidate();
    }




    /**
     * LineData creates
     * @return
     */
    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();
        getSpeedLineEntries(getTrainingsKeys(), entries);


        LineDataSet set = new LineDataSet(entries, null);
        //set.setColor(Color.rgb(240, 238, 70));
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(0);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
       // entries = getBarEnteries(entries);
        getStepCountBarEntries(getTrainingsKeys(), entries);


        BarDataSet set1 = new BarDataSet(entries, null);
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(ColorTemplate.COLORFUL_COLORS);
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);



        float barWidth = 0.2f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }

    private ArrayList<String> getTrainingsKeys(){
        SharedPreferences pre = getSharedPreferences(TRAININGS, MODE_PRIVATE);
        Map<String,?> keys = pre.getAll();
        ArrayList<String> dates = new ArrayList<>();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            dates.add(entry.getKey());
        }
        return dates;
    }


    /**
     * ArrayList with the entries for the BarGraph step values
     */
    private void getStepCountBarEntries(ArrayList<String> dates, ArrayList<BarEntry> barEntries){
        int counter = 1;
        SharedPreferences pre = getSharedPreferences(TRAININGS, MODE_PRIVATE);
        int stepCount = 0;
        for (String date : dates) {
            HashSet<String> mySet = (HashSet<String>) pre.getStringSet(date, new HashSet<String>());
            for (String s: mySet) {
                if(s.startsWith("steps:")){
                    s = s.replace("steps:", "");
                    stepCount = Integer.parseInt(s);
                    barEntries.add(new BarEntry(counter, stepCount));
                    counter ++;
                }
            }
        }
    }

    /**
     * ArrayList with the entries for the line graph speed values
     */
    private void getSpeedLineEntries(ArrayList<String> dates, ArrayList<Entry> lineEntries) {
        int counter = 1;
        SharedPreferences pre = getSharedPreferences(TRAININGS, MODE_PRIVATE);
        float speed = 0;
        for (String date : dates) {
            HashSet<String> mySet = (HashSet<String>) pre.getStringSet(date, new HashSet<String>());
            for (String s: mySet) {
                if(s.startsWith("speed:")){
                    s = s.replace("speed:", "");
                    speed = Float.parseFloat(s);
                    lineEntries.add(new BarEntry(counter, speed));
                    counter ++;
                }
            }
        }
    }

}
