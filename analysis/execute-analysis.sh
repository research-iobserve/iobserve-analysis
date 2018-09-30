#!/bin/bash

# configuration

BASE_DIR=$(cd "$(dirname "$0")"; pwd)

export ANALYSIS_OPTS="-Xmx24g -Xms10m -Dlog4j.configuration=file://$BASE_DIR/log4j.cfg"

# internal data
CLUSTERINGS[0]=xmeans
CLUSTERINGS[1]=em
CLUSTERINGS[2]=hierarchy
CLUSTERINGS[3]=similarity

if [ -f $BASE_DIR/config ] ; then
	. $BASE_DIR/config
else
	echo "Missing configuration"
	exit 1
fi

mode=""
for C in ${CLUSTERINGS[*]} ; do
	if [ "$C" == "$1" ] ; then
		mode="$C"
	fi
done

if [ "$mode" == "" ] ; then
	echo "Unknown mode $1"
	exit 1
fi

if [ ! -x "${ANALYSIS}" ] ; then
	echo "Missing analysis cli"
	exit 1
fi
if [ ! -d "${DATA_DIR}" ] ; then
	echo "Data directory missing"
	exit 1
fi
if [ ! -d "${FIXED_DIR}" ] ; then
	echo "Fixed data directory missing"
	exit 1
fi
if [ ! -d "${PCM_DIR}" ] ; then
	echo "PCM directory missing"
	exit 1
fi
if [ ! -d "${RESULT_DIR}" ] ; then
	mkdir "$RESULT_DIR"
fi

# compute setup
if [ -f $FIXED_DIR/kieker.map ] ; then
	KIEKER_DIRECTORIES=$FIXED_DIR
else
	KIEKER_DIRECTORIES=""
	for D in `ls $FIXED_DIR` ; do
		if [ -f $FIXED_DIR/$D/kieker.map ] ; then
			if [ "$KIEKER_DIRECTORIES" == "" ] ;then
				KIEKER_DIRECTORIES="$FIXED_DIR/$D"
			else
				KIEKER_DIRECTORIES="$KIEKER_DIRECTORIES:$FIXED_DIR/$D"
			fi
		else
			echo "$FIXED_DIR/$D is not a kieker log directory."
		fi
	done
fi


# assemble analysis config
cat << EOF > analysis.config
## The name of the Kieker instance.
kieker.monitoring.name=JIRA
kieker.monitoring.hostname=
kieker.monitoring.metadata=true

iobserve.analysis.source=org.iobserve.service.source.FileSourceCompositeStage
org.iobserve.service.source.FileSourceCompositeStage.sourceDirectories=/Users/student/git/single-jpetstore-clustering-experiment/fixed/FishLover/kieker-20180328-213823-1404917594578390-UTC--

iobserve.analysis.traces=true
iobserve.analysis.dataFlow=true

iobserve.analysis.model.pcm.directory.db=$DB_DIR
iobserve.analysis.model.pcm.directory.init=$PCM_DIR

# trace preparation (note they should be fixed)
iobserve.analysis.behavior.IEntryCallTraceMatcher=org.iobserve.analysis.systems.jira.JIRACallTraceMatcher
iobserve.analysis.behavior.IEntryCallAcceptanceMatcher=org.iobserve.analysis.systems.jira.JIRATraceAcceptanceMatcher
iobserve.analysis.behavior.ITraceSignatureCleanupRewriter=org.iobserve.analysis.systems.jira.JIRASignatureCleanupRewriter
iobserve.analysis.behavior.IModelGenerationFilterFactory=org.iobserve.analysis.systems.jpetstore.JPetStoreEntryCallRulesFactory

iobserve.analysis.behavior.triggerInterval=1000

iobserve.analysis.behavior.sink.baseUrl=/Users/student/git/single-jpetstore-clustering-experiment/results
iobserve.analysis.container.management.sink.visualizationUrl=http://localhost:8080
EOF

