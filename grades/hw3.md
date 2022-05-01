hw3 Feedback
============

#### Demonstrate mastery of earlier learning goals, especially the concepts of information hiding and polymorphism, software design based on informal specifications, testing, and testing best practices. (45/60)

* Information hiding (10/10): Good job using the principles of information hiding!

* Compliance with specification (30/30)
  * Good job complying with specifications!

* Testing practices (3/10): Your test suite has inadequate structure. It is not easy to follow your tests, or understand their purpose in your test suite.
* You have tests but they fail on Travis CI/Gradle. Specifically, your `EndToEndTest` `testProgram` fails. 
  * You did not test whether your permutation generator produces unique permutations that contain all the elements from the original set.

* Java coding best practices and style (2/10)
  * -0.0001, Travis CI does not actually build Homework 3 because you did not include Homework 3 in `settings.gradle`. 
  * -4, You committed all or most of your code in a single big commit. It is good practice (and also a good backup strategy) to commit regularly at milestones while you are still developing the solution.
  * -0.0001, When throwing `IllegalArgumentException`, it is best practice to give a description why this is being thrown so the user can figure out how to properly format. ([link](https://github.com/CMU-17-214/xinyub/blob/main/homework/3/src/main/java/edu/cmu/cs/cs214/hw3/cryptarithm/expression/Cryptarithm.java#L75))
  * -0.0001, Since you implemented the Iterator Pattern, you might as well implement the `Iterable<T>` class built into `java.lang.Iterable`. This will allow you to use the enhanced for-loop syntax offered by Java instead of manually calling `hasNext` and `next`. For example, code that looks like this:

```
// Don't write code like this! There's a better way in modern Java.
Iterator<T> iterator = list.iterator();
while (iterator.hasNext()) {
  T x = iterator.next();
  /* Code using x */
}
```

can be converted to code that looks like this:

```
// Much better :)
for (T x : list) {
  /* Code using x */
}
```
  * -1, You should declare variables by their interfaces, not by the class that implements that interface. For example, it is better practice to declare a variable as having type `List` instead of `ArrayList`. ([link](https://github.com/CMU-17-214/xinyub/blob/main/homework/3/src/main/java/edu/cmu/cs/cs214/hw3/permutation/Permutation.java#L14)) See Effective Java Item 64.
  * -1, Avoid using magic numbers in your code. Declare variables as constants at the top of the file. ([link](https://github.com/CMU-17-214/xinyub/blob/main/homework/3/src/main/java/edu/cmu/cs/cs214/hw3/cryptarithm/expression/Cryptarithm.java#L118))
  * -2, Please remove commented out code or dead code, your submissions should be finalized. These include unused imports, private methods, variables, and initializations. ([link](https://github.com/CMU-17-214/xinyub/blob/main/homework/3/src/main/java/edu/cmu/cs/cs214/hw3/cryptarithm/expression/Cryptarithm.java#L82))
  * -0.0001, It is best practice to make a copy before returning a mutable field. Your `getPermutation` method is declared as public and returns a reference to a mutable field, and so code external to the package can modify the data directly and destroy invariants. Fields of immutable type, like `String`, don't suffer from this problem. See Effective Java Item 50: Make defensive copies when needed ([link](https://github.com/CMU-17-214/xinyub/blob/main/homework/3/src/main/java/edu/cmu/cs/cs214/hw3/permutation/Permutation.java#L53)).
  * -0.0001 When you are doing the [`formalCheck`](https://github.com/CMU-17-214/xinyub/blob/main/homework/3/src/main/java/edu/cmu/cs/cs214/hw3/cryptarithm/expression/Cryptarithm.java#L43-L83) in your `Cryptarithm` class, it would have been better practice to create the `CrytarithmExpression`s for the left and right hand side then, rather than repeatedly creating it whenever `evalExpr` was called on each side. The representaiton of the sides as `String[]` is not robust and leads to repeated creation of the `CryptarithmExpression` objects.

#### Use inheritance and delegation, and design patterns effectively to achieve design flexibility and code reuse (19/30)
  * -5, Your cryptarithm uses the provided `Expression` library inappropriately because your `CrytarithmExpressionContext` has information about solving the crytarithm. The purpose of this object was simply providing the context, not a place of solving logic or to store multiple mappings. ([link](https://github.com/CMU-17-214/xinyub/blob/main/homework/3/src/main/java/edu/cmu/cs/cs214/hw3/cryptarithm/expression/CryptarithmExpressionContext.java))
  * -5, For sets of any non-trivial size, it's not feasible to store a collection of all permutations in memory (as your solution does). The goal of using the command pattern or iterator pattern in this context is to allow a client to execute code for each permutation as the permutations are generated, without storing all the permutations simultaneously in memory.
  * -1, Your cryptarithm solver's return value is not reusable for anything other than printing the solution. This makes your program not testable separately and is generally bad practice.

#### Discussion of design alternatives (7/10): Your design rationale was adequate, but we identified minor flaws.
  * You do not adequately discuss how you achieved specific design goals with your design decisions.

---

#### Total (71/100)

Late days used: 0 (5 left)

---

#### Additional Notes

Graded by: Michelle Zhu (mzhu3@andrew.cmu.edu)

To view this file with formatting, visit the following page: https://github.com/CMU-17-214/xinyub/blob/master/grades/hw3.md
