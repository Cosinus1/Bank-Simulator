#!/bin/bash

# Set the path to the Java executable
JAVA_EXEC="/usr/lib/jvm/java-11-openjdk-amd64/bin/java"

# Read dependencies from dependencies.md and construct the classpath
CLASSPATH=".:"
while IFS= read -r line; do
    if [[ "$line" == *"- ["* ]]; then
        jar=$(echo "$line" | grep -oP '\(\K[^\)]+')
        CLASSPATH+="lib/$jar:"
    fi
done < dependencies.md

# Set the source file path
SOURCE_FILE="src/main/java/com/Banksim/Front/FrontMain.java"

# Compile the Java code
javac -cp $CLASSPATH $SOURCE_FILE

# Check if the compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful. Running FrontEnd..."

    # Extract the class name without the extension
    CLASS_NAME=$(basename $SOURCE_FILE .java)

    # Run the JavaFX application
    $JAVA_EXEC -cp $CLASSPATH $CLASS_NAME

else
    echo "Compilation failed. Please check your code for errors."
fi
