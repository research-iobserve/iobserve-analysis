#!/bin/bash

BINDIR=$(cd "$(dirname "$0")"; pwd)

LIBS=~/eclipse-mars/plugins

LIST=`cat << EOF
de.uka.ipd.sdq.identifier
org.palladiosimulator.pcm
de.uka.ipd.sdq.units
de.uka.ipd.sdq.stoex
de.uka.ipd.sdq.probfunction
protocom.extension
EOF`

MVNDIR="${BINDIR}/mvn-repo"

if [ ! -d "${MVNDIR}" ] ; then
	mkdir "${MVNDIR}"
fi

PROPERTIES=""
NEWLINE=$'\n'

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

			PROPERTIES="${PROPERTIES}<${ARTIFACT}>${VERSION}</${ARTIFACT}>"
		fi
	done
done

MVNDIR_SED=`echo ${MVNDIR} | sed 's/\//\\\\\\//g'`
PROPERTIES_SED=`echo ${PROPERTIES} | sed 's/\//\\\\\\//g'`

echo $PROPERTIES

cat "${BINDIR}/settings.xml.template" | \
	sed "s/:properties:/${PROPERTIES_SED}/g" | \
	sed "s/:mvn-repo:/${MVNDIR_SED}/g" > "${BINDIR}/settings.xml"

# end
