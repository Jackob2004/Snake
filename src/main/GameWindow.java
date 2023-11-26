package main;

import javax.swing.*;

public class GameWindow {
    private JFrame frame;
    public GameWindow(GamePanel panel){
        frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.pack();

        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }
}
