// Yihong Yin
// May/04/2024
// CSE 123
// Creative Project 2: Mondrian Art
// TA: Evan Wu
/*This is a Mondrian Art generator which the user can choose the basic mode or
the complex mode. The user will enter the size of canvas, should be no less than
300 * 300, and it will output image randomly.
*/
import java.util.*;
import java.awt.*;

public class Mondrian {
    private Random randy;

    /**
     * Behavior: Constructs a new Mondrian object 
     initializing the random number generator.
     */
    public Mondrian(){
        randy = new Random();
    }

    /**
     * Behavior: Paints a basic Mondrian pattern on the given pixel array.
     * Parameters: pixels - a 2D array of Color representing the canvas
                    num - aim to separate basic and complex versions.
     */
    public void paintBasicMondrian(Color[][] pixels){
        divideCanvas(pixels, 1);
    }

    /**
     * Behavior: Paints a complex Mondrian pattern on the given pixel array.
     * Parameters: pixels - a 2D array of Color representing the canvas
                num - aim to separate basic and complex versions.
     */
    public void paintComplexMondrian(Color[][] pixels){
        divideCanvas(pixels, 2);
    }

    /**
     * Behavior: Divides the entire canvas into parts recursively,
     starting the process of creating a Mondrian art.
     * Parameters: pixels - a 2D array of Color representing the canvas
                    num - aim to separate basic and complex versions.
     */
    public void divideCanvas(Color[][] pixels, int num){
        int oneFourthRowVal = pixels.length * 1/4;
        int oneFourthColVal = pixels[0].length * 1/4;
        divideCanvas(pixels, 0, 0, pixels[0].length, pixels.length, 
                        oneFourthRowVal, oneFourthColVal, num);
    }

    /**
     * Behavior: Divides a specified section of the canvas into smaller 
     parts based on defined criteria, potentially filling sections with color.
     * Parameters:
     *  - pixels - a 2D array of Color representing the canvas
     *  - x1, y1 - the starting coordinates of the region to divide
     *  - x2, y2 - the ending coordinates of the region to divide
     *  - oneFourthRowVal, oneFourthColVal - parameters to decide the 
     division points based on the size of the region
     *  - num - aim to separate basic and complex versions.
     */
    private void divideCanvas(Color[][] pixels, int x1, int y1, int x2, int y2,
                                 int oneFourthRowVal, int oneFourthColVal, int num){

        int height = x2 - x1;
        int width = y2 - y1;

        if(width >= oneFourthRowVal && height >= oneFourthColVal){
            int xMin = x1 + 10;
            int yMin = y1 + 10;
            int xMax = x2 - 10;
            int yMax = y2 - 10;
            // Random randy = new Random();
            if(xMax > xMin && yMax > yMin){
                int horiRand = randy.nextInt(xMin, xMax);
                int vertRand = randy.nextInt(yMin, yMax);
                
                divideCanvas(pixels, horiRand, vertRand, x2, y2, 
                                oneFourthRowVal, oneFourthColVal, num); // bot right
                divideCanvas(pixels, x1, vertRand, horiRand, y2, 
                                oneFourthRowVal, oneFourthColVal, num); // bot left
                divideCanvas(pixels, horiRand, y1, x2, vertRand, 
                                oneFourthRowVal, oneFourthColVal, num); // top right
                divideCanvas(pixels, x1, y1, horiRand, vertRand, 
                                oneFourthRowVal, oneFourthColVal, num); // top left
            }
        }
        //new recursive cases
        //1. region >= 1/4(total height, total width) --> split to 4 smaller 
        //piece by adding 1 horizontal & 1 vertical
        //2. region >= 1/4 total height --> add 1 horizontal line
        //3. region >= 1/4 total width --> add 1 verticle line
        //4. base case: do not divide region(< 1/4(height, width))
        else if (width >= oneFourthRowVal) {
            //Random randy = new Random();
            int yMin = y1 + 10;
            int yMax = y2 - 10;
            if(yMax > yMin){
                int y3 = randy.nextInt(yMin, yMax);
                //randomly divide honrizontally
                divideCanvas(pixels, x1, y1, x2, y3, oneFourthRowVal, oneFourthColVal, num);
                divideCanvas(pixels, x1, y3, x2, y2, oneFourthRowVal, oneFourthColVal, num);
            }
        }
        else if (height >= oneFourthColVal) {
            //Random randy = new Random();
            int xMin = x1 + 10;
            int xMax = x2 - 10;
            if(xMax > xMin){
                int x3 = randy.nextInt(xMin, xMax);
                //randomly divide vertically
                divideCanvas(pixels, x1, y1, x3, y2, oneFourthRowVal, oneFourthColVal, num);
                divideCanvas(pixels, x3, y1, x2, y2, oneFourthRowVal, oneFourthColVal, num);
            }
        }
        else {
            if(num == 1){
                fill(pixels, x1, x2, y1, y2);
            } else {
                fillExtension(pixels, x1, x2, y1, y2);
            }
        }
    }

