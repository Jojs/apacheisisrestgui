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

package domainapp.fixture.dom.company;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import domainapp.dom.company.Company;
import domainapp.dom.company.Companies;
import domainapp.dom.company.IndustryGroups;

public class CompanyCreate extends FixtureScript {

    //region > name (input)
    private String name;
    private IndustryGroups industryGroup;

    /**
     * Name of the object (required)
     */
    public String getName() {
        return name;
    }

    public IndustryGroups getIndustryGroup() {
        return industryGroup;
    }

    public CompanyCreate setNameAndGroup(final String name, final IndustryGroups industryGroup) {
        this.name = name;
        this.industryGroup = industryGroup;
        return this;
    }
    //endregion


    //region > company (output)
    private Company company;

    /**
     * The created company object (output).
     * @return
     */
    public Company getCompany() {
        return company;
    }
    //endregion

    @Override

    protected void execute(final ExecutionContext ec) {

        String name = checkParam("name", ec, String.class);
        IndustryGroups industryGroup = checkParam("industryGroup", ec, IndustryGroups.class);

        this.company = wrap(companies).create(name, industryGroup);

        // also make available to UI
        ec.addResult(this, company);
    }

    @javax.inject.Inject
    private Companies companies;

}
