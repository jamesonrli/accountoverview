package com.jamesonli.accountview.common;

import android.content.Context;
import android.database.Cursor;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jamesonli.accountview.R;
import com.jamesonli.accountview.provider.AVContract;

import java.util.ArrayList;
import java.util.List;

public class GraphDataUtils {

    public static LineData getAccountOverviewData(Context context, Cursor cursor) {
        List<Entry> vals = new ArrayList<>();
        List<String> xVals = new ArrayList<>();
        int index = 0;

        while (cursor.moveToNext()) {
            vals.add(new Entry(cursor.getFloat(cursor.getColumnIndexOrThrow(AVContract.BALANCE_TABLE_BALANCE)), index++));
            xVals.add(AVUtils.dateGraphLabelParser(cursor.getLong(cursor.getColumnIndexOrThrow(AVContract.BALANCE_TABLE_DATE))));
        }

        LineDataSet dataSet = new LineDataSet(vals, context.getString(R.string.graph_balance_label));
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        return new LineData(xVals, dataSet);
    }

}
