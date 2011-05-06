import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.Point;
import java.awt.event.*;


class TronServer{
  public static void main(String args[]){new TronServer();}
  
  
  private int connections = 0;
  private int gamePort = 2001;
  private int dataPort = 2002;
  private String os = System.getProperty("os.name").toLowerCase();
  private Socket myS;
  private LinkedList<LinkedList<Point>> points = new LinkedList<LinkedList<Point>>();
  private LinkedList<String> address = new LinkedList<String>();
  private LinkedList<String> players = new LinkedList<String>();
  private int games = 0;
  private LinkedList<String> scores = new LinkedList<String>();
  
  
  
  public TronServer(){
    System.out.println(os);
    //loop to accept a connection
    while(true){   
      try{    
        // create socket
        ServerSocket serversocket = new ServerSocket(gamePort);
        System.out.println("Waiting for player number: "+(connections+1));
        myS = serversocket.accept();
        connections++;
        System.out.println("connection accepted");
        //create new linked list and add it to the points linked list, this new linked list will hold each point that tron has visited.
        points.add(new LinkedList<Point>());
        scores.add("0");
        if(connections % 4 == 0 || games == 0){games++;}
        //start new thread to listen for this clients point updates, sending it the socket pointer and its player number
        new Thread(new SocketThread(myS, connections, games)).start();
        
        //read remote address
        InetAddress remoteAddress = myS.getInetAddress();
        byte[] ipAddr = remoteAddress.getAddress();
        String remoteIP = ipAddr[0]+"."+ipAddr[1]+"."+ipAddr[2]+"."+ipAddr[3]; 
        address.add(remoteIP);
        
        //print any exceptions
      }catch(Exception e){}
    }
  }//end constructor 
  
  
  class SocketThread implements Runnable{
    private Socket myS;
    private int player;
    private int game;
    
    
    public SocketThread(Socket rs, int c, int g)
    {
      myS = rs;
      player = c;
      game = g;
    }//constructor
    
    public void run(){      
      try{
        
        PrintWriter pw = new PrintWriter(myS.getOutputStream(), true);
        pw.println((int)((connections)/game));
        
      }catch(Exception e){System.out.println("Error sending message to client");}
      
      while(true){        
        //read points from client and add them to there point list
        
        try{
          ObjectInputStream os = new ObjectInputStream(myS.getInputStream());
          LinkedList<Point> p = (LinkedList<Point>)os.readObject();
          if(p.size() > 1){
          points.set(player-1, p);
          
          ObjectOutputStream oos = new ObjectOutputStream(myS.getOutputStream());
          oos.writeObject(points);
          }
          
          else{
          String winner = (String)os.readObject(); 
          System.out.println(winner);
          points.set(player-1, new LinkedList<Point>());
          scores.set(Integer.parseInt(winner), Integer.toString((Integer.parseInt((String)scores.get(Integer.parseInt(winner)))+30)));
          System.out.println(scores.get(Integer.parseInt(winner)));
          myS.close();
          break;
            
          }
        }catch(Exception e){
          System.out.println(e);
          points.set(player-1, new LinkedList<Point>());
          break;}
        
        
      }
    }//run
    
  }
  
  
}
  
