## Reader Writer

- __RWSemReaderPri.java__ contains code for implementation of Reader Writer Problem __using Semaphores__. Here, _Readers have priority._

- __RWSemWriterPri.java__ contains code for implementation of Reader Writer Problem __using Semaphores__. Here, _Writers have priority._

- __RWMesPassngWP.java__ contains code for implementation of Reader Writer Problem __using Message Passing__. Here, _Writers have priority._

- __RWMonRP.java__ contains code for implementation of Reader Writer Problem __using Monitors__. Here, _Readers have priority._

__Problem Statement:__
  There is a shared memory. There are multiple readers and writers. Now, reading and writing should be such that following conditions are satisfied:

  - Multiple readers can read from the same memory location at the same time.
  - Only one writer can write at a memory location at a time.
  - While a writer is writing, no other writers or readers should access that memory location.
