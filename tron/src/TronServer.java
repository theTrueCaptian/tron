import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.Point;
import java.awt.event.*;


class TronServer{
  public static void main(String args[]){new TronServer();}
  
  private DatagramSocket broadcast; 
  
  private int socketExceptions = 0;
  private int connections = 0;
  private int gamePort = 2001;
  private int dataPort = 2002;
  private int games = 0;
  
  private String os = System.getProperty("os.name").toLowerCase();
  
  private Socket myS, dataS;
  
  private LinkedList<Point> points = new LinkedList<Point>();
  private LinkedList<String> address = new LinkedList<String>();
  private LinkedList<String> players = new LinkedList<String>();
  private LinkedList<String> scores = new LinkedList<String>();
  

  
  
  public TronServer(){
    
    points.add(new Point());
    points.add(new Point());
    points.add(new Point());
    points.add(new Point());
    
    
    new Thread(new BroadcastMessage()).start();
    new Thread(new DataSocket()).start();
    
    System.out.println(os);
    //loop to accept a connection
    while(true){   
      
      try{   
        
        // create socket
        ServerSocket serversocket = new ServerSocket(gamePort);
        System.out.println("Waiting for player number: "+(connections+1));
        myS = new Socket();
        myS = serversocket.accept();
        
        //itterate connections variable
        connections++;
        //add to number of games variable 
        if(connections % 4 == 0 || games == 0){games++;}
        
        System.out.println("connection accepted");
        
        
        
        
        
        //read remote address
        InetAddress remoteAddress = myS.getInetAddress();
        byte[] ipAddr = remoteAddress.getAddress();
        String remoteIP = ipAddr[0]+"."+ipAddr[1]+"."+ipAddr[2]+"."+ipAddr[3]; 
        address.add(remoteIP);
        players.add(remoteIP);
        points.add(new Point(0,0));
        scores.add("0");
        
        //start new thread to listen for this clients point updates, sending it the socket pointer and its player number
        new Thread(new SocketThread(myS, connections, games)).start();
        
        //print any exceptions
      }catch(Exception e){ }
    }
  }//end constructor 
  
  
   
   class DataSocket implements Runnable{
     public void run(){
       while(true){
         try{
           ServerSocket ss = new ServerSocket(dataPort);
           dataS = new Socket();
           dataS = ss.accept();
           
           ObjectOutputStream oos = new ObjectOutputStream(dataS.getOutputStream());
           oos.writeObject(players);
           oos.writeObject(scores);
           
         }catch(Exception e){}

       }
     }
   }
   
  class BroadcastMessage implements Runnable{
    public void run(){
      while(true){
        try{
        Thread.sleep(5000); 
        broadcast = new DatagramSocket();
        InetAddress addr = InetAddress.getLocalHost();
        
        byte[] buf = addr.getAddress();
        
        InetAddress group = InetAddress.getByName("224.0.0.251");
        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
        broadcast.send(packet);
        
        }catch(Exception e){}
      }    
    }
  }
  
  class SocketThread implements Runnable{
    private Socket localSocket;
    private int player;
    private int game;
    private Point[] myPoints = new Point[4];
    private boolean firstTime = true;
    
    public SocketThread(Socket rs, int c, int g)
    {
      localSocket = rs;
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
              PrintWriter pw = new PrintWriter(localSocket.getOutputStream(), true);
              pw.println((int)((connections)/game));
              firstTime = false;
            }
            
            //read points from client and set them in the points list
            ObjectInputStream os = new ObjectInputStream(localSocket.getInputStream());
            Point p = (Point)os.readObject();
            
            if(p.x != -5){
              points.set(player-1, p);
              
              //collect 4 points lists for players in this game only and send it back
              myPoints[0] = points.get((game*1)-1);
              myPoints[1] = points.get((game*2)-1);
              myPoints[2] = points.get((game*3)-1);
              myPoints[3] = points.get((game*4)-1);
              
              ObjectOutputStream oos = new ObjectOutputStream(localSocket.getOutputStream());
              
              oos.writeObject(myPoints);
              
              
            }
            else{
              BufferedReader read = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));
              String line = read.readLine();
              int winner = Integer.parseInt(Character.toString(line.charAt(line.length()-1))); 
              System.out.println(winner);
              points.set(player-1, new Point());
              scores.set(winner-1, Integer.toString((Integer.parseInt((String)scores.get(winner-1))+30)));
              System.out.println(scores.get(winner-1));
              localSocket.close();
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
  
  

  
