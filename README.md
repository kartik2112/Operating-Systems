# Operating-Systems
Implementations of standard OS algorithms

#### All algorithms used are referred from Operating Systems by William Stallings (ISBN: 97988131725283)
##### Implementations are coded in Java

- __Dining Philosopher's Problem:__

    Five silent philosophers sit at a round table with bowls of spaghetti. Forks are placed between each pair of adjacent philosophers.
    
    Each philosopher must alternately think and eat. However, a philosopher can only eat spaghetti when they have both left and right forks. Each fork can be held by only one philosopher and so a philosopher can use the fork only if it is not being used by another philosopher. After an individual philosopher finishes eating, they need to put down both forks so that the forks become available to others. A philosopher can take the fork on their right or the one on their left as they become available, but cannot start eating before getting both forks.
    
    Eating is not limited by the remaining amounts of spaghetti or stomach space; an infinite supply and an infinite demand are assumed.
    
    The problem is how to design a discipline of behavior (a concurrent algorithm) such that no philosopher will starve; i.e., each can forever continue to alternate between eating and thinking, assuming that no philosopher can know when others may want to eat or think.

    [Wikipedia](https://en.wikipedia.org/wiki/Dining_philosophers_problem)

- __Producer Consumer Problem:__

    The problem describes two processes, the producer and the consumer, who share a common, fixed-size buffer used as a queue. The producer's job is to generate data, put it into the buffer, and start again. At the same time, the consumer is consuming the data (i.e., removing it from the buffer), one piece at a time. The problem is to make sure that the producer won't try to add data into the buffer if it's full and that the consumer won't try to remove data from an empty buffer.
    
    [Wikipedia](https://en.wikipedia.org/wiki/Producer%E2%80%93consumer_problem)

- __Reader Writer Problem:__
    
    There is a shared memory. There are multiple readers and writers. Now, reading and writing should be such that following conditions are satisfied:
    
    - Multiple readers can read from the same memory location at the same time.
    - Only one writer can write at a memory location at a time.
    - While a writer is writing, no other writers or readers should access that memory location.
