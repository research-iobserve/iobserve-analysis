options("scipen"=10)    # set high penalty for scientific display

TPreprocess <- matrix(nrow = 10, ncol = 6)
TRuntimeUpdate <- matrix(nrow = 10, ncol = 6)

experimentSizes <- c(1,10,100,1000,10000,100000)

for(k in 0:9) {
	for(i in 1:length(experimentSizes)) {
		path <- 'D:\\Dokumente\\Uni\\HiWi\\iobserve\\iobserve-analysis\\experiments\\scalability_experiment\\results\\raw results\\X_different_events_one_user\\'
		experimentNr <- k
		experimentNrFolder <- paste(paste(path, experimentNr, sep=""), "\\", sep="")
		
		experimentSize <- experimentSizes[i]
		experimentDescription <- paste(experimentSize, '_different_events_one_user', sep="")
		
		experimentFolder <- paste(paste(experimentNrFolder, experimentDescription, sep=""), '\\', sep="")
		
		# calculating values for TRuntimeUpdate
		entryEventSequenceLoggingPath <- paste(experimentFolder, "TEntryEventSequenceLogging.csv", sep="")
		
		entryEventSequence <- read.csv(file = entryEventSequenceLoggingPath, header = F, sep = ";")
		behTransf <- as.numeric(as.character(entryEventSequence$V4[3]))
		modelUpdate <- as.numeric(as.character(entryEventSequence$V5[3]))
		experimentNr <- k + 1 
		TRuntimeUpdate[experimentNr,i] <- behTransf + modelUpdate
		
		# calculating values for TPreprocess
		entryCallLoggingPath <- paste(experimentFolder, "TEntryCallLogging.csv", sep="")
		entryCallSequenceLoggingPath <- paste(experimentFolder, "TEntryCallSequenceLogging.csv", sep="")

		entryCall <- read.csv(file = entryCallLoggingPath, header = F, sep = ";")
		entryCallSequence <- read.csv(file = entryCallSequenceLoggingPath, header = F, sep = ";")
		
		endIndex <- experimentSize + 2
		
		entryCallSum <- sum(as.numeric(as.character(entryCall$V4[c(3:endIndex)])))
		entryCallSequenceSum <- sum(as.numeric(as.character(entryCallSequence$V6[c(3:endIndex)])))
		
		TPreprocess[experimentNr,i] <- entryCallSum + entryCallSequenceSum
	}
}

TPreprocessMeans <- rep(0,length(experimentSizes))
TPreprocessMeansMs <- rep(0,length(experimentSizes))
TRuntimeUpdateMeans <- rep(0,length(experimentSizes))

TPreprocessMedian <- rep(0,length(experimentSizes))
TPreprocessMedianMs <- rep(0,length(experimentSizes))
TRuntimeUpdateMedian <- rep(0,length(experimentSizes))

for(k in 1:length(experimentSizes)) {
	TPreprocessMeans[k] <- mean(TPreprocess[,k], trim = 0.1)
	TPreprocessMeansMs[k] <- TPreprocessMeans[k]/1000000
	TRuntimeUpdateMeans[k] <- mean(TRuntimeUpdate[,k], trim = 0.1)
	
	TPreprocessMedian[k] <- median(TPreprocess[,k])
	TPreprocessMedianMs[k] <- TPreprocessMedian[k]/1000000
	TRuntimeUpdateMedian[k] <- median(TRuntimeUpdate[,k])
}

options("scipen"=0)
labelsX=parse(text=paste(seq(10, 10, length.out = 6), "^", seq(0,5), sep=""))


#Plots and saves pdf for TPreprocessMedian in ms
pdf("D:\\Dokumente\\Uni\\HiWi\\iobserve\\iobserve-analysis\\experiments\\scalability_experiment\\results\\TPreprocess_median_different_events.pdf") 

plot(experimentSizes, TPreprocessMedianMs, log="xy", axes=FALSE, type = "o", main = expression('T'[Preprocess]), ylim = c(1, 100000), xlab="Number of Services Called", ylab="Median of Execution Time [ms]")

experimentYAxis <- 10^(0:5)
abline(h=experimentYAxis, col="gray", lty=3)
labelsY=parse(text=paste(seq(10, 10, length.out = 6), "^", seq(0,5), sep=""))

axis(1, at = experimentSizes, las = 1, labels = labelsX)
axis(2, at = experimentYAxis, las = 1, labels = labelsY)
box()

dev.off()

#Plots and saves pdf for TRuntimeUpdateMedian
pdf("D:\\Dokumente\\Uni\\HiWi\\iobserve\\iobserve-analysis\\experiments\\scalability_experiment\\results\\TRuntimeUpdate_median_different_events.pdf") 

plot(experimentSizes, TRuntimeUpdateMedian, log="xy", axes=FALSE, type = "o", main = expression('T'[RuntimeUpdate]), ylim = c(10,1000000), xlab="Number of Services Called", ylab="Median of Execution Time [ms]")

experimentYAxis <- 10^(1:6)
abline(h=experimentYAxis, col="gray", lty=3)
labelsY=parse(text=paste(seq(10, 10, length.out = 6), "^", seq(1,6), sep=""))

axis(1, at = experimentSizes, las = 1, labels = labelsX)
axis(2, at = experimentYAxis, las = 1, labels = labelsY)
box()

dev.off()
