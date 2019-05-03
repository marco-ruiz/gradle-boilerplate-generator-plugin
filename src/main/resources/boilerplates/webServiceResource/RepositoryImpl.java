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

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ${rootPackage}.model.${resourceName}Entity;
import ${rootPackage}.model.${resourceName}Entity_;
import ${rootPackage}.model.${resourceName}QueryParameters;

public class ${resourceName}RepositoryImpl implements ${resourceName}RepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<${resourceName}Entity> findMatching(${resourceName}QueryParameters qParams) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<${resourceName}Entity> criteria = cb.createQuery(${resourceName}Entity.class);
		Root<${resourceName}Entity> srcEntity = criteria.from(${resourceName}Entity.class);
		criteria.select(srcEntity);
		Set<String> names = qParams.getNames();
		if (names != null && !names.isEmpty())
			criteria.where(srcEntity.get(${resourceName}Entity_.name).in(names));
		return em.createQuery(criteria).getResultList();
	}
}
