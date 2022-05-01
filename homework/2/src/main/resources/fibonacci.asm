        LOADI   R5   11    ; R5 should contain fibonacci number to generate
        CALL    32         ; call fib subroutine, return value is in R5
        CALL    6          ; call printdec to print the computed number (fib(11) is 89)
        LOADI   R0   10    ; '\n'
        WRITE   R0         ; prints the newline
        HALT

        PUSH    R0         ; printdec: start of decimal-printing sub-routine
        PUSH    R1         ; save registers in subroutine to restore later
        LOADI   R1   0     ; R1 tracks number of digits to print
        JEQ     R5   21    ; jeq zero: number to print is 0
        JGT     R5   14    ; jgt pos: number to print is positive
        LOADI   R0   45    ; number to print is negative, ASCII for '-'
        WRITE   R0         ; write ASCII for '-'
        MULI    R5   -1    ; then generate digits as if positive
        JEQ     R5   24    ; pos: jeq print, ready to print if R5 is zero
        MOV     R0   R5 
        MODI    R0   10    ; compute least-significant digit in R0
        PUSH    R0         ; and store to print later
        ADDI    R1   1     ; count that digit to print
        DIVI    R5   10    ; divide by 10 to eliminate least-significant digit
        JUMP    14         ; jump pos,continue printing a positive number
        LOADI   R0   0
        PUSH    R0 
        ADDI    R1   1     ; implicit JUMP print (falling through)
        POP     R0         ; print: start of loop to print one digit at a time
        ADDI    R0   48    ; ASCII '0' is 48, so compute 48 + digit
        WRITE   R0
        SUBI    R1   1     ; R1 contains the number of digits to print
        JGT     R1   24    ; print: loop if there are more digits to print
        POP     R1         ; restore registers before subroutine returns
        POP     R0
        RET

        PUSH    R0         ; fib: subroutine, saves registers
        PUSH    R1
        PUSH    R2
        LOADI   R0  0      ; fib(0) = 0
        LOADI   R1  1      ; fib(1) = 1
        JEQ     R5  43     ; jeq exitfib, if computing fib(0)
        MOV     R2  R1     ; copy R1 to R2
        ADD     R1  R0     ; R1 += R0 to compute next fibonacci number
        MOV     R0  R2     ; R0 = R2 (old R1)
        SUBI    R5  1      ; R5 counts down to 0, with the invariant that
        JGT     R5  38     ; (jgt loop) R0 contains fib(i) after i loop executions
        MOV     R5  R0     ; copy return value to R5
        POP     R2         ; restore registers
        POP     R1
        POP     R0
        RET
