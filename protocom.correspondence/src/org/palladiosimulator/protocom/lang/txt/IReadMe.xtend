package org.palladiosimulator.protocom.lang.txt

import com.google.common.collect.ArrayListMultimap
import java.util.HashMap
import org.palladiosimulator.protocom.lang.ICompilationUnit

interface IReadMe extends ICompilationUnit {
	
	def HashMap<String,String> basicComponentClassName()
	
	def ArrayListMultimap<String,String> basicComponentPortClassName()
	
}