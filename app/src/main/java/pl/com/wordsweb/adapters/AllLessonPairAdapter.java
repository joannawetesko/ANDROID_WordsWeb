package pl.com.wordsweb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl.com.wordsweb.R;
import pl.com.wordsweb.entities.Pair;

/**
 * Created by jwetesko on 03.05.16.
 */
public class AllLessonPairAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Pair> pairsList;

    public AllLessonPairAdapter(Context context, ArrayList<Pair> pairsList) {
        this.context = context;
        this.pairsList = pairsList;
    }

    @Override
    public int getCount() {
        return this.pairsList.size();
    }

    @Override
    public Pair getItem(int position) {
        return this.pairsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View pairListRow;

        if (convertView == null) {
            pairListRow = LayoutInflater.from(context).inflate(R.layout.pair_list_element, parent, false);
        } else {
            pairListRow = convertView;
        }

        bindListsToView(getItem(position), pairListRow);

        return pairListRow;
    }

    private void bindListsToView(Pair pair, View lessonListRow) {
        TextView phrase1 = (TextView) lessonListRow.findViewById(R.id.phrase_list_element_phrase_1);
        phrase1.setText(pair.getPhraseUseOne().getPhrase().getContent());

        TextView phrase2 = (TextView) lessonListRow.findViewById(R.id.phrase_list_element_phrase_2);
        phrase2.setText(pair.getPhraseUseTwo().getPhrase().getContent());
    }

    public void setPairsList(ArrayList<Pair> pairsList) {
        this.pairsList = pairsList;

    }
}
