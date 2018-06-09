#include <iostream>
#include <thread>
#include "cable.h"
#include "host.h"

const int CABLE_SIZE = 62;

void print_arrau(int array[], int size);



int main(){

    Cable cable(CABLE_SIZE);
    Host host1("host1", cable, 0);
    Host host2("host2", cable, CABLE_SIZE / 2);
    Host host3("host3", cable, CABLE_SIZE - 1);

    std::thread thread1(&Host::propagate, &host1, " AA ");
    std::thread thread2(&Host::propagate, &host2, " B ");
    std::thread thread3(&Host::propagate, &host3, " XYZ ");

    thread1.join();
    thread2.join();
    thread3.join();

    return 0;
}


void print_arrau(int array[], int size){
    for(int i = 0; i < size; i++)
        std::cout << array[i] << " ";
}