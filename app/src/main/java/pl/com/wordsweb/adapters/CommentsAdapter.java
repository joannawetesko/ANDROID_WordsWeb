package pl.com.wordsweb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.com.wordsweb.R;
import pl.com.wordsweb.entities.comments.CommentObject;
import pl.com.wordsweb.event.OnClickReplayCommentEvent;

import static android.view.View.GONE;
import static pl.com.wordsweb.config.AppSettings.bus;

/**
 * Created by jwetesko on 02.11.16.
 */

public class CommentsAdapter extends BaseAdapter {
    private Context context;
    private List<CommentObject> comments = new ArrayList<>();
    private String type;

    public CommentsAdapter(List<CommentObject> comments, Context context, String constant) {
        this.comments = comments;
        this.context = context;
        this.type = constant;
    }

    public void setLessonList(List<CommentObject> _comments) {
        comments.clear();
        comments.addAll(_comments);
    }

    @Override
    public int getCount() {
        return this.comments.size();
    }

    @Override
    public CommentObject getItem(int position) {
        return this.comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View commentRow;

        if (convertView == null) {
            commentRow = LayoutInflater.from(context).inflate(R.layout.listelement_comment, parent, false);
        } else {
            commentRow = convertView;
        }

        bindCommentsToView(getItem(position), commentRow);
        return commentRow;
    }

    private void bindCommentsToView(final CommentObject comment, View commentRow) {

        /* comment textviews */
        TextView creationDateTV = (TextView) commentRow.findViewById(R.id.comments_creation_date);
        String date = getProperDate(comment.getComment().getCreationDate());
        creationDateTV.setText(date);

        TextView authorTV = (TextView) commentRow.findViewById(R.id.comments_author);
        authorTV.setText(comment.getComment().getAuthor().getUsername());
        final TextView contentTV = (TextView) commentRow.findViewById(R.id.comments_content);
        contentTV.setText(comment.getComment().getContent());

        /* replying */
        ImageButton replyBtn = (ImageButton) commentRow.findViewById(R.id.comment_reply_button);
        if (type.equals("answers")) {
            replyBtn.setVisibility(GONE);
        }
        else {
            replyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bus.post(new OnClickReplayCommentEvent(comment.getComment()));
                }
            });
        }
    }

    private String getProperDate(long milisec) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milisec);

        String hour = buildHour(calendar.get(Calendar.AM_PM), calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
        String date = buildDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, Calendar.DAY_OF_MONTH);

        return hour + "  " + date;
    }

    private String buildHour(int am_pm, int hour, int minute) {

        if (am_pm == 1) {
            hour = hour + 12;
        }

        if (hour < 10) {
            if (minute < 10) {
                return "0" + hour + ":0" + minute;
            }
            else return "0" + hour + ":" + minute;
        }
        else {
            if (minute < 10) {
                return hour + ":0" + minute;
            }
            else return hour + ":" + minute;
        }
    }

    private String buildDate(int year, int month, int day){
        if (month < 10) {
            if (day < 10) {
                return "0" + day + ".0" + month + "." + year;
            }
            else return day + ".0" + month + "." + year;
        }
        else {
            if (day < 10) {
                return "0" + day + "." + month + "." + year;
            }
            else return day + "." + month + "." + year;
        }
    }
}