    //TODO: Paste in 'fill' from the previous slide
    /**
     * Behavior: Fills a specified section of the canvas with a color 
     selected randomly from a predefined set.
     * Parameters:
     *  - pixels - a 2D array of Color representing the canvas
     *  - x1, x2 - horizontal boundaries of the region to fill
     *  - y1, y2 - vertical boundaries of the region to fill
     */
    public void fill(Color[][] pixels, int x1, int x2, int y1, int y2){
        //Random rand = new Random();
        int randomNum = randy.nextInt(4);
        Color theColor = Color.WHITE;
        if(randomNum == 0){
            theColor = Color.GREEN;
        } else if(randomNum == 1){
            theColor = Color.RED;
        } else if(randomNum == 2){
            theColor = Color.YELLOW;
        } else {
            theColor = Color.CYAN;
        }
        //System.out.print(x1 + " " + x2 + " " + y1 + " " + y2);

        for(int i = y1 + 1; i < y2 - 1; i++){
            for(int j = x1 + 1; j < x2 - 1; j++){
                if (i >= 0 && i < pixels.length && j >= 0 && j < pixels[0].length) {
                    pixels[i][j] = theColor;
                }
            }
        }
    }

    /**Below is for the creative extension **/ 
    /**
     * Behavior: Fills a specified section of the canvas with a color 
     selected randomly from a predefined set includes {white,red,yellow,cyan},
      region closer to the top left are more likely to be lighter;
      region closer to the bottom right are darker.
     * Parameters:
     *  - pixels - a 2D array of Color representing the canvas
     *  - x1, x2 - horizontal boundaries of the region to fill
     *  - y1, y2 - vertical boundaries of the region to fill
     */
    public void fillExtension(Color[][] pixels, int x1, int x2, int y1, int y2){
        double centerX = (x1 + x2) / 2 * 1.0;
        double centerY = (y1 + y2) / 2 * 1.0;
        int maxX = pixels[0].length;
        int maxY = pixels.length;

        double relativePosition = (centerX / maxX + centerY / maxY) / 2;
        int randomNum = randy.nextInt(4);
        Color theColor;
        if(randomNum == 0){
            theColor = Color.GREEN;
        } else if(randomNum == 1){
            theColor = Color.RED;
        } else if(randomNum == 2){
            theColor = Color.YELLOW;
        } else {
            theColor = Color.CYAN;
        }
        //System.out.print(x1 + " " + x2 + " " + y1 + " " + y2);
        int red = Math.max(0, theColor.getRed() - (int)(255 * relativePosition));
        int green = Math.max(0, theColor.getGreen() - (int)(255 * relativePosition));
        int blue = Math.max(0, theColor.getBlue() - (int)(255 * relativePosition));

        Color myColor = new Color(red, green, blue);

        for(int i = y1 + 1; i < y2 - 1; i++){
            for(int j = x1 + 1; j < x2 - 1; j++){
                if (i >= 0 && i < pixels.length && j >= 0 && j < pixels[0].length) {
                    pixels[i][j] = myColor;
                }
            }
        }
    }
}
