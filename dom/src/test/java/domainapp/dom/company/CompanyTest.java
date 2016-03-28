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

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanyTest {

    Company company;

    @Before
    public void setUp() throws Exception {
        company = new Company();
    }

    public static class Name extends CompanyTest {

        @Test
        public void happyCase() throws Exception {
            // given
            String name = "Foobar";
            assertThat(company.getName()).isNull();

            // when
            company.setName(name);

            // then
            assertThat(company.getName()).isEqualTo(name);
        }
    }

    @Test
    public void mod() throws Exception {
        for (int i = 0; i < 20; i++)
        {
            System.out.println(i + " % 8 " + (i % 8));
        }

    }
}
