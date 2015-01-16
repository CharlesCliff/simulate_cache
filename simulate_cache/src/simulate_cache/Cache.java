package simulate_cache;

public class Cache {
	/*static variables definition
	 * 
	 * */
	public static final int CACHE_INDEX=128;
	public static final int CACHE_INTERN=2;
	public static final int BLOCK_SIZE = 32;
	public static final short TYPE_8 = 8;
	public static final short TYPE_16 = 16;
	public static final short TYPE_32 = 32;
	public static final short MEM_SIZE = 1024;
	/*
	 * private variables definition
	 */
	private short byteoffset,wordoffset;
	private short index;
	private int tags;
	private Boolean[][] valid;// valid or not,0 for invalid ,1 for valid
	private Integer[][] tag;// tag for cache locate
	private byte[][][] block;//data block
	private byte[][] lru;
	private byte[][] mem;
	private int addr;
	/*
	 * initial the cache
	 */
	public Cache(){
		valid = new Boolean[CACHE_INDEX][CACHE_INTERN];
		tag = new Integer[CACHE_INDEX][CACHE_INTERN];
		block = new byte[CACHE_INDEX][CACHE_INTERN][BLOCK_SIZE];
		mem = new byte[MEM_SIZE][BLOCK_SIZE];
		for(int i = 0 ; i < CACHE_INDEX;i++){
			for (int j = 0 ; j < CACHE_INTERN;j++){
			valid[i][j]=false;
			tag[i][j]=0;
			lru[i][j]=0;
				for (int k = 0 ; k < BLOCK_SIZE; k++){
					block[i][j][k]=0x0;
				}
			}
		}
		// mem initialize
		for (int i = 0 ; i < MEM_SIZE;i++){
			for (int j = 0 ;j < BLOCK_SIZE;j++){
				mem[i][j]=(byte)(i+j);
			}
		}
	}
	/*
	 * 
	 */
	public byte[] loadMem(int addr){
		//地址簿超过内存的大小
		addr = addr&(MEM_SIZE*BLOCK_SIZE*8);
		int memindex=addr>>8;
			byte result[] = new byte[BLOCK_SIZE];
			for(int i = 0 ; i < BLOCK_SIZE ; i ++){
				result[i]=mem[memindex][i];
			}
			return result;
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
	public void lineFill(short index, short intern){
		//@TODO
		block[index][intern] = loadMem(addr);
		return;
	}
	/*
	 * read
	 */
	public int read_cache(short type){
		int rtvalue = 0;
		Boolean flag = false;
		int vaccum = 0;
		for(int i = 0;i<CACHE_INTERN;i++){
			/*
			 * hit
			 */
			if (valid[index][i]==true && tag[index][i]==tags){
				lru[index][i]+=1;
				System.out.println("hit");
				if(type==TYPE_8){
					rtvalue = block[index][i][wordoffset*4+byteoffset];
					return (int)(rtvalue);
				}
				else if(type==TYPE_16){
					int j = 0 ;
					for (j=0;j<2;j++){
						short tmp= block[index][i][wordoffset*4+byteoffset+j];
						rtvalue+=(rtvalue<<8)+tmp;
					}
					return rtvalue;
				}
				else if(type==TYPE_32){
					int j = 0 ;
					for (j=0;j<4;j++){
						short tmp= block[index][i][wordoffset*4+byteoffset+j];
						rtvalue+=(rtvalue<<8)+tmp;
					}
					return rtvalue;
				}
				//rtvalue = block[index][i][wordoffset*4+byteoffset];
				
				return -1;
			}
			/*
			 * flag if need to  LRU
			 */
			if(valid[index][i]==false){
				flag=true;
				vaccum = i;
			}
		}
		
		/*
		 * miss
		 * LRU 
		 */
		if(flag){// do not need to lru
			lineFill((short)index,(short)vaccum);
			valid[index][vaccum]=true;
			tag[index][vaccum] = tags;
		}
		else{// LRU needed
			int max_time = 1024;
			int choice = -1;
			for(int i = 0 ; i < CACHE_INTERN ; i ++ ){
				if(lru[index][i]<max_time){
					choice = i;
				}
			}
			if (choice>=0){
				lineFill(index,(short)choice);
				tag[index][choice] = tags;
			}
		}
		System.out.println("miss");
		return 0;
	}
	
	/*write cache
	 * 
	 */
	public Boolean write_cache( int value,short type){
		Boolean flag = false;
		//hit
		for(int i = 0;i<CACHE_INTERN;i++){
			if (valid[index][i]==true && tag[index][i]==tags){
				System.out.println("write hit");
				lru[index][i]++;
				if(type==TYPE_8){
					block[index][i][wordoffset*4+byteoffset]=(byte)(value&0xff);
					return true;
				}
				else if(type==TYPE_16){
					int j = 0 ;
					for (j=1;j>=0;j--){
						byte tmp = (byte)(value&0xff);
						block[index][i][wordoffset*4+byteoffset+j] = tmp;
						value = value>>8;
					}
					return true;
				}
				else if(type==TYPE_32){
					int j = 0 ;
					for (j=3;j>=0;j--){
						byte tmp = (byte)(value&0xff);
						block[index][i][wordoffset*4+byteoffset+j] = tmp;
						value = value>>8;
					}
					return true;
				}
				
			}
		}
		
		///miss
		
		
		return false;
	}
	/*
	 * print cache status information
	 */
	public void Print_Cache(){
		
		
	}
	/*
	 * Address Resolve
	 */
	public void addressResolve(int addr){
		if (addr<0){
			System.out.println("Bad address");
			return;
		}
		//地址不超过内存大小
		addr = addr&(MEM_SIZE*BLOCK_SIZE*8);
		addr = addr&0x7fffffff;
		byteoffset = (short) (addr&0x03);
		wordoffset = (short)(addr&0x1c);
		index=(short)(addr&0xfe0);
		tags = addr&0xfffff000;
		return;
	}
}
