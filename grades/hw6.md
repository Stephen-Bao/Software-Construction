hw6 Feedback
============

## Implementation of an executor-based parallel renderer (30/30)
Your executor-based parallel renderer implementation functioned largely as we expected, and was functionally equivalent to your sequential implementation. Good job!

#### Functionality (5/5)
Your executor-based parallel renderer works as expected.

#### Granularity / Effective Task Management (10/10)
The granularity of your task size effectively balances the overhead with the benefits of concurrency, good job!
  * -0.0001, It is unnecessary to store pixel position in a `Pair` class.
  It would be better to use a single integer that can be broken down using division and mod to find the row and column of the pixel position,
  or to structure the computation so that the pixel positions don't need to be stored.

#### Effective Use of the java.util.concurrent Library (15/15)
Your solution demonstrates effective use of the java.util.concurrent library, good job!

## Implementation of a streams-based parallel renderer (25/30)
Your executor-based parallel renderer implementation included the following issues:

#### Functionality (5/5)
Your streams-based parallel renderer works as expected.

#### Efficiency (10/10)
The efficiency of your streams-based parallel renderer meets the requirements, good job!
  * -0.0001, It is unnecessary to store pixel position in a `Pair` class.
  It would be better to use a single integer that can be broken down using division and mod to find the row and column of the pixel position,
  or to structure the computation so that the pixel positions don't need to be stored.

#### Use of the Java Stream library (10/15)
Your solution has minor issues with the use of the java streams library.
  * -5, You materialize a Collection of integers for each row rather than using an `IntStream`. It would have been more effective to use a built-in functionality of the Java Stream library.

## Demonstrating software development best practices (28/30)
Your software development practices were largely as we expected.  Good job!

#### Software Design (4/4)

#### Use of Version Control System (6/6)

#### Build Automation and Continuous Automation (5/5)

#### Static Analysis (4/4)

#### Testing (6/6)

#### Documentation and Style (3/5)
  * -1, Ignored exception with empty `catch` block without leaving a comment. ([link](https://github.com/CMU-17-214/xinyub/blob/5dbffa0f4230d92016f8e87ce5c8dd8c06ad95b5/homework/6/src/main/java/edu/cmu/cs/cs214/hw6/MandelbrotMain.java#L116-L118))
  * -1, Please remove dead code; your submissions should be finalized. ([link](https://github.com/CMU-17-214/xinyub/blob/5dbffa0f4230d92016f8e87ce5c8dd8c06ad95b5/homework/6/src/main/java/edu/cmu/cs/cs214/hw6/MandelbrotMain.java#L84-L110))

## Performance benchmarking and discussion (7/10)
Your discussion of your strategies for parallelization included the following issues:
   * In your discussion it's not clear how your implementation guarantees safety in the face of concurrency.
   Your discussion should mention how the tools you use ensure that your implicit assumptions about safety are maintained. 
---
 
#### Total (90/100)
 
Late days used: 2 (3 left)
 
---
 
#### Additional Notes
 
Graded by: Hyun Jun Bae (hyunjunb@andrew.cmu.edu)
 
To view this file with formatting, visit the following page: https://github.com/CMU-17-214/xinyub/blob/master/grades/hw6.md
