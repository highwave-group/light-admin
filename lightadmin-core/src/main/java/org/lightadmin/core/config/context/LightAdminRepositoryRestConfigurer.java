/**
 * 
 */
package org.lightadmin.core.config.context;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.web.json.LightAdminJacksonModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Gazi Rahman
 *
 */
@Configuration
public class LightAdminRepositoryRestConfigurer extends RepositoryRestConfigurerAdapter
		implements RepositoryRestConfigurer {
	private static Logger logger = LoggerFactory.getLogger(LightAdminRepositoryRestConfigurer.class);

    @Autowired
    private ListableBeanFactory beanFactory;

	/**
	 * 
	 */
	public LightAdminRepositoryRestConfigurer() {
		logger.debug("LightAdminRepositoryRestConfigurer instantiated");
	}

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.setDefaultPageSize(10);
        config.setBasePath(lightAdminConfiguration().getApplicationRestBasePath());
        config.exposeIdsFor(globalAdministrationConfiguration().getAllDomainTypesAsArray());
        config.setReturnBodyOnCreate(true);
        config.setReturnBodyOnUpdate(true);
    }
    
    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
      validatingListener.addValidator("beforeCreate", validator());
      validatingListener.addValidator("beforeSave", validator());
    }
    
    @Override
    public void configureJacksonObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new LightAdminJacksonModule(globalAdministrationConfiguration()));
    }

    private GlobalAdministrationConfiguration globalAdministrationConfiguration() {
        return beanFactory.getBean(GlobalAdministrationConfiguration.class);
    }

    private LightAdminConfiguration lightAdminConfiguration() {
        return beanFactory.getBean(LightAdminConfiguration.class);
    }

    private Validator validator() {
        return beanFactory.getBean("validator", Validator.class);
    }

}
