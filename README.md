# README Mission-Life

***

This document describes how to get the programme up and running.
In addition, relevant documents that have contributed to
the creation of this project are uploaded and linked.
Further, the document also describes the use of the programme.

## Requirements

***

Java: Version 21 or newer must be installed.


## Installation instructions

***

Follow the steps below to run the programme locally on your computer:

Clone the repository to your computer:

bash

````bash
git clone https://github.com/Bruno-Pinto/Mission-Life.git
````

Execute:
enter the project folder in terminal with
````bash
cd ./Mission-Life/
````
then to run the programme execute following command

````bash
./gradlew run
````

Further arguments can be passed to set the run mode of the programme.<br>
They affect the level of logging that will be output and the following options are available:<br>
- "debug" sets the level to Level.FINER<br>
- "development" sets the level to Level.INFO<br>
- "release" sets the level to Level.WARNING<br>

by adding the -DappMode flag after run
````bash
./gradlew run -DappMode=release
````
If no run mode is set, a default level of Level.INFO will be used.
***