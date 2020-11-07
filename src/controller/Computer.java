package controller;

import exceptions.conditionalJumpException;

public class Computer {

    private final Register[] registers;
    private String[] program;

    public Computer() {
        this.registers = new Register[8];
        for (int i = 0; i < registers.length; i++) {
            Register register = new Register(0);
            this.registers[i] = register;
        }
    }

    public void fibonacci(int n) {
        String binaryN = Integer.toBinaryString(n);
        String correctSizeN = String.format("%3s", binaryN).replaceAll(" ", "0");
        String[] instructions = {
                "000 000 000 000 0000",             // #0  r0 = 0  will be f(n-2)
                "000 001 000 000 0001",             // #1  r1 = 1  will be f(n-1)
                "000 010 000 000 0000",             // #2  r2 = 0  will be f(n)
                "000 011 000 000 0001",             // #3  r3 = 1  will be counter
                "000 100 000 000 0000",             // #4  r4 = 0  will be used to move numbers between registers
                "000 101 000 000 0" + correctSizeN, // #5  r5 = n
                "011 101 011 000 1010",             // #6  goto 12 if r5 (n) < r3 (counter)
                "010 010 000 001 0000",             // #7  r2 = r0 + r1 = f(n-2) + f(n-1)
                "001 011 011 000 0001",             // #8  r3 = r3 + 1
                "010 000 001 100 0000",             // #9  r0 = r1 + r4
                "010 001 010 100 0000",             // #10 r1 = r2 + r4
                "011 011 101 000 0111",             // #11 goto 7 if r3 (counter) < r5 (n)
                "100 010 000 000 0000"              // #12 print out the value
        };
        this.program = instructions;
        computeProgram(instructions);
    }


    public void computeProgram(String[] instructions) {
        try {
            for (String instruction : instructions) {
                instruction = instruction.replaceAll("\\s+", "");
                String instructionCode = instruction.substring(0, 3);
                String instructionValue = instruction.substring(3);
                switch (instructionCode) {
                    case "000":
                        loadImmediate(instructionValue);
                        break;
                    case "001":
                        addImmediate(instructionValue);
                        break;
                    case "010":
                        add(instructionValue);
                        break;
                    case "011":
                        conditionalJump(instructionValue);
                        break;
                    case "100":
                        printRegister(instructionValue);
                        break;
                    default:
                        System.out.println("Invalid opcode");
                }
            }
        } catch (conditionalJumpException e) {
            int value = Integer.parseInt(e.getMessage(), 2);
            String[] newInstruction = new String[this.program.length - value];
            System.arraycopy(this.program, value, newInstruction, 0, this.program.length - value);
            computeProgram(newInstruction);
        }
    }

    private void loadImmediate(String instruction) {
        int defIndex = Integer.parseInt(instruction.substring(0, 3), 2);
        int value = Integer.parseInt(instruction.substring(3), 2);
        registers[defIndex].setRegisterValue(value);
    }

    private void addImmediate(String instruction) {
        int defIndex = Integer.parseInt(instruction.substring(0, 3), 2);
        int ghiIndex = Integer.parseInt(instruction.substring(3, 6), 2);
        int value = Integer.parseInt(instruction.substring(6), 2);
        int resultValue = registers[ghiIndex].getRegisterValue() + value;
        registers[defIndex].setRegisterValue(resultValue);
    }

    private void add(String instruction) {
        int defIndex = Integer.parseInt(instruction.substring(0, 3), 2);
        int ghiIndex = Integer.parseInt(instruction.substring(3, 6), 2);
        int jklIndex = Integer.parseInt(instruction.substring(6, 9), 2);
        int resultValue = registers[ghiIndex].getRegisterValue() + registers[jklIndex].getRegisterValue();
        registers[defIndex].setRegisterValue(resultValue);
    }

    private void conditionalJump(String instruction) throws conditionalJumpException {
        int defIndex = Integer.parseInt(instruction.substring(0, 3), 2);
        int ghiIndex = Integer.parseInt(instruction.substring(3, 6), 2);
        if (registers[defIndex].getRegisterValue() < registers[ghiIndex].getRegisterValue()) {
            String jumpTo = instruction.substring(6);
            throw new conditionalJumpException(jumpTo);
        }
    }

    private void printRegister(String instruction) {
        int defIndex = Integer.parseInt(instruction.substring(0, 3), 2);
        System.out.println(registers[defIndex].getRegisterValue());
    }
}
