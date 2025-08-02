//I did the 2ed method that the professor said we could do in class it will be a little harder
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Disassembler {

    private static final String[] CONDITIONS = {
        "EQ", "NE", "HS", "LO", "MI", "PL", "VS", "VC",
        "HI", "LS", "GE", "LT", "GT", "LE"
    };

    private static final Map<Integer, String> labelMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Disassembler <binaryfile>");
            return;
        }

        FileInputStream fis = new FileInputStream(args[0]);
        DataInputStream dis = new DataInputStream(fis);
        int address = 0;

        // First pass: collect labels
        while (dis.available() >= 4) {
            int instruction = dis.readInt();
            checkBranchTarget(instruction, address);
            address += 4;
        }
        dis.close();

        // Second pass: disassemble
        fis = new FileInputStream(args[0]);
        dis = new DataInputStream(fis);
        address = 0;

        while (dis.available() >= 4) {
            int instruction = dis.readInt();
            if (labelMap.containsKey(address)) {
                System.out.printf("%s:\n", labelMap.get(address));
            }
            decodeInstruction(instruction, address);
            address += 4;
        }
        dis.close();
    }

    private static void checkBranchTarget(int inst, int currentAddress) {
        int op6 = (inst >>> 26) & 0x3F;
        int op8 = (inst >>> 24) & 0xFF;

        if (op6 == 0b000101 || op6 == 0b100101) { // B or BL
            int offset = signExtend(inst & 0x3FFFFFF, 26) << 2;
            labelMap.put(currentAddress + offset, generateLabel(currentAddress + offset));
        } else if (op8 == 0b10110100 || op8 == 0b10110101 || op8 == 0b01010100) { // CBZ, CBNZ, B.cond
            int offset = signExtend((inst >>> 5) & 0x7FFFF, 19) << 2;
            labelMap.put(currentAddress + offset, generateLabel(currentAddress + offset));
        }
    }

    private static void decodeInstruction(int inst, int currentAddress) {
        int op11 = (inst >>> 21) & 0x7FF;
        int op10 = (inst >>> 22) & 0x3FF;
        int op8  = (inst >>> 24) & 0xFF;
        int op6  = (inst >>> 26) & 0x3F;

        // --- R-type ---
        if (op11 == 0b10001011000) { printR("ADD", inst); }
        else if (op11 == 0b10001010000) { printR("AND", inst); }
        else if (op11 == 0b11001010000) { printR("EOR", inst); }
        else if (op11 == 0b10101010000) { printR("ORR", inst); }
        else if (op11 == 0b11001011000) { printR("SUB", inst); }
        else if (op11 == 0b11101011000) { printR("SUBS", inst); }
        else if (op11 == 0b10011011000) { printRMul("MUL", inst); }
        else if (op11 == 0b11010011011) { printShift("LSL", inst); }
        else if (op11 == 0b11010011010) { printShift("LSR", inst); }
        else if (op11 == 0b11010110000) { int rn = (inst >>> 5) & 0x1F; System.out.printf("BR X%d\n", rn); }

        // --- I-type ---
        else if (op10 == 0b1001000100) { printI("ADDI", inst); }
        else if (op10 == 0b1001001000) { printI("ANDI", inst); }
        else if (op10 == 0b1101001000) { printI("EORI", inst); }
        else if (op10 == 0b1011001000) { printI("ORRI", inst); }
        else if (op10 == 0b1101000100) { printI("SUBI", inst); }
        else if (op10 == 0b1111000100) { printI("SUBIS", inst); }

        // --- D-type ---
        else if (op11 == 0b11111000010) { printD("LDUR", inst); }
        else if (op11 == 0b11111000000) { printD("STUR", inst); }

        // --- CB-type ---
        else if (op8 == 0b10110100) {
            int offset = signExtend((inst >>> 5) & 0x7FFFF, 19) << 2;
            int rt = inst & 0x1F;
            System.out.printf("CBZ X%d, %s\n", rt, generateLabel(currentAddress + offset));
        }
        else if (op8 == 0b10110101) {
            int offset = signExtend((inst >>> 5) & 0x7FFFF, 19) << 2;
            int rt = inst & 0x1F;
            System.out.printf("CBNZ X%d, %s\n", rt, generateLabel(currentAddress + offset));
        }
        else if (op8 == 0b01010100) {
            int offset = signExtend((inst >>> 5) & 0x7FFFF, 19) << 2;
            int cond = inst & 0x1F;
            String condStr = (cond < CONDITIONS.length) ? CONDITIONS[cond] : "??";
            System.out.printf("B.%s %s\n", condStr, generateLabel(currentAddress + offset));
        }

        // --- B-type ---
        else if (op6 == 0b000101) {
            int offset = signExtend(inst & 0x3FFFFFF, 26) << 2;
            System.out.printf("B %s\n", generateLabel(currentAddress + offset));
        }
        else if (op6 == 0b100101) {
            int offset = signExtend(inst & 0x3FFFFFF, 26) << 2;
            System.out.printf("BL %s\n", generateLabel(currentAddress + offset));
        }

        // --- Custom ---
        else if (op11 == 0b11111111101) { int rd = inst & 0x1F; System.out.printf("PRNT X%d\n", rd); }
        else if (op11 == 0b11111111100) { System.out.println("PRNL"); }
        else if (op11 == 0b11111111110) { System.out.println("DUMP"); }
        else if (op11 == 0b11111111111) { System.out.println("HALT"); }

        else {
            System.out.printf("UNKNOWN (opcode = 0x%X)\n", op11);
        }
    }

    // --- Helpers ---

    private static void printR(String name, int inst) {
        int rm = (inst >>> 16) & 0x1F;
        int rn = (inst >>> 5) & 0x1F;
        int rd = inst & 0x1F;
        System.out.printf("%s X%d, X%d, X%d\n", name, rd, rn, rm);
    }

    private static void printRMul(String name, int inst) {
        int rm = (inst >>> 16) & 0x1F;
        int rn = (inst >>> 5) & 0x1F;
        int rd = inst & 0x1F;
        System.out.printf("%s X%d, X%d, X%d\n", name, rd, rn, rm);
    }

    private static void printShift(String name, int inst) {
        int shamt = (inst >>> 10) & 0x3F;
        int rn = (inst >>> 5) & 0x1F;
        int rd = inst & 0x1F;
        System.out.printf("%s X%d, X%d, #%d\n", name, rd, rn, shamt);
    }

    private static void printI(String name, int inst) {
        int imm = (inst >>> 10) & 0xFFF;
        int rn = (inst >>> 5) & 0x1F;
        int rd = inst & 0x1F;
        System.out.printf("%s X%d, X%d, #%d\n", name, rd, rn, imm);
    }

    private static void printD(String name, int inst) {
        int address = (inst >>> 12) & 0x1FF;
        int rn = (inst >>> 5) & 0x1F;
        int rt = inst & 0x1F;
        System.out.printf("%s X%d, [X%d, #%d]\n", name, rt, rn, address);
    }

    private static int signExtend(int value, int bits) {
        int shift = 32 - bits;
        return (value << shift) >> shift;
    }

    private static String generateLabel(int address) {
        return String.format("label_%04X", address);
    }
}


