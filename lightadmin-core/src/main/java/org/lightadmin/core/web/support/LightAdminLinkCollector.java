/**
 * 
 */
package org.lightadmin.core.web.support;

import java.util.List;

import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.rest.core.support.SelfLinkProvider;
import org.springframework.data.rest.webmvc.mapping.Associations;
import org.springframework.data.rest.webmvc.mapping.LinkCollector;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.util.Assert;

/**
 * Overriding default {@link LinkCollector} so that correct
 * <code>Persistent Entity</code> (not the {@link PersistentEntityWrapper} is
 * used to generate the links.
 * 
 * @author <a href="mailto:gazi.mr.rahman@gmail.com">Gazi Rahman</a>
 *
 */
public class LightAdminLinkCollector extends org.springframework.data.rest.webmvc.mapping.LinkCollector {
	
	/**
	 * @param entities
	 * @param linkProvider
	 * @param associationLinks
	 */
	public LightAdminLinkCollector(PersistentEntities entities, SelfLinkProvider linkProvider, Associations associationLinks) {
		super(entities, linkProvider, associationLinks);
	}
	
	@Override
	public Links getLinksFor(Object object, List<Link> existingLinks) {

		Assert.notNull(object, "Object must not be null!");
		
		if (object instanceof PersistentEntityWrapper) {
			object = ((PersistentEntityWrapper) object).getPersistentEntity();
		}

		return super.getLinksFor(object, existingLinks);
	}

}
