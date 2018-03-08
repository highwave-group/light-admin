/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.core.web.support;

import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.rest.core.support.SelfLinkProvider;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.mapping.Associations;
import org.springframework.data.rest.webmvc.support.Projector;
import org.springframework.hateoas.Link;
import org.springframework.util.Assert;

import static org.springframework.beans.PropertyAccessorFactory.forDirectFieldAccess;

/**
 * Fix of Spring Data REST related defect
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class DynamicPersistentEntityResourceAssembler extends PersistentEntityResourceAssembler {
	
    public DynamicPersistentEntityResourceAssembler(PersistentEntityResourceAssembler resourceAssembler) {
        super(entities(resourceAssembler), projector(resourceAssembler), associations(resourceAssembler), linkProvider(resourceAssembler));
    }

	/**
     * @see DATAREST-269 (https://jira.spring.io/browse/DATAREST-269)
     */
    @Override
    public Link getSelfLinkFor(Object instance) {
        Assert.notNull(instance, "Domain object must not be null!");

        @SuppressWarnings("rawtypes")
		Class instanceType = instance.getClass();
        PersistentEntity<?, ?> entity = entities(this).getPersistentEntity(instanceType);

        if (entity == null) {
            throw new IllegalArgumentException(String.format("Cannot create self link for %s! No persistent entity found!", instanceType));
        }

        Object id = entity.getIdentifierAccessor(instance).getIdentifier();

        if (id == null) {
        	throw new IllegalArgumentException(String.format("Cannot create self link for %s of Type: %s! No Id found!",
        			instance, instanceType));
        }


		return super.getSelfLinkFor(instance);
    }

    private static Projector projector(PersistentEntityResourceAssembler persistentEntityResourceAssembler) {
        return (Projector) forDirectFieldAccess(persistentEntityResourceAssembler).getPropertyValue("projector");
    }

    private static PersistentEntities entities(PersistentEntityResourceAssembler persistentEntityResourceAssembler) {
        return (PersistentEntities) forDirectFieldAccess(persistentEntityResourceAssembler).getPropertyValue("entities");
	}
    
    private static Associations associations(PersistentEntityResourceAssembler persistentEntityResourceAssembler) {
    	return (Associations) forDirectFieldAccess(persistentEntityResourceAssembler).getPropertyValue("associations");
    }
    
    private static SelfLinkProvider linkProvider(PersistentEntityResourceAssembler persistentEntityResourceAssembler) {
    	return (SelfLinkProvider) forDirectFieldAccess(persistentEntityResourceAssembler).getPropertyValue("linkProvider");
    }
}