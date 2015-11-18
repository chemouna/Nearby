package com.mounacheikhna.snipschallenge.ui.base

import javax.inject.Scope
import kotlin.reflect.KClass

@Scope
annotation class ScopeSingleton(val value: KClass<*>)
