import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

class TronClient extends JFrame implements ActionListener, KeyListener{
  public static void main(String args[]){new TronClient();}
  //global variables
  private javax.swing.Timer soundLoop = new javax.swing.Timer(64800, this);
  private javax.swing.Timer move = new javax.swing.Timer(70, this);
  private boolean connected = false;
  private boolean readyToConnect = false;
  private int port = 2001;
  private String host = "www.michaeljackson.com";
  private Socket myS;
  private int timeOut = 10000;
  private String otherPlayers;
  private String games;
  private String myName = "";
  
  
  
  
  private int score = 0;
  private Toolkit toolkit = Toolkit.getDefaultToolkit();
  private TronFrame window;
  private GameMenu menu;
  private InfoFrame info;
  private int width, height;
  private Point me = new Point(0,0);
  private int player;
  private int x, y;
  private String direction = "u";
  private Random random = new Random();
  private Point[] allPoints = new Point[4];
  private int[][] grid = new int[100][100];
  
  //change to menu from info panel
  private void changeToMenu(){
    this.add(menu);
    info.setVisible(false);  
  }
  
  //constructor
  public TronClient(){
    
    for(int a = 0; a<100;a++){
      for(int b = 0; b<100;b++){
       grid[b][a] = 0; 
      }
      
    }
    
    try{
      System.setProperty("apple.laf.useScreenMenuBar", "true");
      System.setProperty ("com.apple.macos.useScreenMenuBar","true");
      System.setProperty("com.apple.mrj.application.apple.menu.about.name", "TRON");
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }catch(Exception e){System.out.println(e);}
    
    
   
   
  new AePlayWave("media/daft_punk.wav").start();
  soundLoop.start();

  this.setSize(500,500);
  
  width = this.getWidth();
  height = this.getHeight();  
  window = new TronFrame();
  menu = new GameMenu();
  info = new InfoFrame();

  this.setVisible(true);   

  this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  this.addKeyListener(this);
  menu.addKeyListener(this);
  info.addKeyListener(this);
  this.setLocationRelativeTo(null);
  this.setMinimumSize(this.getSize());
  
  this.add(info);
  //new InfoFrame();
  new SendData().run();
  }
  
 public void goGame(){
  this.add(window);
  menu.setVisible(false);
  move.start(); 
  }
 
  /****************************
   *begin coding event handlers*
   ****************************/
  public void keyTyped(KeyEvent e){}
  public void keyPressed(KeyEvent e){
    
   if(e.getKeyCode() == e.VK_LEFT){if(direction.equals("r")){}else{direction = "l";}}
   else if(e.getKeyCode() == e.VK_RIGHT){if(direction.equals("l")){}else{direction = "r";}}
   else if(e.getKeyCode() == e.VK_UP){if(direction.equals("d")){}else{direction = "u";}}
   else if(e.getKeyCode() == e.VK_DOWN){if(direction.equals("u")){}else{direction = "d";}}
  
  }
  public void keyReleased(KeyEvent e){}
  

  
  
  
  public void actionPerformed(ActionEvent e){
    if(e.getSource()==soundLoop){new AePlayWave("media/daft_punk.wav").start();}
    else if(e.getSource()==move){
      if(direction.equals("u")){y--;}
      else if(direction.equals("d")){y++;}
      else if(direction.equals("r")){x++;}
      else{x--;}
      window.repaint();
      me = new Point(x,y);
      
      if(x >= 99){x=1;}
      else if(x<=1){x=99;}
      else if(y>=99){y=1;}
      else if(y<=1){y=99;}
      
      //collision detection
      
      
        if(grid[x][y] != 0){
        
          try{  
        ObjectOutputStream oos = new ObjectOutputStream(myS.getOutputStream());
        oos.writeObject(new Point(-5,-5));
        oos.flush();
        PrintWriter pw = new PrintWriter(myS.getOutputStream(), true);
        pw.println(Integer.toString(grid[x][y]));
        Thread.sleep(3000);
          }catch(Exception ex){}
          System.out.println("hit");
          System.exit(0);
        } 
        
      
      
      
    }
  }
  
  /********************************
   *begin coding of the game menu *
   ********************************/
  
  public class GameMenu extends JPanel implements ActionListener{
    int t = 0;
    javax.swing.Timer time = new javax.swing.Timer(1000, this);
    public void actionPerformed(ActionEvent e){
      t++;
      
    }

    Image tron1 = toolkit.getImage("media/tron1.jpg");
    Image tron2 = toolkit.getImage("media/tron2.jpg");
    Image loading = toolkit.getImage("media/loading.gif");
    GameMenu(){
      time.start();
  
    }

     public void paintComponent(Graphics g){
     super.paintComponent(g); 
     if(t<10){g.drawImage(tron1, 0,0, width, height, this);}
     else if(t<20){g.drawImage(tron2, 0,0, width, height, this);}
     else{t=0;}
     g.drawImage(loading, (width/2)-50, (height/2)-30, this);
     
     }
  }
  
  /**************************************
   * begin coding of the tron game panel*
   **************************************/
  
