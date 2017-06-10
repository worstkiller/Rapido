package com.example.rapido;

import com.example.rapido.network.ApiServiceNetwork;
import com.example.rapido.util.WebConstants;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {
    @Test
    public void test_NetworkClass() throws Exception{
        ApiServiceNetwork apiNetwork = new ApiServiceNetwork();
        assertNotNull(apiNetwork.getNetworkService(null, WebConstants.PLACES_API));
    }

    @Test
    public void checkClass_Exists() throws  Exception{
        Class clazz = Class.forName("com.example.rapido.activity.MainActivity");
        assertNotNull(clazz);
    }
}