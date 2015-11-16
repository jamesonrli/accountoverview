package com.jamesonli.accountview.ui.graph;

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
import com.jamesonli.accountview.core.GraphDataListener;
import com.jamesonli.accountview.core.GraphManager;

import java.lang.ref.WeakReference;

public class GraphFragment extends Fragment implements GraphDataListener, OnChartValueSelectedListener {

    private LineData lineData;
    private LineChart lineChart;

    private GraphFragmentInteraction mListener;

    public static GraphFragment newInstance(GraphFragmentInteraction listener) {
        GraphFragment fragment = new GraphFragment();
        fragment.setInteractionListener(listener);
        return fragment;
    }

    public GraphFragment() {}

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

        notifyGraphDataChange();
    }

    public void notifyGraphDataChange() {
        GraphManager.getAccountOverviewData(getActivity().getApplicationContext(), new GraphDataResultListener(this));
    }

    public void updateGraphData(final LineData data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lineData = data;
                if(lineChart != null) {
                    updateChart();
                }
            }
        });
    }

    private void updateChart() {
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh
    }

    private static class GraphDataResultListener implements GraphDataListener {
        private WeakReference<GraphFragment> graphViewRef;

        public GraphDataResultListener(GraphFragment view) {
            graphViewRef = new WeakReference<>(view);
        }

        @Override
        public void updateGraphData(LineData data) {
            GraphFragment graphFragment = graphViewRef.get();

            if(graphFragment != null && graphFragment.isAdded()) {
                graphFragment.updateGraphData(data);
            }
        }
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        mListener.updateSelectedValue(lineChart.getLineData().getXVals().get(e.getXIndex()), e.getVal());
    }

    @Override
    public void onNothingSelected() {}

    public void setInteractionListener(GraphFragmentInteraction listener) {
        mListener = listener;
    }

    public interface GraphFragmentInteraction {
        void updateSelectedValue(String date, float balance);
    }
}
