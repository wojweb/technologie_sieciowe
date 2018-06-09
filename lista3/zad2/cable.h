//
// Created by pan on 28.05.18.
//

#ifndef ZAD2_CABLE_H
#define ZAD2_CABLE_H

#include <mutex>

class Cable {

    char *array;
    int size_of_array;
    std::mutex mutex;

public:
    Cable(int size);
    ~Cable();

    char & operator[](int index);
    void lock();
    void unlock();
    void print();
    int size();
};


#endif //ZAD2_CABLE_H
