package org.palladiosimulator.protocom.lang.java.util

/**
 * Java-related constants.
 * 
 * @author Thomas Zolynski
 */
abstract class JavaConstants {
	
	public static val RMI_REMOTE_INTERFACE = "java.rmi.Remote"
	public static val RMI_REMOTE_EXCEPTION = "java.rmi.RemoteException"
	public static val RMI_REMOTE_OBJECT_CLASS = "java.rmi.server.UnicastRemoteObject"
	
	public static val SERIALIZABLE_INTERFACE = "java.io.Serializable"
	
	public static val TYPE_STRING = "String"
	
	public static val VISIBILITY_PRIVATE = "private"
	public static val VISIBILITY_PROTECTED = "protected"
	public static val VISIBILITY_PUBLIC = "public"
	
	public static val JEE_EJB_ANNOTATION_STATELESS = "javax.ejb.Stateless"
	public static val JEE_EJB_ANNOTATION_STATEFUL = "javax.ejb.Stateful"
	public static val JEE_EJB_ANNOTATION_EJB = "javax.ejb.EJB"
	
	public static val JEE_INTERFACE_ANNOTATION_REMOTE = "javax.ejb.Remote"
	public static val JEE_INTERFACE_ANNOTATION_LOCAL = "javax.ejb.Local"
}