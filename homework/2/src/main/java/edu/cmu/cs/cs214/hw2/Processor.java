package edu.cmu.cs.cs214.hw2;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class mimics the behaviour of a processor, which is able to read a file of assembly code
 * and parse it in order to execute.
 * @author Xinyu Bao
 */
public class Processor {
    private int[] registers;
    private int[] dataMemory;
    static final int memorySize = 2048;
    private ArrayList<String> instructionMemory;
    private boolean endFlag;
    private BufferedReader br;
    private HashSet<String> twoArgKey;
    private HashSet<String> oneArgKey;
    private HashSet<String> zeroArgKey;


    Processor() {
        registers = new int[8];  // R0-R5, PC, SP
        dataMemory = new int[memorySize];
        instructionMemory = new ArrayList<String>();
        registers[7] = memorySize - 1;  // SP initialization
        endFlag = false;
        br = new BufferedReader(new InputStreamReader(System.in));
        twoArgKey = new HashSet<>(Arrays.asList("MOV", "LOAD", "LOADI", "STORE",
                "ADD", "ADDI", "SUB", "SUBI", "MUL", "MULI", "DIV", "DIVI", "MOD", "MODI",
                "JEQ", "JNE", "JGT"));
        oneArgKey = new HashSet<>(Arrays.asList("READ", "WRITE", "JUMP",
                "PUSH", "POP", "CALL"));
        zeroArgKey = new HashSet<>(Arrays.asList("RET", "HALT"));
    }

    void reset() {
        instructionMemory.clear();
        setPc(0);
        setEndFlag(false);
    }

    int getRegister(int index) { return registers[index]; }
    int getPc() { return registers[6]; }
    int getSp() { return registers[7]; }
    int getDataMemory(int addr) { return dataMemory[addr]; }
    String getInstructionMemory(int index) { return instructionMemory.get(index); }
    boolean getEndFlag() { return endFlag; }
    BufferedReader getBr() { return br; }

    void setRegister(int index, int val) { registers[index] = val; }
    void setPc(int val) { registers[6] = val; }
    void setSp(int val) { registers[7] = val; }
    void setDataMemory(int addr, int val) { dataMemory[addr] = val; }
    void setEndFlag(boolean b) { endFlag = b; }
    void setBr(BufferedReader newBr) { br = newBr; }

    void addPc() { registers[6] = registers[6] + 1; }

