#!/bin/sh
classpath="bin"

for lib in $(find lib -name '*.jar'); do
	classpath="${classpath}:${lib}"
done

rm -rf bin
mkdir -p bin

find src -name '*.java' -exec javac -d bin -cp "${classpath}" {} \+
