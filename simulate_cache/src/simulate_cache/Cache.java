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
	private short byteoffset,wordoffset;
	private short index;
	private int tags;
	private Boolean[][] valid;// valid or not,0 for invalid ,1 for valid
	private Integer[][] tag;// tag for cache locate
	private byte[][][] block;//data block
	/*
	 * initial the cache
	 */
	public Cache(){
		valid = new Boolean[CACHE_INDEX][CACHE_INTERN];
		tag = new Integer[CACHE_INDEX][CACHE_INTERN];
		block = new byte[CACHE_INDEX][CACHE_INTERN][BLOCK_SIZE];
		for(int i = 0 ; i < CACHE_INDEX;i++){
			for (int j = 0 ; j < CACHE_INTERN;j++){
			valid[i][j]=false;
			tag[i][j]=0;
			
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
		for(int i=0;i<CACHE_INDEX;i++){
			for (int j = 0; j <CACHE_INTERN;j++)
			valid[i][j]= false;
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
	/*
	 * read
	 */
	public int read_cache(){
		
		return 0;
	}
	
	/*
	 * 
	 */
	public Boolean write_cache(){
		return false;
	}
	/*
	 * print cache status information
	 */
	public void Print_Cache(){
		
		
	}
	/*
	 * 
	 */
	public void addressResolve(int addr){
		if (addr<0){
			System.out.println("Bad address");
			return;
		}
		
		addr = addr&0x7fffffff;
		byteoffset = (short) (addr&0x03);
		wordoffset = (short)(addr&0x1c);
		index=(short)(addr&0xfe0);
		tags = addr&0xfffff000;
		
		
		return;
	}
}
