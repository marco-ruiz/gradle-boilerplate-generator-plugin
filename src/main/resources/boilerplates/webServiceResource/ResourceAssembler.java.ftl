/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
