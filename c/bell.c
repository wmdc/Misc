/* Bell example. Pass the number of bells in as a command line argument
   to the executable. */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define WAIT_MS 300

void sleep(int delayMs);

int main(int argc, char **argv)
{
  printf("%d bells with delay of %d\n", atoi(argv[1]), WAIT_MS);

  for(int i = atoi(argv[1]); i > 0; i--) {
    putchar('\a'); //or '\x07'
    sleep(WAIT_MS);
    printf("the process time is %d ticks.\n", clock());
  }
}

void sleep(int delayMs) {
  clock_t s = clock();
  clock_t e = CLOCKS_PER_SEC * (delayMs / 1000.0f);

  while(clock() < s + e);
}
