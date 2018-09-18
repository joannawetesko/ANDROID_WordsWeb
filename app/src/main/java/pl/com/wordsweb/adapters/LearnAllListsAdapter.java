package pl.com.wordsweb.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import pl.com.wordsweb.R;
import pl.com.wordsweb.config.AppSettings;
import pl.com.wordsweb.entities.LessonList;

import static android.view.View.GONE;
import static pl.com.wordsweb.config.Constants.BASE_PHOTO_URL;
import static pl.com.wordsweb.config.Constants.PHOTO_FILE_EXTENSION;

/**
 * Created by jwetesko on 02.11.16.
 */

public class LearnAllListsAdapter extends BaseAdapter {
    private Context context;
    private List<LessonList> lessonsList = new ArrayList<>();

    public LearnAllListsAdapter(Context context) {
        this.context = context;
    }

    public void setLessonList(List<LessonList> _lessonsList) {
        lessonsList.clear();
        lessonsList.addAll(_lessonsList);
    }

    @Override
    public int getCount() {
        return this.lessonsList.size();
    }

    @Override
    public LessonList getItem(int position) {
        return this.lessonsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View lessonListRow;

        lessonListRow = LayoutInflater.from(context).inflate(R.layout.listelement_learn_choose_list, parent, false);

        bindListsToView(getItem(position), lessonListRow);

            TextView listTitle = (TextView) lessonListRow.findViewById(R.id.lesson_list_element_name);
            Typeface roboto = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
            listTitle.setTypeface(roboto);

            loadImage(getItem(position).getFirstLanguage().getFlag(), lessonListRow, R.id.firstLanguageImageView);
            loadImage(getItem(position).getSecondLanguage().getFlag(), lessonListRow, R.id.secondLanguageImageView);
        return lessonListRow;
    }

    private void bindListsToView(LessonList lessonList, View lessonListRow) {
        TextView lessonListLabel = (TextView) lessonListRow.findViewById(R.id.lesson_list_element_name);
        lessonListLabel.setText(lessonList.getName());

        CheckBox lessonListChB = (CheckBox) lessonListRow.findViewById(R.id.learn_lists_listelement_checkbox);
        if (lessonList.getSize() == 0) {
            lessonListChB.setVisibility(GONE);
        }
    }

    private void loadImage(String flagLink, View lessonListRow, int viewId) {

        final ImageView imageView = (ImageView) lessonListRow.findViewById(viewId);
        imageView.setImageBitmap(null);
        if (flagLink != null) {
            Uri uri = Uri.parse(BASE_PHOTO_URL + flagLink + PHOTO_FILE_EXTENSION);
            AppSettings.requestBuilder
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    // SVG cannot be serialized so it's not worth to cache it
                    .load(uri)
                    .into(imageView);

        }
    }
}
