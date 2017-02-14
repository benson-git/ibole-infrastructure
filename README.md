# ibole-infrastructure

================================================================
Source Building
================================================================

1. Checkout the ibole-infrastructure source code:

    cd ~
    git clone https://github.com/benson-git/ibole-infrastructure.git ibole-infrastructure

    git checkout master
    or: git checkout -b -0.0.1

2. Import the ibole-infrastructure source code to eclipse project:

    cd ~/ibole-infrastructure
    mvn eclipse:eclipse
    Eclipse -> Menu -> File -> Import -> Exsiting Projects to Workspace -> Browse -> Finish

3. Build the ibole-infrastructure binary package:

    cd ~/ibole-infrastructure
    mvn clean install -Dmaven.test.skip
    cd ibole-infrastructure/target
    ls