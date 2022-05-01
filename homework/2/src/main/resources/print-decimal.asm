        LOADI   R5   1337  ; R5 should contain the number to print
        CALL    5          ; CALL printdec; decimal-printing subroutine
        LOADI   R0   10    ; '\n'
        WRITE   R0
        HALT

        PUSH    R0         ; printdec: start of decimal-printing sub-routine
        PUSH    R1         ; save registers in subroutine to restore later
        LOADI   R1   0     ; R1 tracks number of digits to print
        JEQ     R5   20    ; JEQ zero: number to print is 0
        JGT     R5   13    ; JGT pos: number to print is positive
        LOADI   R0   45    ; case: number to print is negative, ASCII for '-'
        WRITE   R0         ; write ASCII for '-'
        MULI    R5   -1    ; then generate digits as if positive
        JEQ     R5   23    ; pos: JEQ print: ready to print if R5 is zero
        MOV     R0   R5 
        MODI    R0   10    ; compute least-significant digit in R0
        PUSH    R0         ; and store to print later
        ADDI    R1   1     ; count that digit to print
        DIVI    R5   10    ; divide by 10 to eliminate least-significant digit
        JUMP    13         ; JUMP pos: continue printing a positive number
        LOADI   R0   0
        PUSH    R0 
        ADDI    R1   1     ; implicit JUMP print (falling through)
        POP     R0         ; start of loop to print one digit at a time
        ADDI    R0   48    ; ASCII '0' is 48, so compute 48 + digit
        WRITE   R0
        SUBI    R1   1     ; R1 contains the number of digits to print
        JGT     R1   23    ; JGT print: loop if there are more digits to print
        POP     R1         ; restore registers before subroutine returns
        POP     R0
        RET

