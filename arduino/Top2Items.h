#ifndef TOP2ITEMS_H
#define TOP2ITEMS_H

#include <Arduino.h>

#define MAX_TASK_LENGTH 33 // 32 + 1 for null terminator

struct Top2Items {
    char task1[MAX_TASK_LENGTH];
    char task2[MAX_TASK_LENGTH];
};

#endif