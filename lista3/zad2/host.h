//
// Created by pan on 28.05.18.
//

#ifndef ZAD2_COMPUTER_H
#define ZAD2_COMPUTER_H

#include <string>
#include "cable.h"

using std::string;

class Host {
    string host_name;
    int port;
    Cable & cable;
    static const int START_WAIT_GAP;
    static const int INTERFRAME_GAP;


    void shift_right(int dest_index);
    void shift_left(int dest_index);

public:
    Host(string name_v, Cable & cable_v, int port_v);
    void propagate(const string & message);

};


#endif //ZAD2_COMPUTER_H
