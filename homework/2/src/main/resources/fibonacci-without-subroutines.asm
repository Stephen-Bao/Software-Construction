        LOADI   R5  11     ; R5 should contain fibonacci number to generate
        LOADI   R0  0      ; fib(0) = 0
        LOADI   R1  1      ; fib(1) = 1
        JEQ     R5  9      ; JUMPZ exitfib; R5 counts down until we're done
        MOV     R2  R1     ; copy R1 to R2 so we can compute new fib
        ADD     R1  R0     ; R1 += R0, adds previous fibs to compute new fib
        MOV     R0  R2     ; R0 = R2 (old R1)
        SUBI    R5  1      ; R5 -= 1
        JGT     R5  4      ; JUMPP loop; R5 counts down until we're done
        MOV     R5  R0     ; exitfib: copy return value to R5
        HALT               ; machine halts without printing anything
        
