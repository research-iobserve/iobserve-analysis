/**
 * This package provides a method to traverse through a PCM model.
 * For each entry point entity (Repository, System, etc.) a corresponding PCMRepresentative class is generated.
 * 
 * Note that the traversing is not complete at this moment. So far only the PCM entities used for the JSE 
 * transformation are traversed. However, all traverse methods in this package can be extended without breaking 
 * any existing transformations.
 * 
 * @author Thomas Zolynski
 */
package org.palladiosimulator.protocom.traverse.framework;