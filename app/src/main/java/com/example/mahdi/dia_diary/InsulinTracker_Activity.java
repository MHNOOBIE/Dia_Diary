package com.example.mahdi.dia_diary;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
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

public class InsulinTracker_Activity extends AppCompatActivity {

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

    private TextView time_tv, insulin_tv;
    private TableLayout tableLayout;
    private TableRow tableRow;
    private LineChart lineChart;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insulin_tracker);


        lineChart = findViewById(R.id.chart);

        tableLayout = findViewById(R.id.tableLayout);
        tableLayout.setColumnStretchable(0, true);
        tableLayout.setColumnStretchable(1, true);


        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("InsulinEntry").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Insulin").orderBy("time").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<Entry> lineEntry = new ArrayList<Entry>();
                float max =0;

                for (DocumentSnapshot dc : queryDocumentSnapshots) {


                    Insulin insulin = dc.toObject(Insulin.class);
                    tableRow = new TableRow(InsulinTracker_Activity.this);

                    time_tv = new TextView(InsulinTracker_Activity.this);
                    insulin_tv = new TextView(InsulinTracker_Activity.this);


                    String date_n = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).format(insulin.time);

                    time_tv.setText(date_n);
                    time_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    time_tv.setTextColor(Color.parseColor("#111111"));

                    insulin_tv.setText(insulin.insulin_value);
                    insulin_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    insulin_tv.setTextColor(Color.parseColor("#111111"));

                    tableRow.addView(time_tv);
                    tableRow.addView(insulin_tv);

                    tableLayout.addView(tableRow);

                    Entry temp = new Entry(insulin.time.getTime(), Float.parseFloat(insulin.insulin_value)); // 0 == quarter 1
                    lineEntry.add(temp);
                    max = Float.parseFloat(insulin.insulin_value);

                }

                if(lineEntry.size()>0) {

                    YAxis leftAxis = lineChart.getAxisLeft();
                    leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                    leftAxis.setAxisMinimum(0f);
                    leftAxis.setAxisMaximum(max+10f);


                    YAxis rightAxis = lineChart.getAxisRight();
                    rightAxis.setEnabled(false);

                    XAxis xAxis = lineChart.getXAxis();

                    xAxis.setValueFormatter(new InsulinTracker_Activity.MyXAxisValueFormatter(null));

                    xAxis.setLabelCount(2);


                    LineDataSet lineDataSet = new LineDataSet(lineEntry, "Insulin Value");
                    lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                    lineDataSet.setColor(Color.CYAN);
                    lineDataSet.setCircleColor(Color.RED);
                    lineDataSet.setCircleHoleColor(Color.RED);

                    LineData lineData = new LineData(lineDataSet);

                    lineChart.setData(lineData);
                    lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    lineChart.getDescription().setEnabled(false);
                    lineChart.invalidate();
                }


            }
        });

    }
}
