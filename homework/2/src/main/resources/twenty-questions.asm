;;; Main program - Plays twenty questions (ten, really). Pick a number between 1 and 1000!
        LOADI R0 1      ; Lowest legal guess
        LOADI R1 1000   ; Highest legal guess
	
        ;; Invariant: guessed number between number stored in R0 and R1, inclusive
        ;: Put midpoint between low and high in R2
        MOV R2 R1       ; Copy high endpoint to R2
        ADD R2 R0       ; Add low endpoint to copy of high one
        DIVI R2 2       ; Compute midpoint (Assert R0 <= R1 <= R2)

        ;; Print "Is your # > "
        LOADI R3 73     WRITE R3
        LOADI R3 115    WRITE R3
        LOADI R3 32     WRITE R3
        LOADI R3 121    WRITE R3
        LOADI R3 111    WRITE R3
        LOADI R3 117    WRITE R3
        LOADI R3 114    WRITE R3
        LOADI R3 32     WRITE R3
        LOADI R3 35     WRITE R3
        LOADI R3 32     WRITE R3
        LOADI R3 62     WRITE R3
        LOADI R3 32     WRITE R3
        
        MOV R5 R2     ; Put midpoint in R5 for printing
        CALL 92       ; Print it

        ;; Print " (y/n)? "
        LOADI R3 32     WRITE R3
        LOADI R3 40     WRITE R3
        LOADI R3 121    WRITE R3
        LOADI R3 47     WRITE R3
        LOADI R3 110    WRITE R3
        LOADI R3 41     WRITE R3
        LOADI R3 63     WRITE R3
        LOADI R3 32     WRITE R3

        READ R3           ; Read a character into R3 - should be 'y' or 'n'

        ;; Flush input buffer until we've read a newline char
        MOV  R4 R3      ; Copy input into R4        
        SUBI R4 10      ; Subtract '\n'
        JEQ  R4 53      ; If result is zero, we're done flushing
        READ R4         ; Read another character
        JUMP 49         ; See if we're done

        MOV  R4 R3      ; Copy input into R4        
        SUBI R4 121     ; Subtract 'y'
        JEQ  R4 60      ; If result is 0, input was 'y'; process it
        
        MOV  R4 R3      ; Copy input into R4
        SUBI R4 110     ; Subtract 'n'
        JEQ  R4 63      ; If result is 0, input was 'n'; process it
        
        JUMP 5          ; Invalid input - ask again

        MOV R0 R2       ; Response was yes; set low endpoint to midpoint...
        ADDI R0 1       ; and add 1
        JUMP 64         ; Go check if we're done
        
        MOV R1 R2       ; Response was no; set high endpoint to midpoint
        
        ;; If low and high enpoints still differ, ask more questions
        MOV R2 R0
        SUB R2 R1
        JNE R2 2

        ;; We're done. Print "Your # is "
        LOADI R3 89     WRITE R3
        LOADI R3 111    WRITE R3
        LOADI R3 117    WRITE R3
        LOADI R3 114    WRITE R3
        LOADI R3 32     WRITE R3
        LOADI R3 35     WRITE R3
        LOADI R3 32     WRITE R3
        LOADI R3 105    WRITE R3        
        LOADI R3 115    WRITE R3        
        LOADI R3 32     WRITE R3        

        ;; Print the guessed number
        MOV R5 R0     ; Put low endpoint (== high endpoint) in R5 for printing
        CALL 92       ; Print it
        LOADI R3 10   ; newline character
        WRITE R3
        HALT
        
;;; This recursive subroutine prints the non-negative int in R5 in decimal.
;;; Address had better be 92. This subroutine modifies R4.
        MOV   R4 R5  ; Copy arg from R5 to scratch register R4
        SUBI  R4  9  ; Subtract 9
        JGT   R4 98  ; If arg (R5) is > 9 deal with multiple digits
        
        ADDI  R5 48  ; Arg is a single digit; Translate it to ASCII
        WRITE R5     ; Print it
        RET          ; And return 

        ;; Address had better be 98. Precondition: argument (in R5) is >= 10
        MOV  R4 R5   ; Save a copy of arg in R4...
        MODI R4 10   ; And turn the copy into its low-order digit
        DIVI R5 10   ; Discard low order digit from R5 (e.g, Turn 345 into 34)
        
        PUSH R4      ; Save low-ordr digit on stack for subsequent printing
        CALL 92      ; Recurse on quotient (higher order digits)

        ;; There are one or more higher order digits. Print them
        POP   R4     ; Get digit from stack
        ADDI  R4 48  ; Make it ASCII
        WRITE R4     ; Print it
        RET          ; Keep printing higher order digits until it's time to return to caller

