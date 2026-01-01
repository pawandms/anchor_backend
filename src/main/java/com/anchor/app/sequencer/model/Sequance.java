package com.anchor.app.sequencer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection= "sequence")
public class Sequance {

	@Id
	private String id;
	private String name;
	private long seed;
	private long lastupdate;
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSeed() {
		return seed;
	}
	public void setSeed(long seed) {
		this.seed = seed;
	}
	public long getLastupdate() {
		return lastupdate;
	}
	public void setLastupdate(long lastupdate) {
		this.lastupdate = lastupdate;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (int) (lastupdate ^ (lastupdate >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (seed ^ (seed >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sequance other = (Sequance) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastupdate != other.lastupdate)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (seed != other.seed)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Sequance [id=" + id + ", name=" + name + ", seed=" + seed + ", lastupdate=" + lastupdate + "]";
	}
	
	
	
}
