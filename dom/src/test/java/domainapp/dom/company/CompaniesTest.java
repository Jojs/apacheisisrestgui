/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package domainapp.dom.company;

import java.util.List;

import com.google.common.collect.Lists;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;

import static org.assertj.core.api.Assertions.assertThat;

public class CompaniesTest {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    DomainObjectContainer mockContainer;
    
    Companies companies;

    @Before
    public void setUp() throws Exception {
        companies = new Companies();
        companies.container = mockContainer;
    }

    public static class Create extends CompaniesTest {

        @Test
        public void happyCase() throws Exception {

            // given
            final Company simpleObject = new Company();

            final Sequence seq = context.sequence("create");
            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).newTransientInstance(Company.class);
                    inSequence(seq);
                    will(returnValue(simpleObject));

                    oneOf(mockContainer).persistIfNotAlready(simpleObject);
                    inSequence(seq);
                }
            });

            // when
            final Company obj = companies.create("Foobar", IndustryGroups.BASIC_INDUSTRIES);

            // then
            assertThat(obj).isEqualTo(simpleObject);
            assertThat(obj.getName()).isEqualTo("Foobar");
        }

    }

    public static class ListAll extends CompaniesTest {

        @Test
        public void happyCase() throws Exception {

            // given
            final List<Company> all = Lists.newArrayList();

            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).allInstances(Company.class);
                    will(returnValue(all));
                }
            });

            // when
            final List<Company> list = companies.listAll();

            // then
            assertThat(list).isEqualTo(all);
        }
    }
}
