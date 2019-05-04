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

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import ${rootPackage}.model.${resourceName}Entity;
import ${rootPackage}.repo.${resourceName}Repository;

@Service
public class AppServiceImpl implements AppService {

	private static <ENTITY_T> ENTITY_T findEntity(JpaRepository<ENTITY_T, Long> repo, Class<ENTITY_T> entityClass, Long id) {
		Objects.requireNonNull(id, String.format("Cannot find a %s with a null id", entityClass));
		return repo.findOne(id);
	}

	@Autowired
	private ${resourceName}Repository repo;

	@Override
	public ${resourceName}Entity update${resourceName}(Long id, String name, String user) {
		${resourceName}Entity entity = findEntity(repo, ${resourceName}Entity.class, id);
		entity.setName(name);
		return repo.saveAndFlush(entity);
	}
}

