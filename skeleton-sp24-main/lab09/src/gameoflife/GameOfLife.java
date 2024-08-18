package gameoflife;

import edu.princeton.cs.algs4.StdDraw;
import org.reflections.vfs.Vfs;
import picocli.CommandLine;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Am implementation of Conway's Game of Life using StdDraw.
 * Credits to Erik Nelson, Jasmine Lin and Elana Ho for
 * creating the assignment.
 */
public class GameOfLife {

    private static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_HEIGHT = 50;
    private static final String SAVE_FILE = "src/save.txt";
    private long prevFrameTimestep;
    private TERenderer ter;
    private Random random;
    private TETile[][] currentState;
    private int width;
    private int height;

    /**
     * Initializes our world.
     * @param seed
     */
    public GameOfLife(long seed) {
        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;
        ter = new TERenderer();
        ter.initialize(width, height);
        random = new Random(seed);
        TETile[][] randomTiles = new TETile[width][height];
        fillWithRandomTiles(randomTiles);
        currentState = randomTiles;
    }

    /**
     * Constructor for loading in the state of the game from the
     * given filename and initializing it.
     * @param filename
     */
    public GameOfLife(String filename) {
        this.currentState = loadBoard(filename);
        ter = new TERenderer();
        ter.initialize(width, height);
    }

    /**
     * Constructor for loading in the state of the game from the
     * given filename and initializing it. For testing purposes only, so
     * do not modify.
     * @param filename
     */
    public GameOfLife(String filename, boolean test) {
        this.currentState = loadBoard(filename);
    }

    /**
     * Initializes our world without using StdDraw. For testing purposes only,
     * so do not modify.
     * @param seed
     */
    public GameOfLife(long seed, boolean test) {
        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;
        random = new Random(seed);
        TETile[][] randomTiles = new TETile[width][height];
        fillWithRandomTiles(randomTiles);
        currentState = randomTiles;
    }

    /**
     * Initializes our world with a given TETile[][] without using StdDraw.
     * For testing purposes only, so do not modify.
     * @param tiles
     * @param test
     */
    public GameOfLife(TETile[][] tiles, boolean test) {
        TETile[][] transposeState = transpose(tiles);
        this.currentState = flip(transposeState);
        this.width = tiles[0].length;
        this.height = tiles.length;
    }

