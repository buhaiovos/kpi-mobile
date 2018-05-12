package com.osb.mobiledev.kpi.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.osb.mobiledev.kpi.R;
import com.osb.mobiledev.kpi.model.Currency;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class CurrencyListAdapter extends ArrayAdapter<Currency> {
    private Activity activity;
    private List<Currency> currencies;
    private static LayoutInflater inflater = null;

    public CurrencyListAdapter(Activity activity, List<Currency> currencies) {
        super(activity, 0, currencies);

        this.activity = activity;
        this.currencies = currencies;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return currencies.size();
    }

    @Nullable
    @Override
    public Currency getItem(int position) {
        return currencies.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Currency currency = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.currency_list_row, parent, false);
        }
        TextView shortTitleView = convertView.findViewById(R.id.textview_list_row_shortTitle);
        TextView rateView = convertView.findViewById(R.id.textview_list_row_rate);
        TextView fullTitleView = convertView.findViewById(R.id.textview_list_row_fullTitle);
        TextView codeView = convertView.findViewById(R.id.textview_list_row_code);
        TextView dateView = convertView.findViewById(R.id.textview_list_row_date);

        shortTitleView.setText(currency.getCc());
        NumberFormat decimalFormat = new DecimalFormat("#0.00000");
        rateView.setText(decimalFormat.format(currency.getRate()));
        fullTitleView.setText(currency.getTxt());
        codeView.setText(currency.getR030().toString());
        dateView.setText(currency.getExchangeDate());

        return convertView;
    }
}
