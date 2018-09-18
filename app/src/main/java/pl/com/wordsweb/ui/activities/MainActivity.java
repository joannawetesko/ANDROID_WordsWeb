package pl.com.wordsweb.ui.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import pl.com.wordsweb.R;
import pl.com.wordsweb.api.ErrorMessageParser;
import pl.com.wordsweb.config.AppSettings;
import pl.com.wordsweb.entities.Language;
import pl.com.wordsweb.entities.LessonList;
import pl.com.wordsweb.entities.Pair;
import pl.com.wordsweb.entities.Token;
import pl.com.wordsweb.entities.comments.Comment;
import pl.com.wordsweb.entities.comments.CommentSend;
import pl.com.wordsweb.entities.comments.GetCheckListResponse;
import pl.com.wordsweb.entities.comments.GetCommentResponse;
import pl.com.wordsweb.entities.quizzes.OpenQuizzesList;
import pl.com.wordsweb.entities.quizzes.Quiz;
import pl.com.wordsweb.entities.quizzes.QuizAnswer;
import pl.com.wordsweb.entities.quizzes.QuizToLearn;
import pl.com.wordsweb.event.OnCheckUserEvent;
import pl.com.wordsweb.event.OnMustLogoutEvent;
import pl.com.wordsweb.event.OnRestCommentGetEvent;
import pl.com.wordsweb.event.OnRestCommentSendEvent;
import pl.com.wordsweb.event.OnRestCurrentLessonListGetEvent;
import pl.com.wordsweb.event.OnRestLessonListGetEvent;
import pl.com.wordsweb.event.OnRestListQuizzesToLearnGetEvent;
import pl.com.wordsweb.event.OnRestNewLessonListSendEvent;
import pl.com.wordsweb.event.OnRestPairPostEvent;
import pl.com.wordsweb.event.OnRestQuizAnswerSendEvent;
import pl.com.wordsweb.event.OnRestQuizToLearnGetEvent;
import pl.com.wordsweb.storage.DbProvider;
import pl.com.wordsweb.storage.SharedPreferencesProvider;
import pl.com.wordsweb.ui.fragments.MainPageFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.com.wordsweb.config.AppSettings.bus;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.loading_progressBar);
        sharedPreferencesProvider = new SharedPreferencesProvider(this);
        getLanguages();
        openFragment(new MainPageFragment());
        rootView = findViewById(R.id.container);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferencesProvider == null) {
            sharedPreferencesProvider = new SharedPreferencesProvider(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
    //end

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMustLogoutEvent(OnMustLogoutEvent event) {
        logout();
    }

    // API
    public void getLanguages() {
        final ArrayList<Language> allLanguages = new ArrayList<>();
        if (sharedPreferencesProvider.checkToken()) {
            Log.d("GET LIST", "Token exists");
            Token token = sharedPreferencesProvider.getAcessToken();
            Log.d("TOKEN: ", token.getToken_type() + " " + token.getAccess_token());
            Call<ArrayList<Language>> call = AppSettings.phraseApi.getLanguages(token.getToken());
            Log.d("BODY", call.toString());
            call.enqueue(new Callback<ArrayList<Language>>() {

                @Override
                public void onResponse(Call<ArrayList<Language>> call, Response<ArrayList<Language>> response) {
                    Log.d("CallBack", " code is " + response.code());
                    if (response.isSuccessful()) {
                        Log.d("CallBack", " response is " + new Gson().toJson(response.body()));
                        allLanguages.addAll(response.body());
                        DbProvider.addLanguage(getBaseContext(), allLanguages);
                        //   bus.post(new OnRestLanguageListGetEvent(allLanguages));
                    } else {
                        Log.d("CallBack", " response is " + response.code());
                        showSnackbar(ErrorMessageParser.getErrorMessage(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Language>> call, Throwable t) {
                    Log.d("CallBack", " Throwable is " + t);
                }
            });
        } else {
            Log.d("GET LIST", "Token NOT exists");
        }
    }

    public void getLessonLists(final boolean progressBarVisibility) {
        final ArrayList<LessonList> allLessonsLists = new ArrayList<>();
        if (sharedPreferencesProvider.checkToken()) {
            if (progressBarVisibility) progressBar.setVisibility(View.VISIBLE);
            Log.d("GET LIST", "Token exists");
            final Token token = sharedPreferencesProvider.getAcessToken();
            Log.d("TOKEN: ", token.getToken_type() + " " + token.getAccess_token());
            Log.d("TOKEN REFRESH: ", token.getRefresh_token());
            Call<ArrayList<LessonList>> call = AppSettings.phraseApi.getAllLessonLists("true", token.getToken());
            Log.d("BODY", call.toString());
            call.enqueue(new Callback<ArrayList<LessonList>>() {
                @Override
                public void onResponse(Call<ArrayList<LessonList>> call, Response<ArrayList<LessonList>> response) {
                    Log.d("CallBack", " code is " + response.code());
                    if (response.isSuccessful()) {
                        Log.d("CallBack", " response is " + new Gson().toJson(response.body()));
                        allLessonsLists.addAll(response.body());
                        bus.post(new OnRestLessonListGetEvent(allLessonsLists));
                        if (progressBarVisibility) progressBar.setVisibility(View.GONE);
                    } else {
                        if (progressBarVisibility) progressBar.setVisibility(View.GONE);
                        Log.d("CallBack", " response is " + response.code());
                        showSnackbar(ErrorMessageParser.getErrorMessage(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<LessonList>> call, Throwable t) {
                    Log.d("CallBack", " Throwable is " + t);
                    Log.d("CallBack", " Throwable is " + t.getMessage());
                    Log.d("CallBack", " Throwable is " + t.getLocalizedMessage());
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Log.d("GET LIST", "Token NOT exists");
        }
    }

    public void postPair(int lessonListId, final Pair currentPair) {
        if (sharedPreferencesProvider.checkToken()) {
            Log.d("POST LIST", "Token exists");
            Token token = sharedPreferencesProvider.getAcessToken();
            Log.d("TOKEN: ", token.getToken_type() + " " + token.getAccess_token());
            Call<Pair> call = AppSettings.phraseApi.createPair(lessonListId, currentPair, token.getToken());
            Log.d("BODY", call.toString());
            call.enqueue(new Callback<Pair>() {
                             @Override
                             public void onResponse(Call<Pair> call, Response<Pair> response) {
                                 Log.d("CallBack", " code is " + response.code());
                                 if (response.isSuccessful()) {
                                     bus.post(new OnRestPairPostEvent());
                                     Log.d("CallBack", " response is " + new Gson().toJson(response.body()));
                                 } else {
                                     Log.d("CallBack", " response is " + response.code());
                                     showSnackbar(ErrorMessageParser.getErrorMessage(response.code()));
                                 }
                             }

                             @Override
                             public void onFailure(Call<Pair> call, Throwable t) {
                                 Log.d("CallBack", " Throwable is " + t);
                             }
                         }
            );
        } else {
            Log.d("POST LIST", "Token NOT exists");
        }
    }

    public void getMyLessonLists(final boolean progressBarVisibility) {
        final ArrayList<LessonList> myLessonsLists = new ArrayList<>();
        if (sharedPreferencesProvider.checkToken()) {
            if (progressBarVisibility) progressBar.setVisibility(View.VISIBLE);
            Log.d("GET LIST", "Token exists");
            final Token token = sharedPreferencesProvider.getAcessToken();
            Log.d("TOKEN: ", token.getToken_type() + " " + token.getAccess_token());
            Log.d("TOKEN REFRESH: ", token.getRefresh_token());
            Call<ArrayList<LessonList>> call = AppSettings.phraseApi.getMyLessonLists(token.getToken());
            Log.d("BODY", call.toString());
            call.enqueue(new Callback<ArrayList<LessonList>>() {
                @Override
                public void onResponse(Call<ArrayList<LessonList>> call, Response<ArrayList<LessonList>> response) {
                    Log.d("CallBack", " code is " + response.code());
                    if (response.isSuccessful()) {
                        Log.d("CallBack", " response is " + new Gson().toJson(response.body()));
                        myLessonsLists.addAll(response.body());
                        bus.post(new OnRestLessonListGetEvent(myLessonsLists));
                        if (progressBarVisibility) progressBar.setVisibility(View.GONE);
                    } else {
                        if (progressBarVisibility) progressBar.setVisibility(View.GONE);
                        Log.d("CallBack", " response is " + response.code());
                        showSnackbar(ErrorMessageParser.getErrorMessage(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<LessonList>> call, Throwable t) {
                    Log.d("CallBack", " Throwable is " + t);
                    Log.d("CallBack", " Throwable is " + t.getMessage());
                    Log.d("CallBack", " Throwable is " + t.getLocalizedMessage());
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Log.d("GET LIST", "Token NOT exists");
        }
    }

    public void getCurrentLessonList(final boolean progressBarVisibility, int id) {
        if (progressBarVisibility) progressBar.setVisibility(View.VISIBLE);
        if (sharedPreferencesProvider.checkToken()) {
            Log.d("GET LIST", "Token exists");
            Token token = sharedPreferencesProvider.getAcessToken();
            Log.d("TOKEN: ", token.getToken_type() + " " + token.getAccess_token());
            Call<LessonList> call = AppSettings.phraseApi.getCurrentLessonList(id, token.getToken());
            Log.d("BODY", call.toString());
            call.enqueue(new Callback<LessonList>() {

                @Override
                public void onResponse(Call<LessonList> call, Response<LessonList> response) {
                    Log.d("CallBack", " code is " + response.code());
                    if (response.isSuccessful()) {
                        Log.d("CallBack", " response is " + new Gson().toJson(response.body()));
                        bus.post(new OnRestCurrentLessonListGetEvent(response.body()));
                        if (progressBarVisibility) progressBar.setVisibility(View.GONE);
                    } else {
                        Log.d("CallBack", " response is " + response.code());
                        showSnackbar(ErrorMessageParser.getErrorMessage(response.code()));
                        if (progressBarVisibility) progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<LessonList> call, Throwable t) {
                    if (progressBarVisibility) progressBar.setVisibility(View.GONE);
                    Log.d("CallBack", " Throwable is " + t);
                    Log.d("CallBack", " Throwable is " + t.getMessage());
                    Log.d("CallBack", " Throwable is " + t.getLocalizedMessage());
                }
            });
        } else {
            Log.d("GET LIST", "Token NOT exists");
        }
    }

    public void deleteList(int selectedListId) {
        if (sharedPreferencesProvider.checkToken()) {
            Log.d("GET LIST", "Token exists");
            Token token = sharedPreferencesProvider.getAcessToken();
            Log.d("TOKEN: ", token.getToken_type() + " " + token.getAccess_token());
            Call<okhttp3.ResponseBody> call = AppSettings.phraseApi.deleteLists(selectedListId, token.getToken());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d("CallBack", " code is " + response.code());
                        getMyLessonLists(true);
                    } else {
                        showSnackbar(ErrorMessageParser.getErrorMessage(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        } else {
            Log.d("GET LIST", "Token NOT exists");
        }
    }

    public void postLessonList(final LessonList currentLessonList) {
        if (sharedPreferencesProvider.checkToken()) {
            Log.d("POST LIST", "Token exists");
            Token token = sharedPreferencesProvider.getAcessToken();
            Log.d("TOKEN: ", token.getToken_type() + " " + token.getAccess_token());
            Call<LessonList> call = AppSettings.phraseApi.createLessonList(currentLessonList, token.getToken());
            Log.d("BODY", call.toString());
            call.enqueue(new Callback<LessonList>() {
                @Override
                public void onResponse(Call<LessonList> call, Response<LessonList> response) {
                    Log.d("CallBack", " code is " + response.code());
                    if (response.isSuccessful()) {
                        Log.d("CallBack", " response is " + new Gson().toJson(response.body()));
                        bus.post(new OnRestNewLessonListSendEvent());
                    } else {
                        Log.d("CallBack", " response is " + response.code());
                        showSnackbar(ErrorMessageParser.getErrorMessage(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<LessonList> call, Throwable t) {
                    Log.d("CallBack", " Throwable is " + t);
                }
            });
        } else {
            Log.d("POST LIST", "Token NOT exists");
        }
    }

    public void postQuiz(final Quiz quiz) {
        if (sharedPreferencesProvider.checkToken()) {
            Log.d("POST Quiz", "Token exists");
            Token token = sharedPreferencesProvider.getAcessToken();
            progressBar.setVisibility(View.VISIBLE);
            Log.d("TOKEN: ", token.getToken_type() + " " + token.getAccess_token());
            Call<QuizToLearn> call = AppSettings.learnApi.createQuiz(quiz, token.getToken());
            call.enqueue(new Callback<QuizToLearn>() {
                @Override
                public void onResponse(Call<QuizToLearn> call, Response<QuizToLearn> response) {
                    Log.d("CallBack: ", " " + response.body());
                    if (response.isSuccessful()) {
                        Log.d("CallBack", " response is " + new Gson().toJson(response.body()));
                        bus.post(new OnRestQuizToLearnGetEvent(response.body()));
                    } else {
                        Log.d("CallBack", " response is " + response.code());
                        showSnackbar(ErrorMessageParser.getErrorMessage(response.code()));
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<QuizToLearn> call, Throwable t) {
                    Log.d("CallBack", " Throwable: " + t);
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Log.d("POST LIST", "Token NOT exists");
        }
    }

    public void sendQuizAnswer(QuizAnswer quizAnswer, String quizId) {
        if (sharedPreferencesProvider.checkToken()) {
            progressBar.setVisibility(View.VISIBLE);
            Log.d("SEND ANSWER", "Token exists");
            Token token = sharedPreferencesProvider.getAcessToken();
            Log.d("TOKEN: ", token.getToken_type() + " " + token.getAccess_token());
            Call<ResponseBody> call = AppSettings.learnApi.sendAnswer(quizId, quizAnswer, token.getToken());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 202) {
                            bus.post(new OnRestQuizAnswerSendEvent(true));
                        }
                    } else {
                        showSnackbar(ErrorMessageParser.getErrorMessage(response.code()));
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("CallBack", " Throwable: " + t);
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Log.d("SEND ANSWER", "Token NOT exists");
        }
    }

    public void getQuiz(String quizId) {
        if (sharedPreferencesProvider.checkToken()) {
            progressBar.setVisibility(View.VISIBLE);
            Token token = sharedPreferencesProvider.getAcessToken();
            Call<QuizToLearn> call = AppSettings.learnApi.getQuiz(quizId, token.getToken());
            call.enqueue(new Callback<QuizToLearn>() {
                @Override
                public void onResponse(Call<QuizToLearn> call, Response<QuizToLearn> response) {
                    if (response.isSuccessful()) {
                        bus.post(new OnRestQuizToLearnGetEvent(response.body()));
                    } else {
                        Log.d("CallBack", " response is " + response.code());
                        showSnackbar(ErrorMessageParser.getErrorMessage(response.code()));
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<QuizToLearn> call, Throwable t) {
                    Log.d("CallBack", " Throwable: " + t);
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Log.d("GET QUIZ", "Token NOT exists");
        }
    }

    public void getQuizzes() {
        if (sharedPreferencesProvider.checkToken()) {
            progressBar.setVisibility(View.VISIBLE);
            Token token = sharedPreferencesProvider.getAcessToken();
            Call<OpenQuizzesList> call = AppSettings.learnApi.getQuizzes(token.getToken());
            call.enqueue(new Callback<OpenQuizzesList>() {
                @Override
                public void onResponse(Call<OpenQuizzesList> call, Response<OpenQuizzesList> response) {
                    if (response.isSuccessful()) {
                        bus.post(new OnRestListQuizzesToLearnGetEvent(response.body()));
                    } else {
                        Log.d("CallBack", " response is " + response.code());
                        showSnackbar(ErrorMessageParser.getErrorMessage(response.code()));
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<OpenQuizzesList> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Log.d("GET QUIZ", "Token NOT exists");
        }
    }

    public void getComments(String expressionType, String expressionId) {
        if (sharedPreferencesProvider.checkToken()) {
            progressBar.setVisibility(View.VISIBLE);
            Token token = sharedPreferencesProvider.getAcessToken();
            Call<GetCommentResponse> call = AppSettings.opinionApi.getComments(0, 1000, expressionType, expressionId, token.getToken());
            call.enqueue(new Callback<GetCommentResponse>() {
                @Override
                public void onResponse(Call<GetCommentResponse> call, Response<GetCommentResponse> response) {
                    if (response.isSuccessful()) {
                        bus.post(new OnRestCommentGetEvent(response.body()));
                    } else {
                        Log.d("CallBack", " response is " + response.code());
                        showSnackbar(ErrorMessageParser.getErrorMessage(response.code()));
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<GetCommentResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Log.d("GET QUIZ", "Token NOT exists");
        }
    }

    public void postComment(CommentSend commentSend) {
        if (sharedPreferencesProvider.checkToken()) {
            progressBar.setVisibility(View.VISIBLE);
            Token token = sharedPreferencesProvider.getAcessToken();
            Call<Comment> call = AppSettings.opinionApi.sendComment(commentSend, token.getToken());
            call.enqueue(new Callback<Comment>() {
                @Override
                public void onResponse(Call<Comment> call, Response<Comment> response) {
                    if (response.isSuccessful()) {
                        bus.post(new OnRestCommentSendEvent());
                    } else {
                        Log.d("CallBack", " response is " + response.code());
                        showSnackbar(ErrorMessageParser.getErrorMessage(response.code()));
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<Comment> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Log.d("GET QUIZ", "Token NOT exists");
        }
    }

    public void deleteComment(String expressionId) {
        if (sharedPreferencesProvider.checkToken()) {
            progressBar.setVisibility(View.VISIBLE);
            Token token = sharedPreferencesProvider.getAcessToken();
            Call<ResponseBody> call = AppSettings.opinionApi.deleteComment(expressionId, token.getToken());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                    } else {
                        Log.d("CallBack", " response is " + response.code());
                        showSnackbar(ErrorMessageParser.getErrorMessage(response.code()));
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Log.d("GET QUIZ", "Token NOT exists");
        }
    }

    public void checkUserList() {
        if (sharedPreferencesProvider.checkToken()) {
            Token token = sharedPreferencesProvider.getAcessToken();
            Call<GetCheckListResponse> call = AppSettings.learnApi.checkUserLists(0, 1000, token.getToken());
            call.enqueue(new Callback<GetCheckListResponse>() {
                @Override
                public void onResponse(Call<GetCheckListResponse> call, Response<GetCheckListResponse> response) {
                    if (response.isSuccessful()) {
                        bus.post(new OnCheckUserEvent(response.body()));
                    } else {
                        Log.d("CallBack", " response is " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<GetCheckListResponse> call, Throwable t) {
                }
            });
        } else {
            Log.d("GET QUIZ", "Token NOT exists");
        }
    }

    public void showSnackbar(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }
}
