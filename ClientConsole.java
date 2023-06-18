// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  EchoServer sv;
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String loginID,String host, int port) 
  {
	  
    try 
    {
    	 
      client= new ChatClient(loginID,host, port, this);
      
      
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  
  public void accept() 
  {
    try
    {

      String message;

      while (true) 
      {
    	  message = fromConsole.nextLine();
    	  char[] ch = message.toCharArray();
    	  String H="";
    	  String P="";
    	  for(int i=10;i<ch.length-1;i++) {
    		   H=H+ch[i];
    	  }
    	  for(int x=0;x<ch.length;x++) {
    		  if(ch[x]==' ') {
    			  break;
    		  }
   		   P=P+ch[x];
    	  }
    	  if(ch[0]=='#') {
    		  switch(P) {
    		  case "#quit":
    		    //close the connection and exit
    			  client.quit();
    		    break;
    		    
    		  case "#logoff":
    		    // only close connection
    			  client.closeConnection();
    		    break;
    		    
    		  case "#sethost":
    			  if(!(client.isConnected())) {
    			    	client.setHost(H);
    			    	      			    
    			  }else{
    				  System.out.println("Client should log off first!");
    			  }
    			    break;
    			    
    		  case "#setport":
    			  
      			    if(!(client.isConnected())) {
      			    	client.setPort(Integer.parseInt(H));
      			    	      			    
      			  }else{
      				  System.out.println("Client should log off first!");
      			  }
    			    break;
    		  case "#login":
    			  if(!(client.isConnected())) {
    			    	client.openConnection();
    			    	      			    
    			  }else{
    				  System.out.println("Client is already connected!");
    			  }
    			    break;
    		  case "#gethost":
    			  System.out.println("The current Host is : "+client.getHost());
    			    break;
    		  case "#getport":
    			  System.out.println("The current Port is : "+String.valueOf(client.getPort()));
    			    break;
    		  default:
    		    System.out.println("Wrong command!");
    		}
    	  }else {
        
        client.handleMessageFromClientUI(message);
    	  }
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
 * @throws IOException 
   */
  public static void main(String[] args) throws IOException 
  {
	  String loginID =null;
	  String host = "";
	  int port=0;
	  
    try
    {
      host = args[1];
      
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    try
    {
      port = Integer.parseInt(args[2]);
      
    }
    catch(Throwable e)
    {
      port = DEFAULT_PORT;
    }
    try 
    {
	   loginID=args[0];
    }
	    
    catch(Exception e) 
    {
	    	throw new RuntimeException("No login ID specified.  Connection aborted.");  
    }

	
    
    
    ClientConsole chat= new ClientConsole(loginID,host, port);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
