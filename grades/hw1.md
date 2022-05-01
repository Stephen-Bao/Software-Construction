hw1 Feedback
============

#### Successful use of Git, GitHub, and build automation tools (4/5)

* -1, Your commit history ([link](https://github.com/CMU-17-214/xinyub/commits/main)) is not useful. A useful commit history separates work into multiple commits, instead of one or two for the whole assignment. Each commit should have a concise, but descriptive name about what was changed. Committing your work incrementally protects you from data loss or network problems, which might otherwise cause you to lose work or fail to meet the homework deadlines (which are strictly enforced). For more information on writing useful commit messages, see [here](https://git-scm.com/book/ch5-2.html#Commit-Guidelines) or [here](http://chris.beams.io/posts/git-commit/).

#### Basic proficiency in Java (20/20)

* -0.0001, Please add `@Override` annotation before overriding methods. ([link](https://github.com/CMU-17-214/xinyub/blob/main/homework/1/src/main/java/edu/cmu/cs/cs214/hw1/Document.java#L65)) This annotation helps because (1) it adds a compile-time check that makes sure you're actually overriding a method, and (2) it makes your code easier to understand to the human reader.
* -0.0001, You should declare variables by their interfaces, not by the class that implements that interface. For example, it is better practice to declare a variable as having type `Map` instead of `HashMap`. ([link](https://github.com/CMU-17-214/xinyub/blob/main/homework/1/src/main/java/edu/cmu/cs/cs214/hw1/Document.java#L14)) See Effective Java Item 64.
* -0.0001, You should auto-close resources with a `try-with-resources` block or manually close with a `finally` block when you are done with them. Although it does not matter in a short lived command line program you wrote here, for larger-scale programs, it is important to ensure that leaky resources are closed.
 Example:

```java
try (Scanner sc = new Scanner(new URL(this.url).openStream())) {
  while (sc.hasNext()) {
    ...
  }
}
```
* -0.0001, You haven't checked the number of input arguments. The program requires more than one URL to work; try to use IllegalArgumentException to warn the user.
* -0.0001, You do not need type inference [here](https://github.com/CMU-17-214/xinyub/blob/main/homework/1/src/main/java/edu/cmu/cs/cs214/hw1/Document.java#L32); you should replace that line with this: `String word = in.next();`.

#### Fulfilling the technical requirements of the program specification (13/15)

* -2, `FindClosestMatches` does not conform to specifications. It does not use a collection as specified in the writeup. See [link](https://github.com/CMU-17-214/xinyub/blob/main/homework/1/src/main/java/edu/cmu/cs/cs214/hw1/FindClosestMatches.java#L23).
* -0.0001, The writeup specified creating a `Document` class rather than a program called `Document`. You do not need [this](https://github.com/CMU-17-214/xinyub/blob/main/homework/1/src/main/java/edu/cmu/cs/cs214/hw1/Document.java#L69-L85).

#### Documentation and code style (9/10)

* -1, Please remove print statements used for debugging, your submissions should be finalized. ([here](https://github.com/CMU-17-214/xinyub/blob/main/homework/1/src/main/java/edu/cmu/cs/cs214/hw1/Document.java#L36) and [here](https://github.com/CMU-17-214/xinyub/blob/main/homework/1/src/main/java/edu/cmu/cs/cs214/hw1/Document.java#L44))
* -0.0001, Your files contain both tabs and spaces, or your files use inconsistent indentation levels. While we don't care what style indentation you use, we do care about consistency.

---

#### Total (46/50)

Late days used: 0 (5 left)

---

#### Additional Notes

Graded by: Olivia Xu (okx@andrew.cmu.edu)

To view this file with formatting, visit the following page: https://github.com/CMU-17-214/xinyub/blob/master/grades/hw1.md
