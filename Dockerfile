FROM josediaz30/oracle-jdk8-maven3

# File Author
MAINTAINER Jose Diaz

# Bundle app source
COPY . /src

# Install app dependencies
RUN cd /src; mvn package install

# Expose port
EXPOSE 3000

# Setting run configuration
#ENV MODE=production

# Run app
CMD ["java", "jar","-Dmode=production", "target/mars-1.0.0-jar-with-dependencies.jar"]
