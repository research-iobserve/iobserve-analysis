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
package org.iobserve.model.persistence.neo4j;

/**
 * @author Reiner Jung
 *
 */
public class DBException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 9189670144768136835L;

    /**
     *
     */
    public DBException() {
        super();
    }

    /**
     * @param message
     *            exception message
     */
    public DBException(final String message) {
        super(message);
    }

    /**
     * @param cause
     *            cause of the exception
     */
    public DBException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     *            exception message
     * @param cause
     *            cause of the exception
     */
    public DBException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     *            exception message
     * @param cause
     *            cause of the exception
     * @param enableSuppression
     *            allow to suppress exception
     * @param writableStackTrace
     *            the stack trace
     */
    public DBException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
