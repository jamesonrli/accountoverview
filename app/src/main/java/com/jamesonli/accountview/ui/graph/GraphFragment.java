package com.jamesonli.accountview.ui.graph;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jamesonli.accountview.R;
import com.jamesonli.accountview.core.AccountDataManager;
import com.jamesonli.accountview.common.GraphDataUtils;

public class GraphFragment extends Fragment implements OnChartValueSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int GRAPH_DATA_LOADER_ID = 0;

    private LineData lineData;
    private LineChart lineChart;

    private GraphFragmentInteraction mListener;

    public static GraphFragment newInstance(GraphFragmentInteraction listener) {
        GraphFragment fragment = new GraphFragment();
        fragment.setInteractionListener(listener);
        return fragment;
    }

    public GraphFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lineChart = (LineChart) view.findViewById(R.id.overview_chart);
        lineChart.getAxisRight().setEnabled(false); // only show left label
        lineChart.getXAxis().setDrawGridLines(false);

        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setScaleXEnabled(false);
        lineChart.setScaleYEnabled(false);
        lineChart.setDescription("");
        lineChart.setAutoScaleMinMaxEnabled(true);
        lineChart.getLegend().setEnabled(false);
        lineChart.setOnChartValueSelectedListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(GRAPH_DATA_LOADER_ID, null, this);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        mListener.updateSelectedValue(lineChart.getLineData().getXVals().get(e.getXIndex()), e.getVal());
    }

    @Override
    public void onNothingSelected() {
    }

    public void setInteractionListener(GraphFragmentInteraction listener) {
        mListener = listener;
    }

    public interface GraphFragmentInteraction {
        void updateSelectedValue(String date, float balance);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case GRAPH_DATA_LOADER_ID:
                return AccountDataManager.getBalanceEntriesCursorLoader(getActivity().getApplicationContext());
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        updateGraphData(GraphDataUtils.getAccountOverviewData(getActivity(), data));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // no action
    }

    private void updateGraphData(final LineData data) {
        lineData = data;
        if (lineChart != null) {
            lineChart.setData(lineData);
            lineChart.invalidate();
        }
    }

}
