package org.palladiosimulator.protocom.lang.manifest

interface IJseManifest extends IManifest {

	def String bundleManifestVersion()

	def String bundleName()

	def String bundleSymbolicName()

	def String bundleVersion()

	def String bundleActivator()

	def String requireBundle()

	def String eclipseLazyStart()

	def String bundleClassPath()
}
