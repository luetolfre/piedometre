package net.ictcampus.piedometre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;


/**
 * <h3> Stats Activity </h3>
 * It shows the Statistics of the taken Trainings
 *
 * @author luetolfre
 * @version 1.0
 * @since 2020-06-11
 */
public class StatsActivity extends AppCompatActivity {

    private static final String TRAININGS_SHARED_PREFS = "trainings";

    private TextView noDataFoundTextView;


    /**
     * create a combined chart
     * @param savedInstanceState SavedInstance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        noDataFoundTextView = findViewById(R.id.noDataFoundTextView);

        // initialize combined chart for a BarChart and a LineChart combination
        CombinedChart mChart = findViewById(R.id.lineChart);
        // mChart.setHighlightFullBarEnabled(true);
        mChart.setDescription(null);

        // methods that calibrate the mChart so that the Axis, Lines and Columns are correctly shown up

        // draw bars behind lines
        mChart.setDrawOrder(new CombinedChart.DrawOrder[] {CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE});

        // Axis on both sides of the Graph
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        CombinedData data = new CombinedData();
        data.setData(generateLineData());
        data.setData(generateBarData());

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        mChart.setData(data);
        mChart.getLegend().setCustom( new LegendEntry[0]);

        checkTrainingBeforeSet();
        mChart.invalidate();
    }


    /**
     * LineData creates values for the Line Chart
     * @return d LineData
     */
    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();
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

    /**
     * Creates values for the Bar Chart
     * @return d BarData
     */
    private BarData generateBarData() {

        ArrayList<BarEntry> entries = new ArrayList<>();
       // entries = getBarEntries(entries);
        getStepCountBarEntries(getTrainingsKeys(), entries);


        BarDataSet set1 = new BarDataSet(entries, null);
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(ColorTemplate.COLORFUL_COLORS);
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);



        float barWidth = 0.2f; // x2 data set

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }

    /**
     * read dates from
     * @return ArrayList with all keys containing dates
     */
    private ArrayList<String> getTrainingsKeys(){
        SharedPreferences pre = getSharedPreferences(TRAININGS_SHARED_PREFS, MODE_PRIVATE);
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
        SharedPreferences pre = getSharedPreferences(TRAININGS_SHARED_PREFS, MODE_PRIVATE);
        int stepCount;
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
        SharedPreferences pre = getSharedPreferences(TRAININGS_SHARED_PREFS, MODE_PRIVATE);
        float speed;
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

    /**
     * check if there are trainings are recorded
     */
    private void checkTrainingBeforeSet() {
        if (getTrainingsKeys().size() == 0) {
            noDataFoundTextView.setVisibility(View.VISIBLE);
        }
    }

}