    void loadFile(String filePath) throws IOException {
        try (Scanner scan = new Scanner(new File(filePath));) {
            while (scan.hasNextLine()) {
                StringBuilder line = new StringBuilder(scan.nextLine());
                int index = -1;
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == ';') {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    line.delete(index, line.length());
                }
                // eliminate possible tab and spaces at the head of each line
                int start = 0;
                if (line.length() != 0 && line.charAt(0) == '\t') { line.delete(0, 1); }
                if (line.length() != 0 && line.charAt(start) == ' ') {
                    for (int end = 1; end < line.length(); end++) {
                        if (line.charAt(end - 1) == ' ' && line.charAt(end) != ' ') {
                            line.delete(start, end);
                            break;
                        }
                        else if (end == line.length() - 1){
                            line.delete(start, line.length());
                        }
                    }
                }

                if (line.length() != 0) {
                    // replace ' ' with 32
                    String regex = "\' \'";
                    String replace = "32";
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(line);
                    String lineStr = m.replaceAll(replace);
                    // if there are multiple instructions in one line, divide and add to memory
                    ArrayList<String> devided = new ArrayList<>();
                    ArrayList<Integer> indexList = new ArrayList<>();
                    Scanner sc = new Scanner(lineStr);
                    while (sc.hasNext()) {
                        devided.add(sc.next());
                    }
                    for (int i = 0; i < devided.size(); i++) {
                        if (twoArgKey.contains(devided.get(i)) || oneArgKey.contains(devided.get(i)) ||
                                zeroArgKey.contains(devided.get(i))) {
                            indexList.add(i);
                        }
                    }
                    for (int i = 0; i < indexList.size(); i++) {
                        StringBuilder sb = new StringBuilder(devided.get(indexList.get(i)));
                        if (i == indexList.size() - 1) {
                            for (int j = indexList.get(i) + 1; j < devided.size(); j++) {
                                sb.append(" ").append(devided.get(j));
                            }
                        } else {
                            for (int j = indexList.get(i) + 1; j < indexList.get(i + 1); j++) {
                                sb.append(" ").append(devided.get(j));
                            }
                        }
                        instructionMemory.add(sb.toString());
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("Could not read file: " + e);
            return;
        }
        // make sure the instruction format is correct
        checkFormat();
    }

    void checkFormat() throws IOException {
        HashSet<String> registerName = new HashSet<String>(Arrays.asList("R0", "R1",
                "R2", "R3", "R4", "R5", "PC", "SP"));
        HashSet<String> format1 = new HashSet<String>(Arrays.asList("MOV",
                "LOAD", "STORE", "ADD", "SUB", "MUL", "DIV", "MOD"));
        HashSet<String> format2 = new HashSet<String>(Arrays.asList("LOADI", "ADDI",
                "SUBI", "MULI", "DIVI", "MODI", "JEQ", "JNE", "JGT"));
        HashSet<String> format3 = new HashSet<String>(Arrays.asList("READ", "WRITE",
                "PUSH", "POP"));
        HashSet<String> format4 = new HashSet<String>(Arrays.asList("JUMP", "CALL"));
        HashSet<String> format5 = new HashSet<String>(Arrays.asList("HALT", "RET"));

        for (String instruction : instructionMemory) {
            ArrayList<String> decodedIns= new ArrayList<String>();
            Scanner scan = new Scanner(instruction);
            while (scan.hasNext()) {
                decodedIns.add(scan.next());
            }
            if (format1.contains(decodedIns.get(0))) {
                if (decodedIns.size() != 3 ||
                        !registerName.contains(decodedIns.get(1)) ||
                        !registerName.contains(decodedIns.get(2))) {
                    System.out.println("Instruction: " + instruction + " is broken!");
                    throw new IOException();
                }
            } else if (format2.contains(decodedIns.get(0))) {
                if (decodedIns.size() != 3 ||
                        !registerName.contains(decodedIns.get(1))) {
                    System.out.println("Instruction: " + instruction + " is broken!");
                    throw new IOException();
                }
                // check if the number is of char format
                String numberStr = decodedIns.get(2);
                if (numberStr.length() == 3 && numberStr.charAt(0) == '\'' &&
                        numberStr.charAt(2) == '\'') {
                    continue;
                }
                // if not, try to parse the arg as integer
                try {
                    Integer.parseInt(decodedIns.get(2));
                } catch (NumberFormatException e){
                    System.out.println("Instruction: " + instruction + " is broken!");
                    throw new IOException();
                }
            } else if (format3.contains(decodedIns.get(0))) {
                if (decodedIns.size() != 2 || !registerName.contains(decodedIns.get(1))) {
                    System.out.println("Instruction: " + instruction + " is broken!");
                    throw new IOException();
                }
            } else if (format4.contains(decodedIns.get(0))) {
                if (decodedIns.size() != 2) {
                    System.out.println("Instruction: " + instruction + " is broken!");
                    throw new IOException();
                }
                try {
                    Integer.parseInt(decodedIns.get(1));
                } catch (NumberFormatException e){
                    System.out.println("Instruction: " + instruction + " is broken!");
                    throw new IOException();
                }
            } else if (format5.contains(decodedIns.get(0))) {
                if (decodedIns.size() != 1) {
                    System.out.println("Instruction: " + instruction + " is broken!");
                    throw new IOException();
                }
            } else {
                System.out.println("Instruction: " + instruction + " is broken!");
                throw new IOException();
            }
        }
    }

    void printInstructionMemory() {
        for (String instruction : instructionMemory) {
            System.out.println(instruction);
        }
    }

    private int parseAsInt(String s) {
        if (s.equals("R0")) { return 0; }
        else if (s.equals("R1")) { return 1; }
        else if (s.equals("R2")) { return 2; }
        else if (s.equals("R3")) { return 3; }
        else if (s.equals("R4")) { return 4; }
        else if (s.equals("R5")) { return 5; }
        else if (s.equals("PC")) { return 6; }
        else if (s.equals("SP")) { return 7; }
        else if (s.length() == 3 && s.charAt(0) == '\'' &&
                    s.charAt(2) == '\'') {
            return s.charAt(1);
        }
        else { return Integer.parseInt(s); }
    }

    void execute() {
        while (!endFlag) {
            Scanner scan = new Scanner(instructionMemory.get(getPc()));
            String opString = scan.next();

            if (twoArgKey.contains(opString)) {
                TwoArgOperation op = TwoArgOperation.valueOf(opString);
                int lhs = parseAsInt(scan.next());
                int rhs = parseAsInt(scan.next());
                op.execute(this, lhs, rhs);
            } else if (oneArgKey.contains(opString)) {
                OneArgOperation op = OneArgOperation.valueOf(opString);
                int arg = parseAsInt(scan.next());
                op.execute(this, arg);
            } else if (zeroArgKey.contains(opString)) {
                ZeroArgOperation op = ZeroArgOperation.valueOf(opString);
                op.execute(this);
            }
        }
    }
}
















