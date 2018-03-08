/**
 * 
 */
package org.lightadmin.core.web.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimpleAssociationHandler;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.rest.core.Path;
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
 * @author Gazi Rahman
 *
 */
public class LightAdminLinkCollector extends org.springframework.data.rest.webmvc.mapping.LinkCollector {
	
	private PersistentEntities entities;
	
	private SelfLinkProvider links;
	
	private Associations associationLinks;

	/**
	 * @param entities
	 * @param linkProvider
	 * @param associationLinks
	 */
	public LightAdminLinkCollector(PersistentEntities entities, SelfLinkProvider linkProvider, Associations associationLinks) {
		super(entities, linkProvider, associationLinks);
		this.entities = entities;
		this.links = linkProvider;
		this.associationLinks = associationLinks;
	}
	
	@Override
	public Links getLinksFor(Object object, List<Link> existingLinks) {

		Assert.notNull(object, "Object must not be null!");
		Assert.notNull(existingLinks, "Existing links must not be null!");
		
		if (object instanceof PersistentEntityWrapper) {
			object = ((PersistentEntityWrapper) object).getPersistentEntity();
		}

		PersistentEntity<?, ?> entity = entities.getPersistentEntity(object.getClass());

		Links links = new Links(existingLinks);
		Link selfLink = createSelfLink(object, links);

		if (selfLink == null) {
			return links;
		}

		Path path = new Path(selfLink.expand().getHref());

		LinkCollectingAssociationHandler handler = new LinkCollectingAssociationHandler(entities, path, associationLinks);
		entity.doWithAssociations(handler);

		List<Link> result = new ArrayList<Link>(existingLinks);
		result.addAll(handler.getLinks());

		return addSelfLinkIfNecessary(object, result);
	}

	private Links addSelfLinkIfNecessary(Object object, List<Link> existing) {

		Links result = new Links(existing);

		if (result.hasLink(Link.REL_SELF)) {
			return result;
		}

		List<Link> list = new ArrayList<Link>();
		list.add(createSelfLink(object, result));
		list.addAll(existing);

		return new Links(list);
	}

	private Link createSelfLink(Object object, Links existing) {

		if (existing.hasLink(Link.REL_SELF)) {
			return existing.getLink(Link.REL_SELF);
		}

		return links.createSelfLinkFor(object).withSelfRel();
	}

	/**
	 * {@link SimpleAssociationHandler} that will collect {@link Link}s for all linkable associations.
	 *
	 * @author Oliver Gierke
	 * @since 2.1
	 */
	private static class LinkCollectingAssociationHandler implements SimpleAssociationHandler {

		private static final String AMBIGUOUS_ASSOCIATIONS = "Detected multiple association links with same relation type! Disambiguate association %s using @RestResource!";

		@SuppressWarnings("unused")
		private final PersistentEntities entities;
		private final Path basePath;
		private final Associations associationLinks;
		private final List<Link> links = new ArrayList<Link>();
		
		public LinkCollectingAssociationHandler(PersistentEntities entities, Path basePath, Associations associationLink) {
			this.entities = entities;
			this.basePath = basePath;
			this.associationLinks = associationLink;
		}

		/**
		 * Returns the links collected after the {@link Association} has been traversed.
		 *
		 * @return the links
		 */
		public List<Link> getLinks() {
			return links;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.mapping.SimpleAssociationHandler#doWithAssociation(org.springframework.data.mapping.Association)
		 */
		@Override
		public void doWithAssociation(final Association<? extends PersistentProperty<?>> association) {

			if (associationLinks.isLinkableAssociation(association)) {

				PersistentProperty<?> property = association.getInverse();
				Links existingLinks = new Links(links);

				for (Link link : associationLinks.getLinksFor(association, basePath)) {
					if (existingLinks.hasLink(link.getRel())) {
						throw new MappingException(String.format(AMBIGUOUS_ASSOCIATIONS, property.toString()));
					} else {
						links.add(link);
					}
				}
			}
		}
	}

}
