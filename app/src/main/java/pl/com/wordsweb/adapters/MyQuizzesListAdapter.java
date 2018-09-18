package pl.com.wordsweb.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pl.com.wordsweb.R;
import pl.com.wordsweb.entities.quizzes.QuizToLearn;
import pl.com.wordsweb.entities.quizzes.VocabularyList;
import pl.com.wordsweb.ui.utils.OnQuizClickListener;

/**
 * Created by wewe on 23.12.16.
 */

public class MyQuizzesListAdapter extends RecyclerView.Adapter<MyQuizzesListAdapter.ViewHolder> {
    private final OnQuizClickListener listener;
    View rootView;
    private Context context;
    private ArrayList<QuizToLearn> quizzes;

    public MyQuizzesListAdapter(Context context, ArrayList<QuizToLearn> quizzes, OnQuizClickListener listener) {
        this.context = context;
        this.quizzes = quizzes;
        this.listener = listener;
    }


    public Context getContext() {
        return context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        rootView = inflater.inflate(R.layout.item_quiz, parent, false);

        ViewHolder viewHolder = new ViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final QuizToLearn quizToLearn = quizzes.get(position);
        TextView tvCorrectAnswerCount = holder.tvCorrectAnswerCount;
        TextView tvIncorrectAnswerCount = holder.tvIncorrectAnswerCount;
        TextView tvStartDate = holder.tvStartDate;
        TextView tvEndDate = holder.tvEndDate;
        ImageView ivIcon = holder.ivIcon;
        LinearLayout llListTitleContainer = holder.llListTitleContainer;

        chooseIcon(quizToLearn, ivIcon);
        tvCorrectAnswerCount.setText(String.valueOf(quizToLearn.getResult().getCorrect()));
        tvIncorrectAnswerCount.setText(String.valueOf(quizToLearn.getResult().getIncorrect()));
        ArrayList<VocabularyList> listsToLearn = quizToLearn.getVocabularyLists();
        llListTitleContainer.removeAllViews();
        tvStartDate.setText(parseDate(quizToLearn.getStartDate()));
        if (quizToLearn.getQuizBasicDto().isFinished()) {
            tvEndDate.setVisibility(View.VISIBLE);
            tvEndDate.setText(parseDate(quizToLearn.getEndDate()));
        } else {
            tvEndDate.setVisibility(View.GONE);
        }
        if (listsToLearn != null) {

            for (VocabularyList list : listsToLearn) {

                TextView tvList = new TextView(context);
                tvList.setText(list.getName());
                tvList.setTypeface(tvList.getTypeface(), Typeface.BOLD);
                tvList.setGravity(Gravity.CENTER_HORIZONTAL);
                llListTitleContainer.addView(tvList);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onQuizClick(quizToLearn);
            }
        });


    }

    @Override
    public int getItemCount() {
        return quizzes.size();
    }


    private void chooseIcon(QuizToLearn quizToLearn, ImageView imageView) {
        if (!quizToLearn.getQuizBasicDto().isFinished()) {
            imageView.setImageResource(R.drawable.ic_fast_forward_black_48px);
            imageView.setBackgroundColor(this.context.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            imageView.setImageResource(R.drawable.ic_done_24dp);
            imageView.setBackgroundColor(this.context.getResources().getColor(R.color.colorAccent));
        }
    }

    private String parseDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(new Date(timestamp));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCorrectAnswerCount;
        TextView tvIncorrectAnswerCount;
        TextView tvStartDate;
        TextView tvEndDate;
        ImageView ivIcon;
        LinearLayout llListTitleContainer;


        public ViewHolder(View itemView) {
            super(itemView);

            llListTitleContainer = (LinearLayout) itemView.findViewById(R.id.ll_title_container);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvCorrectAnswerCount = (TextView) itemView.findViewById(R.id.correct_answers_count);
            tvIncorrectAnswerCount = (TextView) itemView.findViewById(R.id.irrcorrect_answers_count);
            tvStartDate = (TextView) itemView.findViewById(R.id.tv_start_date);
            tvEndDate = (TextView) itemView.findViewById(R.id.tv_end_date);
        }
    }
}
