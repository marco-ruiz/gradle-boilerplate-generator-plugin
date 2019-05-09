package ${fullPackage};

import ${packagePrefix}model.${resourceName}Entity;

public interface AppService {

	${resourceName}Entity update${resourceName}(Long id, String name, String user);
}
