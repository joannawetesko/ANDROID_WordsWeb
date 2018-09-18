package pl.com.wordsweb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl.com.wordsweb.R;
import pl.com.wordsweb.entities.PhraseUse;

/**
 * Created by jwetesko on 13.06.16.
 */
public class AutocompletePhraseUsesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PhraseUse> phraseUses;

    public AutocompletePhraseUsesAdapter(Context context,ArrayList<PhraseUse> phraseUses){
        this.phraseUses = phraseUses;
        this.context = context;
    }

    public void clear() {
        this.phraseUses.clear();
    }
    
    @Override
    public int getCount() {
        return this.phraseUses.size();
    }

    @Override
    public PhraseUse getItem(int position) {
        return this.phraseUses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View spinnerView;
        if(convertView == null){
            spinnerView = LayoutInflater.from(context).inflate(R.layout.add_lessonlist_spinner_element, null);
        }
        else{
            spinnerView = convertView;
        }

        TextView language_first = (TextView) spinnerView.findViewById(R.id.spinnerValue);
        language_first.setText(getItem(position).getExamples().get(0).getContent());

        return spinnerView;
    }
}