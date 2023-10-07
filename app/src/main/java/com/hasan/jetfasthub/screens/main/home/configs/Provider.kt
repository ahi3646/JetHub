package com.hasan.jetfasthub.screens.main.home.configs

/**
 * Provider for lazy initialization
 *
 * @param action initialization action
 *
 */
class Provider<T>(action: () -> T) : () -> T by action