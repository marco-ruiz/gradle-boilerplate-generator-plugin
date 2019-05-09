package ${fullPackage};

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import ${packagePrefix}model.${resourceName}Entity;
import ${packagePrefix}repo.${resourceName}Repository;

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

