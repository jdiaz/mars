mvn clean package
chmod 777 /target/mars-1.0.0-jar-with-dependencies.jar
cd target/
java -jar -Dmode=production mars-1.0.0-jar-with-dependencies.jar