  public class TronFrame extends JPanel implements KeyListener{
     public void keyTyped(KeyEvent e){}
  public void keyPressed(KeyEvent e){
    
   if(e.getKeyCode() == e.VK_LEFT){if(direction.equals("r")){}else{direction = "l";}}
   else if(e.getKeyCode() == e.VK_RIGHT){if(direction.equals("l")){}else{direction = "r";}}
   else if(e.getKeyCode() == e.VK_UP){if(direction.equals("d")){}else{direction = "u";}}
   else if(e.getKeyCode() == e.VK_DOWN){if(direction.equals("u")){}else{direction = "d";}}
  
  }
  public void keyReleased(KeyEvent e){}
  
    public void paintComponent(Graphics g){
     super.paintComponent(g); 
     this.setBackground(new Color(0,100,200));
     
     
     
     for(int a = 0; a<99; a++){
       for(int b = 0; b<99; b++){
         switch(grid[b][a]){
         case 1:{g.setColor(new Color(255, 0, 0)); g.fill3DRect((int)((width/100)*b),(int)((height/100)*a), width/100,height/100,true); break;}
         case 2:{g.setColor(new Color(0, 255, 0)); g.fill3DRect((int)((width/100)*b),(int)((height/100)*a), width/100,height/100,true); break;}
         case 3:{g.setColor(new Color(0, 0, 255)); g.fill3DRect((int)((width/100)*b),(int)((height/100)*a), width/100,height/100,true); break;}
         case 4:{g.setColor(new Color(255, 255, 0)); g.fill3DRect((int)((width/100)*b),(int)((height/100)*a), width/100,height/100,true); break;}
         }
       }     
     }
    }
  }
  
  /**********************************
   * begin background process coding*
   **********************************/
  
  
  public class SendData extends SwingWorker<Void, Void>{
    
    //method to reset a dead players grid points to 0 so they wont be drawn
    public void reset(int p){
      for(int a = 0; a<100; a++){
        for(int b = 0; b<100; b++){
          if(grid[b][a] == p){grid[b][a] = 0;} 
        }
      }
    }
    
    public Void doInBackground(){
      
      //connect
      while(!connected){if(readyToConnect){connect();}}
      //start moving
      move.start();
      //start sending list of points
      while(connected){
      try{
        //send my current point to the server
        ObjectOutputStream oos = new ObjectOutputStream(myS.getOutputStream());
        oos.writeObject(me);
        //read an array of points from the server
        ObjectInputStream ois = new ObjectInputStream(myS.getInputStream());
        Point[] p = (Point[])ois.readObject();
        //if one of these points contains -5, player has died
        for(int a = 0; a<4; a++){
          if(p[a].x == -5){reset(a); p[a].x = 0; p[a].y = 0;} 
        }
        //set grid value so the players point can be drawn
        grid[p[0].x][p[0].y] = 1;
        grid[p[1].x][p[1].y] = 2;
        grid[p[2].x][p[2].y] = 3;
        grid[p[3].x][p[3].y] = 4;
        
      }
      catch(Exception e){System.out.println(e);}
      }
      
      
      
      return null;
    }
    
    public void connect(){
      try{
      Thread.sleep(3000);
      myS = new Socket();
      //connect to server
      myS.connect(new InetSocketAddress(host, port), timeOut);
      //print connected message
      System.out.println("Connected!");
      //read servers answer message
      BufferedReader read = new BufferedReader(new InputStreamReader(myS.getInputStream()));
      String line = read.readLine();
      //set connected to true
      connected = true;
      //set your player number
      player = Integer.parseInt(line);
      System.out.println("You are player number: "+player);
      //set your position and direction
      switch(player){
        case 1:{x = 50; y = 99; direction="u"; break;}
        case 2:{x = 1; y = 50; direction="r"; break;}
        case 3:{x = 99; y = 50; direction="l"; break;}
        case 4:{x = 50; y = 1; direction="d"; break;}
      }
      //add new point
      me = new Point(x,y);
      
      //start game
      goGame();
      }catch(Exception e){System.out.println(e);}
    }
    
  }
  
  /*****************************************
   * begin coding of game information frame*
   *****************************************/
  public class InfoFrame extends JPanel implements MouseListener{
    JTextArea textArea = new JTextArea(5, 20);
    JTextArea players = new JTextArea(20, 20);
    JPanel mainPanel = new JPanel();
    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JButton b = new JButton("Connect");
    JTextField address = new JTextField(20);
    JTextField name = new JTextField(20);

    
    
    
    
    public InfoFrame(){
    name.setBorder(BorderFactory.createTitledBorder("My name"));
    players.setBorder(BorderFactory.createTitledBorder("Players"));
    address.setBorder(BorderFactory.createTitledBorder("Host Address"));    
    leftPanel.add(players);
    b.addMouseListener(this);
    rightPanel.add(name);
    rightPanel.add(address);
    rightPanel.add(textArea);
    rightPanel.add(b);  
    this.setLayout(new GridLayout(1, 2));
    this.add(leftPanel);
    this.add(rightPanel);

    address.setText("127.0.0.1");
    
    
    }
  
  /***********************************
   * event handlers for the infoframe*
   ***********************************/
    
  public void mouseMoved(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}
  public void mousePressed(MouseEvent e){}
  public void mouseReleased(MouseEvent e){}
  public void mouseClicked(MouseEvent e){
    changeToMenu();
    myName = name.getText();
    host = address.getText();
    readyToConnect = true;
    
  }
   
 
  
  
    
  }
  
  
}