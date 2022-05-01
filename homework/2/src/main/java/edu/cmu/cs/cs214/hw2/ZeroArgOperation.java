package edu.cmu.cs.cs214.hw2;

/**
 * The enum class for operations with zero integer arguments.
 * @author Xinyu Bao
 */
public enum ZeroArgOperation {
    RET {
        @Override
        void execute(Processor p) {
            p.setSp(p.getSp() + 1);
            int val = p.getDataMemory(p.getSp());
            p.setPc(val);
        }
    },
    HALT {
        @Override
        void execute(Processor p) {
            p.setEndFlag(true);
        }
    };


    abstract void execute(Processor p);
}
