package core;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.w3c.dom.css.Rect;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.*;

//生成world
public class World {
    // build your own world!
    Stack<int[]> stack = new Stack<>();
    private final int WIDTH = 62;
    private final int HEIGHT = 42;
    public Random rdm;
    private int[][] directionOffsets = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
    TETile[][] world = new TETile[WIDTH][HEIGHT];
//    Stack<int[]> stack = new Stack<>();
    public Stack<int[]> points = new Stack<>();
    World() {//初始化为全为草
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.GRASS;
            }
        }
        for (int h = 0; h <HEIGHT; h++) {
            world[0][h] = Tileset.WALL;
            world[WIDTH-1][h] = Tileset.WALL;
        }
        for (int l = 0; l < WIDTH; l++) {
            world[l][0] = Tileset.WALL;
            world[l][HEIGHT-1] = Tileset.WALL;
        }
    }

    //    放置一堆随机的互相不会覆盖的房间；
//    把房间之外的空地用迷宫填满；
//    将所有相邻的迷宫和房间连接起来，同时也增加少量的连接；
//    移除掉所有的死胡同。
    public TETile[][] buildwhouse(long seed) {
        rdm = new Random(seed);
        int HOUSETRY = 500;//尝试次数
        for (int i = 0; i < HOUSETRY; i++) {
            int size = rdm.nextInt(3, 6) * 2 + 1;//必须为奇数
            int xiuzheng = rdm.nextInt(1 + size / 2) * 2;//防止过于狭长
            int width = size;
            int height = size;
            if (rdm.nextInt(100)%2==0) {
                width+=xiuzheng;
            } else {
                height+=xiuzheng;
            }//创建好房间尺寸
            int x = rdm.nextInt((WIDTH-width)/2)*2+1;
            int y = rdm.nextInt((HEIGHT-height)/2)*2+1;
            int dx = width / 2;
            int dy = height / 2;
            if (x - dx > 1 && y - dy > 1 && x + dx < WIDTH - 1 && y + dy < HEIGHT - 1) {
                House house = new House(x, y, width, height);
                if (!house.overlap()) {
                    house.build();
                }
            }
        }
        return world;

    }

    public class House {
        int x;
        int y;
        int dx;
        int dy;

        House(int h_x, int h_y, int h_width, int h_height) {
            x = h_x;
            y = h_y;
            dx = h_width / 2;
            dy = h_height / 2;
        }

        public void build() {
//            if(x-dx>0&&y-dy>0&&x+dx<WIDTH&&y+dy<HEIGHT){
            //先造两条边
            for (int h = y - dy; h <= y + dy; h++) {
                world[x - dx][h] = Tileset.WALL;
                world[x + dx][h] = Tileset.WALL;
            }
            for (int l = x - dx; l <= x + dx; l++) {
                world[l][y - dy] = Tileset.WALL;
                world[l][y + dy] = Tileset.WALL;
            }
            //铺地板
            for (int m = x - dx + 1; m < x + dx; m++) {
                for (int s = y - dy + 1; s < y + dy; s++) {
                    world[m][s] = Tileset.CELL;
                }
            }

            //}
        }

        public boolean overlap() {
//            if(x-dx>0&&y-dy>0&&x+dx<WIDTH&&y+dy<HEIGHT){
            for (int h = y - dy; h <= y + dy; h++) {
                if (world[x - dx][h] == Tileset.WALL
                        || world[x + dx][h] == Tileset.WALL
                        || world[x - dx-1][h] == Tileset.WALL
                        || world[x + dx+1][h] == Tileset.WALL//使间隔1tile
                        || world[x - dx][h] == Tileset.CELL//排除包含
                        || world[x + dx][h] == Tileset.CELL
                ) {
                    return true;
                }
            }
            for (int l = x - dx; l <= x + dx; l++) {
                if (world[l][y - dy] == Tileset.WALL
                        || world[l][y + dy] == Tileset.WALL
                        || world[l][y - dy] == Tileset.CELL
                        || world[l][y + dy] == Tileset.CELL
                        || world[l][y - dy -1] == Tileset.WALL
                        || world[l][y + dy +1] == Tileset.WALL) {
                    return true;
                }
                //}
            }
            return false;
        }
    }

    //用dfs生成迷宫
