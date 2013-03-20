import java.io.*;
import java.net.*;


class RelayServer extends Thread
{
	private ServerSocket serverSocket = null;
	
	private BufferedReader in = null;
	
	private boolean threadDone = true;
	
	public void run(){
		while(threadDone){
			startServerSocket();
		}
	}
	
	public void startServerSocket(){
		Socket socket = null;
		try {
			   serverSocket = new ServerSocket(8080);
			   System.out.println("myApp, Listening :8080");
	
				  socket = serverSocket.accept();
				  
				  // read the request from the socket
				  String request = readFromSocket(socket);
				  
				  //start client and get response
				  byte[] reply = new RelayClient().execute(request);
				  
				  //write the response to the socket
				  writeToSocket(socket, reply);
				  System.out.println("RelayServer: reply sent to mobile app");
				  
		}catch (IOException e) {
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try {
				if(socket!= null) socket.close();
				if(threadDone = true) threadDone = false;
			}catch (IOException e) {
			      e.printStackTrace();
			     }
		}
	}

	private void writeToSocket(Socket socket, byte[] response) {
		try{
			OutputStream out = socket.getOutputStream();
	    	out.write(response);
	        out.flush();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private String readFromSocket(Socket socket) {
		  try{
			  
			  String inputLine = "";
			  InputStream input = socket.getInputStream();
			  in = new BufferedReader(new InputStreamReader(input));
			  
			  StringBuffer request = new StringBuffer();
			  //print the input
			  while (!(inputLine = in.readLine()).equals("")) {
			      System.out.println(inputLine);
			      request.append(inputLine + "\r\n");
			  }
			  request.append("\r\n");
			  
			  return request.toString();
		  }catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}