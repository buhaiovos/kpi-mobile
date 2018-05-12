package com.osb.mobiledev.kpi.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.osb.mobiledev.kpi.R;
import com.osb.mobiledev.kpi.database.AppDatabase;
import com.osb.mobiledev.kpi.database.History;
import com.osb.mobiledev.kpi.database.HistoryDAO;
import com.osb.mobiledev.kpi.model.Currency;
import com.osb.mobiledev.kpi.model.CurrencyDTO;

import java.util.Locale;

public class CurrencyDetailFragment extends Fragment {
    private static final String TAG = CurrencyDetailFragment.class.getName();
    AppDatabase database;

    private CurrencyDTO currencyDTO;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "CurrencyDetailFragment.onCreateView entered");

        View view = inflater.inflate(R.layout.currency_details_fragment, container, false);

        nullCheck(container, this.getClass());
        nullCheck(container.getContext(), this.getClass());

        database = Room.databaseBuilder(container.getContext(), AppDatabase.class, "appDB").build();

        currencyDTO = (CurrencyDTO)this.getArguments().getSerializable("currency");
        populateView(view, currencyDTO);
        configureButtonsListeners(view);

        Log.d(TAG, "CurrencyDetailFragment.onCreateView exiting, returning view:" + view);
        return view;
    }

    private void populateView(View view, CurrencyDTO data) {
        Log.d(TAG, "CurrencyDetailFragment.populateView entered");
        nullCheck(data, this.getClass());

        TextView titleValue = view.findViewById(R.id.textView_currency_title_value);
        TextView rateValue = view.findViewById(R.id.textView_currency_rate_value);

        titleValue.setText(data.getCurrencyTitle());
        rateValue.setText(String.format(Locale.US, "%.5f", data.getRate()));

        Log.d(TAG, "CurrencyDetailFragment.populateView exiting");
    }

    private void configureButtonsListeners(final View view) {
        Log.d(TAG, "CurrencyDetailFragment.configureButtonListeners entered");

        Button searchButton = view.findViewById(R.id.button_search_exchange);
        Button saveButton = view.findViewById(R.id.button_save);
        final Button historyButton = view.findViewById(R.id.button_show_history);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=обмін валют");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "SAVE BUTTON");
                new SaveToDBAsyncTask(database, currencyDTO).execute();
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "HISTORY BUTTON");
                Fragment historyFragment = new HistoryFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("currency_code", currencyDTO.r030);
                historyFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, historyFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void nullCheck(Object obj, Class<?> context) {
        if (null == obj) {
            throw new NullPointerException("Null in " + context.getSimpleName() +"'s  method");
        }
    }

    private static class SaveToDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private AppDatabase database;
        private CurrencyDTO currencyDTO;

        public SaveToDBAsyncTask(AppDatabase database, CurrencyDTO dto) {
            this.database = database;
            this.currencyDTO = dto;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "Persisting history to DB");
            HistoryDAO historyDAO = database.historyDAO();
            historyDAO.insert(
                    new History(this.currencyDTO.r030, this.currencyDTO.rate,
                            this.currencyDTO.date)
            );
            return null;
        }
    }
}
