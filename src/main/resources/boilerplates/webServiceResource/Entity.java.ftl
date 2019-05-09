package ${fullPackage};

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="${resourceName}")
public class ${resourceName}Entity {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, updatable = true, unique = true)
    private String name;

    @Column(nullable = false, updatable = false)
    private Instant createdOn;

    @Column(nullable = false, updatable = false)
    private String createdBy;

    public ${resourceName}Entity() {}

	public ${resourceName}Entity(String name, String user) {
		this(user);
		setName(name);
	}

	public ${resourceName}Entity(String user) {
		setCreatedBy(user);
		setCreatedOn(Instant.now());
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
