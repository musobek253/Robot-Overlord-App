package com.marginallyclever.robotOverlord.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Entities are nodes in a tree of data that can find each other and observe/be observed
 * @author Dan Royer
 *
 */
public class Entity extends Observable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -994214494049397444L;
	
	private String name;
	// my children 
	protected ArrayList<Entity> children = new ArrayList<Entity>();
	// my parent
	protected Entity parent;
	
	
	public Entity() {
		super();
	}
	
	public Entity(String name) {
		super();
		this.name=name;
	}
	
	public void set(Entity b) {
		name = b.name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void update(double dt) {
		for(Entity e : children ) {
			e.update(dt);
		}
	}	

	public boolean hasChild(Entity o) {
		return children.contains(o);
	}
	
	public void addChild(Entity e) {
		children.add(e);
		e.setParent(this);
	}
	
	public void removeChild(Entity e) {
		if(children.contains(e)) {
			children.remove(e);
			e.setParent(null);
		}
	}
	
	public ArrayList<Entity> getChildren() {
		return children;
	}
	
	public void removeParent() {
		parent=null;
	}

	public Entity getParent() {
		return parent;
	}
	
	public void setParent(Entity e) {
		parent = e;
	}
	
	public String toString() {
		return name;
	}

	// Find the root node.
	public Entity getRoot() {
		Entity e=this;
		while( e.getParent() != null ) e = e.getParent();
		return e;
	}
	
	/**
	 * Search the entity tree based on an absolute or relative Unix-style path.
	 * 
	 * @param path the search query
	 * @return the requested entity or null.
	 */
	public Entity findByPath(String path) {
		String [] pathComponents = path.split("/");
		
		// if absolute path, start with root node.
		Entity e = path.startsWith("/") ? getRoot() : this;
		
		for( String name : pathComponents ) {
			if(e==null) break;
			if(name.contentEquals("..")) {
				// ".." = my parent
				e = e.getParent();
			} else if(name.contentEquals(".")) {
				// "." is me!
				continue;
			} else {
				boolean found=false;
				for( Entity c : e.getChildren() ) {
					if( name.contentEquals(c.getName()) ) {
						e = c;
						found=true;
						break;
					}
				}
				if(found==false) return null;  // does not exist
			}
		}
		
		return e;
	}

	public String getCanonicalName() {
		String sum="";
		Entity e=this;
		
		do {
			sum = "/" + e.getName() + sum;
			e=e.getParent();
		} while(e!=null);

		return sum;
	}
}
