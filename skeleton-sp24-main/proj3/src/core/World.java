package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.*;

public class World {
    private final int WIDTH = 62;
    private final int HEIGHT = 42;
    public TETile[][] world = new TETile[WIDTH][HEIGHT];
    private Stack<int[]> stack = new Stack<>();
    private Stack<int[]> points = new Stack<>();
    private Random rdm;

    // 构造函数，初始化地图
    public World() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.LOCKED_DOOR;
            }
        }
        for (int h = 0; h < HEIGHT; h++) {
            world[0][h] = Tileset.WALL;
            world[WIDTH - 1][h] = Tileset.WALL;
        }
        for (int l = 0; l < WIDTH; l++) {
            world[l][0] = Tileset.WALL;
            world[l][HEIGHT - 1] = Tileset.WALL;
        }
    }

    // 创建世界，放置房间并生成迷宫
    public TETile[][] buildWorld(long seed) {
        rdm = new Random(seed);
        generateRooms();
        buildMaze();
        connectRegions();
        return world;
    }

    // 生成房间
    public void generateRooms() {
        int HOUSETRY = 500;
        for (int i = 0; i < HOUSETRY; i++) {
            int size = rdm.nextInt(3, 6) * 2 + 1;
            int adjustment = rdm.nextInt(1 + size / 2) * 2;
            int width = size;
            int height = size;
            if (rdm.nextInt(100) % 2 == 0) {
                width += adjustment;
            } else {
                height += adjustment;
            }
            int x = rdm.nextInt((WIDTH - width) / 2) * 2 + 1;
            int y = rdm.nextInt((HEIGHT - height) / 2) * 2 + 1;
            int dx = width / 2;
            int dy = height / 2;
            if (x - dx > 2 && y - dy > 2 && x + dx < WIDTH - 2 && y + dy < HEIGHT - 2) {
                House house = new House(x, y, width, height);
                if (!house.overlap()) {
                    house.build();
                }
            }
        }
    }

    // 房间类
    private class House {
        int x, y, dx, dy;

        House(int h_x, int h_y, int h_width, int h_height) {
            x = h_x;
            y = h_y;
            dx = h_width / 2;
            dy = h_height / 2;
        }

        void build() {
            for (int h = y - dy; h <= y + dy; h++) {
                world[x - dx][h] = Tileset.WALL;
                world[x + dx][h] = Tileset.WALL;
            }
            for (int l = x - dx; l <= x + dx; l++) {
                world[l][y - dy] = Tileset.WALL;
                world[l][y + dy] = Tileset.WALL;
            }
            for (int m = x - dx + 1; m < x + dx; m++) {
                for (int s = y - dy + 1; s < y + dy; s++) {
                    world[m][s] = Tileset.CELL;
                }
            }
        }

        boolean overlap() {
            for (int h = y - dy; h <= y + dy; h++) {
                if (world[x - dx][h] != Tileset.LOCKED_DOOR
                        || world[x + dx][h] != Tileset.LOCKED_DOOR
                        || world[x - dx - 2][h] != Tileset.LOCKED_DOOR
                        || world[x + dx + 2][h] != Tileset.LOCKED_DOOR) {
                    return true;
                }
            }
            for (int l = x - dx; l <= x + dx; l++) {
                if (world[l][y - dy] != Tileset.LOCKED_DOOR
                        || world[l][y + dy] != Tileset.LOCKED_DOOR
                        || world[l][y - dy - 3] != Tileset.LOCKED_DOOR
                        || world[l][y + dy + 3] != Tileset.LOCKED_DOOR) {
                    return true;
                }
            }
            return false;
        }
    }

    // 生成迷宫
    public void buildMaze() {
        for (int mazeX = 2; mazeX < WIDTH; mazeX += 2) {
            for (int mazeY = 2; mazeY < HEIGHT; mazeY += 2) {
                if (world[mazeX][mazeY] == Tileset.LOCKED_DOOR) {
                    world[mazeX][mazeY] = Tileset.NOTHING;
                }
            }
        }

        while (true) {
            int x = rdm.nextInt(WIDTH);
            int y = rdm.nextInt(HEIGHT);
            if (world[x][y] == Tileset.NOTHING) {
                growMaze(x, y);
                break;
            }
        }
    }

    // 生长迷宫
    public void growMaze(int x, int y) {
        int[][] directions = {{0, 2}, {2, 0}, {0, -2}, {-2, 0}};
        stack.push(new int[]{x, y});
        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int now_x = current[0];
            int now_y = current[1];
            boolean found = false;

            shuffleArray(directions);

            for (int[] direction : directions) {
                int newX = now_x + direction[0];
                int newY = now_y + direction[1];

                if (isValid(newX, newY) && world[newX][newY] == Tileset.NOTHING && !containsPoint(points, new int[]{newX, newY})) {
                    world[now_x + direction[0] / 2][now_y + direction[1] / 2] = Tileset.NOTHING;
                    points.add(new int[]{newX, newY});
                    stack.push(new int[]{newX, newY});
                    found = true;
                    break;
                }
            }

            if (!found) {
                stack.pop();
            }
        }
    }

    // 打乱方向数组
    private void shuffleArray(int[][] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = rdm.nextInt(i + 1);
            int[] temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    // 判断是否是有效位置
    private boolean isValid(int x, int y) {
        return (x > 0 && x < WIDTH - 1 && y > 0 && y < HEIGHT - 1);
    }

    // 检查点是否存在于集合中
    private boolean containsPoint(Stack<int[]> stack, int[] point) {
        for (int[] p : stack) {
            if (Arrays.equals(p, point)) {
                return true;
            }
        }
        return false;
    }

    // 连接区域
    public void connectRegions() {
        ArrayList<int[]> passageCandidates = new ArrayList<>();
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        for (int i = 1; i < WIDTH - 1; ++i) {
            for (int j = 1; j < HEIGHT - 1; ++j) {
                if (world[i][j] == Tileset.WALL) {
                    int regionCount = 0;
                    for (int[] dir : directions) {
                        int adjX = i + dir[0];
                        int adjY = j + dir[1];
                        if (world[adjX][adjY] != Tileset.WALL) {
                            regionCount++;
                        }
                    }
                    if (regionCount == 2) {
                        passageCandidates.add(new int[]{i, j});
                    }
                }
            }
        }

        while (!passageCandidates.isEmpty()) {
            int randomIndex = rdm.nextInt(passageCandidates.size());
            int[] selectedTile = passageCandidates.get(randomIndex);
            passageCandidates.remove(randomIndex);
            if (!IsInUnion(selectedTile)) {
                world[selectedTile[0]][selectedTile[1]] = Tileset.FLOOR;
                AddToUnion(selectedTile);
                ConnectRoomsAndPathsVia(selectedTile);
            }
        }
    }

    // 判断是否在同一个联通集中
    private boolean IsInUnion(int[] tile) {
        // 判断某个tile是否属于某个房间或路径的连通集。
        // 简单的思路是，如果这个tile周围的任何一块tile是非墙体（即属于路径或房间），
        // 那么这个tile就已经是连通的，可以不需要处理。
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        for (int[] dir : directions) {
            int adjX = tile[0] + dir[0];
            int adjY = tile[1] + dir[1];
            if (world[adjX][adjY] != Tileset.WALL) {
                return true;
            }
        }
        return false;
    }

    // 将选中的连接点加入到联通集中
    private void AddToUnion(int[] selectedTile) {
        // 简单的实现方式是将这个点周围的空地（即属于路径或房间的点）标记为连通的
        world[selectedTile[0]][selectedTile[1]] = Tileset.FLOOR;

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        for (int[] dir : directions) {
            int adjX = selectedTile[0] + dir[0];
            int adjY = selectedTile[1] + dir[1];
            if (world[adjX][adjY] == Tileset.NOTHING) {
                world[adjX][adjY] = Tileset.FLOOR;
            }
        }

    }

    // 通过选中的连接点连接房间和路径
    private void ConnectRoomsAndPathsVia(int[] selectedTile) {
        // 这个方法可以用来确保选中的连接点将路径和房间连通
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        for (int[] dir : directions) {
            int adjX = selectedTile[0] + dir[0];
            int adjY = selectedTile[1] + dir[1];
            if (world[adjX][adjY] == Tileset.NOTHING) {
                world[adjX][adjY] = Tileset.FLOOR;
            }
        }
    }
}
