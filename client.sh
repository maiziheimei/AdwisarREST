#!/bin/sh
classpath="bin"

for lib in $(find lib -name '*.jar'); do
	classpath="${classpath}:${lib}"
done

java -cp "${classpath}" example.Client $@
