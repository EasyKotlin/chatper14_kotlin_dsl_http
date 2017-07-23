package com.kotlin.easy

import com.alibaba.fastjson.JSONObject
import okhttp3.MediaType
import okhttp3.RequestBody
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by jack on 2017/7/23.
 */

@RunWith(JUnit4::class)
class KAjaxTest {

    @Test fun testHttpOnSuccess() {
        val testUrl = "https://www.baidu.com"

        ajax {
            url = testUrl
            method = "get"
            success {
                string ->
                println(string)
                Assert.assertTrue(string.contains("百度一下"))
            }
            error {
                e ->
                println(e.message)
            }
        }
    }

    @Test fun testHttpOnError() {
        val testUrl = "https://www2.baidu.com"

        ajax {
            url = testUrl
            method = "get"
            success {
                string ->
                println(string)
            }
            error {
                e ->
                println(e.message)
                Assert.assertTrue("connect timed out" == e.message)
            }
        }
    }


    @Test fun testHttpPost() {
        var json = JSONObject()
        json.put("name", "Kotlin DSL Http")
        json.put("owner", "Kotlin")
        val postBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString())
        ajax {
            url = "saveArticle"
            method = "post"
            body = postBody
            success {
                string ->
                println(string)
            }
            error {
                e ->
                println(e.message)
            }
        }
    }
}