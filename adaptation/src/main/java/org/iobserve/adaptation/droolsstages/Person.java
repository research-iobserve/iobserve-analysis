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
package org.iobserve.adaptation.droolsstages;

/**
 * A person for testing.
 *
 * @author Lars Bluemke
 *
 */
public class Person {
    private String name;
    private int time;
    private String greet;
    private Coolness coolness;

    public enum Coolness {
        COOL, UNCOOL
    }

    public Person(final String name, final int time) {
        this.name = name;
        this.time = time;
        this.coolness = Coolness.COOL;
    }

    public String getGreet() {
        return this.greet;
    }

    public void setGreet(final String greet) {
        this.greet = greet;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(final int time) {
        this.time = time;
    }

    public Coolness getCoolness() {
        return this.coolness;
    }

    public void setCoolness(final Coolness coolness) {
        this.coolness = coolness;
    }

    public static String test(final String s) {
        return "hello " + s;
    }
}
