;;; Reads and prints lines of user input repeatedly until the user enters a blank line
;;; (i.e., hits return twice in succession). This little program is a good way to test
;;; that Your READ and WRITE instructions work properly.
;;; This version handles Windows-style line separators (\r\n) as well as Linux-style (\n).
        
        LOADI R0 -1     ; Keep track of the number of characters on the line
        READ  R1        ; Read a character into R1
        WRITE R1        ; Echo it back (this will happen only after user hits return)
        ADDI  R0 1      ; Increment the number of characters on the line
        SUBI  R1 13     ; Subtract '\r' (the cr character that precedes '\n' on Windows)
        JNE   R1 7      ; If char was NOT a cr, check if it was a newline
        SUBI  R0 1      ; We got a cr; decrement char char counter so cr has no effect on it
        ADDI  R1 3      ; 3 is '\r' - '\n', so this adjustment will turn '\n' into 0.
        JNE   R1 1      ; Character was not a newline, go back for another character
        JNE   R0 0      ; Character was a newline; if line was non-empty go back for another line
        HALT            ; Line was empty; we're done
