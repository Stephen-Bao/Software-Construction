# Strategies for parallelization

## 1. Parallelization with the Executors framework
For parallelization with the Executors framework, I started off by creating a single task for computing the escape count and setting the color
for each pixel and submit all of them to the thread pool that I created. Instead of a speed up, however, I found out that the performance even
dropped by a factor of around 2. I asssume that it is perhaps due to the large overhead wasted on maintaining a big task queue, including enqueuing
and dequeuing for each task, since there are millons of small tasks to be done if I specify tasks in this way. Therefore, I changed the strategy to
wrap the rendering of each row/yPixel as a task, in which there would be only a few thousand bigger tasks. As expected, the results turns out to be
great. To take a step further, I tried to implement a version which is able to change the number of rows in which a task contains, trying to optimize
by adjusting the granularity of parallelization. The results turned out to be almost the same as the second approach, and even a little slower. As a result,
the final adopted version is the one that parallel with each row.   

## 2. Parallelization with streams
I also tried two different strategies for parallelization with streams. The first one is to apply parallel stream on a list of y pixels, which is similar
to the adopted one in the Executor strategy, and this also gives me almost the same performance. There's also a version of applying parallel stream on
the rendering of all pixels, but it is slower than the first one in my computer.

# Benchmarking results
|    Renderer   |   Initial figure  |   Outline of the small circular bulb on the left  |   A region contained within the bulb   |
|      :----    |        :----      |             :----                                 |               :----                    |
|   Sequential  |        342        |                    2167                           |                 4447                   |
|    Executor   |        223        |                    768                            |                 1492                   |
|     Stream    |        231        |                    818                            |                 1612                   |

All the time units are microseconds, and the results are the average of three renderings measured on my computer with a 
Intel i5-5200U 2 cores, 4 logical processors. The best result achieves a 1.53X on initial figure rendering, 2.82X on 
circular bulb outline rendering, and 2.98X on the slowest region rendering. This result is resonable to me, because for
simpler regions like the initial figure and the outline of the bulb, the overhead of maintaining the thread pool and task queue
would slightly affect the performance, whereas in the slowest region an up to 3X speed up is achieved, which can be considered as
superlinear in this situation.
