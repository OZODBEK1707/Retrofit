package com.rosh.restapi.retrofit

import com.rosh.restapi.models.MyTodo
import com.rosh.restapi.models.MyTodoRequest
import retrofit2.Call

import retrofit2.http.*

interface MyRetrofitService {
    @GET("plan")
    fun getAllTodo(): Call<List<MyTodo>>

    @POST("plan/")
    fun addToDo(@Body myTodoRequest: MyTodoRequest):Call<MyTodo>

    @DELETE("plan/{id}/")
    fun deleteTodo(@Path("id") id:Int):Call<Int>

    @PATCH("plan/{id}/")
    fun updateTodo(@Path("id") id:Int, @Body myTodoRequest: MyTodoRequest): Call<MyTodo>
}