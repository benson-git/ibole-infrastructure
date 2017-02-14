package io.ibole.infrastructure.common.utils;

import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

/*********************************************************************************************.
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p></p>
 *********************************************************************************************/


public class PortFactory { 
  public static int findFreePort() { 
      int port; 
      try { 
          ServerSocket server = new ServerSocket(0); 
          port = server.getLocalPort(); 
          server.close(); 
          // allow time for the socket to be released 
          TimeUnit.MILLISECONDS.sleep(350); 
      } catch (Exception e) { 
          throw new RuntimeException("Exception while trying to find a free port", e); 
      } 
      return port; 
  } 
  
  public static void main(String[] args){
      System.out.print(findFreePort());
  }
}
