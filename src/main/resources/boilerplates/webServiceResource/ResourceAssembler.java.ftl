package ${fullPackage};

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import ${packagePrefix}model.${resourceName}Entity;
import ${packagePrefix}web.${resourceName}Controller;

@Component
public class ${resourceName}ResourceAssembler extends ResourceAssemblerSupport<${resourceName}Entity, ResourceSupport> {

	public ${resourceName}ResourceAssembler() {
		super(${resourceName}Controller.class, ResourceSupport.class);
	}

	/* (non-Javadoc)
	 * @see org.springframework.hateoas.ResourceAssembler#toResource(java.lang.Object)
	 */
	@Override
	public ResourceSupport toResource(${resourceName}Entity entity) {
		if (entity == null) return null;
		ControllerLinkBuilder selfLinkBuilder = linkTo(methodOn(${resourceName}Controller.class).find(entity.getId()));
		ControllerLinkBuilder allLinkBuilder = linkTo(methodOn(${resourceName}Controller.class).findMatching(null, null));
		return new ${resourceName}Resource(entity,
				selfLinkBuilder.withSelfRel(),
				selfLinkBuilder.withRel(${resourceName}Resource.REL_ITEM),
				allLinkBuilder.withRel(${resourceName}Resource.REL_COL));
	}
}
