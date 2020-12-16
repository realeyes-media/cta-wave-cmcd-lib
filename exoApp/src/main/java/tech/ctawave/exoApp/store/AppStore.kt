package tech.ctawave.exoApp.store

import org.reduxkotlin.createThreadSafeStore
import tech.ctawave.exoApp.store.reducers.rootReducer
import tech.ctawave.exoApp.store.state.createInitialState

val store = createThreadSafeStore(::rootReducer, createInitialState())
