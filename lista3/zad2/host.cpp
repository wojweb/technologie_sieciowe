//
// Created by pan on 28.05.18.
//

#include "host.h"
#include <algorithm>
#include <iostream>
#include <thread>
#include <random>
//miliseconds
const int Host::START_WAIT_GAP = 300;
const int Host::INTERFRAME_GAP = 100;


Host::Host(string name_v, Cable & cable_v, int port_v) :
        port(port_v), cable(cable_v), host_name(name_v){

}

void Host::propagate(const string & message) {
        std::random_device random_device;
        std::default_random_engine random_engine(random_device());
        int wait_time = START_WAIT_GAP;
        bool jammed = true;

        while (jammed) {
            if(cable[port] == '0') {
                int distance_from_port = 0;
                while ((port - int(message.size()) + distance_from_port < cable.size()) ||
                       (port + int(message.size()) - distance_from_port >= 0)) {

                    cable.lock();

                    //Wykrywamy kolizje
                    bool b_left = port - distance_from_port >= 0 ? cable[port - distance_from_port] != '0': false;
                    bool b_right = port + distance_from_port < cable.size() ? cable[port + distance_from_port] != '0' : false;
                    if(b_left || b_right){
                        if(b_left){
                            for(int i = port - distance_from_port + 2; i < std::min(port + distance_from_port - 1, cable.size()); i++)
                                cable[i] = '0';
                            cable[port - distance_from_port] = '0';
                        }
                        if(b_right) {
                            for (int i = std::max(port - distance_from_port + 1, 0); i < port + distance_from_port - 1; i++)
                                cable[i] = '0';
                            cable[port + distance_from_port] = '0';
                        }

                        std::cout << "Host: " << host_name << " wykryl kolizje\n";
                        cable.unlock();
                        std::uniform_int_distribution<int> uniform_dist(1, wait_time);
                        std::this_thread::sleep_for(std::chrono::milliseconds(uniform_dist(random_engine)));
                        wait_time *= 2;
                        jammed = true;

                        break;
                    }

                    //Przesuwamy wiadomosc o jeden element
                    for (int letter_of_message = 0; letter_of_message < message.size(); letter_of_message++) {
                        int letter_distance_from_port = distance_from_port - letter_of_message;
                        if (letter_distance_from_port == 0)
                            cable[port] = message.at(letter_of_message);

                        if (letter_distance_from_port == 1) {
                            if (port - 1 >= 0 && port + 1 < cable.size()) {
                                cable[port - 1] = cable[port + 1] = cable[port];
                                cable[port] = '0';
                            } else if (port - 1 >= 0)
                                shift_left(port - 1);
                            else if (port + 1 < cable.size())
                                shift_right(port + 1);
                        }

                        if (letter_distance_from_port > 1) {
                            if (port + letter_distance_from_port < cable.size())
                                shift_right(letter_distance_from_port + port);
                            if (port + letter_distance_from_port == cable.size())
                                cable[cable.size() - 1] = '0';

                            if (port - letter_distance_from_port >= 0)
                                shift_left(port - letter_distance_from_port);
                            if (port - letter_distance_from_port == -1)
                                cable[0] = '0';
                        }

                    }
                    cable.print();
                    cable.unlock();

                    std::this_thread::sleep_for(std::chrono::milliseconds(2));

                    distance_from_port++;
                    jammed = false;
                }
            } else {
                std::this_thread::sleep_for(std::chrono::milliseconds(INTERFRAME_GAP));
            }
            cable.print();
        }
}

void Host::shift_right(int dest_index) {
    cable[dest_index] = cable[dest_index - 1];
    cable[dest_index - 1] = '0';
}

void Host::shift_left(int dest_index) {
    cable[dest_index] = (cable)[dest_index + 1];
    cable[dest_index + 1] = '0';

}
