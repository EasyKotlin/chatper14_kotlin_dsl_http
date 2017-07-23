package com.kotlin.easy

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * Created by jack on 2017/7/23.
 */

fun ajax(init: HttpRequestWrapper.() -> Unit) {
    val wrap = HttpRequestWrapper()
    wrap.init()
    doCall(wrap)
}

/**
 * 执行请求，并把返回通过 RxKotlin 的接口赋值给
 */
private fun doCall(wrap: HttpRequestWrapper) {
    // 数据发射源
    val sender = Observable.create<Response>({
        e ->
        e.onNext(call(wrap))
    })

    // 数据接收源
    val receiver: Observer<Response> = object : Observer<Response> {
        override fun onNext(resp: Response) {
            wrap.success(resp.body()!!.string())
        }

        override fun onError(e: Throwable) {
            wrap.fail(e)
        }

        override fun onSubscribe(d: Disposable) {
        }

        override fun onComplete() {
        }

    }
    // 将发射源和接收源关联： “异步”、“观察者模式”
    sender.subscribe(receiver)
}


private fun call(wrap: HttpRequestWrapper): Response {

    var req: Request? = null
    when (wrap.method?.toLowerCase()) {

        "get" -> req = Request.Builder().url(wrap.url).build()
        "post" -> req = Request.Builder().url(wrap.url).post(wrap.body).build()
        "put" -> req = Request.Builder().url(wrap.url).put(wrap.body).build()
        "delete" -> req = Request.Builder().url(wrap.url).delete(wrap.body).build()
    }

    val http = OkHttpClient.Builder().connectTimeout(wrap.timeout, TimeUnit.SECONDS).build()
    val resp = http.newCall(req).execute()
    return resp
}

class HttpRequestWrapper {

    var url: String? = null

    var method: String? = null

    var body: RequestBody? = null

    var timeout: Long = 10

    internal var success: (String) -> Unit = {}
    internal var fail: (Throwable) -> Unit = {}

    fun success(onSuccess: (String) -> Unit) {
        success = onSuccess
    }

    fun error(onError: (Throwable) -> Unit) {
        fail = onError
    }
}