case "$mode" in
"${CLUSTERINGS[0]}")
cat << EOF >> analysis.config
# specific setup similarity matching
iobserve.analysis.behavior.filter=org.iobserve.analysis.behavior.clustering.xmeans.XMeansBehaviorCompositeStage
org.iobserve.analysis.behavior.clustering.xmeans.XMeansBehaviorCompositeStage.expectedUserGroups=1
org.iobserve.analysis.behavior.clustering.xmeans.XMeansBehaviorCompositeStage.variance=1
org.iobserve.analysis.behavior.clustering.xmeans.XMeansBehaviorCompositeStage.prefix=jira
org.iobserve.analysis.behavior.clustering.xmeans.XMeansBehaviorCompositeStage.outputUrl=/Users/student/git/single-jpetstore-clustering-experiment/results
org.iobserve.analysis.behavior.clustering.xmeans.XMeansBehaviorCompositeStage.representativeStrategy=org.iobserve.analysis.systems.jira.JIRARepresentativeStrategy
EOF
;;
"${CLUSTERINGS[1]}")
cat << EOF >> analysis.config
# specific setup similarity matching
iobserve.analysis.behavior.filter=org.iobserve.analysis.behavior.clustering.em.EMBehaviorCompositeStage
org.iobserve.analysis.behavior.clustering.em.EMBehaviorCompositeStage.prefix=jira
org.iobserve.analysis.behavior.clustering.em.EMBehaviorCompositeStage.outputUrl=/Users/student/git/single-jpetstore-clustering-experiment/results
org.iobserve.analysis.behavior.clustering.em.EMBehaviorCompositeStage.representativeStrategy=org.iobserve.analysis.systems.jira.JIRARepresentativeStrategy
EOF
;;
"${CLUSTERINGS[2]}")
cat << EOF >> analysis.config
# specific setup similarity matching
iobserve.analysis.behavior.filter=org.iobserve.analysis.clustering.shared.ClassificationCompositeStage
iobserve.analysis.behavior.visualizationUrl=123
iobserve.analysis.behavior.sink.baseUrl=/Users/student/git/single-jpetstore-clustering-experiment/results
iobserve.analysis.behavior.classification=org.iobserve.analysis.behavior.clustering.birch.BirchClassification
iobserve.analysis.behavior.preprocess.keepTime=1000
iobserve.analysis.behavior.preprocess.minSize=1
iobserve.analysis.behavior.preprocess.keepEmpty=true
iobserve.analysis.behavior.birch.useClusterNumberMetric=true
iobserve.analysis.behavior.birch.clusterMetricStrategy=
iobserve.analysis.behavior.birch.lmethodEvalStrategy=
iobserve.analysis.behavior.birch.leafThreshold=2
iobserve.analysis.behavior.birch.maxLeafSize=7
iobserve.analysis.behavior.birch.maxNodeSize=2
iobserve.analysis.behavior.birch.maxLeafEntries=1
iobserve.analysis.behavior.birch.expectedNumberOfClusters=7
EOF
;;
"${CLUSTERINGS[3]}")
cat << EOF >> analysis.config
# specific setup similarity matching
iobserve.analysis.behavior.filter=org.iobserve.analysis.behavior.clustering.similaritymatching.SimilarityBehaviorCompositeStage
iobserve.analysis.behavior.IClassificationStage=org.iobserve.analysis.behavior.clustering.similaritymatching.SimilarityMatchingStage
iobserve.analysis.behavior.sm.IParameterMetric=org.iobserve.analysis.systems.jira.JIRAParameterMetric
iobserve.analysis.behavior.sm.IStructureMetricStrategy=org.iobserve.analysis.behavior.clustering.similaritymatching.GeneralStructureMetric
iobserve.analysis.behavior.sm.IModelGenerationStrategy=org.iobserve.analysis.behavior.clustering.similaritymatching.UnionModelGenerationStrategy
iobserve.analysis.behavior.sm.parameters.radius=2
iobserve.analysis.behavior.sm.structure.radius=2
EOF
;;
esac

# run analysis
echo "------------------------"
echo "Run analysis"
echo "------------------------"

$ANALYSIS -c analysis.config

# end
