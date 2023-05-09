# AtmosFears-BE
AtmosFears backend repository

# Requirements
1. Java 17
2. Maven
3. R (with additional libraries)

# Maven setup for windows
1. Download Maven 3.9.1 by clicking this [link](https://dlcdn.apache.org/maven/maven-3/3.9.1/binaries/apache-maven-3.9.1-bin.zip)
2. Unpack to `C:\Program Files\Maven\`
3. Add Maven to **Path** in your system variables. (if you unpack properly it should be `C:\Program Files\Maven\apache-maven-3.9.1\bin`) 
4. Make sure, you have JAVA_HOME (Java 17) in your System variables. Example path: `C:\Users\<user>\.jdks\corretto-17.0.4.1`
5. Now you should be able to run `mvn --version` in your terminal

# Maven setup for linux
Just run `sudo apt install maven`, and make sure your java version is 17.

If you want the newest version of maven check [this](https://www.digitalocean.com/community/tutorials/install-maven-linux-ubuntu)

# MongoDB setup
1. Create a copy of `db.example.properties` as `db.properties`
2. Fill credentials

# Useful commands
Running app (it also recompile your changes):

`mvn spring-boot:run`


Build app from scratch:

`mvn clean install`

# R setup

To be able to use the prediction model in this project you need to have R environment set up.

First, you need to download and install the R itself.
You can do that via this link: https://cloud.r-project.org/

Then you need to download and install couple additional packages:
1. https://cran.r-project.org/web/packages/Cubist/index.html
2. https://cran.r-project.org/web/packages/reshape2/index.html
3. https://cran.r-project.org/web/packages/plyr/index.html
4. https://cran.r-project.org/web/packages/rules/index.html
5. https://cran.r-project.org/web/packages/rio/index.html
6. https://cran.r-project.org/web/packages/zip/
7. https://cran.r-project.org/web/packages/openxlsx/

Depending on the platform you use you need to do it slightly differently,
but it pretty much comes down to placing the downloaded decompressed file into the library director of R.
For the server, there will a script auto installing this lovely mess (not implemented yet).