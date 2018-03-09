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
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link RepositoryRestConfigurer} for <code>LightAdmin</code>.
 * <p>
 * Spring Data Web requires the configurations to be moved out of {@link RepositoryRestMvcConfiguration}
 * and are configured through <code>RepositoryRestConfigurer</code> beans
 * configured in the context. That's why these configurations are moved out from
 * {@link LightAdminRepositoryRestMvcConfiguration} and placed in a separate
 * <code>RepositoryRestConfigurer</code> class, which is then configured as a
 * bean in the <code>LightAdminRepositoryRestMvcConfiguration</code>.
 * 
 * @author <a href="mailto:gazi.mr.rahman@gmail.com">Gazi Rahman</a>
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