/**

This code was from the first run, written by reading and adding as the assignment required, so it was a bit messy and unorganized.

The code above is the revised version, more organized, and the output formatting is much easier to read. 
Just wanted to add it so you (grader/TA) can see what I did to get where I am 

import java.io.*;

public class Disassembler {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Disassembler <binaryfile>");
            return;
        }

        FileInputStream fis = new FileInputStream(args[0]);
        DataInputStream dis = new DataInputStream(fis);

        int address = 0; // Instruction address

        while (dis.available() >= 4) {
            int instruction = dis.readInt(); // Java reads big-endian by default

            System.out.printf("%s:\t", generateLabel(address));
            decodeInstruction(instruction, address);

            address += 4;
        }

        dis.close();
    }

    private static void decodeInstruction(int inst, int currentAddress) {
        int op11 = (inst >>> 21) & 0x7FF;  // 11-bit opcode (R-type, D-type)
        int op10 = (inst >>> 22) & 0x3FF;  // 10-bit opcode (I-type)
        int op8  = (inst >>> 24) & 0xFF;   // 8-bit opcode (CB-type)
        int op6  = (inst >>> 26) & 0x3F;   // 6-bit opcode (B-type)
    
        if (op11 == 0b10001011000) { // ADD
            int rm = (inst >>> 16) & 0x1F;
            int shamt = (inst >>> 10) & 0x3F;
            int rn = (inst >>> 5) & 0x1F;
            int rd = inst & 0x1F;
            System.out.printf("ADD X%d, X%d, X%d\n", rd, rn, rm);
        }
        else if (op10 == 0b1001000100) { // ADDI
            int imm = (inst >>> 10) & 0xFFF;
            int rn = (inst >>> 5) & 0x1F;
            int rd = inst & 0x1F;
            System.out.printf("ADDI X%d, X%d, #%d\n", rd, rn, imm);
        }
        else if (op11 == 0b10001010000) { // AND
            int rm = (inst >>> 16) & 0x1F;
            int shamt = (inst >>> 10) & 0x3F;
            int rn = (inst >>> 5) & 0x1F;
            int rd = inst & 0x1F;
            System.out.printf("AND X%d, X%d, X%d\n", rd, rn, rm);
        }
        else if (op10 == 0b1001001000) { // ANDI
            int imm = (inst >>> 10) & 0xFFF;
            int rn = (inst >>> 5) & 0x1F;
            int rd = inst & 0x1F;
            System.out.printf("ANDI X%d, X%d, #%d\n", rd, rn, imm);
        }
        else if (op11 == 0b11001010000) { // EOR
            int rm = (inst >>> 16) & 0x1F;
            int shamt = (inst >>> 10) & 0x3F;
            int rn = (inst >>> 5) & 0x1F;
            int rd = inst & 0x1F;
            System.out.printf("EOR X%d, X%d, X%d\n", rd, rn, rm);
        }
        else if (op10 == 0b1101001000) { // EORI
            int imm = (inst >>> 10) & 0xFFF;
            int rn = (inst >>> 5) & 0x1F;
            int rd = inst & 0x1F;
            System.out.printf("EORI X%d, X%d, #%d\n", rd, rn, imm);
        }
        else if (op11 == 0b11111000010) { // LDUR
            int address = (inst >>> 12) & 0x1FF;
            int rn = (inst >>> 5) & 0x1F;
            int rt = inst & 0x1F;
            System.out.printf("LDUR X%d, [X%d, #%d]\n", rt, rn, address);
        }
        else if (op11 == 0b11111000000) { // STUR
            int address = (inst >>> 12) & 0x1FF;
            int rn = (inst >>> 5) & 0x1F;
            int rt = inst & 0x1F;
            System.out.printf("STUR X%d, [X%d, #%d]\n", rt, rn, address);
        }
        else if (op11 == 0b11010011011) { // LSL
            int shamt = (inst >>> 10) & 0x3F;
            int rn = (inst >>> 5) & 0x1F;
            int rd = inst & 0x1F;
            System.out.printf("LSL X%d, X%d, #%d\n", rd, rn, shamt);
        }
        else if (op11 == 0b11010011010) { // LSR
            int shamt = (inst >>> 10) & 0x3F;
            int rn = (inst >>> 5) & 0x1F;
            int rd = inst & 0x1F;
            System.out.printf("LSR X%d, X%d, #%d\n", rd, rn, shamt);
        }
        else if (op11 == 0b10101010000) { // ORR
            int rm = (inst >>> 16) & 0x1F;
            int shamt = (inst >>> 10) & 0x3F;
            int rn = (inst >>> 5) & 0x1F;
            int rd = inst & 0x1F;
            System.out.printf("ORR X%d, X%d, X%d\n", rd, rn, rm);
        }
        else if (op10 == 0b1011001000) { // ORRI
            int imm = (inst >>> 10) & 0xFFF;
            int rn = (inst >>> 5) & 0x1F;
            int rd = inst & 0x1F;
            System.out.printf("ORRI X%d, X%d, #%d\n", rd, rn, imm);
        }
        else if (op11 == 0b11001011000) { // SUB
            int rm = (inst >>> 16) & 0x1F;
            int shamt = (inst >>> 10) & 0x3F;
            int rn = (inst >>> 5) & 0x1F;
            int rd = inst & 0x1F;
            System.out.printf("SUB X%d, X%d, X%d\n", rd, rn, rm);
        }
        else if (op10 == 0b1101000100) { // SUBI
            int imm = (inst >>> 10) & 0xFFF;
            int rn = (inst >>> 5) & 0x1F;
            int rd = inst & 0x1F;
            System.out.printf("SUBI X%d, X%d, #%d\n", rd, rn, imm);
        }
        else if (op10 == 0b1111000100) { // SUBIS
            int imm = (inst >>> 10) & 0xFFF;
            int rn = (inst >>> 5) & 0x1F;
            int rd = inst & 0x1F;
            System.out.printf("SUBIS X%d, X%d, #%d\n", rd, rn, imm);
        }
        else if (op11 == 0b11101011000) { // SUBS
            int rm = (inst >>> 16) & 0x1F;
            int shamt = (inst >>> 10) & 0x3F;
            int rn = (inst >>> 5) & 0x1F;
            int rd = inst & 0x1F;
            System.out.printf("SUBS X%d, X%d, X%d\n", rd, rn, rm);
        }
        else if (op11 == 0b10011011000) { // MUL
            int rm = (inst >>> 16) & 0x1F;
            int rn = (inst >>> 5) & 0x1F;
            int rd = inst & 0x1F;
            System.out.printf("MUL X%d, X%d, X%d\n", rd, rn, rm);
        }
        else if (op11 == 0b11010110000) { // BR
            int rn = (inst >>> 5) & 0x1F;
            System.out.printf("BR X%d\n", rn);
        }
        else if (op8 == 0b10110100) { // CBZ
            int offset = (inst >>> 5) & 0x7FFFF;
            offset = signExtend(offset, 19) << 2;
            int rt = inst & 0x1F;
            int target = currentAddress + offset;
            System.out.printf("CBZ X%d, label_%04X\n", rt, target);
        }
        else if (op8 == 0b10110101) { // CBNZ
            int offset = (inst >>> 5) & 0x7FFFF;
            offset = signExtend(offset, 19) << 2;
            int rt = inst & 0x1F;
            int target = currentAddress + offset;
            System.out.printf("CBNZ X%d, label_%04X\n", rt, target);
        }
        else if (op8 == 0b01010100) { // B.cond
            int offset = (inst >>> 5) & 0x7FFFF;
            offset = signExtend(offset, 19) << 2;
            int cond = inst & 0x1F;
            int target = currentAddress + offset;
            String condStr = (cond < CONDITIONS.length) ? CONDITIONS[cond] : "??";
            System.out.printf("B.%s label_%04X\n", condStr, target);
        }
        else if (op11 == 0b11111111101) { // PRNT
            int rd = inst & 0x1F;
            System.out.printf("PRNT X%d\n", rd);
        }
        else if (op11 == 0b11111111100) { // PRNL
            System.out.printf("PRNL\n");
        }
        else if (op11 == 0b11111111110) { // DUMP
            System.out.printf("DUMP\n");
        }
        else if (op11 == 0b11111111111) { // HALT
            System.out.printf("HALT\n");
        }
        else if (op6 == 0b000101) { // B
            int offset = inst & 0x3FFFFFF;
            offset = signExtend(offset, 26) << 2;
            int target = currentAddress + offset;
            System.out.printf("B label_%04X\n", target);
        }
        else if (op6 == 0b100101) { // BL
            int offset = inst & 0x3FFFFFF;
            offset = signExtend(offset, 26) << 2;
            int target = currentAddress + offset;
            System.out.printf("BL label_%04X\n", target);
        }
        else {
            System.out.printf("UNKNOWN (opcode = 0x%X)\n", op11);
        }
    }    

    private static int signExtend(int value, int bits) {
        int shift = 32 - bits;
        return (value << shift) >> shift;
    }

    private static String generateLabel(int address) {
        return String.format("label_%04X", address);
    }

    private static final String[] CONDITIONS = {
        "EQ", "NE", "HS", "LO",
        "MI", "PL", "VS", "VC",
        "HI", "LS", "GE", "LT",
        "GT", "LE"
    };
    
}
*/
