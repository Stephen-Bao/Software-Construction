;;; Main program - Plays twenty questions (ten, really). Pick a number between 1 and 1000!
        LOADI R0 1      ; Lowest legal guess
        LOADI R1 1000   ; Highest legal guess
	
        ;; Invariant: guessed number between number stored in R0 and R1, inclusive
        ;: Put midpoint between low and high in R2
loop:   MOV R2 R1       ; Copy high endpoint to R2
        ADD R2 R0       ; Add low endpoint to copy of high one
        DIVI R2 2       ; Compute midpoint (Assert R0 <= R1 <= R2)

        ;; Print "Is your # > "
askQst: LOADI R3 'I'    WRITE R3
        LOADI R3 's'    WRITE R3
        LOADI R3 ' '    WRITE R3
        LOADI R3 'y'    WRITE R3
        LOADI R3 'o'    WRITE R3
        LOADI R3 'u'    WRITE R3
        LOADI R3 'r'    WRITE R3
        LOADI R3 ' '    WRITE R3
        LOADI R3 '#'    WRITE R3
        LOADI R3 ' '    WRITE R3
        LOADI R3 '>'    WRITE R3
        LOADI R3 ' '    WRITE R3
        
        MOV R5 R2     ; Put midpoint in R5 for printing
        CALL prtInt   ; Print it

        ;; Print " (y/n)? "
        LOADI R3 ' '    WRITE R3
        LOADI R3 '('    WRITE R3
        LOADI R3 'y'    WRITE R3
        LOADI R3 '/'    WRITE R3
        LOADI R3 'n'    WRITE R3
        LOADI R3 ')'    WRITE R3
        LOADI R3 '?'    WRITE R3
        LOADI R3 ' '    WRITE R3

        READ R3           ; Read a character into R3 - should be 'y' or 'n'

        ;; Flush input buffer until we've read a newline char
        MOV  R4 R3      ; Copy input into R4        
tstEol: SUBI R4 '\n'    ; Subtract '\n'
        JEQ  R4 tstChr  ; If result is zero, we're done flushing
        READ R4         ; Read another character
        JUMP tstEol     ; See if we're done

tstChr: MOV  R4 R3      ; Copy input into R4        
        SUBI R4 'y'     ; Subtract 'y'
        JEQ  R4 wasYes  ; If result is 0, input was 'y'; process it
        
        MOV  R4 R3      ; Copy input into R4
        SUBI R4 'n'     ; Subtract 'n'
        JEQ  R4 wasNo   ; If result is 0, input was 'n'; process it
        
        JUMP askQst     ; Invalid input - ask again

wasYes: MOV R0 R2       ; Response was yes; set low endpoint to midpoint...
        ADDI R0 1       ; and add 1
        JUMP ckDone     ; Go check if we're done
        
wasNo:  MOV R1 R2       ; Response was no; set high endpoint to midpoint
        
        ;; If low and high enpoints still differ, ask more questions
ckDone: MOV R2 R0
        SUB R2 R1
        JNE R2 loop

        ;; We're done. Print "Your # is "
        LOADI R3 'Y'    WRITE R3
        LOADI R3 'o'    WRITE R3
        LOADI R3 'u'    WRITE R3
        LOADI R3 'r'    WRITE R3
        LOADI R3 ' '    WRITE R3
        LOADI R3 '#'    WRITE R3
        LOADI R3 ' '    WRITE R3
        LOADI R3 'i'    WRITE R3        
        LOADI R3 's'    WRITE R3        
        LOADI R3 ' '    WRITE R3        

        ;; Print the guessed number
        MOV R5 R0     ; Put low endpoint (== high endpoint) in R5 for printing
        CALL prtInt   ; Print it
        LOADI R3 10   ; newline character
        WRITE R3
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
