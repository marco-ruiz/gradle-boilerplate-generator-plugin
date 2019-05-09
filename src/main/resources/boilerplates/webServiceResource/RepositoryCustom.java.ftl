package ${fullPackage};

import java.util.List;

import ${packagePrefix}model.${resourceName}Entity;
import ${packagePrefix}model.${resourceName}QueryParameters;

public interface ${resourceName}RepositoryCustom {

	default List<${resourceName}Entity> findMatching() {
		return findMatching(new ${resourceName}QueryParameters());
	}

	List<${resourceName}Entity> findMatching(${resourceName}QueryParameters qParams);
}
