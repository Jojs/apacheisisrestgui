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
package domainapp.dom.address;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.eventbus.PropertyDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;

import domainapp.dom.company.IndustryGroups;

@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "companyindex",
        table = "Address"
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
                        + "FROM domainapp.dom.address.Address "),
        @javax.jdo.annotations.Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.address.Address "
                        + "WHERE name.indexOf(:name) >= 0 ")
})
//@javax.jdo.annotations.Unique(name="SimpleObject_name_UNQ", members = {"id"})
@DomainObject
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
public class Address implements Comparable<Address> {

    public static final int NAME_LENGTH = 40;


    public TranslatableString title() {
        return TranslatableString.tr("Address: {name}", "name", getName());
    }

    public String iconName() {
        return getIndustryGroup().icon();
    }

    public static class NameDomainEvent extends PropertyDomainEvent<Address,String> {}
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
    private String name;
    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }

    private IndustryGroups industryGroup;

    @MemberOrder(sequence = "2")
    @Column(allowsNull = "false")
    public IndustryGroups getIndustryGroup() {
        return industryGroup;
    }

    public void setIndustryGroup(final IndustryGroups industryGroup) {
        this.industryGroup = industryGroup;
    }

    public static class UpdateNameDomainEvent extends ActionDomainEvent<Address> {}
    @Action(
            domainEvent = UpdateNameDomainEvent.class,
            semantics = SemanticsOf.IDEMPOTENT
    )
    public Address updateName(
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



    public static class DeleteDomainEvent extends ActionDomainEvent<Address> {}
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
    public int compareTo(final Address other) {
        return ObjectContracts.compare(this, other, "name");
    }


    @javax.inject.Inject
    @SuppressWarnings("unused")
    private DomainObjectContainer container;

}
