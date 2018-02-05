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
package org.iobserve.analysis;

import java.lang.reflect.InvocationTargetException;

import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;

import teetime.framework.Configuration;

import org.iobserve.analysis.configurations.AnalysisConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Supports the automatic instantiation of classes specified in the configuration.
 *
 * @author Reiner Jung
 *
 */
public class InstantiationFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisConfiguration.class);

    /**
     * This is a helper method trying to find, create and initialize the given class, using its
     * public constructor which accepts a single {@link Configuration}.
     *
     * @param c
     *            This class defines the expected result of the method call.
     * @param className
     *            The name of the class to be created.
     * @param configuration
     *            The configuration which will be used to initialize the class in question.
     *
     * @return A new and initializes class instance if everything went well.
     *
     * @param <C>
     *            The type of the returned class.
     */
    @SuppressWarnings("unchecked")
    public static <C> C createAndInitialize(final Class<C> c, final String className,
            final kieker.common.configuration.Configuration configuration) {
        C createdClass = null; // NOPMD (null)
        try {
            final Class<?> clazz = Class.forName(className);
            if (c.isAssignableFrom(clazz)) {
                createdClass = (C) InstantiationFactory.instantiate(clazz, className, configuration);
            } else {
                InstantiationFactory.LOGGER.error("Class '{}' has to implement '{}'.", className, c.getSimpleName());
            }
        } catch (final ClassNotFoundException e) {
            InstantiationFactory.LOGGER.error("{}: Class '{}' not found: {}", c.getSimpleName(), className,
                    e.getLocalizedMessage());
        } catch (final NoSuchMethodException e) {
            InstantiationFactory.LOGGER.error(
                    "{}: Class '{}' has to implement a (public) constructor that accepts a single Configuration",
                    c.getSimpleName(), className, e.getLocalizedMessage());
        } catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
            // SecurityException, IllegalAccessException, IllegalArgumentException,
            // InstantiationException, InvocationTargetException
            InstantiationFactory.LOGGER.error("{}: Failed to load class for name '{}'", c.getSimpleName(), className,
                    e.getLocalizedMessage());
        }
        return createdClass;
    }

    private static <C> C instantiate(final Class<C> clazz, final String className,
            final kieker.common.configuration.Configuration configuration)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {

        // Choose the appropriate configuration to pass
        final kieker.common.configuration.Configuration configurationToPass;
        if (clazz.isAnnotationPresent(ReceiveUnfilteredConfiguration.class)) {
            configurationToPass = configuration.flatten();
        } else {
            configurationToPass = configuration.getPropertiesStartingWith(className);
        }

        return clazz.getConstructor(Configuration.class).newInstance(configurationToPass);
    }

}
