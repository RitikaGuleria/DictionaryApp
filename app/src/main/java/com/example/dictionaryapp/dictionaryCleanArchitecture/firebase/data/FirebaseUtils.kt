package com.example.dictionaryapp.dictionaryCleanArchitecture.firebase.data

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

//We will use this func to get the result from the task without using callbacks
suspend fun <T> Task<T>.await() : T{
    return suspendCancellableCoroutine { cont->
        addOnCompleteListener{
            if(it.exception!=null){
                cont.resumeWithException(it.exception!!)
            }
            else{
                cont.resume(it.result,null)
            }
        }
    }
}