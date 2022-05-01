hw5a Feedback
============

Team 16

#### Description of Framework (22/25)

* -3, Your domain is too narrow-- it will be difficult to write interesting data plugins for your framework.
  * You can expand to be many viruses and don't just focus on vaccines. If you just focus on covid vaccines then there is really only one data plugin that you can write.

#### Framework Design (32/35)

Basic Structure (Understanding + Old Concepts) (15/15)

* You have demonstrated a clear understanding of the basics of a framework and have used design principles covered in the class effectively. Good job!

Framework/Plugin Interaction & Control Flow (New Concepts) (12/15)

You have demonstrated a good understanding of the assignment of responsibilities between a framework and its plugins, but we identified the following minor issues: 

* -3, It's unclear how the display plugin gets access to the data from the data plugin. Specifically, upon transformation of the data plugin data, it is not clear how new elements are added to the dataset to be used in the display plugin. It would be better to have two objects. For example, the data plugins could return a RawDataSet and then the framework could convert this class into a TransformedDataSet which would make it really easy to maintain both types of data objects.

Style (5/5)

* Your notation meets our expectations regarding style.

#### Planning (10/10)

* Your project planning fully meets our expectations, good job!

#### Presentation (20/20)

Talk quality (17/17)
Timing (3/3)

#### Other Feedback

---

#### Total (84/90)

Late days used: 0 (2 left for hw5)

---

#### Additional Notes

Graded by: Nathaniel Belinkis (nbelinki@andrew.cmu.edu)

To view this file with formatting, visit the following page: https://github.com/CMU-17-214/xinyub/blob/master/grades/hw5a.md

