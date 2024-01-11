package com.example.datebook.domain

interface BaseMapper<From, To> {
    fun map(from: From): To
}
