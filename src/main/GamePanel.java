package main;

import inputs.KeyboardInputs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.Random;

public class GamePanel extends JPanel {
    private KeyboardInputs keyboardInputs;

    private int xApple = 480, yApple = 480;

    private Rectangle appleRect;

    private Random random;

    private Rectangle snakeHeadRect;
    private Game game;

    private String direction = "right";
    private LinkedList<SnakeTail> tails;

    private boolean gameOn = true;

    private boolean appleSpawn = false;

    public GamePanel(Game game){
        keyboardInputs = new KeyboardInputs(this);

        setPanelSize();
        setBackground(Color.BLACK);
        addKeyListener(keyboardInputs);
        initializeTail();
        initializeHitBox();

        this.game = game;
    }


    private void setPanelSize() {
        Dimension dimension = new Dimension(1000,1000);
        setPreferredSize(dimension);
    }

    private void initializeHitBox(){
        appleRect = new Rectangle(xApple,yApple,40,40);
        snakeHeadRect = new Rectangle(tails.getFirst().getX(),tails.getFirst().getY(),40,40);
    }

    private void initializeTail(){
        tails = new LinkedList<>();

        tails.add(new SnakeTail(120,480,"right"));
        tails.add(new SnakeTail(80,480,"right"));
        tails.add(new SnakeTail(40,480,"right"));
    }


    private void spawnApple(){
        random = new Random();
        if (appleRect.contains(tails.getFirst().getX(),tails.getFirst().getY())){
            while (!appleSpawn){
                appleSpawn = randomPosition();
            }
            appleSpawn = false;
            appleRect.setLocation(xApple,yApple);
            addTail();
        }
    }

     private boolean randomPosition(){
        xApple = random.nextInt(24) * 40;
        yApple = random.nextInt(24) * 40;
        for (SnakeTail tail : tails){
            if (xApple == tail.getX() || yApple == tail.getY()){
                return false;
            }
        }
        return true;
    }

    private void canMove(){
        if(tails.get(0).getX() <= -1 || tails.get(0).getX() >= 1001 || tails.get(0).getY() <= -1 || tails.get(0).getY() >= 1001){
            gameOn= false;
            endGame();
        }
        snakeHeadRect.setLocation(tails.getFirst().getX(),tails.getFirst().getY());
        for (int i = 1; i < tails.size(); i++) {
            if (snakeHeadRect.contains(tails.get(i).getX(),tails.get(i).getY())) {
                gameOn= false;
                endGame();
            }
        }

    }

    public void addTail(){
        switch (tails.getLast().getDirection()){
            case "up" ->  tails.add(new SnakeTail(tails.getLast().getX(),tails.getLast().getY()+40,tails.getLast().getDirection()));
            case "down" -> tails.add(new SnakeTail(tails.getLast().getX(),tails.getLast().getY()-40,tails.getLast().getDirection()));
            case "left" -> tails.add(new SnakeTail(tails.getLast().getX()+40,tails.getLast().getY(),tails.getLast().getDirection()));
            case "right" -> tails.add(new SnakeTail(tails.getLast().getX()-40,tails.getLast().getY(),tails.getLast().getDirection()));
        }
    }



    public void changeDirection(String direction) { this.direction = direction; }
    public void moveMethod(){

        for (SnakeTail tail : tails) {
            if (!tails.get(0).getDirection().equals(direction)) {
                tails.get(0).setDirection(direction);
            }
            switch (tail.getDirection()) {
                case "up" -> tail.setY(tail.getY() - 40);
                case "down" -> tail.setY(tail.getY() + 40);
                case "left" -> tail.setX(tail.getX() - 40);
                case "right" -> tail.setX(tail.getX() + 40);
            }

        }

        for (int i = tails.size()-1; i > 0; i--) {
            if (!tails.get(i).getDirection().equals(tails.get(i-1).getDirection())){
                tails.get(i).setDirection(tails.get(i-1).getDirection());
            }
        }
        canMove();
        spawnApple();

    }

    private void endGame(){
        int option = JOptionPane.showConfirmDialog(this,"Score: " + (tails.size()-3) + " Wanna play again ?");

        if (option == JOptionPane.NO_OPTION) game.getWindow().getFrame().dispatchEvent(new WindowEvent(game.getWindow().getFrame(),WindowEvent.WINDOW_CLOSING));
        else if (option == JOptionPane.YES_OPTION) {
            direction = "right";
            xApple = 480;
            yApple = 480;
            appleSpawn = false;

            initializeTail();
            initializeHitBox();

            gameOn = true;
        }
    }



    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(new Color(16, 48, 239));
        for (SnakeTail tail : tails) {
            g.fillRect(tail.getX(), tail.getY(), 40, 40);
            g.setColor(Color.BLUE);
        }
        g.setColor(Color.RED);
        g.fillRect(xApple, yApple, 40, 40);
    }

    public boolean isGameOn() {
        return gameOn;
    }
}


