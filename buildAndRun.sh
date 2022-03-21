#!/bin/sh
mvn clean package
cp ./target/jpa.war C:/Sun/wildfly-26.0.0.Final/standalone/deployments