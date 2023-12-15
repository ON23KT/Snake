import javax.swing.*;
public class first {

	public static void main(String[] args) throws Exception 
	{
		//größe der Spielfläche
				int boardWidth = 600;
				int boardHeight = boardWidth;
				
				//Spiel Fenster erstellen
				JFrame frame = new JFrame("Snake");
				frame.setVisible(true);
				frame.setSize(boardWidth, boardHeight);
				//Fenster wird in der Mitte des Display patziert
				frame.setLocationRelativeTo(null);
				frame.setResizable(false);
				//Spiel wird geschlossen beim Klick auf das X
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
				frame.add(snakeGame);
				//Leiste oben wird jetzt außerhalb des Spielfelds platziert
				frame.pack();
				snakeGame.requestFocus();
				
	}

}
