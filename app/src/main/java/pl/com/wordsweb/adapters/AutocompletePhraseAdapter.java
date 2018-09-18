package pl.com.wordsweb.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import pl.com.wordsweb.R;
import pl.com.wordsweb.config.AppSettings;
import pl.com.wordsweb.entities.Phrase;
import pl.com.wordsweb.entities.Token;
import pl.com.wordsweb.storage.SharedPreferencesProvider;
import retrofit2.Call;

/**
 * Created by jwetesko on 13.06.16.
 */
public class AutocompletePhraseAdapter extends BaseAdapter implements Filterable {

    private static final String TAG = "AutocomplPhraseAdapter";

    private ArrayList<Phrase> currentPhrases;
    private int languageId;
    private Context context;

    public AutocompletePhraseAdapter(Context context, int languageId) {
        this.context = context;
        this.languageId = languageId;
        currentPhrases = new ArrayList<Phrase>();
    }

    @Override
    public int getCount() {
        return currentPhrases.size();
    }

    @Override
    public Phrase getItem(int position) {
        return currentPhrases.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.autocomplete_add_new_phrase, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.add_new_phrase)).setText(getItem(position).getContent());
        return convertView;
    }


    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults filterResults = new FilterResults();
                SharedPreferencesProvider sharedPreferencesProvider = new SharedPreferencesProvider(context);
                if (sharedPreferencesProvider.checkToken()) {
                    Log.d("GET LIST", "Token exists");
                    final Token token = sharedPreferencesProvider.getAcessToken();
                    if (constraint != null) {
                        Call<ArrayList<Phrase>> call;
                        call = AppSettings.phraseApi.getPhrases(constraint.toString().toLowerCase(), languageId, token.getToken());
                        try {
                            ArrayList<Phrase> tempCurrentPhrases = call.execute().body();
                            filterResults.values = tempCurrentPhrases;
                            filterResults.count = tempCurrentPhrases.size();
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results != null && results.count > 0) {
                    currentPhrases = (ArrayList<Phrase>) results.values;
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }
}
