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
package domainapp.dom.company;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.eventbus.PropertyDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;

import domainapp.dom.person.Person;

@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "companyindex",
        table = "Company"
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
                        + "FROM domainapp.dom.company.Company "),
        @javax.jdo.annotations.Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.company.Company "
                        + "WHERE name.indexOf(:name) >= 0 ")
})
//@javax.jdo.annotations.Unique(name="SimpleObject_name_UNQ", members = {"id"})
@DomainObject(autoCompleteAction = "findByName", autoCompleteRepository = Companies.class)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
public class Company implements Comparable<Company> {

    public static final int NAME_LENGTH = 40;


    public TranslatableString title() {
        return TranslatableString.tr("Company: {name}", "name", getName());
    }

    public String iconName() {
        return getIndustryGroup().name();
    }

    public static class NameDomainEvent extends PropertyDomainEvent<Company,String> {}
    @javax.jdo.annotations.Column(
            allowsNull="false",
            length = NAME_LENGTH
    )
    @Property(
            editing = Editing.ENABLED
    )
    @PropertyLayout(
            namedEscaped = false
    )
    private String name;
    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }

    private IndustryGroups industryGroup;

    //region > Directors (collection)
    @Persistent(table = "COMPANY_DIRECTORS")
    @Join(column = "COMPANY_ID")
    @Element(column = "PERSON_ID")
    private SortedSet<Person> directors = new TreeSet<Person>();

    @MemberOrder(sequence = "2")
    @CollectionLayout(render = RenderType.EAGERLY)
    public SortedSet<Person> getDirectors() {
        return directors;
    }

    public void setDirectors(final SortedSet<Person> directors) {
        this.directors = directors;
    }
    //endregion

    private void addAnDirector(final Person director) {
        directors.add(director);
    }

    //region > Owners (collection)
    @Persistent(table = "COMPANY_OWNERS")
    @Join(column = "COMPANY_ID")
    @Element(column = "PERSON_ID")
    private SortedSet<Person> owners = new TreeSet<Person>();

    @MemberOrder(sequence = "3")
    @CollectionLayout(render = RenderType.EAGERLY)
    public SortedSet<Person> getOwners() {
        return owners;
    }

    public void setOwners(final SortedSet<Person> owners) {
        this.owners = owners;
    }
    //endregion

    private void addAnOwner(final Person owner) {
        owners.add(owner);
    }


    @MemberOrder(sequence = "4")
    @Column(allowsNull = "false")
    public IndustryGroups getIndustryGroup() {
        return industryGroup;
    }

    public void setIndustryGroup(final IndustryGroups industryGroup) {
        this.industryGroup = industryGroup;
    }

/*
    public static class UpdateNameDomainEvent extends ActionDomainEvent<Company> {}
    @Action(
            domainEvent = UpdateNameDomainEvent.class,
            semantics = SemanticsOf.IDEMPOTENT
    )
    public Company updateName(
            @Parameter(maxLength = NAME_LENGTH)
            @ParameterLayout(named = "New name")
            final String name) {
        setName(name);
        return this;
    }
    public String default0UpdateName() {
        return getName();
    }
    public TranslatableString validateUpdateName(final String name) {
        return name.contains("!")? TranslatableString.tr("Exclamation mark is not allowed"): null;
    }
*/
    public static class AddDirectorDomainEvent extends ActionDomainEvent<Company> {}
    @Action(
            domainEvent = AddDirectorDomainEvent.class,
            semantics = SemanticsOf.IDEMPOTENT
    )
    public Company addDirector(
            @ParameterLayout(named = "Director")
            final Person director) {
        addAnDirector(director);
        return this;
    }
    public Person default0AddDirector() {
        Person result = null;
        if (!directors.isEmpty()) {
            result = directors.first();
        }
        return result;
    }
    public TranslatableString validateAddDirector(final Person director) {
        return directors.contains(director) ? TranslatableString.tr("Director can only be added once"): null;
    }

    public static class AddOwnerDomainEvent extends ActionDomainEvent<Company> {}
    @Action(
            domainEvent = AddOwnerDomainEvent.class,
            semantics = SemanticsOf.IDEMPOTENT
    )
    public Company addOwner(
            @ParameterLayout(named = "Owner")
            final Person owner) {
        addAnOwner(owner);
        return this;
    }
    public Person default0AddOwner() {
        Person result = null;
        if (!owners.isEmpty()) {
            result = owners.first();
        }
        return result;
    }
    public TranslatableString validateAddOwner(final Person owner) {
        return owners.contains(owner) ? TranslatableString.tr("Owner can only be added once"): null;
    }


    public static class DeleteDomainEvent extends ActionDomainEvent<Company> {}
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
    public int compareTo(final Company other) {
        return ObjectContracts.compare(this, other, "name");
    }


    @javax.inject.Inject
    @SuppressWarnings("unused")
    private DomainObjectContainer container;

}
