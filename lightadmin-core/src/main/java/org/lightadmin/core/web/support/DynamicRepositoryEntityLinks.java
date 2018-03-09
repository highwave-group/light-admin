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
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.data.rest.webmvc.support.PagingAndSortingTemplateVariables;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.plugin.core.PluginRegistry;

import java.io.Serializable;

import static org.springframework.beans.PropertyAccessorFactory.forDirectFieldAccess;

/**
 * {@link RepositoryEntityLinks} for <code>LightAdmin</code> that allows creation
 * of links for entities that are new (having no <code>ID</code>) and also adds
 * support for creating links for <code>File Properties</code>.
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 * @author <a href="mailto:gazi.mr.rahman@gmail.com">Gazi Rahman</a>
 */
public class DynamicRepositoryEntityLinks extends RepositoryEntityLinks implements EntityLinks {


    public DynamicRepositoryEntityLinks(RepositoryEntityLinks delegate) {
    	super(repositories(delegate), mappings(delegate), config(delegate), templateVariables(delegate), idConverters(delegate));
    }

	public Link linkForFilePropertyLink(Object instance,
    		@SuppressWarnings("rawtypes") PersistentProperty persistentProperty) {
        @SuppressWarnings("rawtypes")
		PersistentEntity persistentEntity = persistentProperty.getOwner();
        Serializable id = idValue(instance, persistentEntity);

        return super.linkForSingleResource(persistentEntity.getType(), id).slash(persistentProperty.getName()).slash("file").withSelfRel();
    }

    @Override
    public Link linkToSingleResource(Class<?> type, Object id) {
        if (id == null) {
            return linkFor(type).slash("new").withSelfRel();
        }
        return super.linkToSingleResource(type, id);
    }

    private Serializable idValue(Object instance,
    		@SuppressWarnings("rawtypes") PersistentEntity persistentEntity) {
        return (Serializable) new DirectFieldAccessFallbackBeanWrapper(instance).getPropertyValue(persistentEntity.getIdProperty().getName());
    }

    private static Repositories repositories(RepositoryEntityLinks delegate) {
		return (Repositories) forDirectFieldAccess(delegate).getPropertyValue("repositories");
	}

	@SuppressWarnings("unchecked")
	private static PluginRegistry<BackendIdConverter, Class<?>> idConverters(RepositoryEntityLinks delegate) {
		return (PluginRegistry<BackendIdConverter, Class<?>>) forDirectFieldAccess(delegate).getPropertyValue("idConverters");
	}

	private static PagingAndSortingTemplateVariables templateVariables(RepositoryEntityLinks delegate) {
		return (PagingAndSortingTemplateVariables) forDirectFieldAccess(delegate).getPropertyValue("templateVariables");
	}

	private static RepositoryRestConfiguration config(RepositoryEntityLinks delegate) {
		return (RepositoryRestConfiguration) forDirectFieldAccess(delegate).getPropertyValue("config");
	}

	private static ResourceMappings mappings(RepositoryEntityLinks delegate) {
		return (ResourceMappings) forDirectFieldAccess(delegate).getPropertyValue("mappings");
	}
}