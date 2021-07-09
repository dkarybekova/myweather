package kg.tutorialapp.myweather.network

import kg.tutorialapp.myweather.models.Post
import retrofit2.Call
import retrofit2.http.*

interface PostsApi {

    @GET("posts/{id}")
    fun fetchPostById(
        @Path("id") id: Int
    ): Call<Post>

    @POST("posts")
    fun createPost(
        @Body post: Post
    ): Call<Post>

    @POST("posts")
    @FormUrlEncoded
    fun createPostUsingFields(
        @Field("userId") userId: String,
        @Field("title") title: String,
        @Field("body") body: String
    ): Call<Post>

    @POST("posts")
    @FormUrlEncoded
    fun createPostUsingFieldsMap(
        @FieldMap map: HashMap<String, String>
    ): Call<Post>

    @PUT("posts/{id}")
    fun putPost(
        @Path("id") id: String,
        @Body post: Post
    ): Call<Post>

    @PATCH("posts/{id}")
    fun patchPost(
        @Path("id") id: String,
        @Body post: Post
    ): Call<Post>

    @DELETE("posts/{id}")
    fun deletePost(@Path ("id") id: String): Call<Unit>
}