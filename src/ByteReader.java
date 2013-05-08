import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import java.io.*;

import org.apache.commons.lang3.ArrayUtils;

public class ByteReader {

	public static byte[] readResponse(InputStream in){
		int index = 0;
        int byteRead = 0;
        List<Byte> array = new ArrayList<Byte>();
        List<Byte> header = new ArrayList<Byte>();
        
        try {
	        while(true){
	        	//read byte
	        	byteRead = in.read();
	        	if(byteRead != -1){
	        		//if 13,10,13,10 reached(\r\n\r\n) header has been read, now read the body
	        		if((byteRead == 10) && (array.get(index - 1) == 13) && (array.get(index - 2) == 10) && (array.get(index - 3) == 13)){
	            			array.add((byte)byteRead);
	            			index ++;
	            			//read message body
	            			readMessageBody(in, array);
	            			break;
	            	}else{ //add header bytes
			        	array.add((byte)byteRead);
			        	header.add((byte)byteRead);
			        	index ++;
	            	}
	        	}
	        	
	        }
        }catch (IOException e) {System.err.println(e);} 
        catch (Exception e) {System.err.println(e);} 
        
//      String arrayString = listToString(array);
//      System.out.println(arrayString);
//      printByteArray(array);
        System.out.println("Done: " + array.size());
        
        byte [] response = listToByteArray(array);
        saveHeaderToFile(header);
        
        return response;
	}
	private static void saveHeaderToFile(List<Byte> response_header) {
		byte [] response_header_bytes = ArrayUtils.toPrimitive(response_header.toArray(new Byte[response_header.size()]));
		String response_header_string = new String(response_header_bytes);
		
		if(response_header_string.contains("X-Afma-Debug-Dialog")){
			try {
				FileWriter fw = new FileWriter("response_header.txt",true); 
			    fw.write(response_header_string);
			    fw.close();			   
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
		
	}
	private static void readMessageBody(InputStream in, List<Byte> array){
		int bodyLength = getContentLenght(array);
		int byteRead = 0;
		
		try{
			if(bodyLength == 0){
				return;
			}else{
				while(bodyLength > 0){
					byteRead = in.read();
					array.add((byte)byteRead);
					bodyLength --;
				}
			}
		}catch(Exception e){System.err.println(e);}
		
	}
	private static int getContentLenght(List<Byte> array){
		String listString = listToString(array);
		int startIndex = 0;
		int endIndex = 0;
		String contentLengthString = "Content-Length: ";
		String contentLengthValue = "0";
		if(listString.contains(contentLengthString)) {
			startIndex = listString.lastIndexOf(contentLengthString);
			startIndex += contentLengthString.length();
			endIndex = listString.indexOf("\r", startIndex);
			contentLengthValue = listString.substring(startIndex, endIndex);
		}
		return Integer.parseInt(contentLengthValue);
	}
	
	
	private static void printByteArray(List<Byte> array){
		for(Byte b : array){
	    	 System.out.print(b + ",");
	     }
		System.out.println("");
	}
	
	private static String listToString(List<Byte> array){
		byte [] byteArray = new byte[array.size()];
	     for (int i = 0; i < array.size(); i ++){
	    	 byteArray[i] = array.get(i);
	     }
	  
		return new String(byteArray);
	}
	private static byte[] listToByteArray(List<Byte> array){
		byte [] byteArray = new byte[array.size()];
	     for (int i = 0; i < array.size(); i ++){
	    	 byteArray[i] = array.get(i);
	     }
	  
		return byteArray;
	}
	
}
        