package com.appspace.evybook.manager;


import com.appspace.evybook.model.EvyBook;
import com.appspace.evybook.model.EvyTinkUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by siwaweswongcharoen on 6/6/2016 AD.
 */
public interface EvyTinkAPIService {

    @GET("evycheckfbloginjson.aspx")
    Call<EvyTinkUser[]> register(
            @Query("evarfid") String id,
            @Query("fname") String name,
            @Query("firebase_uid") String firebaseUid
    );

    @GET("betajsonevybook.aspx")
    Call<EvyBook[]> loadBooks(
            @Query("evarid") String userId
    );

    @FormUrlEncoded
    @POST("betajsonsaveebook_onshelf.aspx")
    Call<EvyBook[]> postBookDownloadStat(
            @Field("evyaccountid") String userId,
            @Field("evyebookId") String bookId,
            @Field("Downloadstatus") String downloadStatus
    );

    @FormUrlEncoded
    @POST("betajsondeletebookonshelf.aspx")
    Call<EvyBook[]> postDeleteBook(
            @Field("evyaccountid") String userId,
            @Field("evytinkebookshelfId") String bookId
    );
}
