/**
 * 
 */
package org.lightadmin.core.web.support;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.springframework.hateoas.Link;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Wraps all the <code>Persistent Entities</code>.
 * <p>
 * This class is moved out of {@link DynamicPersistentEntityResourceProcessor}
 * so that it can be used in other classes as well, like the {@link LightAdminLinkCollector}.
 * 
 * @author <a href="mailto:gazi.mr.rahman@gmail.com">Gazi Rahman</a>
 *
 */

public class PersistentEntityWrapper {
    private String stringRepresentation;
    private boolean managedDomainType;
    private String primaryKey;
    private Link domainLink;
    private Object persistentEntity;
    private Map<DomainConfigurationUnitType, Map<String, Object>> dynamicProperties;

    private PersistentEntityWrapper(Object persistentEntity, Map<DomainConfigurationUnitType, Map<String, Object>> dynamicProperties, String stringRepresentation, Link domainLink, boolean managedDomainType, String primaryKey) {
        this.stringRepresentation = stringRepresentation;
        this.domainLink = domainLink;
        this.managedDomainType = managedDomainType;
        this.persistentEntity = persistentEntity;
        this.dynamicProperties = dynamicProperties;
        this.primaryKey = primaryKey;
    }

    public static PersistentEntityWrapper associatedPersistentEntity(String stringRepresentation, boolean managedDomainType, String primaryKey, Object primaryKeyValue, Link domainLink) {
        Map<String, Object> persistentEntity = newHashMap();
        persistentEntity.put(primaryKey, primaryKeyValue);

        return new PersistentEntityWrapper(persistentEntity, null, stringRepresentation, domainLink, managedDomainType, primaryKey);
    }

    public static PersistentEntityWrapper persistentEntity(Object instance, Map<DomainConfigurationUnitType, Map<String, Object>> dynamicProperties, String stringRepresentation, Link domainLink, boolean managedDomainType, String primaryKey) {
        return new PersistentEntityWrapper(instance, dynamicProperties, stringRepresentation, domainLink, managedDomainType, primaryKey);
    }

    @JsonProperty("string_representation")
    public String getStringRepresentation() {
        return stringRepresentation;
    }

    @JsonProperty("primary_key")
    public String getPrimaryKey() {
        return primaryKey;
    }

    @JsonProperty("managed_type")
    public boolean isManagedDomainType() {
        return managedDomainType;
    }

    @JsonProperty("domain_link")
    @JsonInclude(NON_NULL)
    public Link getDomainLink() {
        return domainLink;
    }

    @JsonProperty("original_properties")
    @JsonInclude(NON_NULL)
    public Object getPersistentEntity() {
        return persistentEntity;
    }

    @JsonProperty("dynamic_properties")
    @JsonInclude(NON_EMPTY)
    public Map<DomainConfigurationUnitType, Map<String, Object>> getDynamicProperties() {
        return dynamicProperties;
    }
}
