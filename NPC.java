package npc;

import processing.core.PApplet;
import processing.core.PFont;

public class NPC extends PApplet {

    PFont font;
    PFont playerFont;
    int textYLocation;

    String typing = "";
    String name = "";

    public static void main(String[] args) {
        PApplet.main("npc.NPC");
        String[] fontList = PFont.list();
        printArray(fontList);
    }

    public void settings() { // Set size in settings
        size(600, 500);
    }

    public void setup() {
        font= createFont("Arial",16,true);
        playerFont = createFont("Lucida Bright", 16, true);
    }

    public void draw() {
        background (255, 255, 255);
        textYLocation = 1;
        drawNPCText("Hello adventurer!");
        drawPlayerText("Greetings!");
        drawNPCText("What is your name? Press enter when complete.");
        drawPlayerText(typing);
        if (name != "") {
            drawNPCText("Hello " + name + "! Welcome!" );
        }
    }

    public void drawNPCText(String text) {
        textFont(font);
        fill(200,100,0);
        text(text, 15, 50 + (textYLocation * 20));
        textYLocation += 1;
    }

    public void drawPlayerText(String text) {
        textFont(playerFont);
        fill (100,200,0);
        text(text, 300, 50+(textYLocation * 20));
        textYLocation += 1;
    }

    public void keyPressed() {
        if (key == '\n' ) {
            name = typing;
        } else {
            typing = typing + key;
        }
    }
}