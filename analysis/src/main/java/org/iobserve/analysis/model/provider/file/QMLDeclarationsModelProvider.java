/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.model.provider.file;

import de.uka.ipd.sdq.dsexplore.qml.declarations.QMLDeclarations.QMLDeclarations;
import de.uka.ipd.sdq.dsexplore.qml.declarations.QMLDeclarations.QMLDeclarationsPackage;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;

/**
 * TODO add description.
 *
 * @author unkown
 *
 * @deprecated since 0.0.2 we should use the new neo4j generic provider
 *             {@link org.iobserve.analysis.model.provider.neo4j.ModelProvider}
 */
@Deprecated
public class QMLDeclarationsModelProvider extends AbstractModelProvider<QMLDeclarations> {

    /**
     * Create a QML declaration model provider.
     *
     * @param theUriModelInstance
     *            reference to the model
     */
    public QMLDeclarationsModelProvider(final URI theUriModelInstance) {
        super(theUriModelInstance);
    }

    @Override
    protected EPackage getPackage() {
        return QMLDeclarationsPackage.eINSTANCE;
    }

    @Override
    public void resetModel() {
        this.getModel().getQmlDeclarations().clear();
    }

}
