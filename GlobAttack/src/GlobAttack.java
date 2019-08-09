package globattack;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

public class GlobAttack extends PApplet {
    float playerX = 256;
    float playerY = 352;
    boolean left, right, up, down;

    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    float enemySpeed = 1f;

    float bulletSpeed = 5;
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();

    float spawnRate = 300;
    PImage backgroundImg;
    PImage[] explosionAnimation = new PImage[6];

    PImage[] playerAnim = new PImage[6];
    int animationFrame = 1;

    PImage[][] enemyAnimations = new PImage[3][6];

    public static void main(String[] args) {
        PApplet.main("globattack.GlobAttack");
    }

    public void settings() {
        size(512, 704);
    }

    public void setup() {
        backgroundImg = loadImage("Images/Background.png");
        for (int i = 1; i <= 6; i++) {
            playerAnim[i - 1] = loadImage("Images/Bat_Brains_" + i + ".png");
            playerAnim[i - 1].resize(60, 0);
        }
        for (int j = 1; j <= 6; j++) {
            enemyAnimations[0][j - 1] = loadImage("Images/Bat_Purple" + j + ".png");
            enemyAnimations[1][j - 1] = loadImage("Images/Bat_Square" + j + ".png");
            enemyAnimations[2][j - 1] = loadImage("Images/Bat_Booger" + j + ".png");

            enemyAnimations[0][j - 1].resize(60, 0);
            enemyAnimations[1][j - 1].resize(60, 0);
            enemyAnimations[2][j - 1].resize(60, 0);
        }

        for (int i = 1; i <= 6; i++) {
            explosionAnimation[i - 1] = loadImage("Images/Explosion_FX" + i + ".png");
            explosionAnimation[i - 1].resize(60, 0);
        }
    }

    public void draw() {
        drawBackground();
        noStroke();
        if (frameCount % 5 == 0) {
            animationFrame++;
            animationFrame = animationFrame % 6;
        }
        drawPlayer();
        increaseDifficulty();

        for (int b = 0; b < bullets.size(); b++) {
            Bullet bull = bullets.get(b);
            bull.move();
            bull.drawBullet();
            if (bull.x < 0 || bull.x > width || bull.y < 0 || bull.y > height) {
                bullets.remove(b);
            }
        }
        for (int i = 0; i < enemies.size(); i++) {
            Enemy en = enemies.get(i);
            en.move(playerX, playerY);
            en.drawEnemy();
            for (int j = 0; j < bullets.size(); j++) {
                Bullet b = bullets.get(j);
                if (abs(b.x - en.x) < 15 && abs(b.y - en.y) < 15) {
                    enemies.remove(i);
                    bullets.remove(j);
                    break;
                }
            }
            if (abs(playerX - en.x) < 15 && abs(playerY - en.y) < 15) {
                println(" game over ");
            }
        }
    }

    public void drawBackground() {
        background(250);
        imageMode(CORNER);
        image(backgroundImg, 0, 0);
    }

    public void increaseDifficulty() {
        if (frameCount % spawnRate == 0) {
            generateEnemy();
            if (enemySpeed < 3) {
                enemySpeed += 0.1f;
            }
            if (spawnRate > 50) {
                spawnRate -= 10;
            }
        }
    }

    public void generateEnemy() {
        int side = (int) random(0, 2);
        int side2 = (int) random(0, 2);
        if (side % 2 == 0) { // top and bottom
            enemies.add(new Enemy(random(0, width), height * (side2 % 2), (int) random(0, 3)));
        } else { // sides
            enemies.add(new Enemy(width * (side2 % 2), random(0, height), (int) random(0, 3)));
        }
    }

    public void drawPlayer() {
        if (up) {
            playerY -= 5;
        }
        if (left) {
            playerX -= 5;
        }
        if (right) {
            playerX += 5;
        }
        if (down) {
            playerY += 5;
        }
        playerX = constrain(playerX, 70, width - 70);
        playerY = constrain(playerY, 70, height - 70);
        imageMode(CENTER);
        image(playerAnim[animationFrame], playerX, playerY);
    }

    public void mousePressed() {
        float dx = mouseX - playerX;
        float dy = mouseY - playerY;
        float angle = atan2(dy, dx);
        float vx = bulletSpeed * cos(angle);
        float vy = bulletSpeed * sin(angle);
        bullets.add(new Bullet(playerX, playerY, vx, vy));
    }

    public void keyPressed() {
        if (key == 'w') {
            up = true;
        }
        if (key == 'a') {
            left = true;
        }
        if (key == 's') {
            down = true;
        }
        if (key == 'd') {
            right = true;
        }
    }

    public void keyReleased() {
        if (key == 'w') {
            up = false;
        }
        if (key == 'a') {
            left = false;
        }
        if (key == 's') {
            down = false;
        }
        if (key == 'd') {
            right = false;
        }
    }

    class Enemy {
        boolean isDead = false;
        float x, y, vx, vy;
        int enemyType = 0;
        int explosionFrame = 0;

        Enemy(float x, float y, int enemyType) {
            this.x = x;
            this.y = y;
            this.enemyType = enemyType;
        }

        public void drawEnemy() {
            imageMode(CENTER);
            image(enemyAnimations[enemyType][animationFrame], x, y);

            if (isDead == false) {
                imageMode(CENTER);
                image(enemyAnimations[enemyType][animationFrame], x, y);
            } else {
                image(explosionAnimation[explosionFrame], x, y);
            }
        }

        public void move(float px, float py) {
            float angle = atan2(py - y, px - x);
            vx = cos(angle);
            vy = sin(angle);
            x += vx * enemySpeed;
            y += vy * enemySpeed;
        }
    }

    class Bullet {
        float x, y, vx, vy;

        Bullet(float x, float y, float vx, float vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        void drawBullet() {
            fill(0, 255, 0);
            ellipse(x, y, 10, 10);
        }

        void move() {
            x += vx;
            y += vy;
        }
    }
}