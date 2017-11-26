#!/bin/sh
rm -rf .\src\main\java\com
rm -rf .\src\main\resources\com
java -jar mybatis-generator-core-1.3.2.jar -configfile generatorConfig.xml -overwrite
