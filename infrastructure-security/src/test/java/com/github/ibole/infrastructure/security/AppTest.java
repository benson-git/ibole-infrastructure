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
        String token = "eyJpc3MiOm51bGwsImF1ZCI6ImQiLCJleHAiOjE0OTQ5NDYzNzUsImp0aSI6IjV3LWVTU2dUNFNCSGZNaE42U0JFRFEiLCJpYXQiOjE0OTQ5NDYyNTUsIm5iZiI6MTQ5NDk0NjE5NSwic3ViIjpudWxsLCJjbGllbnRJZCI6IjEyNy4wLjAuMSIsImxvZ2luSWQiOiJkIiwicm9sZXMiOltdfQ";
        String decoded = new String(Base64.decodeBase64(token), "UTF-8");
        System.out.println(decoded);
        Date date = new Date(1494946375*1000L);
        System.out.println(date);
        NumericDate numericDate = NumericDate.now();
        numericDate.addSeconds(0);
        System.out.println(numericDate.toString());
   
    }
}
