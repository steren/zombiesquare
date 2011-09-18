package models;

public enum Weapon {

	GUN("Gun", ""),
	SHOTGUN("Shot Gun",""),
	TRIPLE_BARREL_SHOTGUN("Triple barrel shotgun", ""), 
	CHAINSAW("Chainsaw", ""), 
	FLAME_FLOWER("Flamethrower", ""), 
	LAWNMOWER("Lawnmower", ""), 
	BASEBALL_BAT("Baseball bat", "");
	
	private String name;
	private String img;
	Weapon(String name, String img) {
		this.name = name;
		this.img = img;
	}
	public String getName() {
		return name;
	}
	
	public String getImg() {
		return img;
	}
	
}
