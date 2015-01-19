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
	public static final short MEM_SIZE = 10240;
	public static final short SIGN_EXT = 1; 
	public static final short UNSIGN_EXT = 0; 
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
	private byte[] tlru;
	/*
	 * initial the cache
	 */
	public Cache(){
		valid = new Boolean[CACHE_INDEX][CACHE_INTERN];
		tag = new Integer[CACHE_INDEX][CACHE_INTERN];
		block = new byte[CACHE_INDEX][CACHE_INTERN][BLOCK_SIZE];
		mem = new byte[MEM_SIZE][BLOCK_SIZE];
		tlru = new byte[CACHE_INDEX];
		lru = new byte[CACHE_INDEX][CACHE_INTERN];
		for(int i = 0 ; i < CACHE_INDEX;i++){
			tlru[i]=0;
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
				mem[i][j]=(byte)(j);
			}
		}
	}
	/*
	 *  mem load and write from cache
	 */
	public byte[] loadMem(int taddr){
		//地址簿超过内存的大小
		
		//taddr = taddr&(MEM_SIZE*BLOCK_SIZE*8);
		int memindex=taddr>>5;
			System.out.println("load mem at block "+memindex);
		byte result[] = new byte[BLOCK_SIZE];
		for(int i = 0 ; i < BLOCK_SIZE ; i ++){
			result[i]=mem[memindex][i];
		}
		return result;
	}
	public Boolean writeMem(int taddr,byte[] cblock){
		//taddr = taddr&(MEM_SIZE*BLOCK_SIZE*8);
		
		
		int memindex=taddr>>5;
		System.out.println("write mem : "+memindex);
		System.out.println("CBLOCK : "+ index );
		for (int i = 0 ; i< BLOCK_SIZE ; i ++){
			
			System.out.println(cblock[i]+" ");
			mem[memindex][i] = cblock[i];
		}
		//mem[memindex]=cblock;
		return true;
	}
	public void printMem(int mindex){
		if (mindex > MEM_SIZE*BLOCK_SIZE || mindex < 0){
			System.out.println("index is too big for memory");
			return;
		}
		System.out.println("Mem Status for Block: "+mindex);
		for (int i = 0 ;i < BLOCK_SIZE ; i ++){
			System.out.print(mem[mindex][i]+" ");
		}
		System.out.println("\r\n end of mem status====================================");
		return;
	}
	public void printMemByAddr(int address){
		printMem(address>>5);
		
	}
	/*
	 * invalid entire cache
	 * 
	 */
	public int getBlockAddr(short cindex,int ctag){
		int taddr = (ctag<<12)+(((int)cindex)<<5);
//		System.out.println("taddress is : "+taddr);
		return taddr;
	}
	public Boolean Invalid_Entire_Cache(){
		for(int i=0;i<CACHE_INDEX;i++){
			for (int j = 0; j <CACHE_INTERN;j++)
			valid[i][j]= false;
		}
		System.out.println("Cache invalid entirely");
		return true;
	}
	/*
	 * Linefill
	 * @param:
	 */
	public void lineFill(short cindex, short cintern,int taddr,int ctag){
		//@TODO
		
		block[cindex][cintern] = loadMem(taddr);
		//标记
		valid[cindex][cintern] = true;
		tag[cindex][cintern] = ctag;
		System.out.println("linefill at block "+cindex);
		return;
	}
	/*
	 * read
	 */
	public int readCache(short type){
		int rtvalue = 0;
		Boolean flag = false;
		int vaccum = 0;
		for(int i = 0;i<CACHE_INTERN;i++){
			/*
			 * hit
			 */
			if (valid[index][i]==true && tag[index][i]==tags){
				lru[index][i]+=1;
				tlru[index]=(byte)i;
				System.out.println(" read hit at : "+index );
				rtvalue = typeRead(index,type,i);
				//rtvalue = block[index][i][wordoffset*4+byteoffset];
				
				return rtvalue;
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
			System.out.println("read miss at: "+index);
			lineFill((short)index,(short)vaccum,addr,tags);
			valid[index][vaccum]=true;
			tag[index][vaccum] = tags;
			rtvalue = typeRead(index,type,vaccum);
			
		}
		else{// LRU needed
			System.out.println("read LRU");
			int max_time = 1024;
			int choice = -1;
			for(int i = 0 ; i < CACHE_INTERN ; i ++ ){
				if(lru[index][i]<max_time){
					choice = i;
				}
			}
			/*
			 * tmp lru for 2 
			 */
			if (tlru[index]==0) {choice=1;}
			else choice=0;
			if (choice>=0){
				//先写入mem
				int taddr=getBlockAddr(index,tag[index][(short)choice]);
				writeMem(taddr,block[index][(short)choice]);
				lineFill(index,(short)choice,taddr,tags);
				tag[index][choice] = tags;
				rtvalue = typeRead(index,type,choice);
			}
		}
		
		return rtvalue;
	}
	/**********************************
	 * type read 
	 *********************************/
	public int typeRead(short cindex,short type,int intern){
		lru[cindex][intern]++;
		tlru[cindex]=(byte)intern;
		int rtvalue = 0;
		if(type==TYPE_8){
			rtvalue = (int)(block[cindex][intern][wordoffset*4+byteoffset]&0x0ff);
			return (int)(rtvalue);
		}
		else if(type==TYPE_16){
			int j = 0 ;
			rtvalue = 0;
			for (j=1;j>=0;j--){
				short tmp = 0;
				tmp= block[cindex][intern][wordoffset*4+byteoffset+j];
				rtvalue=(rtvalue<<8)+(((int)tmp)&0xff);
			}
			return rtvalue&0x0000ffff;
		}
		else if(type==TYPE_32){
			int j = 0 ;
			rtvalue = 0;
			for (j=3;j>=0;j--){
				short tmp = 0;
				tmp= block[cindex][intern][wordoffset*4+byteoffset+j];
				rtvalue=(rtvalue<<8)+(((int)tmp)&0xff);
			}
			return rtvalue;
		}
		return rtvalue;
	}
	public int  extendRead(short ext,int value,short type){
		int rtvalue = 0;
		rtvalue = value ;
		if (ext==SIGN_EXT){
			
			if (type==TYPE_8){
				int topbit =((value>>7)&0x01);
				if (0==topbit){
					rtvalue = value&0x0ff;
				}
				else if(1==topbit){
					rtvalue=value&0x0ff;
					rtvalue+=0xffffff00;
				}
			}
			else if(type==TYPE_16){
				int topbit =((value>>15)&0x01);
				if (0==topbit){   
					rtvalue = value&0x0ffff;
				}
				else if(1==topbit){
					rtvalue=value&0x0ffff;
					rtvalue+=0xffff0000;
				}
			}
		}
		else if(ext==UNSIGN_EXT){
			if (type==TYPE_8){
				
					rtvalue = value&0x0ff;
				
			}
			else if(type==TYPE_16){
				
					rtvalue = value&0x0ffff;
				
			}
		}
		
		return rtvalue;
	}
	/*write cache
	 * 
	 */
	public Boolean writeCache( int value,short type){
		Boolean flag = false;
		//hit
		int vaccum=-1;
		for(int i = 0;i<CACHE_INTERN;i++){
			if (valid[index][i]==true && tag[index][i]==tags){
				System.out.println("write hit at : "+index);
				if(typeWrite(value,type,i,index))
				{
					return true;
				}
				else{
					return false;
				}
			}
			if(valid[index][i]==false){
					flag=true;
					vaccum = i;
			}
		}
		
		///miss
		if(flag){//do not need lru
			System.out.println("write miss at  block "+index+" withoutLRU");
			int taddr=getBlockAddr(index,tag[index][(short)vaccum]);
			lineFill((short)index,(short)vaccum,taddr,tags);
			//写cache
			//不需要写到mem中
//			System.out.println("write miss and load mem ,write cache");
				if(typeWrite(value,type,vaccum,index)){
					return true;
				}
				else{
					return false;
				}
				
			//end write
		}
		
		else{// lru needed ，写入mem
			System.out.println("write miss at block "+index+" with LRU");
			System.out.println("write LRU");
			int choice=0;
			if(tlru[index]==0)
			{
				choice=1;
			}
			else{
				choice=0;
			}
			//写入Mem
			int taddr=getBlockAddr(index,tag[index][(short)choice]);
			writeMem(taddr,block[index][(short)choice]);
			lineFill(index,(short)choice,taddr,tags);
			if(typeWrite(value,type,choice,index))
			{
				return true;
			}
			else{
				return false;
			}
			
		}
	}
	/*
	 * @param
	 * value:write value
	 * type:8bit,16bit,32bit
	 * i:0 or 1
	 */
	public Boolean typeWrite(int value,short type,int i,int cindex){
		
		
			lru[cindex][i]++;
			tlru[cindex]=(byte)i;
			if(type==TYPE_8){
				value = value&0x0ff;
				for (int j=0;j<4;j++){
					byte tmp = (byte)(value&0xff);
					block[cindex][i][wordoffset*4+byteoffset+j] = tmp;
					value = value>>8;
				}
				return true;
//				block[cindex][i][wordoffset*4+byteoffset]=(byte)(value&0xff);
//				return true;
			}
			else if(type==TYPE_16){
				int j = 0 ;
				value = value&0x0ffff;
				for (j=0;j<4;j++){
					byte tmp = (byte)(value&0xff);
					block[cindex][i][wordoffset*4+byteoffset+j] = tmp;
					value = value>>8;
				}
				return true;
			}
			else if(type==TYPE_32){
				int j = 0 ;
				for (j=0;j<4;j++){
					byte tmp = (byte)(value&0xff);
					block[cindex][i][wordoffset*4+byteoffset+j] = tmp;
					value = value>>8;
				}
				return true;
			}
			return false;
	}
	
	/*
	 * print cache status information
	 */
	public void printCache(short cindex){
		System.out.println("Cache Status for Index "+cindex+" :");
		System.out.println("valid	"+"tag	"+"Intern	"+"Block");
		for (int i = 0 ;i < CACHE_INTERN ; i ++ ){
			
			System.out.print(valid[cindex][i].toString()+"	"+tag[cindex][i]+"	"+i+"	");
			for (int j = 0 ;j < BLOCK_SIZE;j++){
				System.out.print(block[cindex][i][j]+" ");
			}
			System.out.print("\r\n");	
		}
	}
	public void printCacheByAddr(int address){
		addressResolve(address);
		printCache(index);
	}
	/*
	 * Address Resolve
	 */
	public void addressResolve(int taddr){
		if (taddr<0 || taddr >MEM_SIZE*BLOCK_SIZE*8){
			System.out.println("Bad address");
			return;
		}
		//地址不超过内存大小
		System.out.println("The address Resolve outcome is: "+taddr);
		addr = taddr;
		//taddr = addr&(MEM_SIZE*BLOCK_SIZE*8);
		taddr = taddr&0x7fffffff;
		byteoffset = (short) (taddr&0x03);
		taddr = taddr>>2;
		
		wordoffset = (short)(taddr&0x07);
		taddr = taddr>>3;
		index = (short)(taddr&0x7f);
		taddr = taddr >> 7;
		tags = taddr&0x000fffff;
		//wordoffset = (short)(taddr&0x1c);
		//index=(short)(taddr&0xfe0);
		//tags = taddr&0xfffff000;
		
		System.out.println("index: "+index+" tags: "+tags+" wordoffset: "+wordoffset+" byteoffset: "+byteoffset);
		return;
	} 
}
