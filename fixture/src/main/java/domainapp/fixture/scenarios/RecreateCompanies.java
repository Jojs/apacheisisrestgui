/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package domainapp.fixture.scenarios;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import domainapp.dom.company.Company;
import domainapp.dom.company.IndustryGroups;
import domainapp.fixture.dom.company.CompanyCreate;
import domainapp.fixture.dom.company.CompaniesTearDown;

public class RecreateCompanies extends FixtureScript {

    public final List<String> NAMES = Collections.unmodifiableList(Arrays.asList(
            "Foo", "Bar", "Baz", "Frodo", "Froyo", "Fizz", "Bip", "Bop", "Bang", "Boo"));

    public RecreateCompanies() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }

    //region > number (optional input)
    private Integer number;

    /**
     * The number of objects to create, up to 10; optional, defaults to 3.
     */
    public Integer getNumber() {
        return number;
    }

    public RecreateCompanies setNumber(final Integer number) {
        this.number = number;
        return this;
    }
    //endregion

    //region > companies (output)
    private final List<Company> companies = Lists.newArrayList();

    /**
     * The companies created by this fixture (output).
     */
    public List<Company> getCompanies() {
        return companies;
    }
    //endregion

    @Override
    protected void execute(final ExecutionContext ec) {

        // defaults
        final int number = defaultParam("number", ec, 3);

        // validate
        if(number < 0 || number > NAMES.size()) {
            throw new IllegalArgumentException(String.format("number must be in range [0,%d)", NAMES.size()));
        }

        //
        // execute
        //
        ec.executeChild(this, new CompaniesTearDown());

        IndustryGroups[] industryGroups = IndustryGroups.values();
        for (int i = 0; i < number; i++) {
            int groupIndex = i % industryGroups.length;
            final CompanyCreate fs = new CompanyCreate().setNameAndGroup(NAMES.get(i), industryGroups[groupIndex]);
            ec.executeChild(this, fs.getName(), fs);
            companies.add(fs.getCompany());
        }
    }
}
