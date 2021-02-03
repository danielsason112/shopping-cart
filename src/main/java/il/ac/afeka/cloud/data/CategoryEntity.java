package il.ac.afeka.cloud.data;

public class CategoryEntity {
	private String name;
    private String description;
    
    
	public CategoryEntity() {
		super();
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public String toString() {
		return "CategoryBoundary [name=" + name + ", description=" + description + "]";
	}
    
    
    
    
}
