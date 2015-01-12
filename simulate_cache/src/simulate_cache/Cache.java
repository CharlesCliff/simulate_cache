package simulate_cache;

public class Cache {
	/*static variables definition
	 * 
	 * */
	public static final int CACHE_INDEX=128;
	public static final int CACHE_INTERN=2;
	/*
	 * private variables definition
	 */
	private Integer[] valid;// valid or not,0 for invalid ,1 for valid
	private Integer[] tag;// tag for cache locate
	private Integer[][] block;//data block
}
