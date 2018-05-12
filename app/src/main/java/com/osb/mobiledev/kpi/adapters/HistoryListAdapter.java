package com.osb.mobiledev.kpi.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.osb.mobiledev.kpi.R;
import com.osb.mobiledev.kpi.database.History;

import java.util.List;

public class HistoryListAdapter extends ArrayAdapter<History> {
    private List<History> historyList;

    public HistoryListAdapter(Activity activity, List<History> historyList) {
        super(activity, 0, historyList);

        this.historyList = historyList;
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Nullable
    @Override
    public History getItem(int position) {
        return historyList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        History history = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.history_list_row, parent, false);
        }

        TextView date = convertView.findViewById(R.id.history_date);
        TextView rate = convertView.findViewById(R.id.history_rate);

        date.setText(history.getDate());
        rate.setText(String.format("%.5f",history.getRate()));

        return convertView;
    }
}
