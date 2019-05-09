package ${fullPackage};

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ${packagePrefix}model.${resourceName}Entity;
//import ${packagePrefix}model.${resourceName}Entity_;
import ${packagePrefix}model.${resourceName}QueryParameters;

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
		if (names != null && !names.isEmpty()) {
//			criteria.where(srcEntity.get(${resourceName}Entity_.name).in(names));
		}
		return em.createQuery(criteria).getResultList();
	}
}
