package pl.com.wordsweb.api;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import pl.com.wordsweb.entities.Language;
import pl.com.wordsweb.entities.LessonList;
import pl.com.wordsweb.entities.Pair;
import pl.com.wordsweb.entities.Phrase;
import pl.com.wordsweb.entities.PhraseUse;
import pl.com.wordsweb.entities.Token;
import pl.com.wordsweb.entities.User;
import pl.com.wordsweb.entities.comments.Comment;
import pl.com.wordsweb.entities.comments.CommentObject;
import pl.com.wordsweb.entities.comments.CommentSend;
import pl.com.wordsweb.entities.comments.GetCheckListResponse;
import pl.com.wordsweb.entities.comments.GetCommentResponse;
import pl.com.wordsweb.entities.quizzes.OpenQuizzesList;
import pl.com.wordsweb.entities.quizzes.Quiz;
import pl.com.wordsweb.entities.quizzes.QuizAnswer;
import pl.com.wordsweb.entities.quizzes.QuizToLearn;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by wewe on 11.04.16.
 */

public class RestApi{
    public interface PhraseApi {

        @GET("lists")
        Call<ArrayList<LessonList>> getMyLessonLists(@Header("Authorization") String oauth);

        @GET("lists")
        Call<ArrayList<LessonList>> getAllLessonLists(@Query("all") String all, @Header("Authorization") String oauth);

        @POST("lists")
        Call<LessonList> createLessonList(@Body LessonList lessonlist, @Header("Authorization") String oauth);

        @GET("lists/{id}")
        Call<LessonList> getCurrentLessonList(@Path("id") int lessonListId, @Header("Authorization") String oauth);

        @DELETE("lists/{id}")
        Call<ResponseBody> deleteLists(@Path("id") int lessonListId, @Header("Authorization") String oauth);

        @GET("languages")
        Call<ArrayList<Language>> getLanguages(@Header("Authorization") String oauth);

        @GET("phrases")
        Call<ArrayList<Phrase>> getPhrases(@Header("Authorization") String oauth);

        @GET("phrases")
        Call<ArrayList<Phrase>> getPhrases(@Query("phrase") String phrase, @Query("languageId") int languageId, @Header("Authorization") String oauth);

        @GET("phraseuses")
        Call<ArrayList<PhraseUse>> getPhraseUses(@Query("phraseId") int phraseId, @Query("languageId") int languageId, @Header("Authorization") String oauth);

        @Headers("Content-Type: application/json")
        @POST("lists/{id}/pairs")
        Call<Pair> createPair(@Path("id") int lessonListId, @Body Pair pair, @Header("Authorization") String oauth);
    }

    public interface UserApi {

        @FormUrlEncoded
        @POST("oauth/token")
        Call<Token> generateAccesTokenLogin(@Field("username") String username,
                                            @Field("password") String password,
                                            @Field("grant_type") String grantType,
                                            @Header("Authorization") String basic
        );

        @FormUrlEncoded
        @POST("oauth/token")
        Call<Token> generateAccesTokenRegister(@Header("Authorization") String basic, @Field("grant_type") String grantType);

        @FormUrlEncoded
        @POST("oauth/token")
        Call<Token> getRefreshToken(@Header("Authorization") String basic, @Field("refresh_token") String refresh_token, @Field("grant_type") String grantType);


        @POST("rest/users")
        Call<ResponseBody> registerUser(@Body User user,
                                        @Header("Authorization") String auth);


    }

    public interface LearnApi {

        @Headers("Content-Type: application/json")
        @POST("https://flipgraphedge-dziadzior.rhcloud.com/rest/quizzes")
        Call<QuizToLearn> createQuiz(@Body Quiz quiz, @Header("Authorization") String oauth);

        @GET("https://flipgraphedge-dziadzior.rhcloud.com/rest/quizzes")
        Call<OpenQuizzesList> getQuizzes(@Header("Authorization") String oauth);

        @GET("https://flipgraphedge-dziadzior.rhcloud.com/rest/quizzes/{id}")
        Call<QuizToLearn> getQuiz(@Path("id") String quizId, @Header("Authorization") String oauth);

        @POST("https://flipgraphedge-dziadzior.rhcloud.com/rest/quizzes/{id}/answers")
        Call<ResponseBody> sendAnswer(@Path("id") String quizId, @Body QuizAnswer quizAnswer, @Header("Authorization") String oauth);


        @GET("https://phraseflipbackend.herokuapp.com/listlearnings")
        Call<GetCheckListResponse> checkUserLists(@Query("page") int page, @Query("size") int size, @Header("Authorization") String oauth);
    }

    public interface OpinionApi {
        @POST("https://opinionedge.herokuapp.com/comments")
        Call<Comment> sendComment(@Body CommentSend commentSend, @Header("Authorization") String oauth);

        @GET("https://opinionedge.herokuapp.com/comments")
        Call<GetCommentResponse> getComments(@Query("page") int page,
                                             @Query("size") int size,
                                             @Query("elementType") String elementType,
                                             @Query("elementId") String elementId,
                                             @Header("Authorization") String oauth);

        @PUT("https://opinionedge.herokuapp.com/comments/{id}")
        Call<CommentObject> updateComment(@Path("id") String idComment, @Header("Authorization") String oauth);

        @DELETE("https://opinionedge.herokuapp.com/comments/{id}")
        Call<ResponseBody> deleteComment(@Path("id") String idComment, @Header("Authorization") String oauth);
    }



    }



