package com.mostafatamer.tasktogether.domain.model;


data class Response<T>(
    val message: String,
    val data: T
)
