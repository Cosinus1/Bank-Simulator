#!/bin/bash

# Set the path to the SQLite JDBC driver JAR
SQLITE_JAR="../lib/sqlite-jdbc-3.34.0.jar"

# Set the path to the Java executable
JAVA_EXEC="/usr/lib/jvm/java-11-openjdk-amd64/bin/java"

# Set the main class for your Java application
MAIN_CLASS="com.Banksim.Main"

# Set the classpath including the current directory and SQLite JDBC JAR
CLASSPATH=".:$SQLITE_JAR"

# Compile the Java code
javac -cp $CLASSPATH main/java/com/Banksim/*.java
javac -cp $CLASSPATH main/java/com/Banksim/*/*.java
# Check if the compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful. Running the application..."
    
    # Run the Java application
    $JAVA_EXEC -cp $CLASSPATH main.java.com.Banksim.Main
    
else
    echo "Compilation failed. Please check your code for errors."
fi
