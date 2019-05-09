package ${fullPackage};

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.ResourceProcessor;

import ${packagePrefix}web.${resourceName}Controller;

@Configuration
public class RootResourceProcessor implements ResourceProcessor<RepositoryLinksResource> {

	/* (non-Javadoc)
	 * @see org.springframework.hateoas.ResourceProcessor#process(org.springframework.hateoas.ResourceSupport)
	 */
//	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(${resourceName}Controller.TEMPLATED_LINK_COL.get());
		return resource;
	}
}
