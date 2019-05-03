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

import java.security.Principal;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ${rootPackage}.model.${resourceName}Entity;
import ${rootPackage}.model.${resourceName}QueryParameters;
import ${rootPackage}.repo.${resourceName}Repository;
import ${rootPackage}.service.AppService;
import ${rootPackage}.web.resource.LinkUtils;
import ${rootPackage}.web.resource.${resourceName}Resource;
import ${rootPackage}.web.resource.${resourceName}ResourceAssembler;

@RestController
@RequestMapping(value = "/" + ${resourceName}Resource.REL_COL, produces = MediaTypes.HAL_JSON_VALUE)
public class ${resourceName}Controller extends BaseController {

	public static final Supplier<Link> TEMPLATED_LINK_COL =
			() -> LinkUtils.createTemplatedLink(${resourceName}Controller.class, ${resourceName}Resource.REL_COL, "names", "page", "size", "sort");

	public static final Supplier<Link> TEMPLATED_LINK_ITEM =
			() -> LinkUtils.createTemplatedLink(${resourceName}Controller.class, ${resourceName}Resource.REL_ITEM);

	@Autowired
	private AppService service;

	@Autowired
	private ${resourceName}Repository repo;

	@Autowired
	private ${resourceName}ResourceAssembler entityAssembler;

	@Autowired
    private PagedResourcesAssembler<${resourceName}Entity> pagedAssembler;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResourceSupport> create(@RequestBody ${resourceName}DTO ${resourceName}DTO, Principal principal) {
		String user = resolveUser(principal);
		${resourceName}Entity entity = repo.save(${resourceName}DTO.createEntity(user));
		return createResponse(entity);
	}

	@GetMapping
	public ResponseEntity<ResourceSupport> findMatching(
			@ModelAttribute ${resourceName}QueryParameters qParams,
			Pageable pageRequest) {
		List<${resourceName}Entity> entitiesList = repo.findMatching(qParams);
		Page<${resourceName}Entity> entitiesPage = new PageOfCollection<${resourceName}Entity>(entitiesList, pageRequest);
//		Resources<ResourceSupport> resources = new Resources<>(entityAssembler.toResources(entitiesPage));
		PagedResources<ResourceSupport> resources = pagedAssembler.toResource(entitiesPage, entityAssembler);
		resources.add(TEMPLATED_LINK_COL.get());
		return ResponseEntity.ok(resources);
	}

	@GetMapping(value = "{id}")
	public ResponseEntity<ResourceSupport> find(@PathVariable("id") Long id) {
		${resourceName}Entity entity = repo.findOne(id);
		return createResponse(entity);
	}

	@PatchMapping(value = "{id}")
	public ResponseEntity<ResourceSupport> patch(
			@PathVariable("id") Long id,
			@RequestBody ${resourceName}DTO ${resourceName}DTO,
			Principal principal) {
		String user = resolveUser(principal);
		${resourceName}Entity entity = service.update${resourceName}(id, ${resourceName}DTO.getName(), user);
		return createResponse(entity);
	}

	@DeleteMapping(value = "{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		return ResponseEntity.ok().build();
	}

	//============
	// Utilities
	//============
	private String resolveUser(Principal principal) {
		return (principal == null) ? "" : principal.getName(); // Resolve authenticated user
	}

	private ResponseEntity<ResourceSupport> createResponse(${resourceName}Entity entity) {
		return ResponseEntity.ok(entityAssembler.toResource(entity));
	}
}
