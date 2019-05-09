package ${fullPackage};

import java.time.Instant;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import ${packagePrefix}model.${resourceName}Entity;

@Relation(value = ${resourceName}Resource.REL_ITEM, collectionRelation = ${resourceName}Resource.REL_COL)
public class ${resourceName}Resource extends ResourceSupport {

	public static final String REL_COL = "${resourceName}s";
	public static final String REL_ITEM = "${resourceName}";

    public final long id;
    public final String name;
    public final Instant createdOn;
    public final String createdBy;

    public ${resourceName}Resource(${resourceName}Entity entity, Link... links) {
    	id = entity.getId();
    	name = entity.getName();
    	createdOn = entity.getCreatedOn();
    	createdBy = entity.getCreatedBy();
    	add(links);
    }
}
