#!/bin/sh

# ---------------------------------------------------------------------------
# Batch file to run command line tool for Orange
# ---------------------------------------------------------------------------


ORANGE_HOME=`pwd`

INITIAL_HEAP_SIZE=64m
MAXIMUM_HEAP_SIZE=1024m
STACK_SIZE=3m

JAVA_OPTS="-Xms$INITIAL_HEAP_SIZE -Xmx$MAXIMUM_HEAP_SIZE -Xss$STACK_SIZE"

# Options:
#  -d      => Debug / verbose mode
#  -c | -r => Create or Replace mode
#
java $JAVA_OPTS -cp $ORANGE_HOME/target/orange-1.0-SNAPSHOT.jar:$ORANGE_HOME/lib/antlr-3.1.1.jar OrangeCompiler  $@
