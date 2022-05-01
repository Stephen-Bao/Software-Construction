;;; Main method - Computes and prints the GCD(48, 180), which should is 12.
        LOADI R5 48     ; Put the first param (48) into R5
        PUSH  R5        ; Push it onto the stack
        LOADI R5 180    ; Put the second param (180) into R5
	CALL  gcd       ; Compute the GCD
        ADDI  SP 1      ; Pop second argument off stack, so as not to leak memory
	CALL  gcd       ; Print GCD
        HALT

;;; This recursive subroutine Computes the GCD of its two arguments (in R5 and on stack) by
;;; Euclid's algorithm. The GCD is returned in R5.
;;; Uses the calling conventions described in the sheet, except that it modifies the contents of
;;; R4 and does not restore it to its original value. Because this subroutine stores a parameter
;;; on the stack beneath the return address, it has to use LOAD to retrieve that parameter.
gcd:    MOV   R4 SP     ; Copy SP into R4 to access second argument
        ADDI  R4 2      ; Compute memory address of second argument
        LOAD  R4 R4     ; Put second argument into R4
        JEQ   R4 return ; If second argument is zero, we're done
        MOD   R5 R4     ; Compute First argument % second argument
        PUSH  R5        ; Push result (new second argument) onto the stack
        MOV   R5 R4     ; Put new first argument (old second argument) in R5
        CALL  gcd       ; Call recursively
        ADDI  SP 1      ; Pop new second argument off stack
return: RET             ; Return from recursion and (eventually) to caller

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
        ADDI  R4 '0' ; Make it ASCII
        WRITE R4     ; Print it
        RET          ; Keep printing higher order digits until it's time to return to caller
