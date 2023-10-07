package com.hasan.jetfasthub.screens.main.home.configs.converters

interface Converter<I : Any, O : Any?> {
    fun convert(value: I): O
}