    /**
     * Flips the matrix along the x-axis.
     * @param tiles
     * @return
     */
    private TETile[][] flip(TETile[][] tiles) {
        int w = tiles.length;
        int h = tiles[0].length;

        TETile[][] rotateMatrix = new TETile[w][h];
        int y = h - 1;
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                rotateMatrix[i][y] = tiles[i][j];
            }
            y--;
        }
        return rotateMatrix;
    }

    /**
     * Transposes the tiles.
     * @param tiles
     * @return
     */
    private TETile[][] transpose(TETile[][] tiles) {
        int w = tiles[0].length;
        int h = tiles.length;

        TETile[][] transposeState = new TETile[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                transposeState[x][y] = tiles[y][x];
            }
        }
        return transposeState;
    }

    /**
     * Runs the game. You don't have to worry about how this method works.
     * DO NOT MODIFY THIS METHOD!
     */
    public void runGame() {
        boolean paused = false;
        long evoTimestamp = System.currentTimeMillis();
        long pausedTimestamp = System.currentTimeMillis();
        long clickTimestamp = System.currentTimeMillis();
        while (true) {
            if (!paused && System.currentTimeMillis() - evoTimestamp > 250) {
                evoTimestamp = System.currentTimeMillis();
                currentState = nextGeneration(currentState);
            }
            if (System.currentTimeMillis() - prevFrameTimestep > 17) {
                prevFrameTimestep = System.currentTimeMillis();

                double mouseX = StdDraw.mouseX();
                double mouseY = StdDraw.mouseY();
                int tileX = (int) mouseX;
                int tileY = (int) mouseY;

                TETile currTile = currentState[tileX % width][tileY % height];

                if (StdDraw.isMousePressed() && System.currentTimeMillis() - clickTimestamp > 250) {
                    clickTimestamp = System.currentTimeMillis();
                    if (currTile == Tileset.CELL) {
                        currentState[tileX][tileY] = Tileset.NOTHING;
                    } else {
                        currentState[tileX][tileY] = Tileset.CELL;
                    }
                }
                if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE) && System.currentTimeMillis() - pausedTimestamp > 500) {
                    pausedTimestamp = System.currentTimeMillis();
                    paused = !paused;
                }
                if (StdDraw.isKeyPressed(KeyEvent.VK_S)) {
                    saveBoard();
                    System.exit(0);
                }
                ter.renderFrame(currentState);
            }
        }
    }


    /**
     * Fills the given 2D array of tiles with RANDOM tiles.
     * @param tiles
     */
    public void fillWithRandomTiles(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = randomTile();
            }
        }
    }

    /**
     * Fills the 2D array of tiles with NOTHING tiles.
     * @param tiles
     */
    public void fillWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Selects a random tile, with a 50% change of it being a CELL
     * and a 50% change of being NOTHING.
     */
    private TETile randomTile() {
        // The following call to nextInt() uses a bound of 3 (this is not a seed!) so
        // the result is bounded between 0, inclusive, and 3, exclusive. (0, 1, or 2)
        int tileNum = random.nextInt(2);
        return switch (tileNum) {
            case 0 -> Tileset.CELL;
            default -> Tileset.NOTHING;
        };
    }

    /**
     * Returns the current state of the board.
     * @return
     */
    public TETile[][] returnCurrentState() {
        return currentState;
    }

    /**
     * At each timestep, the transitions will occur based on the following rules:
     *  1.Any live cell with fewer than two live neighbors dies, as if by underpopulation.
     *  2.Any live cell with two or three neighbors lives on to the next generation.
     *  3.Any live cell with more than three neighbors dies, as if by overpopulation,
     *  4.Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
     * @param tiles
     * @return
     */
    public TETile[][] nextGeneration(TETile[][] tiles) {
//        int width1= tiles.length;
//        int height1 = tiles[0].length;
        TETile[][] nextGen = new TETile[width][height];
        // The board is filled with Tileset.NOTHING
        TETile[][] curtile = tiles;
        fillWithNothing(nextGen);
        for(int y = 0;y<height;y++){
            for(int x = 0;x<width;x++){
                //遍历每一块
                int liveneighbor = countLiveN(curtile,x,y);
                //1.Any live cell with fewer than two live neighbors dies, as if by underpopulation.
                //live cell
                if(curtile[x][y] == Tileset.CELL){
                    if(liveneighbor<2||liveneighbor>3){
                            nextGen[x][y] = Tileset.NOTHING;
                    } else {
                            nextGen[x][y]=Tileset.CELL;
                    }
                } else if (liveneighbor==3) {
                    nextGen[x][y]=Tileset.CELL;
                }

            }
        }
        // TODO: Implement this method so that the described transitions occur.
        // TODO: The current state is represented by TETiles[][] tiles and the next
        // TODO: state/evolution should be returned in TETile[][] nextGen.
        // TODO: Returns the next evolution in TETile[][] nextGen.
        return (nextGen);
    }
    public int countLiveN(TETile[][] tiles,int x,int y){
        int count = 0;
        int width = tiles.length;
        int height = tiles[0].length;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue; // Skip the cell itself
                int nx = x + dx;
                int ny = y + dy;
                //cell代表活着
                if (nx >= 0 && nx < width && ny >= 0 && ny < height && tiles[nx][ny] == Tileset.CELL) {
                    count++;
                }
            }
        }
        return count;

    }

    /**
     * Helper method for saveBoard without rendering and running the game.
     * @param tiles
     */
    public void saveBoard(TETile[][] tiles) {
        TETile[][] transposeState = transpose(tiles);
        this.currentState = flip(transposeState);
        this.width = tiles[0].length;
        this.height = tiles.length;
        saveBoard();
    }

    /**
     * Saves the state of the current state of the board into the
     * save.txt file (make sure it's saved into this specific file).
     * 0 represents NOTHING, 1 represents a CELL.
     */
    public void saveBoard() {
        // TODO: Save the dimensions of the board into the first line of the file.
        // TODO: The width and height should be separated by a space, and end with "\n".
        int width = currentState.length;
        int height = currentState[0].length;
        File file = new File("save.txt");
        List<String> lines = new ArrayList<>();
        String dimensions = width + " " + height;
        lines.add(dimensions);
        // 2. 保存当前棋盘状态
        for (int y = 0; y < height; y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < width; x++) {
                // 假设 `TETile` 有一个 `toString` 方法返回该单元格的字符串表示
                line.append(currentState[x][y].toString());
            }
            line.append("\n");
            lines.add(line.toString());
        }

        // 使用 FileUtils 保存到文件中
        FileUtils.writeFile(file.getName(), lines.toString());



        // TODO: Save the current state of the board into save.txt. You should
        // TODO: use the provided FileUtils functions to help you. Make sure
        // TODO: the orientation is correct! Each line in the board should
        // TODO: end with a new line character.





    }

    /**
     * Loads the board from filename and returns it in a 2D TETile array.
     * 0 represents NOTHING, 1 represents a CELL.
     */
    public TETile[][] loadBoard(String filename) {
        // TODO: Read in the file.

        // TODO: Split the file based on the new line character.

        // TODO: Grab and set the dimensions from the first line.

        // TODO: Create a TETile[][] to load the board from the file into
        // TODO: and any additional variables that you think might help.


        // TODO: Load the state of the board from the given filename. You can
        // TODO: use the provided builder variable to help you and FileUtils
        // TODO: functions. Make sure the orientation is correct!
        // 1. 读取文件内容
        File file = new File(filename);
//        String line = FileUtils.readFile(file.getName());
        String str = FileUtils.readFile(file.getName());
        String[] array = str.split(",");
        List<String> lines =  Arrays.asList(array);

        // 2. 分割文件内容
        if (lines.size() < 1) {
            throw new IllegalArgumentException("File is empty or incorrectly formatted");
        }

        // 3. 提取并设置棋盘尺寸
        String[] dimensions = lines.get(0).split(" ");
        int width = Integer.parseInt(dimensions[0]);
        int height = Integer.parseInt(dimensions[1]);

        // 4. 创建棋盘二维数组
        TETile[][] board = new TETile[width][height];

        // 5. 加载棋盘状态
        for (int y = 0; y < height; y++) {
            String line = lines.get(y + 1);
            for (int x = 0; x < width; x++) {
                // 假设 `TETile.fromString()` 方法可以将字符转为 TETile 对象
                if(line.charAt(x)==1){
                    board[x][y] = Tileset.CELL;
                }else{
                    board[x][y] = Tileset.NOTHING;
                }
            }
        }

        // 6. 返回加载的棋盘
        return board;
        // TODO: Return the board you loaded. Replace/delete this line.
    }

    /**
     * This is where we run the program. DO NOT MODIFY THIS METHOD!
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 2) {
            // Read in the board from a file.
            if (args[0].equals("-l")) {
                GameOfLife g = new GameOfLife(args[1]);
                g.runGame();
            }
            System.out.println("Verify your program arguments!");
            System.exit(0);
        } else {
            long seed = args.length > 0 ? Long.parseLong(args[0]) : (new Random()).nextLong();
            GameOfLife g = new GameOfLife(seed);
            g.runGame();
        }
    }
}
