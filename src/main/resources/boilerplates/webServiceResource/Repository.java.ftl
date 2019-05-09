package ${fullPackage};

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ${packagePrefix}model.${resourceName}Entity;

@Repository
public interface ${resourceName}Repository extends JpaRepository<${resourceName}Entity, Long>, ${resourceName}RepositoryCustom {

	${resourceName}Entity findByName(String name);
}
