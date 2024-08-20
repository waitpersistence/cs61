package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;

public class AutograderBuddy {

    /**
     * Simulates a game, but doesn't render anything or call any StdDraw
     * methods. Instead, returns the world that would result if the input string
     * had been typed on the keyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quit and
     * save. To "quit" in this method, save the game to a file, then just return
     * the TETile[][]. Do not call System.exit(0) in this method.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public static TETile[][] getWorldFromInput(String input) {
        long seed = getseed(input);
        World buildworld = new World();
        TETile[][] world = buildworld.world;
        world = buildworld.buildwhouse(seed);//第一步完成
        buildworld.buildmaze();

        return world;

        //hrow new RuntimeException("Please fill out AutograderBuddy!");
    }
    public static long getseed(String input){
        String re = "";
        int index=0;
        Random rdm;
        for(int i = 0;i<input.length();i++){
            if(input.charAt(i)=='n'){
                index = i+1;//
                break;
            }
        }
        for(int i =index;i<input.length();i++){
            if(input.charAt(i)!='s'){
                re += input.charAt(i);
            }
        }//先获得种子
        long seed = Long.valueOf(re);
        return seed;
    }


    /**
     * Used to tell the autograder which tiles are the floor/ground (including
     * any lights/items resting on the ground). Change this
     * method if you add additional tiles.
     */
    public static boolean isGroundTile(TETile t) {
        return t.character() == Tileset.FLOOR.character()
                || t.character() == Tileset.AVATAR.character()
                || t.character() == Tileset.FLOWER.character();
    }

    /**
     * Used to tell the autograder while tiles are the walls/boundaries. Change
     * this method if you add additional tiles.
     */
    public static boolean isBoundaryTile(TETile t) {
        return t.character() == Tileset.WALL.character()
                || t.character() == Tileset.LOCKED_DOOR.character()
                || t.character() == Tileset.UNLOCKED_DOOR.character();
    }
}
