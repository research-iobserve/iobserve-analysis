/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.deployment;

/**
 * This class is used to block the deployment updater until a running undeployment update task is
 * finished. This is rubbish and should be replaced by some other logic instead.
 *
 * @author Reiner Jung
 *
 */
public final class DeploymentLock {

    private static boolean lockState;

    private DeploymentLock() {
        // private constructor ensures singleton
    }

    /**
     * Try to lock semaphore. In case of use wait until released.
     */
    public static synchronized void lock() { // NOPMD in this case better method sync
        while (DeploymentLock.lockState) {
            try {
                Thread.sleep(20);
            } catch (final InterruptedException e) {
                // ignore
            }
        }
        DeploymentLock.lockState = true;
    }

    /**
     * Unlock semaphore.
     */
    public static void unlock() {
        DeploymentLock.lockState = false;
    }

}
