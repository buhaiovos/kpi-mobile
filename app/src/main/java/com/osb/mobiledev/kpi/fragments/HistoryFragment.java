package com.osb.mobiledev.kpi.fragments;

import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.osb.mobiledev.kpi.R;
import com.osb.mobiledev.kpi.adapters.HistoryListAdapter;
import com.osb.mobiledev.kpi.database.AppDatabase;
import com.osb.mobiledev.kpi.database.History;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.val;

public class HistoryFragment extends Fragment {
    public static final String TAG = HistoryFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_list_fragment, container, false);
        ListView listView = (ListView) getView(view, R.id.listView_history);

        Bundle bundle = getArguments();
        Long c030 = bundle.getLong("currency_code");

        AppDatabase appDB =
                Room.databaseBuilder(container.getContext(), AppDatabase.class, "appDB").build();

        new FetchDBAsyncTask().execute(new AsyncTaskDataDTO(appDB, c030, listView));

        return view;
    }

    private View getView(View parentView, int requestedViewId) {
        View ret = parentView.findViewById(requestedViewId);
        if (ret == null) {
            throw new RuntimeException("View with ID: " + requestedViewId + " could not be found!");
        }
        return ret;
    }

    @AllArgsConstructor
    private static class AsyncTaskDataDTO {
        public AppDatabase database;
        public Long currencyCode;
        public ListView target;
    }

    private class FetchDBAsyncTask extends AsyncTask<AsyncTaskDataDTO, Void, List<History>> {
        private ListView target;

        @Override
        protected List<History> doInBackground(AsyncTaskDataDTO... asyncTaskDataDTOS) {
            AsyncTaskDataDTO data = asyncTaskDataDTOS[0];
            val appDB = data.database;
            val currencyCode = data.currencyCode;
            this.target = data.target;

            List<History> history = appDB.historyDAO().loadAllByCurrencyCode(currencyCode);

            Log.d(TAG, "fetched DB data:" + history);

            return history;
        }

        @Override
        protected void onPostExecute(List<History> historyList) {
            Collections.sort(historyList, new Comparator<History>() {
                @Override
                public int compare(History o1, History o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });

            HistoryListAdapter adapter = new HistoryListAdapter(getActivity(), historyList);
            target.setAdapter(adapter);
            Log.d(TAG, "Finishing onPostExecute. CurrencyListAdapter set.");
        }
    }
}
