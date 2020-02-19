/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.model.test.data;

import org.iobserve.model.test.storage.one.EnumValueExample;
import org.iobserve.model.test.storage.one.OneFactory;
import org.iobserve.model.test.storage.one.Other;
import org.iobserve.model.test.storage.one.OtherInterface;
import org.iobserve.model.test.storage.one.Root;
import org.iobserve.model.test.storage.one.SpecialA;
import org.iobserve.model.test.storage.one.SpecialB;
import org.iobserve.model.test.storage.two.Link;
import org.iobserve.model.test.storage.two.Two;
import org.iobserve.model.test.storage.two.TwoFactory;

/**
 * @author Reiner Jung
 *
 */
public final class TestModelData {

    public static final Other FIRST_OTHER = TestModelData.createOther("first other");
    public static final Other SECOND_OTHER = TestModelData.createOther("second other");

    private TestModelData() {

    }

    /**
     * Create Model two.
     *
     * @return returns model two
     */
    public static Two createModelTwo() {
        final Two modelTwo = TwoFactory.eINSTANCE.createTwo();

        modelTwo.getLinks().add(TestModelData.createLink(TestModelData.FIRST_OTHER));

        return modelTwo;
    }

    /**
     * Create a link to an other object.
     * 
     * @param other
     *            the other object
     * @return returns the link
     */
    public static Link createLink(final Other other) {
        final Link link = TwoFactory.eINSTANCE.createLink();
        link.setReference(other);
        return link;
    }

    /**
     * Create model one.
     *
     * @return returns model one
     */
    public static Root createModelOne() {
        final Root modelOne = OneFactory.eINSTANCE.createRoot();

        modelOne.setEnumerate(EnumValueExample.B);
        modelOne.setFixed("fixed value");
        modelOne.setName("root name");

        modelOne.getLabels().add("label 1");
        modelOne.getLabels().add("label 2");
        modelOne.getLabels().add("label 3");

        modelOne.getOthers().add(TestModelData.FIRST_OTHER);
        modelOne.getOthers().add(TestModelData.SECOND_OTHER);

        modelOne.getIfaceOthers().add(TestModelData.createSpecialA(TestModelData.FIRST_OTHER));
        modelOne.getIfaceOthers().add(TestModelData.createSpecialB());

        return modelOne;
    }

    private static OtherInterface createSpecialB() {
        final SpecialA subspecial = OneFactory.eINSTANCE.createSpecialA();

        final SpecialB special = OneFactory.eINSTANCE.createSpecialB();
        special.setNext(subspecial);
        return special;
    }

    private static OtherInterface createSpecialA(final Other other) {
        final SpecialB subspecial = OneFactory.eINSTANCE.createSpecialB();

        final SpecialA special = OneFactory.eINSTANCE.createSpecialA();
        special.setNext(subspecial);
        special.setRelate(other);
        return special;
    }

    private static Other createOther(final String string) {
        final Other other = OneFactory.eINSTANCE.createOther();
        other.setName(string);

        return other;
    }

}
