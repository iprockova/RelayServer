import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class RelayClient {
	 
	 DataOutputStream dataOutputStream = null;
	 DataInputStream dataInputStream = null;
	 
	public byte[] execute(String input) {
		String testServerName = "media.admob.com";
	    int port = 80;
	    Socket socket=null;
	    try
	    {
	      // open a socket
	       socket = openSocket(testServerName, port);
	      
	      // write-to and read-from the socket.
	      byte[] response =  writeToAndReadFromSocket(socket, input);
	       
	      // close the socket, and we're done
	      socket.close();
	      
	      return response;
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	      return null;
	    }finally{
			try {
				if(socket!= null) socket.close();
			}catch (IOException e) {
			      e.printStackTrace();
			}
		}
	}
	  
	  private byte[] writeToAndReadFromSocket(Socket socket, String input) throws Exception
	  {
	    try 
	    {
	    	//write to socket 
	    	OutputStream out = socket.getOutputStream();
	    	out.write(input.getBytes());
	        out.flush();
	    	
	    	//read from socket
	        InputStream in = socket.getInputStream();
	        byte [] response = RelayClientRead.readResponse(in);
		    System.out.println("RelayClient: done reading");
		    
		    out.close();
		    in.close();
	    	return response;
	    }catch (Exception e) {
		   e.printStackTrace();
		   return null;
		}
	  }
	  private Socket openSocket(String server, int port) throws Exception{
	    Socket socket;
	    try
	    {
	      InetAddress inteAddress = InetAddress.getByName(server);
	  
	      // create a socket
	      socket = new Socket(inteAddress, port);
	      
	      return socket;
	    } 
	    catch (SocketTimeoutException ste) 
	    {
	      ste.printStackTrace();
	      throw ste;
	    }
	  }

}
