# Mars
Blog articles API for **Script Fuzz**

### Requirements:
1. ```Java 8 ``` or higher
2. ```MongoDB```
3. ```Maven 3.3.1``` or higher

### Technologies:
* AngularJS 1.2.x
* Java8
* [SparkJava](http://sparkjava.com/) 2.2.x
* MongoDB 2.6.x

### Development Usage:
* Import the src code to your IDE
* Make sure you have ```mongod``` running in ```localhost@27017```
* Execute ```import-seed.sh```  
* Add VM parameter ```-Dmode=dev``` to IDE's run configuration
* Run BlogServer and visit localhost:4567 

### Standalone/Production:
* Add **Maven** and **Java8** to your ```PATH``` variable
* Assuming you have ```MongoDB``` runnning on your system ```@port 27017```
* Clone this repository
* Execute ```./deploy.sh``` -Edit this script for custom deployment
* Visit ```localhost:4567```

### Distribution:
* Running ```mvn clean package``` will create the distribution jar.
* The application is packaged as a ```fat jar``` named ```mars-VERSION-jar-with-dependencies.jar``` in the ```/target``` directory. Feel free to move this jar to your deployment directory of choosing.
* To Run simple execute: ```java -jar mars-VERSION-jar-with-dependnecies.jar```, where VERSION is as specified in the ```pom.xml```. 

### Preview:
![](http://i1370.photobucket.com/albums/ag268/josediaz301/mars_zpsaurru6cf.png)

