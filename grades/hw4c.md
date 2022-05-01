hw4c Feedback
============


### Design of GUI (30/30)




##### Separation of GUI and Core (20/20), Your implementation effectively separates the GUI from the core implementation.





##### General GUI design (10/10), You effectively applied best practices of GUI design.




### Implementation of GUI (39/40)




##### Appropriate handling of events using the observer pattern (10/10), Your GUI correctly handled the various board state change events. Well done!




##### GUI Gameplay (20/20), Your GUI contains all the key aspects of Carcassonne gameplay. Great job!




##### Build Automation using Travis and Gradle (5/5), Build automation using gradle and Travis CI seems to work fine and we were able to start your game using `gradle run`. Nice! 





##### Documentation + Style (4/5) Your implementation mostly meets our expectations regarding documentation and style, but we have some smaller suggestions for improvement:

 * -1, You had large pieces of repetitive code that could have been avoided by implementing it another way. The way you create tiles, as well as some of your switch cases in your GUI implementation are what is being referred to here.


### Reflection on the design process (10/10), You submitted a design change discussion that explained your design choices. Well done!





---

#### Total (79/80)

#### hw4a points Back: 8 * 0.75 = 6
In the future, please make it clear what specific rubric item you are trying to get points back for. You didn't do so--you listed all your changes, even the small ones you didn't get points off for, so typically the policy is we would not look at your changes and you would not get points back. It is unclear from your response.pdf what specific rubric items you want the grader to look at.


Point values and what you earned them for:

Domain Model 

  * +2, Segments included

SSD


Behavioral Contract



Interaction Diagram 1 - Tile Validation

  * Notation +1, fixed external call/user issue
  * Notation +1, if statement notation
  * +0, You added more detail but you did not show looping through neighboring locations to check for an existing abutting tile. I'm sorry, but this part doesn't qualify for points back.

Interaction Diagram 2 - Monastery Scoring

  * Notation +0, You fixed some issues but didn't our final diagram doesn't extend lifelines after the solid rectangles. 
  * You say you redrew the diagram but did not point out any rubric items you are trying to gain back. I'm very sorry, but the policy is we don't give points back for this. 

Object Model

  * +1, Fixed: It's not clear how the game models a tile / It is not clear if the game's model of a tile will be sufficient to fully determine scoring interactions.
  * You did not point out any rubric items you are trying to gain back. I'm very sorry, but the policy is we don't give points back for this. 

Rationale

  * +3, mentioned design principles



Late days used: 0 (5 left)

---

#### Additional Notes

Graded by: Ashley Wang (awwang@andrew.cmu.edu)

To view this file with formatting, visit the following page: https://github.com/CMU-17-214/xinyub/blob/master/grades/hw4c.md

