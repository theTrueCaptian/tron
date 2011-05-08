import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.Point;
import java.awt.event.*;


class TronServer{
  public static void main(String args[]){new TronServer();}
  
  private int socketExceptions = 0;
  private int connections = 0;
  private int gamePort = 2001;
  private String os = System.getProperty("os.name").toLowerCase();
  private Socket myS;
  private LinkedList<Point> points = new LinkedList<Point>();
  private LinkedList<String> address = new LinkedList<String>();
  private LinkedList<String> players = new LinkedList<String>();
  private int games = 0;
  private LinkedList<String> scores = new LinkedList<String>();
  
  private void resetSocket(){
    try{myS = new Socket(); myS.setReuseAddress(true); }catch(Exception e){System.out.println(e);}
  }
  
  
  public TronServer(){
    
    points.add(new Point());
    points.add(new Point());
    points.add(new Point());
    points.add(new Point());
    
    
    
    
    System.out.println(os);
    //loop to accept a connection
    while(true){   
      if(socketExceptions > 10){resetSocket(); socketExceptions = 0;}
      try{   
        
        // create socket
        ServerSocket serversocket = new ServerSocket(gamePort);
        System.out.println("Waiting for player number: "+(connections+1));
        myS = new Socket();
        myS = serversocket.accept();
        connections++;
        System.out.println("connection accepted");
        //create new linked list and add it to the points linked list, this new linked list will hold each point that tron has visited.
        points.add(new Point(0,0));
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
      }catch(Exception e){socketExceptions++;}
    }
  }//end constructor 
  
  
  class SocketThread implements Runnable{
    private Socket myS;
    private int player;
    private int game;
    private Point[] myPoints = new Point[4];
    private boolean firstTime = true;
    
    public SocketThread(Socket rs, int c, int g)
    {
      myS = rs;
      player = c;
      game = g;
      myPoints[0] = new Point();
      myPoints[1] = new Point();
      myPoints[2] = new Point();
      myPoints[3] = new Point();
      
    }//constructor
    
    public void run(){      
      try{
        while(true){        
          //read points from client and add them to there point list
          
          //check if there are 4 players in this game
          if(connections == game*4 || true){
            
            
            if(firstTime){
              //send your player number only once
              PrintWriter pw = new PrintWriter(myS.getOutputStream(), true);
              pw.println((int)((connections)/game));
              firstTime = false;
            }
            
            //read points from client and set them in the points list
            ObjectInputStream os = new ObjectInputStream(myS.getInputStream());
            Point p = (Point)os.readObject();
            
            if(p.x != -5){
              points.set(player-1, p);
              
              //collect 4 points lists for players in this game only and send it back
              myPoints[0] = points.get((game*1)-1);
              myPoints[1] = points.get((game*2)-1);
              myPoints[2] = points.get((game*3)-1);
              myPoints[3] = points.get((game*4)-1);
              
              ObjectOutputStream oos = new ObjectOutputStream(myS.getOutputStream());
              
              oos.writeObject(myPoints);
              
              
            }
            else{
              BufferedReader read = new BufferedReader(new InputStreamReader(myS.getInputStream()));
              String line = read.readLine();
              int winner = Integer.parseInt(Character.toString(line.charAt(line.length()-1))); 
              System.out.println(winner);
              points.set(player-1, new Point());
              scores.set(winner-1, Integer.toString((Integer.parseInt((String)scores.get(winner-1))+30)));
              System.out.println(scores.get(winner-1));
              myS.close();
              break;
              
            }
          }
          
        }//while
      }
      catch(Exception e){
        System.out.println(e);
        points.set(player-1, new Point(-5,-5));
        }  
      
      
    }//run
  }
  
}
  
  

  
