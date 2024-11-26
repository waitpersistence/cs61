package bomb;

import common.IntList;

public class BombMain {
    public static void answers(String[] args) {
        int phase = 2;
        if (args.length > 0) {
            phase = Integer.parseInt(args[0]);
        }
        // TODO: Find the correct inputs (passwords) to each phase using debugging techniques
        Bomb b = new Bomb();
        if (phase >= 0) {
            b.phase0("39291226"/*"Figure; this out. I wonder where the phases are defined..."*/);
        }
        if (phase >= 1) {
            b.phase1(IntList.of(0,9,3,0,8)); // Figure this out too
        }
        if (phase >= 2) {
            int i=0;
            String a="A ";
            while(i<1336){
                a=a+"A ";
                i++;
            }
            a=a+ "-81201430";
            b.phase2(a);
        }
    }
}
