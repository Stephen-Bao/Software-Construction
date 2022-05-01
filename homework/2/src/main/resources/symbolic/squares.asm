;;; Main program - Prints a table of the squares of the first 20 positive integers
        LOADI R0 1      ; First number whose square to print

prtSqr: MOV   R5 R0     ; Put next number whose square to print in R5
        CALL  prtInt    ; Print number to be squared

        LOADI R1 ' '    ; Prepare to print a space
        WRITE R1        ; Print it

        MOV   R1 R0     ; Prepare to square
        MUL   R1 R0     ; R1 = R0 * R0
        MOV   R5 R1     ; Put square in R5
        CALL  prtInt    ; Print square

        LOADI R1 '\n'   ; Prepare to print a newline
        WRITE R1        ; Print it

        ADDI  R0 1      ; Increment loop index
        MOV   R1 R0     ; Find out if we're done by...
        SUBI  R1 21     ; Subtracting 21 (the number of squares to print + 1)...
        JNE   R1 prtSqr ; ... and going back for more if the result is non-zero
        HALT
        
;;; This recursive subroutine prints the non-negative int in R5 in decimal.
;;; This subroutine modifies R4.
prtInt: MOV   R4 R5     ; Copy arg from R5 to scratch register R4
        SUBI  R4  9     ; Subtract 9
        JGT   R4 bigArg ; If arg (R5) is > 9 deal with multiple digits
        
        ADDI  R5 '0' ; Arg is a single digit; Translate it to ASCII
        WRITE R5     ; Print it
        RET          ; And return 

        ;; Precondition: argument (in R5) is >= 10
bigArg: MOV  R4 R5   ; Save a copy of arg in R4...
        MODI R4 10   ; And turn the copy into its low-order digit
        DIVI R5 10   ; Discard low order digit from R5 (e.g, Turn 345 into 34)
        
        PUSH R4      ; Save low-ordr digit on stack for subsequent printing
        CALL prtInt  ; Recurse on quotient (higher order digits)

        ;; There are one or more higher order digits. Print them
        POP   R4     ; Get digit from stack
        ADDI  R4 '0'  ; Make it ASCII
        WRITE R4     ; Print it
        RET          ; Keep printing higher order digits until it's time to return to caller
