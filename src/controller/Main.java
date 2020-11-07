package controller;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Computer computer = new Computer();
        while (true) {
            System.out.println("Enter a number to get the Fibonacci number at that position, or enter -1 to exit: ");
            Scanner scanner = new Scanner(System.in);
            int n = scanner.nextInt();
            if (n == -1) {
                break;
            }
            computer.fibonacci(n);
        }
    }
}
