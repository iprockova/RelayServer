import java.io.*;
import java.net.*;


class RelayServer extends Thread
{
	private ServerSocket serverSocket = null;
	
	public void run(){
			startServerSocket();
	}
	
	public void startServerSocket(){
		try {
			   serverSocket = new ServerSocket(8080);
			   System.out.println("Listening :8080");
				  while(true){
					  ClientWorker w;
					    try{
					      w = new ClientWorker(serverSocket.accept());
					      Thread t = new Thread(w);
					      t.start();
					    } catch (IOException e) {
					      System.out.println("Accept failed: 8080");
					      System.exit(-1);
					    }
				  }
				  
		}catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
		finally{
		}
	}
	public class ClientWorker implements Runnable{
		private Socket client;

		  ClientWorker(Socket client) {
		    this.client = client;
		  }
		  
		@Override
		public void run() {
			String inputLine;
		    BufferedReader in = null;
		    OutputStream out = null;
		    try{
			    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			    out = client.getOutputStream();
		   
			    byte [] outputLine;
			    StringBuffer request = new StringBuffer();
		    
			    while(true){
			        inputLine = in.readLine();
			        if(inputLine != null){
				        System.out.println(inputLine);
					    if(!(inputLine.equals("")))
					    	request.append(inputLine + "\r\n");
					    else {
					    	request.append("\r\n");
					    	outputLine = new RelayClient().execute(request.toString());
						    out.write(outputLine);
						    out.flush();
						    
						    if((request.toString().contains("GET /mads/gma?preqs=") && request.toString().contains("Host: googleads.g.doubleclick.net"))) //if it is ad request
						    	saveRequestToFile(request.toString()); //for analysis 
						    	
						    request  = new StringBuffer();
						    
						    
					    }
			         }
			     }
			    }catch (IOException e) {
			       System.out.println("Read failed");
			       System.exit(-1);
			   }
		}

		private void saveRequestToFile(String request) {
			FileWriter fw;
			try {
				fw = new FileWriter("request_header.txt",true);
				fw.write(request);
				fw.close();	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //the true will append the new data
		   
			
		} 
	}
}