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
package domainapp.dom.person;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.eventbus.PropertyDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;

import domainapp.dom.company.Company;

@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "companyindex",
        table = "Person"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
//        strategy=VersionStrategy.VERSION_NUMBER,
        strategy= VersionStrategy.DATE_TIME,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "find", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.person.Person "),
        @javax.jdo.annotations.Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.person.Person "
                        + "WHERE firstName.indexOf(:firstName) >= 0 ")
})
// @javax.jdo.annotations.Unique(name="SimpleObject_name_UNQ", members = {"firstName"})
@DomainObject(autoCompleteAction = "findByName", autoCompleteRepository = Persons.class)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
public class Person implements Comparable<Person> {

    public static final int NAME_LENGTH = 40;


    public TranslatableString title() {
        return TranslatableString.tr("Person: {firstName}", "firstName", getFirstName());
    }

    public static class NameDomainEvent extends PropertyDomainEvent<Person,String> {}
    @Column(
            allowsNull="false",
            length = NAME_LENGTH
    )
    @Property(
            editing = Editing.ENABLED
    )
    @PropertyLayout(
            namedEscaped = false
    )
    private String firstName;
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    @MemberOrder(sequence = "2")
    @Column(allowsNull = "false")
    private String lastName;
    public String getLastName() {
        return lastName;
    }
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    //region > directedCompanies (collection)
    @Persistent(mappedBy = "directors")
    private SortedSet<Company> directedCompanies = new TreeSet<Company>();

    public SortedSet<Company> getDirectedCompanies() {
        return directedCompanies;
    }

    public void setDirectedCompanies(final SortedSet<Company> directedCompanies) {
        this.directedCompanies = directedCompanies;
    }
    //endregion

    //region > directedCompanies (collection)
    @Persistent(mappedBy = "owners")
    private SortedSet<Company> ownedCompanies = new TreeSet<Company>();

    public SortedSet<Company> getOwnedCompanies() {
        return ownedCompanies;
    }

    public void setOwnedCompanies(final SortedSet<Company> ownedCompanies) {
        this.ownedCompanies = ownedCompanies;
    }
    //endregion



    /*
    public static class UpdateNameDomainEvent extends IsisApplibModule.ActionDomainEvent<Person> {}
    @Action(
            domainEvent = UpdateNameDomainEvent.class,
            semantics = SemanticsOf.IDEMPOTENT
    )
    public Person updateName(
            @Parameter(maxLength = NAME_LENGTH)
            @ParameterLayout(named = "New firstName")
            final String name) {
        setFirstName(name);
        return this;
    }
    public String default0UpdateName() {
        return getFirstName();
    }
    public TranslatableString validateUpdateName(final String name) {
        return name.contains("!")? TranslatableString.tr("Exclamation mark is not allowed"): null;
    }
    */


    public static class DeleteDomainEvent extends ActionDomainEvent<Person> {}
    @Action(
            domainEvent = DeleteDomainEvent.class,
            semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE
    )
    public void delete() {
        container.removeIfNotAlready(this);
    }


    /**
     * version (derived property)
     */
    public java.sql.Timestamp getVersionSequence() {
        return (java.sql.Timestamp) JDOHelper.getVersion(this);
    }


    @Override
    public int compareTo(final Person other) {
        return ObjectContracts.compare(this, other, "firstName");
    }


    @javax.inject.Inject
    @SuppressWarnings("unused")
    private DomainObjectContainer container;

}
