package com.hasan.jetfasthub.screens.main.home.presentation.state.converters

interface Converter<I : Any, O : Any?> {
    fun convert(value: I): O
}