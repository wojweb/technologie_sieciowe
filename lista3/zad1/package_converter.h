//
// Created by pan on 27.05.18.
//

#ifndef ZAD1_PAKIETCONVERTER_H
#define ZAD1_PAKIETCONVERTER_H


#include <string>

using std::string;

class PackageConverter {
private:


    static const string SOH; //Start Of Header 0x01
    static const string EOT; //End Of Transmission 0x04
    static const string ESC; //Escape 0x1B
    static const string INSTEAD_OF_SOH; //A bin value
    static const string INSTEAD_OF_EOT; //B bin value
    static const string INSTEAD_OF_ESC; //C bin value

    string crc_value;

    const string crc(const string & str) const;

public:
    PackageConverter(const string & crc_v) : crc_value(crc_v){}
    const string isolate(const string & package_str) const;
    const string package(const string & str) const;
};


#endif //ZAD1_PAKIETCONVERTER_H
