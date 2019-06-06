package com.d1540173108.hrz;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        long l = System.currentTimeMillis() / 1000;
        System.out.println(l);
        long v = String.valueOf((long) (Math.random() * l) ).length();
        System.out.println(v);

    }
}