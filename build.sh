#!/bin/bash

echo  " ==============tips =============="
echo  "0.x use jdk8 please"
echo  "jdk manage tool :  https://sdkman.io/install"
echo  " ==============tips =============="
echo  " ==============当前maven环境 =============="
echo  $(mvn -v)
echo  " ==============当前maven环境 =============="


cd jdevelops-build
mvn clean package install -Dmaven.test.skip=true | tee -a build.log

cd ../jdevelops-parent
mvn clean package install -Dmaven.test.skip=true | tee -a parent.log

cd ../jdevelops-annotations
mvn clean package install -Dmaven.test.skip=true | tee -a annotations.log

cd ../jdevelops-spi
mvn clean package install -Dmaven.test.skip=true | tee -a spi.log

cd ../jdevelops-utils
mvn clean package install -Dmaven.test.skip=true | tee -a utils.log


cd ../jdevelops-webs
mvn clean package install -Dmaven.test.skip=true | tee -a webs.log


cd ../jdevelops-apis
mvn clean package install -Dmaven.test.skip=true | tee -a apis.log


cd ../jdevelops-dals
mvn clean package install -Dmaven.test.skip=true | tee -a dals.log

cd ../jdevelops-delays
mvn clean package install -Dmaven.test.skip=true | tee -a delays.log

cd ../jdevelops-events
mvn clean package install -Dmaven.test.skip=true | tee -a events.log

cd ../jdevelops-logs
mvn clean package install -Dmaven.test.skip=true | tee -a logs.log

cd ../jdevelops-files
mvn clean package install -Dmaven.test.skip=true | tee -a files.log


cd ../jdevelops-authentications
mvn clean package install -Dmaven.test.skip=true | tee -a authentications.log

cd ../jdevelops-monitor
mvn clean package install -Dmaven.test.skip=true | tee -a frameworks.log

cd ../jdevelops-dependencies
mvn clean package install -Dmaven.test.skip=true | tee -a dependencies.log

cd ../jdevelops-frameworks
mvn clean package install -Dmaven.test.skip=true | tee -a frameworks.log


read -n 1 -s -r -p "Press any key to continue..."
