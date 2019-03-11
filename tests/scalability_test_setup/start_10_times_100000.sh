#!/bin/bash

startTestSeries() {
    # contains testfiles and scenarios
    path_testfile="/home/nboltz/iobserve/test-data/testfiles"
    # contains the files actively used for the analysis
    path_pcm="/home/nboltz/iobserve/test-data/pcm"
    # path to logging files
    path_logging=$1
    echo $path_logging
    
    for (( c=0; c<10; c=c+1 ))
    do
		# switch comment to change testscenario
        for testseries in "equal_events_X_users" #"X_different_events_one_user"
        do
            # specification of the testscenario
            testseries_spez=$testseries
            echo $testseries_spez
            # name of the acutal testscenario
            testseries_name="$(echo ${testseries_spez} | sed -e "s/X/100000/g")"
            echo ${testseries_name}
            # paste current date time
            echo $(date +%y-%m-%d_%H:%M:%S)
            # path to the files of the scenario
            testseries_path="${path_testfile}/${testseries_spez}/${testseries_name}"
            echo $testseries_path
        
            # call iobserve
            ./iobserve/application/analysis.cli-0.0.2-SNAPSHOT/bin/analysis.cli -i ${testseries_path} -p ${path_pcm} -t 0 -v 0
        
            logging_folder="${path_logging}/${testseries_spez}/${c}"
            # creating new logging folder
            mkdir $logging_folder
            # moving logging files to new folder
            mv ${PWD}/TAllocationLogging.csv ${logging_folder}
            mv ${PWD}/TDeploymentLogging.csv ${logging_folder}
            mv ${PWD}/TEntryCallLogging.csv ${logging_folder}
            mv ${PWD}/TEntryCallSequenceLogging.csv ${logging_folder}
            mv ${PWD}/TEntryEventSequenceLogging.csv ${logging_folder}
            mv ${PWD}/TUndeploymentLogging.csv ${logging_folder}
            mv ${PWD}/UserBehaviorTransformationLogging.csv ${logging_folder}
        done
    done
}

# main function
start_timestamp=$(date +%y-%m-%d_%H:%M:%S)
echo $start_timestamp
logging_folder="logging"$start_timestamp
echo $logging_folder
logging_path="/home/nboltz/Dropbox/Scalability/"$logging_folder
echo $logging_path
mkdir $logging_path
mkdir $logging_path"/equal_events_X_users"
mkdir $logging_path"/X_different_events_one_user"
startTestSeries $logging_path


