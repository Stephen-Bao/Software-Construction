package edu.cmu.cs.cs214.hw2;

import java.io.IOException;

/**
 * The enum class for operations with one integer arguments.
 * @author Xinyu Bao
 */
public enum OneArgOperation {
    READ {
        @Override
        void execute(Processor p, int reg) {
            char c;
            try {
                c = (char)p.getBr().read();
                p.setRegister(reg, c);
            } catch (IOException e) {
                System.err.println("READ exception: " + e);
                return;
            }
            p.addPc();
        }
    },
    WRITE {
        @Override
        void execute(Processor p, int reg) {
            char ch = (char)p.getRegister(reg);
            System.out.print(ch);
            p.addPc();
        }
    },


    JUMP {
        @Override
        void execute(Processor p, int addr) {
            p.setPc(addr);
        }
    },


    PUSH {
        @Override
        void execute(Processor p, int reg) {
            int val = p.getRegister(reg);
            p.setDataMemory(p.getSp(), val);
            p.setSp(p.getSp() - 1);
            p.addPc();
        }
    },
    POP {
        @Override
        void execute(Processor p, int reg) {
            p.setSp(p.getSp() + 1);
            int val = p.getDataMemory(p.getSp());
            p.setRegister(reg, val);
            p.addPc();
        }
    },
    CALL {
        @Override
        void execute(Processor p, int addr) {
            int retAddr = p.getPc() + 1;
            p.setDataMemory(p.getSp(), retAddr);
            p.setSp(p.getSp() - 1);
            p.setPc(addr);
        }
    };

    abstract void execute(Processor p, int arg);
}















