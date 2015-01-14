package simulate_cache;

public class Cache {
	/*static variables definition
	 * 
	 * */
	public static final int CACHE_INDEX=128;
	public static final int CACHE_INTERN=2;
	public static final int BLOCK_SIZE = 32;
	/*
	 * private variables definition
	 */
	private Boolean valid[];// valid or not,0 for invalid ,1 for valid
	private Integer[] tag;// tag for cache locate
	private Integer[][][] block;//data block
	/*
	 * initial the cache
	 */
	public Cache(){
		valid = new Boolean[CACHE_INDEX];
		tag = new Integer[CACHE_INDEX];
		block = new Integer[CACHE_INDEX][CACHE_INTERN][BLOCK_SIZE];
		for(int i = 0 ; i < CACHE_INDEX;i++){
			valid[i]=false;
			tag[0]=0;
			for (int j = 0 ; j < CACHE_INTERN;j++){
				for (int k = 0 ; k < BLOCK_SIZE; k++){
					block[i][j][k]=0x0;
				}
			}
		}
	}
	
	/*
	 * invalid entire cache
	 * 
	 */
	public Boolean Invalid_Entire_Cache(){
		for(int i=0;i<valid.length;i++){
			valid[i]= false;
		}
		return true;
	}
	/*
	 * Linefill
	 * @param:
	 */
	public void Linefill(){
		//@TODO
		return;
	}
	public int read(){
		return 0;
	}
}
