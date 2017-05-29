#! /bin/sh
#  /etc/init.d/iobserve

### BEGIN INIT INFO
# Provides:          mydaemon
# Required-Start:    $remote_fs $syslog
# Required-Stop:     $remote_fs $syslog
# Short-Description: Starts the MyDaemon service
# Description:       This file is used to start the daemon
#                    and should be placed in /etc/init.d
### END INIT INFO

NAME="iobserve"
DESC="iobserve service"

# The path to Jsvc
EXEC="/usr/bin/jsvc"

# The path to the folder containing MyDaemon.jar
FILE_PATH="/home/jweg/iobserve-analysis/"

# The path to the folder containing the java runtime
JAVA_HOME="/usr/lib/jvm/java-8-oracle"

# Our classpath including our jar file and the Apache Commons Daemon library
CLASS_PATH="$FILE_PATH/analysis.service/build/libs/analysis.service-0.0.2-SNAPSHOT.jar"

# The fully qualified name of the class to execute
CLASS="org.iobserve.analysis.service.AnalysisDaemon"

# Any command line arguments to be passed to the our Java Daemon implementations init() method
ARGS="-i 3000 -o localhost:9090 -p /home/jweg/models/WorkingTestPCM/pcm/ -c test.rac"

#The user to run the daemon as
USER="jweg"

# The file that will contain our process identification number (pid) for other scripts/programs that need to access it.
PID="$FILE_PATH/$NAME.pid"

# System.out writes to this file...
LOG_OUT="$FILE_PATH/log/$NAME.out"

# System.err writes to this file...
LOG_ERR="$FILE_PATH/err/$NAME.err"

jsvc_exec()
{  
    cd $FILE_PATH
    $EXEC -home $JAVA_HOME -cp $CLASS_PATH -user $USER -outfile $LOG_OUT -errfile $LOG_ERR -pidfile $PID $1 $CLASS $ARGS
}

if [ ! -f $CLASS_PATH ] ; then
	echo $CLASS_PATH error
	exit 1
fi

case "$1" in
    start) 
        echo "Starting the $DESC..."       
       
        # Start the service
        jsvc_exec
       
        echo "The $DESC has started."
    ;;
    stop)
        echo "Stopping the $DESC..."
       
        # Stop the service
        jsvc_exec "-stop"      
       
        echo "The $DESC has stopped."
    ;;
    restart)
        if [ -f "$PID" ]; then
           
            echo "Restarting the $DESC..."
           
            # Stop the service
            jsvc_exec "-stop"
           
            # Start the service
            jsvc_exec
           
            echo "The $DESC has restarted."
        else
            echo "Daemon not running, no action taken"
            exit 1
        fi
            ;;
    *)
    echo "Usage: /etc/init.d/$NAME {start|stop|restart}" >&2
    exit 3
    ;;
esac

# end
