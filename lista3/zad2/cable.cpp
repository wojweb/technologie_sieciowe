//
// Created by pan on 28.05.18.
//

#include <iostream>
#include "cable.h"

Cable::Cable(int size_v) : size_of_array(size_v){
    array = new char[size_v];
    for(int i = 0; i < size_of_array; i++)
        array[i] = '0';
}

Cable::~Cable() {
    delete array;
}

void Cable::lock() {
    mutex.lock();
}

void Cable::unlock() {
    mutex.unlock();

}

char &Cable::operator[](int index) {
    return array[index];
}

void Cable::print() {
    for(int i = 0; i < size_of_array; i++)
        std::cout << array[i];

    std::cout << std::endl;
}

int Cable::size() {
    return size_of_array;
}
