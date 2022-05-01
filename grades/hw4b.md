hw4b Feedback
============

#### Implementation of Carcassonne game (56/60)


* Representation of important classes (20/20), Your solution demonstrates a clear representation of domain concepts as data structures, with each component keeping track of sufficient information to perform its associated responsibilities.


* Tile placement and validation (15/15), Your solution demonstrates a correct and effective use of your representation of the board, tiles, and segments to perform tile validation and placement.



* Meeple placement and validation (5/5), The implementation of meeple placement and validation is correct or mostly correct.



* Scoring (16/20), The implementation of the scoring of features (and the associated state updates) is mostly correct, but we identified some flaws that threaten the correctness of this computation.

  * The game does not correctly compute the score for a road and city features as it calculated the number of segments that are in a feature instead of the number of tiles. However a tile may have multiple road segments and if the segments end up in the same feature you are double counting the tile. 
  


#### Program design (15/25), The design aspects of your implementation are mostly reasonable, but we have some major suggestions for improvement. 

* Responsibility assignment (mainly coupling and cohesion)  
  * Your implementation unnecessarily couples Segments and their positions. The responsibility of keeping track of the position (x,y) of the Segment is more appropriate for the Board class. Having the double-composition relationship here is not necessary and tightly couples the two classes together. ([link](https://github.com/CMU-17-214/xinyub/blob/8a4b37c0500ad5158abc8a2da2ffa6fa63fcdd7c/homework/4/src/main/java/edu/cmu/cs/cs214/hw4/core/Segment.java#L19-L20))
 
  * You intermix the functionality of *collecting scoring information about a feature* and *actually performing state updates related to completing a feature*. Consider separating them to improve cohesion and to facilitate testing. Furthermore, information collected during scoring (like completion status and other meeples on the feature) is useful in other places in the game, such as in move validation. For this reason, it is convenient to separate the information collection phase from the state updating phase.

  * The API exposed to the user should likely be concentrated in a single controller class, named something like `Game` or `Carcassonne`. This avoids scattering the user-visible API across many different classes. Many of your classes are public and contain public methods.

  * The game does not perform scoring at appropriate times (after each turn; at the end of the game). You do scoring when `placeNewTile` or `placeNewTileWithMeeple` is called, but these tasks should be separated and the scoring would belong better as a step in a method called `executeTurn` or something similar. Or, you can put it in your `nextRound` method, which currently only updates which player is the current player. 

 


* Extensibility  



* Code reuse

 * Your implementation copies quite a bit of code for initializing the tile stack. It would have been more appropriate to use the JSON configuration file format described on Piazza. ([link](https://github.com/CMU-17-214/xinyub/blob/main/homework/4/src/main/java/edu/cmu/cs/cs214/hw4/core/Deck.java))

 * We identified missed opportunities for code reuse in the implementation of updating score. Your `computeFinalScore` and `updateScoreAndMeeple` are very similar and the code code have been reused. [link](https://github.com/CMU-17-214/xinyub/blob/8a4b37c0500ad5158abc8a2da2ffa6fa63fcdd7c/homework/4/src/main/java/edu/cmu/cs/cs214/hw4/core/GameSystem.java#L121-L208)  


#### Testing and build automation (18/25)



* Testing practices (8/10), Your tests seem reasonable, but we have some smaller suggestions for improvement.

 * -2, Repetitive set-up/tear-down code should be in the `SetUp` or `tearDown` methods. You repeatedly create new tiles to use in many tests, which can be done in `setUp`.


* Test coverage (10/10), Your testing coverage seems reasonable. 

* Build automation (0/5), Your submission is not built on Travis CI due to errors in the top-level settings.gradle file or the homework 4 build.gradle file. Make sure to inspect the output of running `gradle build` so you can confirm that the correct project builds without errors.
 * ([link to Travis buiild] (https://travis-ci.com/github/CMU-17-214/xinyub/builds/221199237))


#### Documentation and style (9/10)

 * -1, You have a massive amount of commented out print statements in there that should not be in your final submission. ([link](https://github.com/CMU-17-214/xinyub/blob/main/homework/4/src/test/java/edu/cmu/cs/cs214/hw4/core/BoardTest.java))

---

#### Total (98/120)

Late days used: 0 (5 left)

---

#### Additional Notes

Graded by: Ashley Wang (awwang@andrew.cmu.edu)

To view this file with formatting, visit the following page: https://github.com/CMU-17-214/xinyub/blob/master/grades/hw4b.md

