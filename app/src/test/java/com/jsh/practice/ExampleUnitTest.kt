package com.jsh.practice

import org.junit.Test
import org.junit.Assert.*
import timber.log.Timber


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun `안녕하세요`(){
        Timber.e("hello world")
    }

}