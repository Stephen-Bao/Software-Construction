;;; Main method - prompts for and reads to in values, then computes and prints their gcd
	;; Print prompt "i: "
        LOADI R3 'i'    WRITE R3
        LOADI R3 ':'    WRITE R3
        LOADI R3 ' '    WRITE R3
	
        CALL  64    ; Read an int value into R5
        PUSH  R5    ; Push it onto the stack
	
	;; Print prompt "j: "
        LOADI R3 'j'    WRITE R3
        LOADI R3 ':'    WRITE R3
        LOADI R3 ' '    WRITE R3

        CALL  64    ; Read another int value into R5
	
	;; Print "gcd(i,j): "
        LOADI R3 'g'    WRITE R3
        LOADI R3 'c'    WRITE R3
        LOADI R3 'd'    WRITE R3
        LOADI R3 '('    WRITE R3	
	LOADI R3 'i'    WRITE R3
        LOADI R3 ','    WRITE R3
	LOADI R3 'j'    WRITE R3
	LOADI R3 ')'    WRITE R3
	LOADI R3 ':'    WRITE R3
	LOADI R3 ' '    WRITE R3
	
	CALL  39       ; Compute the GCD
        ADDI  SP 1     ; Pop second argument off stack, so as not to leak memory
	CALL  49       ; Print GCD
        HALT

;;; This recursive subroutine Computes the GCD of its two arguments (in R5 and on stack) by
;;; Euclid's algorithm. The GCD is returned in R5.
;;; Uses the calling conventions described in the sheet, except that it modifies the contents of
;;; R4 and does not restore it to its original value. Because this subroutine stores a parameter
;;; on the stack beneath the return address, it has to use LOAD to retrieve that parameter.
;;; This subroutine begins at address 39.
        MOV   R4 SP     ; Copy SP into R4 to access second argument
        ADDI  R4 2      ; Compute memory address of second argument
        LOAD  R4 R4     ; Put second argument into R4
        JEQ   R4 48     ; If second argument is zero, we're done
        MOD   R5 R4     ; Compute First argument % second argument
        PUSH  R5        ; Push result (new second argument) onto the stack
        MOV   R5 R4     ; Put new first argument (old second argument) in R5
        CALL  39        ; Call recursively
        ADDI  SP 1      ; Pop new second argument off stack
        RET             ; Return from recursion and (eventually) to caller

;;; This recursive subroutine prints the non-negative int in R5 in decimal.
;;; This subroutine modifies R4.
;;; This subroutine begins at address 49.
        MOV   R4 R5     ; Copy arg from R5 to scratch register R4
        SUBI  R4  9     ; Subtract 9
        JGT   R4 55     ; If arg (R5) is > 9 deal with multiple digits
        
        ADDI  R5 '0' ; Arg is a single digit; Translate it to ASCII
        WRITE R5     ; Print it
        RET          ; And return 

        ;; Precondition: argument (in R5) is >= 10
        MOV  R4 R5   ; Save a copy of arg in R4...
        MODI R4 10   ; And turn the copy into its low-order digit
        DIVI R5 10   ; Discard low order digit from R5 (e.g, Turn 345 into 34)
        
        PUSH R4      ; Save low-ordr digit on stack for subsequent printing
        CALL 49      ; Recurse on quotient (higher order digits)

        ;; There are one or more higher order digits. Print them
        POP   R4     ; Get digit from stack
        ADDI  R4 '0' ; Make it ASCII
        WRITE R4     ; Print it
        RET          ; Keep printing higher order digits until it's time to return to caller

;;; This subroutine reads a non-negative integer from the console and returns it in R5.
;;; It reads numeric characters until it reaches the first non-numeric character, which
;;; it consumes. It returns the resulting integer. (If the first character encountered
;;; on input is non-numeric, it returns 0.)	
;;; This subroutine modifies R4.
;;; This subroutine begins at address 64.
        LOADI R5 0     ; Initialize result
        READ R4        ; Read next char
        SUBI R4 '9'    ; Transform valid digit chars into [-9,0]
        JGT  R4 70     ; If char is to high to be a digit, we're done
        ADDI R4 10     ; Modify offset so ['0','9'] yields [1,10]
        JGT  R4 71     ; If char was high enough to be a digit, incorporate it
        RET
        SUBI R4 1      ; Eliminate digit offset (so digit value is correct)
        MULI R5 10     ; Shift result to make room for digit
        ADD  R5 R4     ; Add in the digit
        JUMP 65        ; See if there's another digit on input
