#!/bin/bash

BINDIR=$(cd "$(dirname "$0")"; pwd)

LIBS=~/eclipse-mars/plugins

LIST=`cat << EOF
org.eclipse.osgi
org.eclipse.equinox.preferences
org.eclipse.core.resources
org.eclipse.core.runtime
org.eclipse.emf.ecore
org.eclipse.emf.mwe.utils
org.eclipse.emf.mwe.core
org.eclipse.xtext
org.eclipse.xtext.common.types
org.eclipse.xtext.common.types.ui
org.eclipse.emf.common
org.eclipse.emf.ecore.xmi
org.eclipse.xtext.util
org.eclipse.equinox.common
org.antlr.runtime
org.eclipse.xtext.xbase
org.eclipse.xtext.xbase.lib
EOF`

mkdir "${BINDIR}/mvn-repo"

for I in $LIST ; do
	for P in `find ${LIBS} -name "${I}_*.jar"` ; do
		if [ -f "$P" ] ; then
			N=`basename $P`
			ARTIFACT=`echo $N | sed 's/^\(.*\)_.*$/\1/'`
			VERSION=`echo $N | sed 's/^.*_\(.*\)\.jar$/\1/'`
			echo $N
		#	echo $ARTIFACT
		#	echo $VERSION
			mvn org.apache.maven.plugins:maven-install-plugin:2.3.1:install-file -Dfile="$P" \
				-DgroupId=local -DartifactId=$ARTIFACT -Dpackaging=jar -Dversion=$VERSION \
				-DlocalRepositoryPath=mvn-repo
		fi
	done
done
