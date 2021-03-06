package com.osb.mobiledev.kpi.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.osb.mobiledev.kpi.R;
import com.osb.mobiledev.kpi.adapters.CurrencyListAdapter;
import com.osb.mobiledev.kpi.database.AppDatabase;
import com.osb.mobiledev.kpi.model.Currency;
import com.osb.mobiledev.kpi.model.CurrencyDTO;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import lombok.val;

import static java.util.Collections.singletonList;

public class ListFragment extends Fragment {
    private static final String TAG = ListFragment.class.getName();

    private AppDatabase database;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        val view = createAndConfigureView(inflater, container, savedInstanceState);

        return view;
    }

    private View createAndConfigureView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.currency_list_fragment, container, false);

        // init list view
        ListView listView = (ListView) getView(view, R.id.listView_currencies);

        // set item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Passing data to Details fragment");
                Fragment detailsFragment = new CurrencyDetailFragment();
                detailsFragment.setArguments(
                        getBundleForDetails((Currency) parent.getItemAtPosition(position))
                );
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, detailsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            private Bundle getBundleForDetails(Currency currency) {
                Bundle currencyBundle = new Bundle();
                currencyBundle.putSerializable(
                        "currency",
                        new CurrencyDTO(currency.getTxt(), currency.getRate(), currency.getExchangeDate(), currency.getR030())
                );
                Log.d(TAG, currencyBundle.toString());
                return currencyBundle;
            }
        });
        if (getActivity() != null) {
            new FetchDataTask(listView).execute();
        }

        return view;
    }

    private View getView(View parentView, int requestedViewId) {
        View ret = parentView.findViewById(requestedViewId);
        if (ret == null) {
            throw new RuntimeException("View with ID: " + requestedViewId + " could not be found!");
        }
        return ret;
    }

    private class FetchDataTask extends AsyncTask<Void, Void, List<Currency>> {
        private static final String API_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";
        private ListView targetListView;

        public FetchDataTask(ListView listView) {
            super();
            this.targetListView = listView;
        }

        @Override
        protected List<Currency> doInBackground(Void... voids) {
            Log.d(TAG, "Fetching currency API...");
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
            HttpEntity requestEntity = new HttpEntity(headers);

            ResponseEntity<List<Currency>> currencyResponse = new RestTemplate().exchange(
                    API_URL, HttpMethod.GET, requestEntity,
                    new ParameterizedTypeReference<List<Currency>>() {
                    }
            );

            List<Currency> results = currencyResponse.getBody();
            Log.d(TAG, "Fetched api data: " + results.size() + " results");
            return results;
        }

        @Override
        protected void onPostExecute(List<Currency> currencies) {
            CurrencyListAdapter adapter = new CurrencyListAdapter(getActivity(), currencies);
            targetListView.setAdapter(adapter);
            Log.d(TAG, "Finishing onPostExecute. CurrencyListAdapter set.");
        }
    }
}
