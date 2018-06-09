//
// Created by pan on 27.05.18.
//

#include "package_converter.h"
#include <iostream>

const std::string PackageConverter::SOH = "00000001";
const std::string PackageConverter::EOT = "00000100";
const std::string PackageConverter::ESC = "00011011";
const std::string PackageConverter::INSTEAD_OF_SOH = "01000001"; //A = 0x41
const std::string PackageConverter::INSTEAD_OF_EOT = "01000010"; //B
const std::string PackageConverter::INSTEAD_OF_ESC = "01000011"; //C

const std::string PackageConverter::isolate(const std::string &packaged_str) const {
    const int ASCII_CHAR_SIZE = 8;
    string str;
    string temp;
    int i = 0;
    while(packaged_str.substr(i, SOH.size()) != SOH && i < packaged_str.size()){
        i++;
    }
    i += ASCII_CHAR_SIZE;

    while(packaged_str.substr(i, EOT.size()) != EOT && i < packaged_str.size()){
        if(packaged_str.substr(i, ESC.size()) == ESC){
            i += ASCII_CHAR_SIZE;
            temp = packaged_str.substr(i, ASCII_CHAR_SIZE);
            if(temp == INSTEAD_OF_SOH){
                str += SOH;
                i += ASCII_CHAR_SIZE;
            } else if(temp == INSTEAD_OF_EOT){
                str += EOT;
                i += ASCII_CHAR_SIZE;
            } else if(temp == INSTEAD_OF_ESC){
                str += ESC;
                i += ASCII_CHAR_SIZE;
            } else {
                std::cerr << "Znaleziono ESC, ale nie znaleziono zakodowanego znaku za nia, to nie powinno sie wydarzyc" << std::endl;
            }
        } else{
            str += packaged_str.at(i);
            i++;
        }
    }
    i += ASCII_CHAR_SIZE;

    if (i >= packaged_str.size()){
        std::cerr << "Zly format ramki" << std::endl;
        return "";
    }

    if(packaged_str.substr(i, crc_value.size() - 1) != crc(str)){
        std::cerr << packaged_str << std::endl;
        std::cerr << str << std::endl;
        std::cerr << i << std::endl;
        std::cerr << packaged_str.substr(i, 10000) << std::endl;
        std::cerr << "Bledny klucz crc!" << std::endl;
        return "";
    }

    return str;
}

const std::string PackageConverter::package(const std::string &str) const {
    const int ASCII_CHAR_SIZE = 8;

    string frame;
    string temp;
    frame+=SOH;
    int i = 0;
    while(i < str.size()){
        temp = str.substr(i, ASCII_CHAR_SIZE);
        if(temp == SOH){
            frame += ESC;
            frame += INSTEAD_OF_SOH;
            i += ASCII_CHAR_SIZE;
        } else if(temp == EOT){
            frame += ESC;
            frame += INSTEAD_OF_EOT;
            i += ASCII_CHAR_SIZE;
        } else if(temp == ESC){
            frame += ESC;
            frame += INSTEAD_OF_ESC;
            i += ASCII_CHAR_SIZE;
        } else {
            frame += str.at(i);
            i++;
        }

    }
    frame += EOT;
    frame += crc(str);

    return frame;
}

 /* Private methods */
const std::string PackageConverter::crc(const std::string & str) const {
    string code = str;
    for(int i = 0; i < crc_value.size() - 1; i++)
        code += "0";

    for(int i = 0; i < str.size(); i++){
        if(code.at(i) == '0')
            continue;
        for(int j = 0; j < crc_value.size(); j++){
            if(code.at(i + j) == crc_value.at(j))
                code.at(i + j) = '0';
            else
                code.at(i + j) = '1';
        }
    }

    return code.substr(code.size() - (crc_value.size() - 1), crc_value.size() - 1);
}
