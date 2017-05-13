package com.github.ibole.infrastructure.security;

import org.apache.commons.codec.binary.Base64;
import org.jose4j.jwt.NumericDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Unit test for simple App.
 */

@RunWith(JUnit4.class)
public class AppTest 
{

    /**
     * Rigourous Test :-)
     * @throws UnsupportedEncodingException 
     */
    @Test
    public void testApp() throws UnsupportedEncodingException
    {
        String token = "eyJpc3MiOm51bGwsImF1ZCI6InRlc3QiLCJleHAiOjE0OTQ0Mzg5ODEsImp0aSI6IkZWUDFoWUNRWG02VHJTQkQwdU1RdkEiLCJpYXQiOjE0OTQ0MzUzODEsIm5iZiI6MTQ5NDQzNTMyMSwic3ViIjpudWxsLCJjbGllbnRJZCI6IjA6MDowOjA6MDowOjA6MSIsImxvZ2luSWQiOiJ0ZXN0Iiwicm9sZXMiOltdfQ";
        
        String decoded = new String(Base64.decodeBase64(token), "UTF-8");
        System.out.println(decoded);
        Date date = new Date(1494438494*1000L);
        System.out.println(date);
        NumericDate numericDate = NumericDate.now();
        numericDate.addSeconds(60);
        System.out.println(numericDate.toString());
   
    }
}
