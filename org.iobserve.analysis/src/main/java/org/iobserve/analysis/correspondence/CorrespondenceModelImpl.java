package org.iobserve.analysis.correspondence;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXB;

import protocom.extension.mapping.PcmCorrespondentMethod;
import protocom.extension.mapping.PcmEntity;
import protocom.extension.mapping.PcmEntityCorrespondent;
import protocom.extension.mapping.PcmMapping;
import protocom.extension.mapping.PcmOperationSignature;

import com.google.common.base.Optional;

/**
 * Implementation of {@link ICorrespondence}.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
class CorrespondenceModelImpl implements ICorrespondence {

	/**
	 * String builder to build method signatures based on the given 
	 * {@link PcmCorrespondentMethod} instance.
	 */
	private interface MethodSignatureBuilder {
		/**
		 * @param method method
		 * @return signature of the method based on the given 
		 * 	{@link PcmCorrespondentMethod}
		 */
		String build(PcmCorrespondentMethod method);
	}

	/**namespace of current palladio framework.*/
	private static final String PROTOCOM_BASE_PACKAGE_NAME = 
			"org.palladiosimulator.protocom";

	/** cache for already mapped correspondences. */
	private final Map<String, Correspondent> cachedCorrespondents =
			new HashMap<String, Correspondent>();

	/** raw mapping objects created during ProtoCom artifacts generation. */
	private final PcmMapping rawMapping;

	/** mapper for method signature to operation signature. */
	private final OperationSignatureSelector opSigMapper;

	/** fast access map for class-signature to object. */
	private Map<String, PcmEntityCorrespondent> mapping;

	// ********************************************************************
	// * INITIALIZATION
	// ********************************************************************

	/**
	 * Create correspondence model.
	 * @param theMapping mapping instance
	 * @param mapper selector
	 */
	CorrespondenceModelImpl(final PcmMapping theMapping,
			final OperationSignatureSelector mapper) {
		this.rawMapping = theMapping;
		this.opSigMapper = mapper;
	}

	/**
	 * Create the correspondence model.
	 * @param mappingFile input stream of mapping file
	 * @param mapper selector
	 */
	CorrespondenceModelImpl(final InputStream mappingFile,
			final OperationSignatureSelector mapper) {
		this.rawMapping = JAXB.unmarshal(mappingFile, PcmMapping.class);
		this.opSigMapper = mapper;

		this.initMapping();
	}

	// ********************************************************************
	// * INITIATE THE RAC
	// ********************************************************************

	/**
	 * Init mapping.
	 * <ul>
	 * <li>Create Map for fast access</li>
	 * <li>Set parent references on {@link PcmMapping} objects</li>
	 * </ul>
	 */
	public void initMapping() {
		this.mapping = new HashMap<String, PcmEntityCorrespondent>();

		for (final PcmEntity nextPcmEntity : this.rawMapping.getEntities()) {
			nextPcmEntity.setParent(this.rawMapping);

			// set the parent reference
			for (final PcmOperationSignature nextOperation 
					: nextPcmEntity.getOperationSigs()) {
				nextOperation.setParent(nextPcmEntity);
			}

			// set parent reference and convert the mapping
			for (final PcmEntityCorrespondent nextCorrespondent 
					: nextPcmEntity.getCorrespondents()) {
				nextCorrespondent.setParent(nextPcmEntity);

				final String qualifiedName = (nextCorrespondent.getPackageName()
						+ "." + nextCorrespondent.getUnitName())
						.trim().replaceAll(" ", "");
				this.mapping.put(qualifiedName, nextCorrespondent);

				// set parent reference
				for (final PcmCorrespondentMethod nextCorresMethod 
						: nextCorrespondent.getMethods()) {
					nextCorresMethod.setParent(nextCorrespondent);
				}
			}
		}
	}

	// ********************************************************************
	// * MAPPING
	// ********************************************************************

	@Override
	public Optional<Correspondent> getCorrespondent(
			final String classSig, final String operationSig) {
		//TODO debug print, remove later
		System.out.print(String.format("Try to get correspondence "
				+ "for classSig=%s, operationSig=%s...", 
				classSig, operationSig));

		// assert parameters are not null
		if ((classSig == null) || (operationSig == null)) {
			System.out.println("NOK");
			return ICorrespondence.NULL_CORRESPONDENZ;
		}

		// create the request key for searching in the cache
		final String requestKey = classSig.trim().replaceAll(" ", "") 
				+ operationSig.trim().replaceAll(" ", "");

		// try to get the correspondent from the cache
		Correspondent correspondent = this.cachedCorrespondents.get(requestKey);

		// in case the correspondent is not available it has to be mapped
		if (correspondent == null) {
			final PcmEntityCorrespondent pcmEntityCorrespondent = 
					this.getPcmEntityCorrespondent(classSig);
			if (pcmEntityCorrespondent == null) {
				// TODO log
				System.out.println("NOK");
				return ICorrespondence.NULL_CORRESPONDENZ; // or something else
			}

			final PcmOperationSignature pcmOperationSignature = 
					this.getPcmOperationSignature(
							pcmEntityCorrespondent, operationSig);
			if (pcmOperationSignature == null) {
				// TODO log
				System.out.println("NOK");
				return ICorrespondence.NULL_CORRESPONDENZ;
			}

			// create correspondent object
			correspondent = CorrespondentFactory.newInstance(
					pcmEntityCorrespondent.getParent().getName(),
					pcmEntityCorrespondent.getParent().getId(),
					pcmOperationSignature.getName(),
					pcmOperationSignature.getId());

			// put into cache for next time
			this.cachedCorrespondents.put(requestKey, correspondent);
		}

		//TODO this can be removed later
		System.out.println("OK");
		return Optional.of(correspondent);
	}
	
	@Override
	public Optional<Correspondent> getCorrespondent(final String classSig) {
		//TODO debug print, remove later
		System.out.print(String.format(
				"Try to get correspondence for classSig=%s ...", classSig));

		// assert parameters are not null
		if (classSig == null) {
			System.out.println("NOK");
			return ICorrespondence.NULL_CORRESPONDENZ;
		}

		// create the request key for searching in the cache
		final String requestKey = classSig.trim().replaceAll(" ", "");

		// try to get the correspondent from the cache
		Correspondent correspondent = this.cachedCorrespondents.get(requestKey);

		// in case the correspondent is not available it has to be mapped
		if (correspondent == null) {
			final PcmEntityCorrespondent pcmEntityCorrespondent = 
					this.getPcmEntityCorrespondent(classSig);
			if (pcmEntityCorrespondent == null) {
				// TODO log
				System.out.println("NOK");
				return ICorrespondence.NULL_CORRESPONDENZ; // or something else
			}

			// create correspondent object
			correspondent = CorrespondentFactory.newInstance(
					pcmEntityCorrespondent.getParent().getName(),
					pcmEntityCorrespondent.getParent().getId(),
					null,
					null);

			// put into cache for next time
			this.cachedCorrespondents.put(requestKey, correspondent);
		}

		//TODO this can be removed later
		System.out.println("OK");
		return Optional.of(correspondent);
	}

	/**
	 * Get the {@link PcmEntity} based on the qualified class name.
	 *
	 * @param classSig class signature
	 * @return null if not available
	 */
	private PcmEntityCorrespondent getPcmEntityCorrespondent(
			final String classSig) {
		final PcmEntityCorrespondent pcmEntityCorrespondent = 
				this.mapping.get(classSig.trim().replaceAll(" ", ""));
		return pcmEntityCorrespondent;
	}

	/**
	 * Get the corresponding operation signature based the given operation
	 * signature.
	 * @param pcmEntityCorrespondent pcm entity correspondence
	 * @param operationSig operation signature
	 * @return pcm operation signature or null if operation signature not 
	 * 	available
	 */
	private PcmOperationSignature getPcmOperationSignature(
			final PcmEntityCorrespondent pcmEntityCorrespondent,
			final String operationSig) {

		PcmOperationSignature opSig = null;
		for (final PcmCorrespondentMethod nextCorresMethod 
				: pcmEntityCorrespondent.getMethods()) {
			final String methodSig = this.mPackageNameClassNameMethodName
					.build(nextCorresMethod);
			if (operationSig.replaceAll(" ", "").equals(
					methodSig.replaceAll(" ", ""))) {
				opSig = this.mapOperationSignature(nextCorresMethod);
				break;
			}
		}
		return opSig;   
	}

	/**
	 * Builds the signature out of packagname.MethodName().
	 */
	private final MethodSignatureBuilder mPackageNameClassNameMethodName = 
			new MethodSignatureBuilder() {

		@Override
		public String build(final PcmCorrespondentMethod method) {
			final String packageName = method.getParent().getPackageName();
			final String className = method.getParent().getUnitName();
			final String methodName = method.getName();
			return packageName + "." + className + "." + methodName + "()";
		}
	};

	/**
	 * Builds the signature like it would appear in the source code for instance
	 * void Get().
	 */
	private final MethodSignatureBuilder mOnlyMethodName = 
			new MethodSignatureBuilder() {

		@Override
		public String build(final PcmCorrespondentMethod method) {
			final StringBuilder builder = new StringBuilder();

			// build method signature
			builder.append(method.getVisibilityModifier());
			builder.append(" ");
			builder.append(method.getReturnType());
			builder.append(" ");
			builder.append(method.getName());
			builder.append("(");
			builder.append(method.getParameters()
					.replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
			// TODO I do not know how to handle multiple parameters..since 
			// I did not see such after protocom build
			builder.append(")");
			// TODO <exception throws signature>  is missing since this 
			// is not retrievable from protocom-generation process so far.

			final String methodSig = builder.toString().trim();
			return methodSig;
		}
	};

	/**
	 * Map the given method to the correspondent operation signature based on
	 * the name. The comparison is done by searching the operation signature
	 * name which is contained in the given method name.
	 *
	 * @param method
	 *            method
	 * @return null if not found
	 */
	private PcmOperationSignature mapOperationSignature(
			final PcmCorrespondentMethod method) {
		final PcmEntity pcmEntity = method.getParent().getParent();
		PcmOperationSignature opSig = null;
		for (final PcmOperationSignature nextOpSig 
				: pcmEntity.getOperationSigs()) {
			if (this.opSigMapper.select(method, nextOpSig)) {
				opSig = nextOpSig;
				break;
			}
		}
		return opSig;
	}

	/**
	 * Test method to print all mappings.
	 */
	private void printAllMappings() {
		for (String nextMappingKey : this.mapping.keySet()) {
			System.out.println(nextMappingKey);
			final PcmEntityCorrespondent correspondent = 
					this.mapping.get(nextMappingKey);
			System.out.println(correspondent);
		}
	}
}
