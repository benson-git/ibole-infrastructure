# ibole-infrastructure


Provided some out-of-box functionalities

- Distribution session based on redis
- Global lock based on redis
- Jwt utility

[![Build Status](https://travis-ci.org/benson-git/ibole-infrastructure.svg?branch=master)](https://travis-ci.org/benson-git/ibole-infrastructure)

Download
--------

Download [the JARs](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22infrastructure-all%22). Or for Maven, add to your `pom.xml`:
```xml
<dependency>
    <groupId>com.github.ibole</groupId>
    <artifactId>infrastructure-all</artifactId>
    <version>1.0.9</version>
</dependency>
```

Or for Gradle with non-Android, add to your dependencies:
```gradle
compile 'com.github.ibole:infrastructure-all:1.0.9'
```

Source Building
--------

1. Checkout the ibole-infrastructure source code:

  cd ~  
  git clone https://github.com/benson-git/ibole-infrastructure.git ibole-infrastructure  

  git checkout master  
  or: git checkout -b -v1.0.9  

2. Import the ibole-infrastructure source code to eclipse project:

  cd ~/ibole-infrastructure  
  mvn eclipse:eclipse  
  Eclipse -> Menu -> File -> Import -> Exsiting Projects to Workspace -> Browse -> Finish  

3. Build the ibole-infrastructure binary package:

  cd ~/ibole-infrastructure  
  mvn clean install -Dmaven.test.skip  
  cd ibole-infrastructure/target  
  ls  
