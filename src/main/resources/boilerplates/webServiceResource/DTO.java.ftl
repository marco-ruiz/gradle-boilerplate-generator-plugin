package ${fullPackage};

import java.time.Instant;

import ${packagePrefix}model.${resourceName}Entity;

public class ${resourceName}DTO {

    private long id;
    private String name;
    private Instant createdOn;
    private String createdBy;

	public ${resourceName}Entity createEntity(String user) {
		return new ${resourceName}Entity(name, user);
	}

    //================================
    // GENERATED GETTERS AND SETTERS
    //================================
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Instant createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
