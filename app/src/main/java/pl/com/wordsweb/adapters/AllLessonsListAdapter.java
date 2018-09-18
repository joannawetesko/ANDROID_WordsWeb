package pl.com.wordsweb.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import pl.com.wordsweb.R;
import pl.com.wordsweb.config.AppSettings;
import pl.com.wordsweb.entities.LessonList;
import pl.com.wordsweb.event.OnCommentClickButtonEvent;

import static pl.com.wordsweb.config.AppSettings.bus;
import static pl.com.wordsweb.config.Constants.BASE_PHOTO_URL;
import static pl.com.wordsweb.config.Constants.PHOTO_FILE_EXTENSION;

/**
 * Created by jwetesko on 12.04.16.
 */
public class AllLessonsListAdapter extends BaseAdapter {
    private Context context;
    private List<LessonList> lessonsList = new ArrayList<>();

    public AllLessonsListAdapter(Context context) {
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

        if (convertView == null) {
            lessonListRow = LayoutInflater.from(context).inflate(R.layout.lesson_list_element, parent, false);
        } else {
            lessonListRow = convertView;
        }

        bindListsToView(getItem(position), lessonListRow);
        loadImage(getItem(position).getFirstLanguage().getFlag(), lessonListRow, R.id.firstLanguageImageView);
        loadImage(getItem(position).getSecondLanguage().getFlag(), lessonListRow, R.id.secondLanguageImageView);

        return lessonListRow;
    }

    private void bindListsToView(final LessonList lessonList, View lessonListRow) {
        TextView lessonListLabel = (TextView) lessonListRow.findViewById(R.id.lesson_list_element_name);
        lessonListLabel.setText(lessonList.getName());
        ImageView commentImageView = (ImageView) lessonListRow.findViewById(R.id.comment_button);
        commentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bus.post(new OnCommentClickButtonEvent(lessonList));
            }
        });
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
