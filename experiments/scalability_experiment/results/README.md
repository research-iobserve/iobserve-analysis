# Results

The .zip file **'raw results'** contains all resulting files of the experiment. The files of raw results have been gathered by copying the logging files of each iobserve-analysis pipeline filter. For this experiment only the logging files of Filters TEntryCall, TEntryCallSequence and TEntryEventSequence contain relevant information, other files can be disregarded.

** 'summary' folder **

The Excel sheets **'Summary_equal_events'** and **'Summary_different_events'** contain the compiled results of the respective experiment set up.

**'equal_events_evaluation_r_skript'** and **'different_events_evaluation_r_skript'** each contain a R script to create graphs showcasing the results of each respective experiment set up and filter.
The scripts read the raw results of the experiment, calculate the means and each create two PDF files (one for TPreproces and one for TRuntimeUpdate).
To use the scripts, first unpack 'raw results.zip' and change the filepaths in the scripts according to the position of the 'raw results' folder and where the PDF graph files should be created.
The modified script can than be loaded in R, or copied in a R console.

The four pdf files are the summary graphics, already created using the two R scripts.