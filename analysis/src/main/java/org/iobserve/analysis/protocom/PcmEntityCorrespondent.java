/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis.protocom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Correspondence between something.
 *
 * @author Alessandro Guisa
 * @author Nicolas Boltz
 *
 */
@XmlRootElement(name = "Correspondent")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "Correspondent", propOrder = { "filePath", "projectName", "packageName", "interfaces", "unitName",
        "methods" })
public class PcmEntityCorrespondent {
	
    /**
     * Builds the signature out of packagname.MethodName().
     */
    @SuppressWarnings("unused")
	private final IMethodSignatureBuilder mPackageNameClassNameMethodName = new IMethodSignatureBuilder() {

        @Override
        public String build(final PcmCorrespondentMethod method) {
            final String packageName = method.getParent().getPackageName();
            final String className = method.getParent().getUnitName();
            final String methodName = method.getName();
            return packageName + "." + className + "." + methodName + "()";
        }
    };
	
    /**
     * Builds the signature like it would appear in the source code for instance void Get().
     */
    private final IMethodSignatureBuilder mPackageAndMethod = new IMethodSignatureBuilder() {

        @Override
        public String build(final PcmCorrespondentMethod method) {
            final StringBuilder builder = new StringBuilder();

            // build method signature
            builder.append(method.getVisibilityModifier());
            builder.append(" ");
            builder.append(method.getReturnType());
            builder.append(" ");
            builder.append(method.getParent().getPackageName());
            builder.append(".");
            builder.append(method.getParent().getUnitName());
            builder.append(".");
            builder.append(method.getName());
            builder.append("(");
            if(method.getParameters() != null) {
                builder.append(method.getParameters());
            }
            builder.append(")");
            // TODO <exception throws signature> is missing since this
            // is not retrievable from protocom-generation process so far.

            final String methodSig = builder.toString().trim();
            return methodSig;
        }
    };

    private String filePath;
    private String projectName;
    private String packageName;
    private String unitName;
    private List<String> interfaces = new ArrayList<>();
    private List<PcmCorrespondentMethod> methods = new ArrayList<>();
    private Map<String, PcmCorrespondentMethod> methodMap = new HashMap<>();
    private PcmEntity parent;

    /**
     * Default constructor.
     */
    public PcmEntityCorrespondent() {
        /* nothing to do */
    }

    @XmlElement(name = "FilePath")
    public String getFilePath() {
        return this.filePath;
    }

    @XmlElement(name = "ProjectName")
    public String getProjectName() {
        return this.projectName;
    }

    @XmlElement(name = "PackageName")
    public String getPackageName() {
        return this.packageName;
    }

    @XmlElement(name = "UnitName")
    public String getUnitName() {
        return this.unitName;
    }

    @XmlElementWrapper(name = "Interfaces")
    @XmlElement(name = "Interface")
    public List<String> getInterfaces() {
        return this.interfaces;
    }

    @XmlElementWrapper(name = "CorrespondentMethods")
    @XmlElement(name = "CorrespondentMethod")
    public List<PcmCorrespondentMethod> getMethods() {
        return this.methods;
    }

    @XmlTransient
    public PcmEntity getParent() {
        return this.parent;
    }

    public void setFilePath(final String filePath) {
        this.filePath = filePath;
    }

    public void setProjectName(final String projectName) {
        this.projectName = projectName;
    }

    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }

    public void setUnitName(final String unitName) {
        this.unitName = unitName;
    }

    public void setInterfaces(final List<String> interfaces) {
        this.interfaces = interfaces;
    }

    public void setMethods(final List<PcmCorrespondentMethod> methods) {
        this.methods = methods;
    }

    public void setParent(final PcmEntity parent) {
        this.parent = parent;
    }

    public void initMethodMap() {
    	for(PcmCorrespondentMethod method : methods) {
    		final String methodSig = this.mPackageAndMethod.build(method).replaceAll(" ", "");
    		methodMap.put(methodSig, method);
    	}
    }
    
    public PcmCorrespondentMethod getMethod(String methodSig) {
    	return methodMap.get(methodSig);
    }
    
    
    /**
     * String builder to build method signatures based on the given {@link PcmCorrespondentMethod}
     * instance.
     */
    private interface IMethodSignatureBuilder {
        /**
         * @param method
         *            method
         * @return signature of the method based on the given {@link PcmCorrespondentMethod}
         */
        String build(PcmCorrespondentMethod method);
    }
}
