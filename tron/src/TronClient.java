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
  private int width, height;
  private LinkedList<Point> myPoints = new LinkedList<Point>();
  private int player;
  private int x, y;
  private String direction = "u";
  private Color color;
  private Random random = new Random();
  private LinkedList<LinkedList<Point>> allPoints = new LinkedList<LinkedList<Point>>();
  
  //constructor
  public TronClient(){
   
    try{
      System.setProperty("apple.laf.useScreenMenuBar", "true");
      System.setProperty ("com.apple.macos.useScreenMenuBar","true");
      System.setProperty("com.apple.mrj.application.apple.menu.about.name", "TRON");
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }catch(Exception e){System.out.println(e);}
    
    
    x = 50;
    y= 100;
    myPoints.add(new Point(x, y));
   
  new AePlayWave("media/daft_punk.wav").start();
  soundLoop.start();
  menu = new GameMenu();
  window = new TronFrame();
  
  this.setSize(500,500);
  
  width = this.getWidth();
  height = this.getHeight();  
  this.setVisible(true);   
  
  
  
  
  this.add(menu);
  
  
  this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  this.addKeyListener(this);
  this.setLocationRelativeTo(null);
  this.setMinimumSize(this.getSize());
  new InfoFrame();
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
      myPoints.add(new Point(x,y));
      
      if(x > 100){x=0;}
      else if(x<0){x=100;}
      else if(y>100){y=0;}
      else if(y<0){y=100;}
      
      for(int a = 0; a<allPoints.size(); a++){
        if(allPoints.get(a).contains(new Point(x, y))){
        
          try{  
        ObjectOutputStream oos = new ObjectOutputStream(myS.getOutputStream());
        oos.writeObject(new LinkedList<Point>());
        oos.writeObject(Integer.toString(a));
        
        
          }catch(Exception ex){}
          
          System.exit(0);
        
        } 
        
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
  
  public class TronFrame extends JPanel{
   
    public void paintComponent(Graphics g){
     super.paintComponent(g); 
     this.setBackground(new Color(0,100,200));
     
     
     
     for(int a = 0; a<allPoints.size(); a++){
       for(int b = 0; b<allPoints.get(a).size(); b++){
         g.fill3DRect(
                      (int)((width/100)*allPoints.get(a).get(b).getX()), 
                      (int)((height/100)*allPoints.get(a).get(b).getY()), width/100,height/100,true); 
       }
       
     }
     
     g.setColor(color);
     for(int a = 0; a<myPoints.size(); a++){
       g.fill3DRect(
                      (int)((width/100)*myPoints.get(a).getX()), 
                      (int)((height/100)*myPoints.get(a).getY()), width/100,height/100,true); 
       
       
     }
     
     
     
     
    }
  }
  
  /**********************************
   * begin background process coding*
   **********************************/
  
  
  public class SendData extends SwingWorker<Void, Void>{
    public Void doInBackground(){
      //connect
      while(!connected){if(readyToConnect){connect();}}
      //start moving
      move.start();
      //start sending list of points
      while(connected){
      try{
        ObjectOutputStream oos = new ObjectOutputStream(myS.getOutputStream());
        oos.writeObject(myPoints);
        ObjectInputStream ois = new ObjectInputStream(myS.getInputStream());
        allPoints = (LinkedList<LinkedList<Point>>)ois.readObject();
      }
      catch(Exception e){System.out.println(e);}
      }
      
      
      
      return null;
    }
    
    public void connect(){
      try{
      Thread.sleep(3000);
      myS = new Socket();
      myS.connect(new InetSocketAddress(host, port), timeOut);
      connected = true;
      System.out.println("Connected!");
      BufferedReader read = new BufferedReader(new InputStreamReader(myS.getInputStream()));
      String line = read.readLine();

      player = Integer.parseInt(line);
      System.out.println("You are player number: "+player);
      switch(player){
        case 1:{x = 50; y = 100; direction="u"; break;}
        case 2:{x = 0; y = 50; direction="r"; break;}
        case 3:{x = 100; y = 50; direction="l"; break;}
        case 4:{x = 50; y = 0; direction="d"; break;}
      }
      myPoints.add(new Point(x,y));
      int r = random.nextInt(255);
      int g = random.nextInt(255);
      int b = random.nextInt(255);
      color = new Color(r,g,b);
      goGame();
      }catch(Exception e){System.out.println("Error connecting to server\nTrying again!!"+e);}
    }
    
  }
  
  /*****************************************
   * begin coding of game information frame*
   *****************************************/
  public class InfoFrame extends JFrame implements MouseListener{
    JTextArea textArea = new JTextArea(5, 20);
    JTextArea players = new JTextArea(20, 20);
    JPanel mainPanel = new JPanel();
    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JButton b = new JButton("Connect");
    JTextField address = new JTextField(20);
    JTextField name = new JTextField(20);
    IndicatorLight ind = new IndicatorLight();
    
    
    
    
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
    rightPanel.add(ind);
    ind.setSize(50, 5);
    mainPanel.setLayout(new GridLayout(1, 2));
    mainPanel.add(leftPanel);
    mainPanel.add(rightPanel);
    this.add(mainPanel);
    
    this.setSize(500, 400);
    this.setMinimumSize(this.getSize());
    this.setLocation(0, (height/3)*2);
    this.setVisible(true);
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
    myName = name.getText();
    host = address.getText();
    readyToConnect = true;
    ind.repaint();
  }
   
  public class IndicatorLight extends JPanel{
    public void paintComponent(Graphics g){
     super.paintComponent(g);
     if(!connected){
       g.setColor(new Color(255, 0,0));
       g.fillOval(0,0,5,5);
       g.setColor(new Color(0,0,0));
       g.drawOval(0,0,5,5);
     }
     else{
       g.setColor(new Color(0, 255,0));
       g.fillOval(0,0,5,5);
       g.setColor(new Color(0,0,0));
       g.drawOval(0,0,5,5);
     }
     
    }
    
  }
  
  
    
  }
  
  
}