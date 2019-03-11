How to use script:

General:
	Paths should be altered according to the location of the script and iobserve distribution.
	
	An distribution of iobserve has to be build, extracted and the call in the script (under comment '# call iobserve') altered accordingly.
	This can be done by calling the gradle wrapper './gradlew build install' located in the base folder of './iobserve-analysis/'.
	The distribution is created in './iobserve-analysis/analysis-cli/build/distributions/'.
	If errors exist while building, calling './gradlew clean' can help. 
	Errors in 'rac_creator' can be ignored, as long as 'analysis' and 'analysis-cli' build without error.

	The PCM models and monitoring data used, are located in folder 'pcm' or 'testdata'.
	There is monitoring data for experiments 'equal_events_X_users' 'X_different_events_one_user',
	with X increasing each by the factor of 10, starting at 1, ending at 100000.
	Folder 'warmup' contains three small runs of iobserve, which are executed before each run of the actual analysis, to warm up the jvm.
	Warmup can be commented out in 'AnalysisMain.java' in folder './iobserve-analysis/analysis-cli/......'.

	'logging_path' of the main function of the script holds the path where the experiment results are saved.
	A new folder is created for each run of the expermient, stating the timestamp of the start of the script.
	Inside a new folder for each experiment ('equal_events_X_users', 'X_different_events_one_user') is created.
	Results of each experiment run are saved in an own folder, named with increasing numbers.
	The execution time of each iteration of a iobserve filter is measured and saved in a .csv file named according to the filter.

start_10_times_100000.sh:
	Should/Can be used and altered according to the goal of the experiment.

start_with_dropbox.sh / start_without_dropbox.sh:
	Should not be used, since experiment makeup is wrong!!! 