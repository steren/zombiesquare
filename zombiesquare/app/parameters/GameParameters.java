package parameters;

public class GameParameters {
	/** probability between 0 and 1 to win a weapon */
	public static double getWeaponProbability = 0.4;
	/** maximum number of weapons a survivor can carry, he can have [0 ... maxCountWeapons] weapons. */
	public static int maxCountWeapons = 3;
	public static int costKeepLive = 1;
	public static int costSaveAll = 2;
	
	public static final int SCORE_CONTAMINATE_VENUE = 1;
	public static final int SCORE_DECONTAMINATE_VENUE = 10;
	public static final int SCORE_DECONTAMINATE_ZOMBIE = 3;
	
}