//    初始化大地图，只有0和1的状态。其中，0和1分别代表道路和墙体，注意四周皆墙
//    靠近边缘随机选取状态为1的道路点，作为起点 a
//    在起点 a 的上下左右四个方向，跨两个寻找同样为1的道路点 c
//    如果找到，则将它们之间的墙体 b 打通，然后将 c 作为新的起点，然后继续进行第2步
//    如果四个方向都没有找到，则不断回退到上一点，直到找到有一点其他方向满足条件，然后继续查询(使用栈进行回退)
//    当查无可查的时候，迷宫也就填充完毕.
    //用单独数字表示方向不好
    /**用chatgpt生成加自己修正
     * */
    public boolean isvalid(int x,int y){
        return (x>0&&x<WIDTH-1&&y>0&&y<HEIGHT-1);
    }
    public void shuffleArray(int[][] direction){
        for (int i = direction.length - 1; i > 0; i--) {
            int index = rdm.nextInt(i + 1);
            int[] temp = direction[index];
            direction[index] = direction[i];
            direction[i] = temp;
        }

    }
    public void buildmaze() {
        Deque<int[]> deque = new LinkedList<>();
       Stack<int[]> stack = new Stack<>();
        //step 1:初始化
        for (int maze = 2; maze < WIDTH; maze = maze + 2) {
            for (int y_maze = 2; y_maze < HEIGHT; y_maze += 2) {
                if(world[maze][y_maze]==Tileset.GRASS&&!fujin(maze,y_maze)) {
                    world[maze][y_maze] = Tileset.UNLOCKED_DOOR;
                }
            }
        }
        //step 2
        //1.随机找点
        int start_x;
        int start_y;
        while(true){
            int x=rdm.nextInt(WIDTH);
            int y = rdm.nextInt(HEIGHT);
            if(world[x][y]==Tileset.UNLOCKED_DOOR){
                start_y=y;
                start_x=x;
                break;
            }
        }
        growmaze(start_x,start_y);

        }
        public void growmaze (int x, int y) {
            int[][] directions = {{0, 2}, {2, 0}, {0, -2}, {-2, 0}};
//            Stack<int[]> stack = new Stack<>();
            stack.push(new int[]{x, y});
            while (!stack.isEmpty()) {
                int[] current = stack.peek();
                int now_x = current[0];
                int now_y = current[1];
                boolean found = false;

                // 打乱方向的顺序
                shuffleArray(directions);

                for (int[] direction : directions) {
                    int newX = now_x + direction[0];
                    int newY = now_y + direction[1];

                    // 检查新位置是否有效且是墙
                    //要用两个栈
                    //一个用于保存走过的点
                    //一个用于回退
                    //DFS走出来的没有循环的
                    if ((isvalid(newX, newY)) && world[newX][newY] == Tileset.UNLOCKED_DOOR&&!containsPoint(points,new int[]{newX,newY})) {
//                    &&world[x + direction[0] / 2][y + direction[1] / 2] != Tileset.) {
                        // 打通墙壁
                        world[now_x + direction[0] / 2][now_y + direction[1] / 2] = Tileset.UNLOCKED_DOOR;
                        //world[newX][newY] = Tileset.UNLOCKED_DOOR;
                        // 推入新位置到栈中
                        points.add(new int[]{newX,newY});
                        stack.push(new int[]{newX, newY});
                        found = true;
                        break;
                    }
                }
                // 如果没有找到可行的方向，回退
                if (!found) {
                    stack.pop();
                }
//            if(stack.size()==18){
//                return;
//            }
            }

        }
    public boolean fujin(int x,int y){
        if(world[x-1][y]==Tileset.WALL
        ||world[x+1][y]==Tileset.WALL
        ||world[x][y-1]==Tileset.WALL
        ||world[x][y+1]==Tileset.WALL
        ||world[x-1][y+1]==Tileset.WALL
                ||world[x-1][y-1]==Tileset.WALL
                ||world[x+1][y-1]==Tileset.WALL
                ||world[x+1][y+1]==Tileset.WALL
        ){
            return true;
        }
        return false;
    }
    public boolean containsPoint(Stack<int[]> stack, int[] point) {
        for (int[] p : stack) {
            if (Arrays.equals(p, point)) {
                return true;
            }
        }
        return false;
    }



}


