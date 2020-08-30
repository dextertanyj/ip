#!/usr/bin/env bash

# create bin directory if it doesn't exist
if [ ! -d "../bin" ]
then
    mkdir ../bin
fi

# delete output from previous run
if [ -e "./ACTUAL.TXT" ]
then
    rm ACTUAL.TXT
fi

mv $HOME/duke/localData/data.duke $HOME/duke/localData/data.duke.temp

# compile the code into the bin folder, terminates if error occurred
if ! javac -cp ../src -Xlint:none -d ../bin ../src/main/java/duke/*/*.java ../src/main/java/duke/*.java
then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

# run the program, feed commands from input.txt file and redirect the output to the ACTUAL.TXT
java -classpath ../bin duke.CliWrapper < input.txt > ACTUAL.TXT

mv $HOME/duke/localData/data.duke.temp $HOME/duke/localData/data.duke

# convert to UNIX format
cp EXPECTED.TXT EXPECTED-UNIX.TXT
dos2unix ACTUAL.TXT EXPECTED-UNIX.TXT

# compare the output to the expected output
diff ACTUAL.TXT EXPECTED-UNIX.TXT
if [ $? -eq 0 ]
then
    echo "Test result: PASSED"
    exit 0
else
    echo "Test result: FAILED"
    exit 1
fi