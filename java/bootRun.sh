#!/bin/bash
exec ps -ef | grep -v ".svn" | grep "japi"
if [ $? -eq 0 ];then
    kill `ps -ef | grep -v ".svn" | grep "japi" | awk '{print $2}'`
fi
gradle bootRun &
