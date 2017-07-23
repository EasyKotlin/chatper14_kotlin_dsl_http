package com.kotlin.easy

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by jack on 2017/7/23.
 */

@RunWith(JUnit4::class)
class KoHttpTest {

    @Test fun testHttpOnSuccess() {
        val testUrl = "https://www.baidu.com"

        val ajax = http {
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

    @Test fun testHttpOnFail() {
        val testUrl = "https://www2.baidu.com"

        val ajax = http {
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

}