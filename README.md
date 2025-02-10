# Dinig-Philosophers-Problem
**Title: Advanced Implementation of the Dining Philosophers Problem (Java)**

**Description**: Implementation of the classic Dining Philosophers problem in Java, extended with additional elements to simulate a more realistic scenario and demonstrate skills in concurrency, resource management, and system design.

**Key Features**:

**"Cook" Process**: Implementation of a separate process ("Cook") that generates "dishes" (data) and writes them to a shared text file (simulating a message queue). Multiple instances of this process run in parallel.

**Main Process**: Responsible for starting the "Cook" processes and the "Waiter" and "Philosopher" threads.

**"Waiter" Threads**: Threads dedicated to reading "dishes" from the shared file and depositing them in a "center table" (shared data structure). The "Waiters" can only deposit dishes if space is available, implementing basic capacity management.

**"Philosopher" Threads**: Threads that consume "dishes" from the "center table." Each philosopher takes the dish that has been waiting the longest (simulating a FIFO queue), provided one is available. The classic Dining Philosophers logic is implemented to prevent starvation and deadlock.

**Termination Control**: Processes and threads are terminated in a controlled manner: the "Cooks" upon reaching a certain number of dishes, the "Waiters" when there are no more dishes to serve, and the "Philosophers" when there are no more dishes available to eat.

**Demonstrated Skills**:

- Concurrency and parallelism.
- Threads and synchronization in Java.
- Shared resource management.
- Distributed system design.
- Message queue implementation.
- Deadlock and starvation problem solving.
