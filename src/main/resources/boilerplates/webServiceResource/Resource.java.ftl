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
