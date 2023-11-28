package inputs;

import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {
    private GamePanel gamePanel;
    public KeyboardInputs(GamePanel panel) { this.gamePanel = panel; }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Q) gamePanel.addTail();

        switch (e.getKeyCode()){
            case KeyEvent.VK_A -> gamePanel.changeDirection("left");
            case KeyEvent.VK_D -> gamePanel.changeDirection("right");
            case KeyEvent.VK_W -> gamePanel.changeDirection("up");
            case KeyEvent.VK_S -> gamePanel.changeDirection("down");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
