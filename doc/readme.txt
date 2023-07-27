---------------------------------------
install python hwp to html library

    pip install cffi==1.15.1
    pip install  cryptography==3.4.6
    pip install lxml
    pip install  olefile==0.46
    pip install  pycparser==2.21
    pip install  pyhwp==0.1b15
    
---------------------------------------
install python pdf to html library

To get started you will need to install the dependencies:

    sudo apt update
    sudo apt install -y libfontconfig1 libcairo2 libjpeg-turbo8

If you get error about unmet dependencies run the following to fix broken packages

    sudo apt apt --fix-broken install

Download latest *.deb package from pdf2htmlEX repository : https://github.com/pdf2htmlEX/pdf2htmlEX/releases

    wget https://github.com/pdf2htmlEX/pdf2htmlEX/releases/download/v0.18.8.rc1/pdf2htmlEX-0.18.8.rc1-master-20200630-Ubuntu-bionic-x86_64.deb
    sudo mv pdf2htmlEX-0.18.8.rc1-master-20200630-Ubuntu-bionic-x86_64.deb pdf2htmlEX.deb

Install the package

    sudo apt install ./pdf2htmlEX.deb

It is very important that you use a (relative or absolute) path to the *.deb file. It is the ./ in front of the pdf2htmlEX.deb file name which tells apt install that it is supposed to install a local file rather than a package name in apt install's internal package database.

Alternatively you could use the following commands:

    sudo dpkg -i pdf2htmlEX.deb

    sudo apt install -f

Test your installation

    pdf2htmlEX -v
---------------------------------------
