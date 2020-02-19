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
package org.iobserve.drive.accounting;

import java.io.Serializable;

/**
 * The Class Account.
 *
 * @author Eduardo Macarron
 */
public class Account implements Serializable {

    private static final long serialVersionUID = 8751282105532159742L;

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String status;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String phone;
    private String favouriteCategoryId;
    private String languagePreference;
    private boolean listOption;
    private boolean bannerOption;
    private String bannerName;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getAddress1() {
        return this.address1;
    }

    public void setAddress1(final String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return this.address2;
    }

    public void setAddress2(final String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getZip() {
        return this.zip;
    }

    public void setZip(final String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getFavouriteCategoryId() {
        return this.favouriteCategoryId;
    }

    public void setFavouriteCategoryId(final String favouriteCategoryId) {
        this.favouriteCategoryId = favouriteCategoryId;
    }

    public String getLanguagePreference() {
        return this.languagePreference;
    }

    public void setLanguagePreference(final String languagePreference) {
        this.languagePreference = languagePreference;
    }

    public boolean isListOption() {
        return this.listOption;
    }

    public void setListOption(final boolean listOption) {
        this.listOption = listOption;
    }

    public boolean isBannerOption() {
        return this.bannerOption;
    }

    public void setBannerOption(final boolean bannerOption) {
        this.bannerOption = bannerOption;
    }

    public String getBannerName() {
        return this.bannerName;
    }

    public void setBannerName(final String bannerName) {
        this.bannerName = bannerName;
    }

    @Override
    public String toString() {
        return "{\n" + "\tusername:" + this.username + "\tpassword:" + this.password + "\temail:" + this.email
                + "\tfirstName:" + this.firstName + "\tlastName:" + this.lastName + "\tstatus:" + this.status
                + "\taddress1:" + this.address1 + "\taddress2:" + this.address2 + "\tcity:" + this.city + "\tstate:"
                + this.state + "\tzip:" + this.zip + "\tcountry:" + this.country + "\tphone" + this.phone
                + "\tfavouriteCategoryId:" + this.favouriteCategoryId + "\tlanguagePreference:"
                + this.languagePreference + "\tlistOption:" + (this.listOption ? "true" : "false") + "\tbannerOption:"
                + (this.bannerOption ? "true" : "false") + "\tbannerName:" + this.bannerName + "}\n";

    }

}
