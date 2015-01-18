package simulate_cache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SimulateCache {
	public static void cacheConsole(Cache scache){
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		String opt;
		try {
			while((opt = buffer.readLine()) != null){
				
				if("read ".equals(opt.toLowerCase())){
					
				}
				if("write".equals(opt.toLowerCase())){
					
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		Cache scache = new Cache();
		cacheConsole(scache);
		scache.printCache((short)10);
		scache.addressResolve(1200);
		System.out.println("value of read cache is: "+scache.readCache((short)32));
		System.out.println("value of read cache is: "+scache.readCache((short)32));
		scache.writeCache(10, (short)32);
		System.out.println("value of read cache is: "+scache.readCache((short)16));
		scache.printCache((short)37); 
		System.out.println("++++++++++---------------------+++++++++++");
		scache.addressResolve(1200+128*32);
		System.out.println("value of read cache is: "+scache.readCache((short)32));
		System.out.println("value of read cache is: "+scache.readCache((short)32));
		scache.writeCache(30, (short)32);
		System.out.println("value of read cache is: "+scache.readCache((short)16));
		scache.printCache((short)37); 
		System.out.println("++++++++++---------------------+++++++++++");
		scache.addressResolve(1200+128*32*2);
		//System.out.println("value of read cache is: "+scache.readCache((short)32));
		//System.out.println("value of read cache is: "+scache.readCache((short)32));
		scache.writeCache(40, (short)32);
		System.out.println("value of read cache is: "+scache.readCache((short)16));
		scache.printCache((short)37);
		scache.printMem(37);
		scache.printMem(37+128);
		scache.printMem(37+256);
	}
}
