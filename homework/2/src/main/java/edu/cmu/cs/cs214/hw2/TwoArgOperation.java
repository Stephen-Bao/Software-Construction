package edu.cmu.cs.cs214.hw2;

/**
 * The enum class for operations with 2 integer arguments.
 * @author Xinyu Bao
 */
public enum TwoArgOperation {
    MOV {
        @Override
        void execute (Processor p, int dstReg, int srcReg) {
            int val = p.getRegister(srcReg);
            p.setRegister(dstReg, val);
            p.addPc();
        }
    },
    LOAD {
        @Override
        void execute (Processor p, int desReg, int srcAddrReg) {
            int val = p.getDataMemory(p.getRegister(srcAddrReg));
            p.setRegister(desReg, val);
            p.addPc();
        }
    },
    LOADI {
        @Override
        void execute(Processor p, int desReg, int val) {
            p.setRegister(desReg, val);
            p.addPc();
        }
    },
    STORE {
        @Override
        void execute(Processor p, int desAddrReg, int srcReg) {
            int val = p.getRegister(srcReg);
            p.setDataMemory(p.getRegister(desAddrReg), val);
            p.addPc();
        }
    },


    ADD {
        @Override
        void execute(Processor p, int sumReg, int valReg) {
            int sum = p.getRegister(sumReg) + p.getRegister(valReg);
            p.setRegister(sumReg, sum);
            p.addPc();
        }
    },
    ADDI {
        @Override
        void execute(Processor p, int sumReg, int val) {
            int sum = p.getRegister(sumReg) + val;
            p.setRegister(sumReg, sum);
            p.addPc();
        }
    },
    SUB {
        @Override
        void execute(Processor p, int differenceReg, int valReg) {
            int diff = p.getRegister(differenceReg) - p.getRegister(valReg);
            p.setRegister(differenceReg, diff);
            p.addPc();
        }
    },
    SUBI {
        @Override
        void execute(Processor p, int differenceReg, int val) {
            int diff = p.getRegister(differenceReg) - val;
            p.setRegister(differenceReg, diff);
            p.addPc();
        }
    },
    MUL {
        @Override
        void execute(Processor p, int productReg, int valReg) {
            int product = p.getRegister(productReg) * p.getRegister(valReg);
            p.setRegister(productReg, product);
            p.addPc();
        }
    },
    MULI {
        @Override
        void execute(Processor p, int productReg, int val) {
            int product = p.getRegister(productReg) * val;
            p.setRegister(productReg, product);
            p.addPc();
        }
    },
    DIV {
        @Override
        void execute(Processor p, int quotientReg, int valReg) {
            int quotient = p.getRegister(quotientReg) / p.getRegister(valReg);
            p.setRegister(quotientReg, quotient);
            p.addPc();
        }
    },
    DIVI {
        @Override
        void execute(Processor p, int quotientReg, int val) {
            int quotient = p.getRegister(quotientReg) / val;
            p.setRegister(quotientReg, quotient);
            p.addPc();
        }
    },
    MOD {
        @Override
        void execute(Processor p, int remainderReg, int valReg) {
            int remainder = p.getRegister(remainderReg) % p.getRegister(valReg);
            p.setRegister(remainderReg, remainder);
            p.addPc();
        }
    },
    MODI {
        @Override
        void execute(Processor p, int remainderReg, int val) {
            int remainder = p.getRegister(remainderReg) % val;
            p.setRegister(remainderReg, remainder);
            p.addPc();
        }
    },


    JEQ {
        @Override
        void execute(Processor p, int reg, int addr) {
            if (p.getRegister(reg) == 0) {
                p.setPc(addr);
            }
            else { p.addPc(); }
        }
    },
    JNE {
        @Override
        void execute(Processor p, int reg, int addr) {
            if (p.getRegister(reg) != 0) {
                p.setPc(addr);
            }
            else { p.addPc(); }
        }
    },
    JGT {
        @Override
        void execute(Processor p, int reg, int addr) {
            if (p.getRegister(reg) > 0) {
                p.setPc(addr);
            }
            else { p.addPc(); }
        }
    };

    abstract void execute(Processor p, int lhs, int rhs);
}
