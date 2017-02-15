package io.ibole.infrastructure.common.utils;

import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
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
