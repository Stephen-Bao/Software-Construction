hw2 Feedback
============

#### Achieving design goals (15/25)
* Extensibility (5/15): While the representations of the assembly instructions, processor, and parser partly reflect a polymorphic, extensible design, we identified major problems or errors that reduce the effectiveness of the design.
  * You should separate your logic into multiple logically-related classes, enabling a greater deal of flexibility in client implementations. One example is separating your parser, processor, and instructions into separate classes. You combined Parser and Processor into [Processor](https://github.com/CMU-17-214/xinyub/blob/edc6aef73b6dfd3f974052c31aa09b61479c7389/homework/2/src/main/java/edu/cmu/cs/cs214/hw2/Processor.java#L13).
  * You have seperated instructions based on their input types and stored their opcode in a hashmap. ([link](https://github.com/CMU-17-214/xinyub/blob/edc6aef73b6dfd3f974052c31aa09b61479c7389/homework/2/src/main/java/edu/cmu/cs/cs214/hw2/Processor.java#L32-L37)). Manually putting opcode into the map is not an ideal way, because you need to modify this code block if you later on add a new instruction, as you stated in the rationale. A better way is to loop over `OneArgOperation.values()` and store each enum constant into the map. 

* Reuse (3/5): Your design for code reuse was good, but there are several missed opportunities for code reuse.
  * You used a series of if statement convert register names into integer, which is error-prone and inefficient. You should instead use a `Map`.

* Information hiding (5/5): We identified major flaws in your solution which violate the principles of information hiding.


#### Java best practices and compatibility with our informal specification (35/40)

* Processor (13/15)
 * -2, The `toString()` implementation in your `Processor` class is missing, or is not meaningful or does not include all the information necessary to express the current state of the instance.
 * -0.0001, It limits the usefulness of the Processor/Machine class to take in only one program at a time. Suppose that a client wanted to run multiple assembly programs. They would have to create a processor for each of the programs, and run them one after the other. It would be better if the client could pass in a list of programs, and one instance of a processor could run all of them sequentially.

* Instructions (15/15)
  * -0.0001, Although the enum's default `toString()` implementation is accurate in this situation, it is generally the best practice to override `toString`. See Effective Java item 12 for why it's important to override `toString`.
* Parser/Main (2/5)
  * -2, Your parser swallows exceptions for incorrectly formatted input. You should throw an exception and/or print an error message so the caller understands that the program is not properly formatted. ([link](https://github.com/CMU-17-214/xinyub/blob/edc6aef73b6dfd3f974052c31aa09b61479c7389/homework/2/src/main/java/edu/cmu/cs/cs214/hw2/Interpret.java#L17))
  * -1, You should close resources, such as any `Scanners` or `Files`, when you are done using them.  We recommend that you see Java's support for `try-with-resources`; it greatly simplifies this requirement. ([link](https://github.com/CMU-17-214/xinyub/blob/edc6aef73b6dfd3f974052c31aa09b61479c7389/homework/2/src/main/java/edu/cmu/cs/cs214/hw2/Processor.java#L150))

* Rationale (5/5)
  * Good job including a well-written rationale! 


#### Unit testing, including coverage and compliance with best practices (21/25)
* Quality of test suite (11/15): While your test suite covers a decent amount of input situations, we identified several ways for your test suite to more exhaustively test the state space.
  * Your test suite does not test parsing invalid programs (e.g. programs that are malformed, contain illegal addresses, contain non-existent registers, etc.) to confirm that an exception is correctly thrown. (This can be accomplished with the annotation `@Test(expected = IllegalArgumentException.class)`, for example.)

* Testing best practices (10/10): Your test suite exhibits good use of best practices.

#### Documentation and style (6/10)
* -2, You should use the interface type where possible in variable declarations (e.g. `List` instead of `ArrayList`). This allows you to change which implementation of that interface is being used with minimal refactoring. The only exception is when you must access fields or methods present only on the narrower type. Read Effective Java item 50 for further information. ([link](https://github.com/CMU-17-214/xinyub/blob/edc6aef73b6dfd3f974052c31aa09b61479c7389/homework/2/src/main/java/edu/cmu/cs/cs214/hw2/Processor.java#L17))

* -1, Please document your public classes/methods with comments. You are missing documentation in several classes. Read Effective Java item 56 for further information. ([link](https://github.com/CMU-17-214/xinyub/blob/edc6aef73b6dfd3f974052c31aa09b61479c7389/homework/2/src/main/java/edu/cmu/cs/cs214/hw2/Processor.java#L13)). 

* -1, Avoid using magic numbers in your code. Declare variables as constants at the top of the file. ([link](https://github.com/CMU-17-214/xinyub/blob/edc6aef73b6dfd3f974052c31aa09b61479c7389/homework/2/src/main/java/edu/cmu/cs/cs214/hw2/Processor.java#L47))

* -0.0001, Constants should be declared `static final`. ([link](https://github.com/CMU-17-214/xinyub/blob/edc6aef73b6dfd3f974052c31aa09b61479c7389/homework/2/src/main/java/edu/cmu/cs/cs214/hw2/Processor.java#L16))

---

#### Total (75/100)

Late days used: 0 (5 left)

---

#### Additional Notes
Graded by: Valentine Wu (jingwen3@andrew.cmu.edu)

To view this file with formatting, visit the following page: https://github.com/CMU-17-214/xinyub/blob/master/grades/hw2.md
