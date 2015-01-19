package simulate_cache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimulateCache {
	public void showOperation(){
		System.out.println("命令说明：\r\n"
				+ "1、读cache:\r\n"
				+ " read address type ext\r\n"
				+ "read:操作名称\r\n"
				+ "address:读地址，十进制正整数，字节地址\r\n"
				+ "type:读粒度，8,16,32\r\n"
				+ "ext:扩展类型，无符号：unsign ,有符号:sign\r\n"
				+"注：命令参数请严格按照顺序书写，中间以空格或制表符隔开，\r\n"
				+ "忽略大小写，type 和ext可省略，缺省为32bit粒度，有符号扩展\r\n"
				+ "2、写cache:\r\n"
				+ " write address value type \r\n"
				+ "write:操作名称\r\n"
				+ "address:读地址，十进制正整数，字节地址\r\n"
				+ "type:读粒度，8,16,32\r\n"
				+ "value:写入的值，十进制整数\r\n"
				+"注：命令参数请严格按照顺序书写，中间以空格或制表符隔开，忽略大小写，type 缺省为32bit粒度"
				+ "\r\n"
				+ "3、显示地址对应的cache块（两组）\r\n"
				+ " showcache address\r\n"
				+ "showcache:操作\r\n"
				+ "address:读地址，十进制正整数，字节地址\r\n"
				+ "4、显示地址对应的memory块的值\r\n"
				+ " showmem address\r\b"
				+ "showmem:操作\r\n"
				+ "address:读地址，十进制正整数，字节地址\r\n"
				+ "5、重置cache(Invalid Entire Cache)\r\n"
				+ "invalidcache\r\n"
				+ "6、显示操作手册：\r\n"
				+ " showopt\r\n");
	}
	public void manipulateManual(){
		System.out.println("----------------------Welocome to Cache Simulate-----------------");
		System.out.println("--author: 陈杨	"+"student number:	1100012936-------------------");
		System.out.println("--please read the manipulate manual carefully before using this\r\n"
				+ " simulator-----\r\n\r\n");
		System.out.println("Note:the cache is initialized with all invalid and block value 0 \r\n"
						+ "and tag value 0.the memory is set to be 10240*32 byte size ,so \r\n"
						+ "the address given should not be beyond 10240*32 byte. Also,the\r\n"
						+ " address we used is byte address,for example the address 1200 \r\n"
				         + "means that the number 1200 byte in memory. The memory is \r\n"
				         + "initialized with 0-31 each block.for example ,memory block 1 \r\n"
				         + "is initialized with 0-31 each byte.");
		System.out.println("Operations 	   Format                    	Details");
		System.out.println("read cache     read  address type ext		read cache address by type(8,16,32)");
		System.out.println("write cache    write address type value     \r\n"
						 + "show Cache     showcache address\r\n"
						 + "show Memory    showmem   address            \r\n"
						 + "invalid cache  invalidcache                invalidcache ,ignore case\r\n"
						 + "show operation showopt                     show operation detail menu");
		this.showOperation();
		
	
	}
	public Boolean checkParams(short type,int address){
		Boolean flag = true;
		if(type!=(short)8 && type!=(short)16 && type!=(short)32){
			System.out.println("Type is Invalid,only be 8,16,32");
			flag = false;
		}
		if(address>Cache.MEM_SIZE*Cache.BLOCK_SIZE||address <0){
			System.out.println("address is invalid,should be between 0~"+Cache.MEM_SIZE*Cache.BLOCK_SIZE);
			flag = false;
		}
		
		return flag;
	}
	public  void cacheConsole(Cache scache){
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		String opt;
		this.manipulateManual();
		System.out.println("Please input the command :");
		try {
			while((opt = buffer.readLine()) != null){
				String[] optstr = opt.split(" |	+");//一个或多个空格
				
				
				System.out.println("\r\n");
				//read cache
				//opt: read address type ext
				if("read".equals(optstr[0].toLowerCase())){
					short type = 0;
					String ext=new String("undefined");
					int address = Integer.parseInt(optstr[1]);
					if(optstr.length==2){
						type=(short)32;
						ext = "SIGN";
					}
					else if(optstr.length==4){
					type = (short)Integer.parseInt(optstr[2]);
					ext = optstr[3].toUpperCase();
					}
					else{
						System.out.println("Invalid read Operation");
						System.out.println("Please input the command :");
						continue;
					}
					scache.addressResolve(address);
					int readvalue = scache.readCache(type);
//					System.out.println("params: "+address+" "+ ext+" "+type);
					int extvalue = 0;
					if("SIGN".equals(ext)){
						extvalue = scache.extendRead(Cache.SIGN_EXT,readvalue,type);
						System.out.println("Sign extend read value: "+extvalue);
					}
					else if("UNSIGN".equals(ext)){
						extvalue = scache.extendRead(Cache.UNSIGN_EXT,readvalue,type);
						System.out.println("Unsign extend read value: "+extvalue);
					}
					else{
						System.out.println("extend option is incorrect,only sign or unsign");
					}
					
					System.out.println("extend read value Hex format: 0x"+Integer.toHexString(extvalue));
					
				}
				//write cache
				//opt:write address type value 
				else if("write".equals(optstr[0].toLowerCase())){
					short type = 0;
					int address = Integer.parseInt(optstr[1]);

					if (optstr.length==3){
						type=(short)32;
					}
					else if(optstr.length==4){
						type = (short)Integer.parseInt(optstr[3]);
					}
					else{
						System.out.println("Invalid write Operation.");
						System.out.println("Please input the command :");
						continue;
					}
					int value = Integer.parseInt(optstr[2]);
					if(this.checkParams(type, address)){
						scache.addressResolve(address);
						scache.writeCache(value, type);
					}
					
				}
				//show cache
				//opt 
				else if("showcache".equals(optstr[0].toLowerCase())){
					int address = Integer.parseInt(optstr[1]);
					scache.printCacheByAddr(address);
				}
				else if("showmem".equals(optstr[0].toLowerCase())){
					int address = Integer.parseInt(optstr[1]);
					scache.printMemByAddr(address);
				}
				else if("invalidcache".equals(optstr[0].toLowerCase())){
					scache.Invalid_Entire_Cache();
				}
				else if("showopt".equals(optstr[0].toLowerCase())){
					this.showOperation();
					
				}
				//invalid opt
				else {
					System.out.println("invalid operation,please check the manual for valid operation");
				}
				System.out.println("Please input the command :");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		SimulateCache sc = new SimulateCache();
		Cache scache = new Cache();
		sc.cacheConsole(scache);
	}
}
