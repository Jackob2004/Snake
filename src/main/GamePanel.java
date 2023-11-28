package main;

import inputs.KeyboardInputs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.Random;

public class GamePanel extends JPanel {
    private KeyboardInputs keyboardInputs;

    private Game game;

    private Random random;

    private int xApple = 480, yApple = 480;

    private LinkedList<SnakeTail> tails;

    private Rectangle appleRect;

    private Rectangle snakeHeadRect;

    private String direction = "right";

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

    private void initializeTail(){ // creating snake at the start of the game
        tails = new LinkedList<>();

        tails.add(new SnakeTail(120,480,"right"));
        tails.add(new SnakeTail(80,480,"right"));
        tails.add(new SnakeTail(40,480,"right"));
    }

    private void initializeHitBox(){
        appleRect = new Rectangle(xApple,yApple,40,40);
        snakeHeadRect = new Rectangle(tails.getFirst().getX(),tails.getFirst().getY(),40,40);
    }

    private void spawnApple(){ // spawning apple outside snake and updating hit-box location
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

    private boolean randomPosition(){ // getting random location for apple outside snake
        xApple = random.nextInt(24) * 40;
        yApple = random.nextInt(24) * 40;
        for (SnakeTail tail : tails){
            if (xApple == tail.getX() || yApple == tail.getY()){
                return false;
            }
        }
        return true;
    }

    private void canMove(){ // checking if snake is out of map or if he touched his body
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

    public void changeDirection(String direction) {
        if(tails.getFirst().getDirection().equals("right") && tails.get(1).getDirection().equals("right") && direction.equals("left")) return;
        else if (tails.getFirst().getDirection().equals("left") && tails.get(1).getDirection().equals("left") && direction.equals("right")) return;
        else if (tails.getFirst().getDirection().equals("up") && tails.get(1).getDirection().equals("up") && direction.equals("down")) return;
        else if (tails.getFirst().getDirection().equals("down") && tails.get(1).getDirection().equals("down") && direction.equals("up")) return;

        this.direction = direction;
    }

    public void moveMethod(){ // updating  snake's position

        for (SnakeTail tail : tails) {
            if (!tails.get(0).getDirection().equals(direction)) { // head position
                tails.get(0).setDirection(direction);
            }
            switch (tail.getDirection()) {
                case "up" -> tail.setY(tail.getY() - 40);
                case "down" -> tail.setY(tail.getY() + 40);
                case "left" -> tail.setX(tail.getX() - 40);
                case "right" -> tail.setX(tail.getX() + 40);
            }
        }

        for (int i = tails.size()-1; i > 0; i--) { // updating tail position one by one each iteration of game loop
            if (!tails.get(i).getDirection().equals(tails.get(i-1).getDirection())){
                tails.get(i).setDirection(tails.get(i-1).getDirection());
            }
        }
        canMove();
        spawnApple();
    }

    private void endGame(){ // window with play again option and exit option
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
    public void paintComponent(Graphics g){ // painting snake and apple
        super.paintComponent(g);

        g.setColor(new Color(16, 48, 239));
        for (SnakeTail tail : tails) {
            g.fillRect(tail.getX(), tail.getY(), 40, 40);
            g.setColor(Color.BLUE);
        }
        g.setColor(Color.RED);
        g.fillRect(xApple, yApple, 40, 40);
    }

    public boolean isGameOn() { return gameOn; }
}


