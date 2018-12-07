package com.example.mahdi.dia_diary;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.TypedValue;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class SupervisedGlucose_Activity extends AppCompatActivity {

    private String pat_id;

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return sdf.format(new Date((long) value));
        }

        /**
         * this is only needed if numbers are returned, else return 0
         */

        public int getDecimalDigits() {
            return 0;
        }
    }

    private TextView time_tv, type_tv, glucose_tv;
    private TableLayout tableLayout;
    private TableRow tableRow;
    private LineChart lineChart;
    private LineChart lineChart2;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervised_glucose_);


        Intent intent = getIntent();
        pat_id=intent.getStringExtra("ID");

        lineChart = findViewById(R.id.chart);
        lineChart2 = findViewById(R.id.chart2);

        tableLayout = findViewById(R.id.tableLayout);
        tableLayout.setColumnStretchable(0, true);
        tableLayout.setColumnStretchable(1, true);
        tableLayout.setColumnStretchable(2, true);


        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("GlucoseEntry").document(pat_id).collection("Glucose").orderBy("time").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<Entry> lineEntry = new ArrayList<Entry>();
                List<Entry> randomEntry = new ArrayList<>();
                List<Entry> fastingEntry = new ArrayList<>();
                List<Entry> aftermealEntry = new ArrayList<>();
                float max =0;

                for (DocumentSnapshot dc : queryDocumentSnapshots) {


                    Glucose glucose = dc.toObject(Glucose.class);
                    tableRow = new TableRow(SupervisedGlucose_Activity.this);

                    time_tv = new TextView(SupervisedGlucose_Activity.this);
                    type_tv = new TextView(SupervisedGlucose_Activity.this);
                    glucose_tv = new TextView(SupervisedGlucose_Activity.this);


                    String date_n = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).format(glucose.time);

                    time_tv.setText(date_n);
                    time_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    time_tv.setTextColor(Color.parseColor("#111111"));

                    type_tv.setText(glucose.type);
                    type_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    type_tv.setTextColor(Color.parseColor("#111111"));


                    glucose_tv.setText(glucose.gluc_value);
                    glucose_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    glucose_tv.setTextColor(Color.parseColor("#111111"));

                    tableRow.addView(time_tv);
                    tableRow.addView(type_tv);
                    tableRow.addView(glucose_tv);

                    tableLayout.addView(tableRow);

                    Entry temp = new Entry(glucose.time.getTime(), Float.parseFloat(glucose.gluc_value)); // 0 == quarter 1
                    if (glucose.type.equals("Fasting")) {
                        fastingEntry.add(temp);
                    } else if (glucose.type.equals("Random")) {
                        randomEntry.add(temp);
                    } else if (glucose.type.equals("After Meal")) {
                        aftermealEntry.add(temp);
                    }
                    lineEntry.add(temp);

                    max = Float.parseFloat(glucose.gluc_value);

                }

                if(lineEntry.size()>0) {



                    LimitLine lower_limit = new LimitLine(3.0f,"Too low");

                    LimitLine upper_Limit = new LimitLine(8.9f,"Too high");


                    upper_Limit.setLineColor(Color.RED);
                    upper_Limit.enableDashedLine(10f,10f,0f);
                    upper_Limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);

                    lower_limit.setLineColor(Color.parseColor("#BD3853"));
                    lower_limit.enableDashedLine(10f,10f,0f);
                    lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);

                    YAxis leftAxis = lineChart.getAxisLeft();
                    leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                    leftAxis.setAxisMinimum(0f);
                    leftAxis.setAxisMaximum(max+10f);

                    leftAxis.addLimitLine(upper_Limit);
                    leftAxis.addLimitLine(lower_limit);

                    YAxis rightAxis = lineChart.getAxisRight();
                    rightAxis.setEnabled(false);

                    XAxis xAxis = lineChart.getXAxis();

                    xAxis.setValueFormatter(new MyXAxisValueFormatter(null));

                    xAxis.setLabelCount(2);


                    LineDataSet lineDataSet = new LineDataSet(lineEntry, "Glucose Value");
                    lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                    lineDataSet.setColor(Color.CYAN);
                    lineDataSet.setCircleColor(Color.RED);
                    lineDataSet.setCircleHoleColor(Color.RED);

                    LineDataSet randomDataSet = new LineDataSet(randomEntry, "Random");
                    LineDataSet fastingDataSet = new LineDataSet(fastingEntry, "Fasting");
                    LineDataSet aftermealDataSet = new LineDataSet(aftermealEntry, "After Meal");


                    randomDataSet.setColor(Color.RED);
                    randomDataSet.setCircleColor(Color.RED);
                    randomDataSet.setCircleHoleColor(Color.RED);

                    fastingDataSet.setCircleHoleColor(Color.YELLOW);
                    fastingDataSet.setColor(Color.YELLOW);
                    fastingDataSet.setCircleColor(Color.YELLOW);

                    aftermealDataSet.setColor(Color.GREEN);
                    aftermealDataSet.setCircleColor(Color.GREEN);
                    aftermealDataSet.setCircleHoleColor(Color.GREEN);


                    List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                    dataSets.add(randomDataSet);
                    dataSets.add(fastingDataSet);
                    dataSets.add(aftermealDataSet);

                    LineData lineData = new LineData(dataSets);

                    lineChart.setData(lineData);
                    lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    lineChart.getDescription().setEnabled(false);


                    leftAxis = lineChart2.getAxisLeft();
                    leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                    leftAxis.setAxisMinimum(0f);
                    leftAxis.setAxisMaximum(max+10f);

                    leftAxis.addLimitLine(upper_Limit);
                    leftAxis.addLimitLine(lower_limit);


                    rightAxis = lineChart2.getAxisRight();
                    rightAxis.setEnabled(false);


                    xAxis = lineChart2.getXAxis();

                    xAxis.setValueFormatter(new MyXAxisValueFormatter(null));

                    xAxis.setLabelCount(2);


                    lineData = new LineData(lineDataSet);
                    lineChart2.setData(lineData);
                    lineChart2.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    lineChart2.getDescription().setEnabled(false);

                    lineChart.invalidate();
                    lineChart2.invalidate();

                }


            }
        });

    }
}
