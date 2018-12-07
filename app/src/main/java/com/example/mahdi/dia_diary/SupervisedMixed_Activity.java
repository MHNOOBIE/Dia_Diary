package com.example.mahdi.dia_diary;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

public class SupervisedMixed_Activity extends AppCompatActivity {

    private LineChart lineChart;
    private String pat_id;
    float max = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mixed_tracker);


        final List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        Intent intent = getIntent();
        pat_id = intent.getStringExtra("ID");

        lineChart = findViewById(R.id.chart);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("InsulinEntry").document(pat_id).collection("Insulin").orderBy("time").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<Entry> lineEntry = new ArrayList<Entry>();


                for (DocumentSnapshot dc : queryDocumentSnapshots) {


                    Insulin insulin = dc.toObject(Insulin.class);

                    String date_n = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).format(insulin.time);

                    Entry temp = new Entry(insulin.time.getTime(), Float.parseFloat(insulin.insulin_value));
                    lineEntry.add(temp);
                    max = Float.parseFloat(insulin.insulin_value);

                }

                if (lineEntry.size() > 0) {

                    YAxis leftAxis = lineChart.getAxisLeft();
                    leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                    leftAxis.setAxisMinimum(0f);


                    YAxis rightAxis = lineChart.getAxisRight();
                    rightAxis.setEnabled(false);

                    XAxis xAxis = lineChart.getXAxis();

                    xAxis.setValueFormatter(new SupervisedMixed_Activity.MyXAxisValueFormatter(null));

                    xAxis.setLabelCount(2);


                    LineDataSet lineDataSet = new LineDataSet(lineEntry, "Insulin Value");
                    lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                    lineDataSet.setColor(Color.CYAN);
                    lineDataSet.setCircleColor(Color.RED);
                    lineDataSet.setCircleHoleColor(Color.RED);

                    dataSets.add(lineDataSet);
                }


            }
        });


        db = FirebaseFirestore.getInstance();
        db.collection("GlucoseEntry").document(pat_id).collection("Glucose").orderBy("time").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<Entry> lineEntry = new ArrayList<Entry>();


                for (DocumentSnapshot dc : queryDocumentSnapshots) {


                    Glucose glucose = dc.toObject(Glucose.class);


                    String date_n = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).format(glucose.time);

                    Entry temp = new Entry(glucose.time.getTime(), Float.parseFloat(glucose.gluc_value)); // 0 == quarter 1

                    lineEntry.add(temp);

                    if (max < Float.parseFloat(glucose.gluc_value))
                        max = Float.parseFloat(glucose.gluc_value);

                }

                if (lineEntry.size() > 0) {


                    LimitLine lower_limit = new LimitLine(3.0f, "Too low");

                    LimitLine upper_Limit = new LimitLine(8.9f, "Too high");


                    upper_Limit.setLineColor(Color.RED);
                    upper_Limit.enableDashedLine(10f, 10f, 0f);
                    upper_Limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);

                    lower_limit.setLineColor(Color.parseColor("#BD3853"));
                    lower_limit.enableDashedLine(10f, 10f, 0f);
                    lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);

                    YAxis leftAxis = lineChart.getAxisLeft();
                    leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                    leftAxis.setAxisMinimum(0f);
                    leftAxis.setAxisMaximum(max + 10f);

                    leftAxis.addLimitLine(upper_Limit);
                    leftAxis.addLimitLine(lower_limit);

                    YAxis rightAxis = lineChart.getAxisRight();
                    rightAxis.setEnabled(false);

                    XAxis xAxis = lineChart.getXAxis();

                    xAxis.setValueFormatter(new SupervisedMixed_Activity.MyXAxisValueFormatter(null));

                    xAxis.setLabelCount(2);


                    LineDataSet lineDataSet1 = new LineDataSet(lineEntry, "Glucose Value");
                    lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
                    lineDataSet1.setColor(Color.CYAN);
                    lineDataSet1.setCircleColor(Color.RED);
                    lineDataSet1.setCircleHoleColor(Color.RED);


                    lineDataSet1.setColor(Color.GREEN);
                    dataSets.add(lineDataSet1);
                    LineData lineData = new LineData(dataSets);

                    lineChart.setData(lineData);
                    lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    lineChart.getDescription().setEnabled(false);


                    lineChart.invalidate();

                }


            }
        });

    }
}

