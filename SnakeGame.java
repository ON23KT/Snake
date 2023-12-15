import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.JPanel;

public class SnakeGame extends JPanel implements ActionListener, KeyListener
//SnakeGame übernimmt die Eigenschaften von JPAnel; KeyListener Snake hört jetzt auf die Movement arrwos
{
	private class Tile
	{
		int x;
		int y;
		//x und y Achse definieren
		Tile(int x, int y)
		{
			this.x = x ;
			this.y = y;
		}
	}
	int boardWidth;
	int boardHeight;
	int tileSize = 25;
	
	//Snake
	Tile snakeHead;
	//SnakeBody jeder Teil des Körpers ist ein Tile
	ArrayList<Tile> snakeBody;
	
	
	//Food
	Tile food;
	Random random;
	
	//game logic
	Timer gameLoop;
	//velocity sorgt für die Bewegung 
	int velocityX;
	int velocityY;
	boolean gameOver = false;
	
	SnakeGame(int boardWidth, int boardHeight)
	{
		//wieso this? einmal um zwischen int board und this board zu unterscheiden. Außerdem um zu definieren das
		//die this boards zu dieser Klasse gehören
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;
		setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
		setBackground(Color.BLACK);
		//Key's sollen reagieren
		addKeyListener(this);
		//Snake game soll den Keys Zuhören darauf achten was passiert
		setFocusable(true);
		//definiere Startpunkt für den Start des Spiels
		snakeHead = new Tile (5,5);	
		//ein Array in dem alle Snake Body Teile gespeichert werden
		snakeBody = new ArrayList<Tile>();
		
		food = new Tile(10,10);
		random = new Random();
		placeFood();
		
		//velocityY = 1 y-Achse wurde so definiert heißt Y verläuft vertikal jedoch wurden jetzt beide werte auf null gesetzt
		//da wir jetzt schauen müssen wie wir es schaffen snake zu bewegen
		velocityX = 0; 
		velocityY = 0;
		
		//sagen dem Timer wie oft er was in der Milisekunde wiederholen soll
		gameLoop = new Timer(100, this);
		gameLoop.start();
		
		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		draw(g);
	}
	public void draw (Graphics g)
	{	//Grid
		for (int i = 0; i < boardWidth/tileSize; i++)
		{
			// (x1,y1,x2,y2)
			g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
			g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
		}
		//Food
		g.setColor(Color.red);
		g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);
		//SnakeHead
		g.setColor(Color.green);
		g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize,tileSize);
		
		//SnakeBody wird länger bei Kontakt mit Frucht
		for (int i = 0; i < snakeBody.size(); i++)
		{
			Tile snakePart = snakeBody.get(i);
			g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize,tileSize, tileSize);
		}
		
		//Score
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		if(gameOver)
		{
			g.setColor(Color.red);
			g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize-16, tileSize);
		}
		else
		{
			g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize-16, tileSize);
		}
	}

	public void placeFood()
	{
		food.x = random.nextInt(boardWidth/tileSize); //600/24=24 gibt uns eine Zufallszahl/Positions zwischen 0 und 24 aus
		food.y = random. nextInt(boardHeight/tileSize);
	}
	// diese Funktionen werden alle 100 Milisekunden geupdatet bzw. erneut durchgeführt (siehe gameLoop)
	
	public boolean collision(Tile tile1, Tile tile2)
	{
		//game over wenn der Kopf die Wand berührt; tile1 = Kopf tile2 = Wand diese werden überprüft
		return tile1.x == tile2.x && tile1.y == tile2.y;
	}
	
	public void move()
	{
		//essen essen
		if (collision(snakeHead, food))
		{
			snakeBody.add(new Tile(food.x, food.y));
			placeFood();
		}
		
		//BodyParts bewegen sich jetzt mit dem Kopf, jeder Tile muss sich bewegen bevor der Kopf sich bewegt
		for (int i = snakeBody.size()-1; i >=0; i--)
		{
			Tile snakePart = snakeBody.get(i);
			if (i==0)
			{
				snakePart.x = snakeHead.x;
				snakePart.y = snakeHead.y;
			}
			else
			{
				Tile prevSnakePart = snakeBody.get(i-1);
				snakePart.x = prevSnakePart.x;
				snakePart.y = prevSnakePart.y;
			}
		}
		
		//Snake Head
		snakeHead.x +=velocityX;
		snakeHead.y += velocityY;
		
		//gameOver condition
		for (int i = 0; i <  snakeBody.size(); i++)
		{
			Tile snakePart = snakeBody.get(i);
			//Kollision mit Kopf
			if (collision(snakeHead,snakePart))
			{
				gameOver = true;
			}
		}
		if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth || 
			snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight)
		{
			gameOver = true;
		}
	}
	
	//alle 100 milisekundend wird die spielfläche sozusagen neugestaltet, durch repaint wird draw immer und immer wieder
	//ausgeführt
	@Override
	public void actionPerformed(ActionEvent e)
	{
		move();
		repaint();
		if (gameOver)
		{
			gameLoop.stop();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		//beim Hoch Pfeil bewegt sich die Snake nach oben; nach dem && ist dazu da das Snake nicht rückwärts gehen kann
		if (e.getKeyCode() == KeyEvent.VK_UP && velocityY !=1)
		{
			velocityX = 0;
			velocityY = -1;
		}
		else if (e.getKeyCode()== KeyEvent.VK_DOWN && velocityY !=-1)
		{
			velocityX = 0;
			velocityY = 1;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX !=1)
		{
			velocityX = -1;
			velocityY = 0;
		}
		else if (e.getKeyCode()== KeyEvent.VK_RIGHT && velocityX !=-1)
		{
			velocityX = 1;
			velocityY = 0;
		}
	}
	//brauchen ma net aber müssen wir definieren deswegen sind die trz da
	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}
	

	
	@Override
	public void keyReleased(KeyEvent e)
	{
		throw new UnsupportedOperationException("Unimpleted method 'keyReleased'");
	}
}
