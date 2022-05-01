Samples files in this directory are:
 * `hello-world.asm`:  A very simple file to parse and execute.  The program uses few instructions, no jump or stack instructions, and does not contain comments.  A sanity check that the parser can parse something and that the machine runs some programs.
 * `hello-world-with-comments.asm`:  Like `hello-world.asm`, but with comments.  A sanity check that the parser can handle non-erroneous comments.
 * `fibonacci-without-subroutines.asm`:  Basic control-flow and arithmetic instructions, without stack instructions.  Does not produce any output, but the output is in register `R5` for debugging purposes.  As distributed, the program computes `fib(11)`.  A sanity-check for basic computation and arithmetic.
 * `print-decimal.asm`:  A decimal-printing subroutine, with input hard-coded.  A sanity check for stack operations, including subroutines.
 * `fibonacci.asm`:  Computes and prints a fibonacci number, using the print-decimal subroutine.  A sanity check for stack operations, including subroutines.
 * `echo.asm`: Tests both input and output, echoing the user's input to the console.
 * `gcd.asm`: Computes the greatest common divisor of two numbers.
 * `twenty-questions.asm`: A more advanced example that plays a game of twenty questions with the user.

The `symbolic` directory contains versions of (some of) these programs written with labels and character literals. Support for this functionality is not required, but you might enjoy the challenge of providing support for these features. A quick glances at these files should convince you that these features make assembly programming much easier and more enjoyable.
