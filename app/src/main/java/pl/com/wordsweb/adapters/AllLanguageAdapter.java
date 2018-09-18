package pl.com.wordsweb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl.com.wordsweb.R;
import pl.com.wordsweb.entities.Language;

/**
 * Created by wewe on 07.05.16.
 */
public class AllLanguageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Language> languages;

    public AllLanguageAdapter(Context context,ArrayList<Language> languages){
       this.languages = languages;
        this.context = context;

    }


    @Override
    public int getCount() {
        return this.languages.size();
    }

    @Override
    public Language getItem(int position) {
        return this.languages.get(position);
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
        language_first.setText(getItem(position).getName());
        //Log.d("SPINNER", getItem(position).getName());


        return spinnerView;
    }
}
