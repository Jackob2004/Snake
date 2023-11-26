package main;

public class Game implements Runnable{
    private GamePanel panel;

    private GameWindow window;

    private Thread thread;
    public Game(){
        panel = new GamePanel(this);
        window = new GameWindow(panel);

        panel.setFocusable(true);
        panel.requestFocusInWindow();
        panel.requestFocus();
        startGameLoop();
    }

    private void startGameLoop(){
        thread = new Thread(this);
        thread.start();
    }


    @Override
    public void run() {
        double refreshTime = 180000000;
        long lastFrame = System.nanoTime();
        long now = System.nanoTime();

        while (true){

            now = System.nanoTime();
            if (now - lastFrame >= refreshTime){
                if(panel.isGameOn()){
                    panel.moveMethod();
                    panel.repaint();
                }
                lastFrame = now;
            }
        }
    }

    public GameWindow getWindow() {
        return window;
    }
}
