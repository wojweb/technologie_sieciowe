#include <iostream>
#include <fstream>
#include "package_converter.h"

const string CRC_INITIALIZATION_VALUE = "1011";

int main() {
    using std::cout;
    using std::cin;
    using std::endl;
    using std::cerr;

    string src, dest, instr;

    cout << "Witam w moim konwerterze" << endl;

    cout << "Podaj plik zrodlowy: ";
    cin >> src;
    std::ifstream input;
    input.open(src);
    if(!input.is_open()){
        cerr << "Nie moge otworzyc pliku zrodlowego!!!!" << endl;
        exit(0);
    }
    string message, temp;
    while(input >> temp)
        message += temp;

    cout << "Podaj plik docelowy: ";
    cin >> dest;
    std::ofstream output;
    output.open(dest);
    if(!output.is_open()){
        cerr << "Nie moge otworzyc pliku docelowego!!!!" << endl;
        exit(0);
    }

    PackageConverter converter(CRC_INITIALIZATION_VALUE);

    cout << "Podaj rodzaj konwersji: \"pakuj\"/\"rozpakuj\": ";
    cin >> instr;
    if(instr == "pakuj")
        message = converter.package(message);
    else if(instr == "rozpakuj")
        message = converter.isolate(message);

    output << message;

    input.close();
    output.close();




    return 0;
}